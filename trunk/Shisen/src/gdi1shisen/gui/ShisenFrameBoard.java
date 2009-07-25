package gdi1shisen.gui;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JButton;
import gdi1shisen.exceptions.InternalFailureException;
import gdi1shisen.exceptions.ParameterOutOfRangeException;
import gdi1shisen.gamecontroller.UserMove;
import gdi1shisen.template.ui.GamePanel;
import javax.swing.border.*;

public class ShisenFrameBoard extends GamePanel
{
	private static final long serialVersionUID = 3860012215771424626L;
	
	//Spielfenster was das Spielfeld umschließt
	private ShisenFrame frame;
	
	//Anbindung an den MoveController
	private UserMove moveController;
	
	//Verwendetes TileSet zur Darstellung der Spielsteine
	private String tileSet;
	
	//Aktuelles Level
	private char[][] level;
	private int levelHeight;
	private int levelWidth;
	
	JButton[][] gameButton;
	
	/**
	 * Konstruktor für ein GamePanel
	 * @param parentWindow ShisenFrame Objekt in dem das Panel angezeigt wird 
	 * @param ds Objekt zur Anbindung an den DataStore
	 */
	public ShisenFrameBoard(ShisenFrame parentWindow)
	{
		//Erzeugen des Spielfeldes innerhalb des Fensters
		super(parentWindow);
		
		this.frame = parentWindow;
		
		//Hintergrundfarbe auf dunkles Grau setzen
		this.setBackground(Color.DARK_GRAY);
	}
	
	/**
	 * Setzen eines neuen TileSets
	 * @param tileSetNew String des Pfads zu den TileSet Images
	 */
	public void setTileSet(String tileSetNew)
	{
		this.tileSet = tileSetNew;
	}
	
	/**
	 * Setzen eines neuen Levels 
	 * @param levelNew char[][] Array eines Levels
	 */
	public void setLevel(char[][] levelNew)
	throws InternalFailureException, ParameterOutOfRangeException
	{
		//speichern der relevanten Leveldaten
		this.level = levelNew;
		this.levelHeight = levelNew.length;
		this.levelWidth = levelNew[0].length;
		
		//Aufruf, dass Level geladen wurde - Anstoss Spielfeldaufbau
		this.notifyLevelLoaded(this.levelWidth, this.levelHeight);
	}
	
	/**
	 * Ersetzt ein bereits bestehendes / gesetztes Level
	 * @param levelNew char[][] Array eines Levels
	 */
	public void replaceLevel(char[][] levelNew)
	{
		this.level = levelNew;
	}
	
	/**
	 * Anpassen des Spielfeldes auf benötigte Darstellungsgröße
	 */
	public void setAutosize()
	{
		this.setAutosize(true);
	}
	
	/**
	 * Zuordnung relevanter Daten zur Spielfelddarstellung.
	 * (siehe gdi1shisen.template.ui.GamePanel.java)
	 * Es wird eine Mapping der Bilder aus dem TileSet auf
	 * die einzelnen Zeichen eines Levels vollzogen.
	 * Anhand dieses Mappings werden entsprechend bebilderte
	 * JButtons auf das Spielfeldgrid gelegt. 
	 */
	public void setGamePanelContents() 
	{
		String[] brickMap = 
		{
				"A", "B", "C", "D", "E", "F", "G", "H", "I",
				"J", "K", "L", "M", "N", "O", "P", "Q", "R",
				"S", "T", "U", "V", "W", "X", "Y", "Z", "0", 
				"1", "2", "3", "4", "5", "6", "7", "8", "9"
		};
		String brickPath = this.tileSet;
		String tileFile;
		
		try
		{
			//Registrieren der verwendeten TileSet Bilder
			for (int i=0; i<36; i++)
			{
				if ((i+1)<10)
					tileFile = "0" + (i+1) + ".png";
				else
					tileFile = (i+1) + ".png";
					
				registerImage(brickMap[i], brickPath + tileFile);
			}
			
			//registrieren eines beliebigen Spielsteins für das leere Feld '-'
			registerImage("-", brickPath + "36.png");
			
			//Positionierung von Bttons auf dem Grid
			this.gameButton = new JButton[this.levelHeight][this.levelWidth];
			for (int i=0; i < this.levelHeight; i++)
			{
				for (int j=0; j < this.levelWidth; j++)
				{
					//Falls ein leeres Feld '-' vorliegt, soll der Button
					//gleich nach dem Einfügen ausgeblendet werden
					if (this.level[i][j] == '-')
					{
						gameButton[i][j] = placeEntity(String.valueOf(this.level[i][j]));
						gameButton[i][j].setVisible(false);
					}
					//Ansonsten soll nur der Button auf dem Feld platziert werden
					else
					{
						gameButton[i][j] = placeEntity(String.valueOf(this.level[i][j]));
					}
				}
			}
			
			// markiert den ersten Spielstein nachdem Spielfeld aufgebaut ist
			this.gameButton[1][1].setEnabled(false);
			
			// setze Eingabe Fokus zurück auf ShisenFrame 
			this.frame.requestFocusInWindow();
		}
		catch (Exception ex)
		{
			System.out.println("Fehler in setGamePanelContens()");
			ex.printStackTrace();
		}
	}
	
