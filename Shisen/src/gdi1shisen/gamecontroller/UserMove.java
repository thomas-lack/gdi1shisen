package gdi1shisen.gamecontroller;

import java.awt.Color;
import java.util.LinkedList;
import gdi1shisen.datastore.GamePlayer;
import gdi1shisen.datastore.Highscore;
import gdi1shisen.datastore.UserMoveHistory;
import gdi1shisen.datastore.MoveData;
import gdi1shisen.datastore.KeyData;
import gdi1shisen.gamecontroller.Move;
import gdi1shisen.gui.HighscoreFrame;
import gdi1shisen.gui.MessageBox;
import gdi1shisen.gui.ShisenFrame;
import gdi1shisen.gui.ShisenFrameBoard;
import gdi1shisen.gui.ShisenFrameInfoBar;
import gdi1shisen.datastore.LevelParser;
import gdi1shisen.exceptions.ParameterOutOfRangeException;

/**
 * Controller Klasse für die Verarbeitung von Benutzereingaben
 * auf dem Spielfeld (insbesondere auch Buttons der Iconleiste)
 * @author Thomas, Benjamin
 */
public class UserMove {
	private ShisenFrame frame;
	private ShisenFrameBoard gamePanel;
	private ShisenFrameInfoBar infoBar;
	private LevelParser levelParser;
	private MoveData moveControllerData = new MoveData();
	private UserMoveMenu userMoveMenu;
	private GamePlayer player;
	
	private UserMoveHistory historyData;
	private Timer timer;
	
	private KeyData moveKeyData;
	
	private int[] firstClick = new int[2];
	private int[] secondClick = new int[2];
	private int clickNumber = 1;
	private int[] firstHintBrick;
	private int[] secondHintBrick;
	
	private int numberBricksAtStart = 0;	
	
	// wird benötigt um abzufragen ob Eingabe erfolgen darf
	private GUISolverThread solverThread =null;
	
	/**
	 * Konstruktor für den Controller der Nutzereingaben auf dem Spielfeld
	 * @param frame	ShisenFrame Objekt des Spielfensters
	 * @param levelParser LevelParser Objekt des aktuellen Levels
	 * @param userMoveMenu UserMoveMenu Objekt des Menüleisten Controllers
	 */
	public UserMove(ShisenFrame frame, LevelParser levelParser,	
			UserMoveMenu userMoveMenu, GamePlayer player)
	{
		//relevante Objektvariablen setzen
		this.frame = frame;
		gamePanel = frame.getGamePanel();
		infoBar = frame.getInfoBar();
		this.levelParser = levelParser;
		numberBricksAtStart = levelParser.countBricks();
		this.userMoveMenu = userMoveMenu;
		historyData  = new UserMoveHistory();
		this.player = player;
		
		// KeyData initialisieren wird benötigt um Cursor Position zu speichern
		//Anschließend Synchronisation mit Menü Controller
		moveKeyData = new KeyData();
		userMoveMenu.setMoveKeyData(moveKeyData);
				
		//Timer Objekt erzeugen (paralleler Thread, der jede Sekunde
		//die Zeitanzeige aktualisiert) und relevante Objektinfos setzen
		this.timer = new Timer();
		this.timer.setInfoBar(infoBar);
		this.timer.start();
					
		//Übergabe des Timer Objekts an Menüleisten Controller, um den Timer
		//zu stoppen, falls asynchron ein anderes Level aufgerufen wird
		this.userMoveMenu.setTimer(timer);
		
		//Erste Spielinfos in der Infoleiste anzeigen 
		setNewInfobarInfos();

	}
	
