package gdi1shisen.gamecontroller;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JOptionPane;

import gdi1shisen.datastore.KeyData;
import gdi1shisen.datastore.LevelParser;
import gdi1shisen.datastore.LevelGenerator;
import gdi1shisen.datastore.GamePlayer;
import gdi1shisen.datastore.UserMoveHistory;
import gdi1shisen.exceptions.InternalFailureException;
import gdi1shisen.exceptions.LevelParserInconsistentException;
import gdi1shisen.exceptions.ParameterOutOfRangeException;
import gdi1shisen.exceptions.SyntacticIncException;
import gdi1shisen.gui.FileDialog;
import gdi1shisen.gui.MessageBox;
import gdi1shisen.gui.ShisenFrame;
import gdi1shisen.gui.ShisenFrameBoard;

public class UserMoveMenu 
{
	private ShisenFrame frame;
	private LevelParser levelParser;
	private LevelParser levelClone;
	private UserMove userMoveController;
	private Timer timer;
	private GamePlayer player;
	private UserMoveHistory userMoveHistory;
	private Long currentGameTime;
	private KeyData moveKeyData;
	
	/**
	 * Konstruktor für den EingabeController der Menüleiste
	 * @param frame ShisenFrame Objekt des Spielfensters
	 */
	public UserMoveMenu(ShisenFrame frame)
	{
		this.frame = frame;
	}
	
	/**
	 * Setzen eines neuen TileSets:
	 * Legt bei bereits laufendem Spiel ein neues Gamepanel an
	 * mit dem neuen Tileset und lässt das Spiel auf diesem Panel
	 * weiterlaufen.
	 * @param tileSet String mit Pfad zum neuen Tileset-Verzeichnis
	 * @throws InternalFailureException
	 * @throws ParameterOutOfRangeException
	 * @throws LevelParserInconsistentException
	 */
	public void setNewTileSet(String tileSet)
	throws InternalFailureException, ParameterOutOfRangeException, 
		LevelParserInconsistentException
	{
		player.setTileSet(tileSet);
		
		//Falls bereits ein Spiel läuft, dann soll das
		//neue Tileset auf das bestehende Spiel angewendet werden
		if (this.frame.getGamePanel() != null)
		{
			//Überprüfen ob vor einem Tausch des TileSets die
			//LevelParser Objekte Konsistent sind
			if (!this.userMoveController.getLevelParser().equals(this.levelParser))
				throw new LevelParserInconsistentException
					("LevelParser zwischen UserMoveMenu Controller und " +
						"UserMove Controller inkonsistent.");
				
			//erstellen eines neuen Spielfeldes
			ShisenFrameBoard newGamePanel = this.frame.createGamePanel();
					
			//löschen des alten Spielfeldes und hinzufügen des neuen
			this.frame.removeGamePanel();
			this.frame.setGamePanel(newGamePanel);
			this.frame.addGamePanel();
			
			//dem MoveController mitteilen, dass das Spiel auf einem neuen GamePanel
			//weitergeht
			this.userMoveController.setGamePanel(newGamePanel);
			
			//Anbindung des Spielfeldes an bestehendes Level- und Controllerobjekt
			newGamePanel.setMoveController(this.userMoveController);
			newGamePanel.setTileSet(tileSet);
			newGamePanel.setLevel(this.levelParser.getLevel());
		}
		//Falls kein Spiel läuft, MessageBox mit Statusmeldung
		else
		{
			new MessageBox(this.frame, "Spielsteinänderung", "Ihre Änderung der Spielsteine " +
					"wird beim\n Start des nächsten Levels aktiv.");
		}
	}
	
	/**
	 * Setzt ein neues Level in das Spielfenster
	 * @param level String mit Pfad des Levels
	 * @throws SyntacticIncException
	 * 				Falls beim Parsen des Levels Fehler auftreten
	 */
	public void setNewLevel(String level)
	throws Exception
	{
		//Anhalten des parallelen Timer Threads aus vorherigen Leveln
		//falls einer läuft
		if (timer != null)
			timer.stopTimer();
		
		//erzeugen eines neuen LevelParser Objekts für das neue Level
		//und setzen der "aktuelles Level" Informationen für das Player Objekt 
		if (level.equals("randomLevel"))
		{
			this.levelParser = new LevelParser(LevelGenerator.generateSolvable());
			player.setActualLevel("RANDOM");
		}	
		else
		{
			this.levelParser = new LevelParser(level);
			player.setActualLevel(level);
		}
		
		//entfernen aller vorherigen Elemente im CENTER des Fensters
		if (frame.getGamePanel() == null)
			frame.removeSplashScreen();
		else
			frame.removeGamePanel();
		frame.pack();
		
		//erstellen eines neuen Spielfeldes
		ShisenFrameBoard gamePanel = frame.createGamePanel();
				
		//zuordnen des Spielfeldes zum Spielfenster und Anzeige
		//von Spielfeld und Infoleiste
		frame.setGamePanel(gamePanel);
		frame.addGamePanel();
		frame.addIconBar();
		frame.addInfoBar();
		
		//erzeugen eines neuen MoveControllers für das Spielfeld und Übergabe
		//an das Spielfeld, Zwischenspeichern des MoveControllers für laden
		//eines neuen TileSets
		UserMove userMoveController = new UserMove(frame, levelParser, this, player);
		this.userMoveController = userMoveController;
		gamePanel.setMoveController(userMoveController);
		frame.getIconBar().setMoveController(userMoveController);
		
		// setzten des geklonten LevelParser (Anfangszustand)
		this.levelClone=this.levelParser.clone();
		
		//Übergeben der Leveldaten an das Spielfeld und Darstellung im Fenster
		gamePanel.setTileSet(player.getTileSet());
		gamePanel.setLevel(this.levelParser.getLevel());
	}
	