	/**
	 * Setzt den Controller für Nutzereingaben
	 * @param moveController UserMove Objekt zur Kontrolle der Nutzereingaben
	 */
	public void setMoveController(UserMove moveController)
	{
		this.moveController = moveController;
	}
	
	/**
	 * Übergabe des aktuellen Spielzuges an den Move Controller
	 * @param positionX Position X des angeklickten Spielsteins
	 * @param positionY Position Y des angeklickten Spielsteins
	 * @throws InternalFailureException 
	 */
	public void entityClicked(int positionX, int positionY) 
	{
		this.moveController.newBrickClicked(positionX, positionY);
	}
	
	/**
	 * Gibt die Position eines mit rechts angeklickten Spielsteins
	 * an den moveController weiter
	 */
	public void entityRightPressed(int positionX, int positionY)
	{
		this.moveController.showMatchingBricks(positionX, positionY);
	}
	
	/**
	 * Teilt dem moveController mit, das die rechte Maustaste nicht
	 * mehr gedrückt wird 
	 */
	public void entityRightReleased()
	{
		this.moveController.hideMatchingBricks();
	}
	
	/**
	 * Entfernen von zwei Spielsteinen vom Spielfeld
	 * @param button1 int[] x und y-Koordinate des ersten Spielsteins
	 * @param button2 int[] x und y-Koordinate des zweiten Spielsteins
	 */
	public void removeButtons(int[] button1, int[] button2)
	{
		gameButton[button1[1]][button1[0]].setVisible(false);
		gameButton[button2[1]][button2[0]].setVisible(false);
	}
	
	/**
	 * Lässt einen Spielstein wieder auf dem Spielfeld anzeigen
	 * Um sicherzustellen, dass nach einem TileSet Wechsel im laufenden Spiel
	 * die korrekte Spielsteingrafik angezeigt wird, wird vor dem Anzeigen des 
	 * Steins noch einmal die Grafik auf dem Button gesetzt.
	 * 
	 * @param x Integer x-Koordinate des Steins
	 * @param y Integer y-Koordinate des Steins
	 * @param symbol char der einer internen Darstellung eins Spielsteins entspricht
	 */
	public void showButtons(int x, int y, char symbol)
	{
		refreshImage(gameButton[y][x], String.valueOf(symbol));
		gameButton[y][x].setVisible(true);
	}
	
	/**
	 * Anpassen der Spielfenstergröße nachdem das Spielfeld in der Größe verändert wurde
	 */
	public void panelResized()
	{
		this.frame.pack();
	}
	
	/**
	 * Markiert einen Spielstein mit blauem Rahmen
	 * @param x Integer x-Koordinate des Steins
	 * @param y Integer y-Koordinate des Steins
	 */
	public void markBrick(int x, int y)
	{
		gameButton[y][x].setBorder(new LineBorder(Color.BLUE, 2));
	}
	
	public void markBrick(int x, int y, Color color)
	{
		gameButton[y][x].setBorder(new LineBorder(color, 2));
	}
	