	/**
	 * Konstruktor mit erweitertem Parametersatz, falls ein Spiel geladen wurde
	 * @param frame	ShisenFrame Objekt des Spielfensters
	 * @param levelParser LevelParser Objekt des aktuellen Levels
	 * @param userMoveMenu UserMoveMenu Objekt des Menüleisten Controllers
	 * @param historyData UserMoveHistory Objekt mit bestehenden Spielzügen
	 * @param currentGameTime Long Zeitangabe, wie lange schon gespielt wurde
	 */
	public UserMove(ShisenFrame frame, LevelParser levelParser, UserMoveMenu userMoveMenu, 
			UserMoveHistory historyData, KeyData moveKeyData, Long currentGameTime,
			GamePlayer player)
	{
		//setzen der Umgebungsobjekte
		this.frame = frame;
		gamePanel = frame.getGamePanel();
		infoBar = frame.getInfoBar();
		this.levelParser = levelParser;
		numberBricksAtStart = levelParser.countBricks();
		this.userMoveMenu = userMoveMenu;
		this.historyData = historyData;
		this.moveKeyData = moveKeyData;
		this.player = player;
		
		//Timer initialisieren mit bereits gespielter Zeit & Timer starten
		timer = new Timer(currentGameTime);
		timer.setInfoBar(infoBar);
		timer.start();
		
		//neues Timer Objekt mit Menüleisten Controller konsistent halten
		this.userMoveMenu.setTimer(timer);
		
		//Erste Spielinfos in der Infoleiste anzeigen
		setNewInfobarInfos();
	}
	