	/**
	 * Eingabe eines neuen Spielernamens
	 */
	public void newPlayerName()
	{
		//Dialogbox, die nach neuem Namen fragt erzeugen
		String newName = JOptionPane.showInputDialog
		(
				frame,
				"Wie lautet Ihr Name?", 
				"Geben Sie bitte Ihren Namen ein",
				JOptionPane.QUESTION_MESSAGE
		);
		
		//Name im Player Objekt ändern
		player.setPlayerName(newName);
		
		//Name in der Infobar aktualisieren
		frame.getInfoBar().setPlayerName(newName);
	}
	
	/**
	 * Speichert ein Level und dazugehörige Objekte in einer Datei ab
	 */
	public void saveLevel()
	{
		//falls noch kein UserMove Controller angelegt wurde, wurde auch
		//noch kein Level gestartet -> es kann nichts gespeichert werden
		if (userMoveController == null)
			ShowMessageBox("Warnung", "Da noch kein Level gestartet wurde, kann nichts\n" +
					"gespeichert werden.");
		else
		{
			//öffnen des Dialogfensters für Dateien und erfragen des Dateinamens
			FileDialog fd = new FileDialog(frame);
			String filename = fd.saveFileDialog();
			
			if (filename != null)
			{
				//Speichern der nötigen Level Objekte
				FileOutputStream fos = null;
				ObjectOutputStream out = null;
				
				try
				{
					fos = new FileOutputStream(filename);
					out = new ObjectOutputStream(fos);
					out.writeObject(levelParser);
					out.writeObject(levelClone);
					out.writeObject(userMoveHistory);
					out.writeObject(player);
					out.writeObject(moveKeyData);
					out.writeObject(timer.getCurrentGameTime());
					out.close();
				}
				catch (IOException ex)
				{
					System.out.println("Fehler beim Speichern (LevelSaver)");
					ex.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Lädt ein Level und zugehörige Objekte aus einer Datei und
	 * erstellt ein neues Spielfeld mit den gegebenen Daten
	 */
	public void loadLevel()
	{
		//öffnen des Dialogfensters für Dateien und erfragen des Dateinamens
		FileDialog fd = new FileDialog(frame);
		String filename = fd.loadFileDialog();
		
		//Falls Datei geladen werden soll -> aktuellen Timer stoppen
		if (filename != null && timer != null)
			timer.stopTimer();
		
		//einlesen der benötigten Level Objekte aus der Datei,
		//entsprechende Objekte werden automatisch in der Instanz dieses
		//Menüleisten Controllers überschrieben
		if (filename != null)
		{
			try
			{
				//Objekte aus Datei auslesen und deserialisieren
				FileInputStream fis = new FileInputStream(filename);
				ObjectInputStream in = new ObjectInputStream(fis);
				levelParser = (LevelParser)in.readObject();
				levelClone = (LevelParser)in.readObject();
				userMoveHistory = (UserMoveHistory)in.readObject();
				player = (GamePlayer)in.readObject();
				moveKeyData = (KeyData)in.readObject();
				currentGameTime = (Long)in.readObject();
			}
			catch (Exception ex)
			{
				System.out.println("Fehler beim Laden (LevelSaver)");
				ex.printStackTrace();
			}

			//setzen des neuen Spielernamens
			frame.getInfoBar().setPlayerName(player.getPlayerName());
			
			//entfernen aller vorherigen Elemente im CENTER des Fensters
			if (frame.getGamePanel() == null)
				frame.removeSplashScreen();
			else
				frame.removeGamePanel();
			frame.pack();
			
			//erstellen eines neuen Spielfeldes
			ShisenFrameBoard gamePanel = frame.createGamePanel();
					
			//zuordnen des Spielfeldes zum Spielfenster und Anzeige
			//von Spielfeld und Infoleiste
			frame.setGamePanel(gamePanel);
			frame.addGamePanel();
			frame.addIconBar();
			frame.addInfoBar();
			
			//erzeugen eines neuen MoveControllers für das Spielfeld und Übergabe
			//an das Spielfeld, Zwischenspeichern des MoveControllers für laden
			//eines neuen TileSets
			UserMove userMoveController = new UserMove(frame, levelParser, this, 
					userMoveHistory, moveKeyData, currentGameTime, player);
			this.userMoveController = userMoveController;
			gamePanel.setMoveController(userMoveController);
			frame.getIconBar().setMoveController(userMoveController);
			
			//Übergeben der Leveldaten an das Spielfeld und Darstellung im Fenster
			gamePanel.setTileSet(player.getTileSet());
			try 
			{
				gamePanel.setLevel(this.levelParser.getLevel());
			}
			catch (Exception e) 
			{
				System.out.println("Fehler beim Laden des Levels - UserMoveMenu");
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * startet ein Level neu wird von UserMove aufgerufen 
	 * @param startstate bekommt einen Klon des Anfangszustandes (levelparser) übergeben
	 */
	public void restartLevel()
	{
		//Anhalten des parallelen Timer Threads aus vorherigen Leveln
		//falls einer läuft
		if (timer != null)
			timer.stopTimer();
		
		//erzeugen eines neuen LevelParser Objekts für das neue Level
		this.levelParser = levelClone;
		
		//entfernen aller vorherigen Elemente im CENTER des Fensters
		if (frame.getGamePanel() == null)
			frame.removeSplashScreen();
		else
			frame.removeGamePanel();
		frame.pack();
		
		//erstellen eines neuen Spielfeldes
		ShisenFrameBoard gamePanel = frame.createGamePanel();
				
		//zuordnen des Spielfeldes zum Spielfenster und Anzeige
		//von Spielfeld und Infoleiste
		frame.setGamePanel(gamePanel);
		frame.addGamePanel();
		frame.addIconBar();
		frame.addInfoBar();
		
		//erzeugen eines neuen MoveControllers für das Spielfeld und Übergabe
		//an das Spielfeld, Zwischenspeichern des MoveControllers für laden
		//eines neuen TileSets
		UserMove userMoveController = new UserMove(frame, levelClone, this, player);
		this.userMoveController = userMoveController;
		gamePanel.setMoveController(userMoveController);
		frame.getIconBar().setMoveController(userMoveController);
		
		//Übergeben der Leveldaten an das Spielfeld und Darstellung im Fenster
		gamePanel.setTileSet(player.getTileSet());
		try
		{
			gamePanel.setLevel(this.levelClone.getLevel());
		}
		catch(Exception e)
		{
			System.out.println("Fehler in restartLevel()");
			e.printStackTrace();
		}
	}
	
	/**
	 * Zeigt eine Dialogbox im Spielfenster an
	 * @param title String Titel der Dialogbox
	 * @param text String Text der Dialogbox
	 */
	public void ShowMessageBox(String title, String text)
	{
		new MessageBox(frame, title, text);
	}
	
	/**
	 * Setzt ein neues Timer Objekt
	 * Diesen bekommt man von einem Spielfeld geliefert was gerade
	 * initialisiert wird, um bei einem asynchronen Wechsel des
	 * Levels über die Menüleiste den parallel laufenden Timer-Thread
	 * anhalten zu können.
	 * @param timer Timer Objekt des aktuellen Spielfeldes
	 */
	public void setTimer(Timer timer)
	{
		this.timer = timer;
	}
	
	/**
	 * Setzt ein neues Spielfenster
	 * @param frame ShisenFrame Objekt des Spielfensters
	 */
	public void setFrame(ShisenFrame frame)
	{
		this.frame = frame;
	}
	
	/**
	 * Setzt einen neuen LevelParser
	 * @param levelParser Objekt des LevelParsers
	 */
	public void setLevelParser(LevelParser levelParser)
	{
		this.levelParser = levelParser;
	}
	
	/**
	 * Setzt eine neues User Move History Objekt
	 * @param userMoveHistory UserMoveHistory Objekt
	 */
	public void setUserMoveHistory(UserMoveHistory userMoveHistory)
	{
		this.userMoveHistory = userMoveHistory;
	}
	
	/**
	 * Setzt ein neues Spieler Objekt
	 * @param player Player Objekt
	 */
	public void setGamePlayer(GamePlayer player)
	{
		this.player = player;
	}
	
	/**
	 * Setzt ein neues KeyData Objekt für die Synchronisation
	 * der Tastatureingaben
	 * @param moveKeyData
	 */
	public void setMoveKeyData(KeyData moveKeyData)
	{
		this.moveKeyData = moveKeyData;
	}
	
	public boolean reloadInput()
	{
		if(this.userMoveController==null)
			return true;
		else
			return this.userMoveController.reloadInput();
		
			
	}
	
}