	/**
	 * De-Markiert einen Spielstein mit blauem Rahmen
	 * @param x Integer x-Koordinate des Steins
	 * @param y Integer y-Koordinate des Steins
	 */
	public void unMarkBrick(int x, int y)
	{
		gameButton[y][x].setBorder(null);
	}
	
	
	/**
	 * Setzt den State von einem Spielstein neu
	 * @param x int x-Koordinate des Spielsteins
	 * @param y int y-Koordinate des Spielsteins
	 * @param state boolean 
	 */
	public void changeBrickState(int x, int y, boolean state)
	{
		gameButton[y][x].setEnabled(state);
			
	}
	
	/**
	 * Setzt den Rahmen aller Spielsteine auf Ursprungszustand
	 */
	public void unMarkAllBricks()
	{
		for (int i=0; i < gameButton.length; i++)
			for (int j=0; j < gameButton[0].length; j++)
			{
				gameButton[i][j].setBorder(new LineBorder(Color.GRAY, 1));
				gameButton[i][j].requestFocus();
			}		
	}
	
	/**
	 * 
	 * @return Höhe eines Spielsteins
	 */
	public int getBrickHeight()
	{
		return this.getComponent(0).getHeight();
	}
	
	
	/**
	 * 
	 * @return Breite eines Spielsteins
	 */
	public int getBrickWidth()
	{
		return this.getComponent(0).getWidth();
	}
	
	/** 
	 * Zeichnet eine Linie vom Punkt (x0,y0) bis Punkt (x1,y1) 
	 * indem es die entsprechende übeschriebene Paint Methode aufruft
	 * und dieser die Koordinaten übergibt
	 * 
	 * @param x0 x - Koordinate von Anfangspunkt
	 * @param y0 y - Koordinate von Anfangspunkt
	 * @param x1 y - Koordinate von Endpunkt
	 * @param y2 y - Koordinate von Endpunkt
	 */
	public void drawLine(int x0,int y0,int x1,int y2)
	{
		this.paint(this.getGraphics(),x0,y0,x1,y2);
	}
	
	/**
	 * Arbeitet analog zu drawLine außer das hier ein Array von x,y Koordinaten
	 * anstatt einzelner Koordinaten übergeben wird
	 * 
	 * @param path Array von x-y Koordinaten wobei die 
	 * 		  2. Dimension die Länge 2 hat [..][1] ist x und [..][2] ist y
	 * 
	 * 
	 */
	public void drawPath(int [][] path)
	{
		this.paint(this.getGraphics(), path);
	}
	
	/**
	 * 
	 * Überschriebene paint Methode die von drawLine aufgerufen wird
	 * Parameter entsprechen denen von drawLine() bis auf g
	 * g ist ein Graphikobjekt das Informationen über das GamePanel beinhaltet
	 * 
	 * @param g
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 */
	private void paint(Graphics g,int x0,int y0,int x1,int y1)
	{
		g.setColor(Color.green);
		g.drawLine(x0, y0, x1, y1);
	}
	
	/**
	 * Überschriebene paint Methode die von drawPath aufgerufen wird
	 * Parameter entspicht dem von drawPath() bis auf g
	 * g ist ein Graphikobjekt das Informationen über das GamePanel beinhaltet
	 * 
	 * @param g
	 * @param path
	 */
	private void paint(Graphics g, int[][] path)
	{
		g.setColor(Color.green);
		int i=0;
		while(i+1<path.length)
		{
			g.drawLine(path[i][0], path[i][1], path[i+1][0], path[i+1][1]); 
			i++;
		}
	}
	
	/**
	 * keyPressed wird von ShisenFrame aus in der jeweiligen Funktion für
	 * entsprechende Keys aufgerufen. 
	 * Die Methode übergibt den Fokus an UserMove wo in der gleichnamigen
	 * Methode keyPressed(Sting key) die eigentlich abarbeitung stattfindet
	 * @param key representiert gedrückte Taste durch einen Integer
	 */
	public void keyPressed(int key)
	{
		moveController.keyPressed(key);
	}
}