	/**
	 * Behandlung des Events "Stein auf Spielfeld angeklickt"
	 * @param posX int x-Koordinate des angeklickten Steins
	 * @param posY int y-Koordinate des angeklickten Steins
	 */
	public void newBrickClicked(int posX, int posY)
	{
		//sobald ein Stein angeklickt wird, sollen potentiell markierte
		//Hint-Steine wieder demarkiert werden
		firstHintBrick = null;
		secondHintBrick = null;
		hideMatchingBricks();
		
		//erster Stein angeklickt ?
		if (clickNumber == 1)
		{
			firstClick[0] = posX;
			firstClick[1] = posY;
			
			//markieren des Spielsteins
			gamePanel.markBrick(posX, posY);
			
			//Anzeige "kein gültiger Zug zurücksetzen"
			infoBar.hideNoValidMove();
			
			clickNumber++;
		}
		//zweiter Stein angeklickt?
		else
		{
			clickNumber--;
			secondClick[0] = posX;
			secondClick[1] = posY;
			
			//ersten Spielstein De-Markieren
			gamePanel.unMarkBrick(firstClick[0], firstClick[1]);
			
			try
			{
				//Herausfinden ob der aktuelle Zug möglich ist
				boolean possible = Move.isWay(levelParser,firstClick,secondClick);
				if (possible)
				{
					//führe den Zug durch falls möglich
					moveControllerData = Move.doMove(levelParser,firstClick,secondClick);
					
					//speicher zug in history
					historyData.addMove(firstClick, secondClick, levelParser.getSymbol(firstClick));
										
					//übergebe neues Level an den LevelParser zurück
					levelParser = new LevelParser(moveControllerData.getLevelRaw());
					
					//Synchronisation relevanter Objekte mit dem Controller für die
					//Menüleiste. (Notwendig, falls dort asynchrone Aufrufe getätigt werden)
					userMoveMenu.setLevelParser(levelParser);
					userMoveMenu.setUserMoveHistory(historyData);
					
					// Zeichne Linie zwischen den zu entfernenden Steinen
					showBrickRemovePath(moveControllerData);

					//entferne die beiden Spielsteine vom Spielfeld
					gamePanel.removeButtons(firstClick, secondClick);
					
					//InfoBar aktualisieren
					setNewInfobarInfos();
					
					//Spiel nach diesem Zug beendet ?
					if (Move.isSolved(levelParser))
						gameIsWon();
				}
				
				//Wenn Zug nicht möglich...
				else
				{
					//Zeige in der Infoleiste entsprechende Rückmeldung an
					this.infoBar.showNoValidMove();					
				}
			}
			catch (Exception ex)
			{
				System.out.println("Fehler in newBrickClicked() - UserMove.java");
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * Markiert alle zu dem angeklickten Stein passenden Steine
	 * @param x int x-Koordinate des Ursprungssteins
	 * @param y int y-Koordinate des Ursprungssteins
	 */
	public void showMatchingBricks(int x, int y)
	{
		char symbol = levelParser.getSymbol(new int[] {x,y});
		LinkedList<int[]> coordsList = levelParser.getCoordsOfSimilarSymbols(symbol);
		for (int i=0; i < coordsList.size(); i++)
			gamePanel.markBrick(coordsList.get(i)[0], coordsList.get(i)[1]);
	}
	
	/**
	 * Setzt alle markierten Steine für die Hilfe-Ausgabe wieder zurück
	 * (Aber nicht einen bereits vorher markierten Spielstein)
	 */
	public void hideMatchingBricks()
	{
		this.gamePanel.unMarkAllBricks();
		
		//falls vor dem Rechtsklick schon Steine mit einem hint
		//belegt waren, soll dieser wieder angezeigt werden
		if (firstHintBrick != null && secondHintBrick != null)
		{
			gamePanel.markBrick(firstHintBrick[0], firstHintBrick[1], Color.YELLOW);
			gamePanel.markBrick(secondHintBrick[0], secondHintBrick[1], Color.YELLOW);
		}
		
		//falls vor einem Rechtsklick schon ein Stein markiert war
		//soll dieser wieder farblich hervorgehoben werden
		if (clickNumber == 2)
			gamePanel.markBrick(firstClick[0], firstClick[1]);
	}
	
	/**
	 * Setze neue Info-Strings in der unteren Infoleiste vom Spielfenster
	 */
	public void setNewInfobarInfos()
	{
		//Setzten der Anzeige wieviele Spielsteine noch auf dem Feld sind
		String actualBricks = 
			"Steine: " +
			String.valueOf(this.levelParser.countBricks()) + 
			" / " +
			String.valueOf(this.numberBricksAtStart);
		this.infoBar.setCountRestMoves(actualBricks);
		
		//Setzen der Anzeige wieviele Züge aktuell möglich sind
		try
		{
			this.infoBar.setMovesPossible(Move.cpmCounter(levelParser));
		}
		catch (ParameterOutOfRangeException ex)
		{
			System.out.println("Fehler in setNewInfoBarInfos - UserMove");
			ex.printStackTrace();
		}
	
		//Setzen der Zeit
		//wird direkt von der Timer-Klasse erledigt in einem parallelen Thread
	}
	
	/**
	 * Zug rückgängig machen
	 */
	public void undo()
	{
		//Gebe Warnung aus, falls undo nicht möglich
		if (!historyData.undoIsPossible())
			showMessageBox("Warnung", "Es existiert kein Zug der rückgängig " +
					"gemacht werden könnte.");
		else
		{
			//hole die Koordinaten der Steine und das Steinsymbol des letztes Zuges
			int[] coords = historyData.getLastMoveCoords();
			char symbol = historyData.getLastMoveSymbol();
			
			//erstellen eine Kopie des raw-Levels, fügen das passende Symbol an
			//entsprechender Stelle ein und erzeugen daraus ein neues LP-Objekt
			char[][] tmpLevel = levelParser.getRawLevel();
			tmpLevel[coords[1]-1][coords[0]-1] = symbol;
			tmpLevel[coords[3]-1][coords[2]-1] = symbol;
			try
			{
				levelParser = new LevelParser(tmpLevel);
			}
			catch (Exception ex)
			{
				System.out.println("Fehler in newBrickClicked() - UserMove.java");
				ex.printStackTrace();
			}
			
			//Synchronisation relevanter Objekte mit dem Controller für die
			//Menüleiste. (Notwendig, falls dort asynchrone Aufrufe getätigt werden)
			userMoveMenu.setLevelParser(levelParser);
			userMoveMenu.setUserMoveHistory(historyData);
			
			//einblenden der Spielsteine auf dem Spielfeld
			gamePanel.showButtons(coords[0], coords[1], symbol);
			gamePanel.showButtons(coords[2], coords[3], symbol);
			
			//Informationen in der Infobar aktualisieren
			setNewInfobarInfos();
			
			//Meldung an das UserMoveHistory Objekt, dass ein Zug rückgängig
			//gemacht wurde
			historyData.undoProposed();
		}	
	}
	
	/**
	 * Zug wiederherstellen
	 */
	public void redo()
	{
		//Gebe Warnung aus, falls redo nicht möglich
		if (!historyData.redoIsPossible())
			showMessageBox("Warnung", "Es existiert kein Zug der wiederhergestellt " +
					"werden könnte.");
		else
		{
			//Meldung an UserMoveHistory Objekt, dass ein Zug wiederhergestellt
			//werden soll
			historyData.redoProposed();
			
			//hole die Koordinaten der Steine
			int[] coords = historyData.getLastMoveCoords();
						
			//erstellen eine Kopie des raw-Levels, fügen das passende Symbol an
			//entsprechender Stelle ein und erzeugen daraus ein neues LP-Objekt
			char[][] tmpLevel = levelParser.getRawLevel();
			tmpLevel[coords[1]-1][coords[0]-1] = '-';
			tmpLevel[coords[3]-1][coords[2]-1] = '-';
			try
			{
				levelParser = new LevelParser(tmpLevel);
			}
			catch (Exception ex)
			{
				System.out.println("Fehler in newBrickClicked() - UserMove.java");
				ex.printStackTrace();
			}
			
			//Synchronisation relevanter Objekte mit dem Controller für die
			//Menüleiste. (Notwendig, falls dort asynchrone Aufrufe getätigt werden)
			userMoveMenu.setLevelParser(levelParser);
			userMoveMenu.setUserMoveHistory(historyData);
			
			//ausblenden der Spielsteine auf dem Spielfeld
			int[] button1 = { coords[0], coords[1] };
			int[] button2 = { coords[2], coords[3] };
			gamePanel.removeButtons(button1, button2);
			
			//Informationen in der Infobar aktualisieren
			setNewInfobarInfos();
		}
	}
	
	/**
	 * Methode zur Abarbeitung aller benötigten Befehle 
	 * nach einem gewonnenen Spiel
	 */
	private void gameIsWon()
	{
		//Spielzeit anhalten
		timer.stopTimer();
				
		//Rückmeldung beim User
		showMessageBox("Gewonnen", "Herzlichen Glückwunsch, Sie haben das Level gelöst!");
		
		//Highscore
		try 
		{
			Highscore highscore = new Highscore(player.getActualLevelHighscore());
			highscore.add(timer.getGameTime(), player.getPlayerName());
			new HighscoreFrame(highscore, frame);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Erstellt eine Nachrichten Dialogbox auf dem Spielfenster
	 * @param title	String der Dialogbox-Überschrift
	 * @param message String der Dialogbox-Nachricht
	 */
	private void showMessageBox(String title, String message)
	{
		new MessageBox(this.frame, title, message);
	}
	
	/**
	 * Getter für aktuellen Levelparser
	 * @return LevelParser Objekt
	 */
	public LevelParser getLevelParser()
	{
		return this.levelParser;
	}
	
	/**
	 * Setter für zum Move Controller gehörigem Spielfeld
	 * @param newGamePanel GamePanel Objekt, auf dem Move Controller arbeiten soll
	 */
	public void setGamePanel(ShisenFrameBoard newGamePanel)
	{
		this.gamePanel = newGamePanel;
	}
	
	/**
	 * Erhält eine Instanz von MoveData und holt sich von diesem den Weg Zwischen den zwei
	 * entfernten Spielsteinen. Dieser wird transformiert und anschließend für 250 ms auf 
	 * dem Spielfeld in form von blauen Linien angezeigt.
	 * @param moveControllerData
	 */
	public void showBrickRemovePath(MoveData moveControllerData)
	{
		int[][] a = moveControllerData.getTheWay();
		
		// definiere Hilfsvariablen für Koordinatentransformation
		int n= this.gamePanel.getBrickWidth()/2;	
		int m= this.gamePanel.getBrickHeight()/2;	
		int i=0;
		
		// Koordinatentransformation
		while(i < a.length)			
		{
	
			a[i][0]=a[i][0]*2*n+n;
			a[i][1]=a[i][1]*2*m+m;
			i++;
		}
		
		// zeichne transformierten Weg
		this.gamePanel.drawPath(a);	
		
		// warte 250 ms
		try
		{
			Thread.sleep(250);   
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		// Spielfenster neu zeichnen
		this.gamePanel.repaint(); 	
	}
	
	/**
	 * Setz den Cursor an Position {x,y} innerhalb des GamePanels
	 * wobei dieses nicht verlassen werden kann
	 * @param x Koordinate
	 * @param y Koordinate
	 */
	public void moveCursor(int x, int y)
	{	
		int[] posPrev = moveKeyData.getCursor(); 
		
		gamePanel.changeBrickState(posPrev[0], posPrev[1],true);

		int ymax = gamePanel.getHeight()/gamePanel.getBrickHeight()-2;
		int xmax = gamePanel.getWidth()/gamePanel.getBrickWidth()-2;
		
		if(posPrev!=null)
		{
			if(x>xmax && y<=ymax)
			{
				this.gamePanel.changeBrickState(1, y, false);
				moveKeyData.setCursor(new int[] {1,y});
			}
			else if(x <= xmax && y>ymax)
			{
				this.gamePanel.changeBrickState(x, 1, false);
				moveKeyData.setCursor(new int[] {x,1});
			}
			else if(x < 1 && y>=1)
			{
				this.gamePanel.changeBrickState(xmax, y, false);
				moveKeyData.setCursor(new int[] {xmax,y});
			}
			else if(x>=1 && y < 1 )
			{
				this.gamePanel.changeBrickState(x, ymax, false);
				moveKeyData.setCursor(new int[] {x,ymax});
			}
			else
			{
				this.gamePanel.changeBrickState(x, y, false);
				moveKeyData.setCursor(new int[] {x,y});
			}
		}
		
		//Synchronisation der aktuellen KeyData mit dem Controller 
		//für das Menü
		userMoveMenu.setMoveKeyData(moveKeyData);
		
	}
	
	/**
	 * Wird von ShisenFrameBoard aufgerufen um die gedrückten Tasten zu
	 * verarbeiten
	 * @param key Integer Wert der Taste repräsentiert
	 * 
	 * Wobei folgendermaßen Codiert:
	 * 0 Left
	 * 1 Down
	 * 2 Right
	 * 3 Up
	 * 4 Redo
	 * 5 Undo
	 * 6 Space
	 * 7 New Game
	 */
	public void keyPressed(int key)
	{
		if(this.reloadInput())
		{
			int [] cursor = moveKeyData.getCursor(); 
			
			switch(key)			// abarbeitung der verschiedenen Keys 
			{	
				case 0:			
					// Left verschiebt Cursor nacht Links x=x-1
					moveCursor(cursor[0]-1,cursor[1]);
					break;	
				case 1:
					// Down verschiebt Cursor um eins nach unten y=y+1 (koordinatenkreuz links oben)
					moveCursor(cursor[0],cursor[1]+1);
					break;		
				case 2:
					// Right verschiebt Cursor um eins nach rechts x=x+1
					moveCursor(cursor[0]+1,cursor[1]);
					break;
				case 3:
					// Up verschiebt Cursor um eins nach oben y=y-1 (koordinatenkreuz links oben)
					moveCursor(cursor[0],cursor[1]-1);
					break;
				case 4:
					redo();
					break;
				case 5:
					undo();
					break;
				case 6:
					// Space hat gleiche funktion wie linke Maustatste 
					// ruft daher entityClicked(.. , ..) auf
					this.gamePanel.entityClicked(cursor[0], cursor[1]);
					break;
				case 7:
					// New Game neues spiel wird gestartet
					// aktuelles Level neu starten!
					this.userMoveMenu.restartLevel();
					break;
				case 8:
					this.showNextPossibleMove();
				default:
					//do nothing...
			}
			
			//Synchronisation der aktuellen KeyData mit dem Controller 
			//für das Menü
			userMoveMenu.setMoveKeyData(moveKeyData);
		}

	}

	
	
	/**
	 * setzt den Eingabe Fokus auf Shisen Frame damit 
	 * Tastatureingaben verarbeitet werden können
	 */	
	public void setFocus()
	{
		this.frame.requestFocusInWindow();
	}
	
	/**
	 * Markiert einen möglichen nächsten Spielzug auf dem Spielfeld
	 */
	public void showNextPossibleMove()
	{
		try 
		{
			//Falls kein Zug mehr möglich ist, soll eine entspr. Meldung kommen
			if (Move.cpmCounter(levelParser) == 0)
				showMessageBox("Warnung", "Es ist kein Zug mehr möglich.");
			else
			{
				//hole einen möglichen nächsten Zug
				int[] coords = Move.hintInt(levelParser);
				
				//markiere die Spielsteine
				gamePanel.markBrick(coords[0], coords[1], Color.YELLOW);
				gamePanel.markBrick(coords[2], coords[3], Color.YELLOW);
				
				//Zwischenspeichern der Anzeige, damit bei einem Rechtsklick
				//oder einer Umfärbung die angezeigten Steine weiter bestehen
				firstHintBrick = new int[] { coords[0], coords[1] };
				secondHintBrick = new int[] { coords[2], coords[3] };
			}
		} 
		catch (Exception ex) 
		{
			System.out.println("Fehler in showNextPossibleMove - UserMove");
			ex.printStackTrace();
		}
	}
	
	/**
	 * Löst das aktuelle Level selbstständig
	 * Zur Visualisierung der Lösung wird ein paralleler Thread 
	 * gestartet, der die Mausklicks zu den gefundenen Spielzügen simuliert.
	 */
	public void solveLevel()
	{
		//Ausgabe mit Hinweis auf Dauer der Berechnungen
		showMessageBox("Achtung", "Je nach Rechenleistung kann die Berechnung\n" +
				"zum Lösen des Levels einige Zeit in Anspruch nehmen.");
			
		try 
		{
			Solver solver = new Solver(levelParser);
			//Falls Level nicht lösbar -> Ausgabe
			if (!solver.isSolvable())
				showMessageBox("Level unlösbar", "Das Level ist leider nicht lösbar.");
			//Ansonsten nach und nach Steine vom Spielfeld entfernen
			else
			{
				//Lösungsliste erzeugen
				LinkedList<int[][]> solution = new LinkedList<int[][]>();
				solution = solver.getSolution();
											
				//Simuliere Mausklicks
				solverThread = new GUISolverThread(this, gamePanel, solution);
				solverThread.start();
			}
		} 
		catch (Exception ex) 
		{
			System.out.println("Fehler in solveLevel - UserMove");
			ex.printStackTrace();
		} 

	}
	
	/**
	 * wird von ShisenFrameBorad aufgerufen um abzufragen ob Eingabe verarbeitet
	 * werden soll oder nicht
	 * @return boolean Eingabe Verarbeiten ja/nein
	 */
	public boolean reloadInput()
	{
		if(this.solverThread != null)
		{
			if(this.solverThread.isAlive())		
				return false;
			else
				return true;
		}
		else
			return true;
	}
}


	