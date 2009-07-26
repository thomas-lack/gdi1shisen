package gdi1shisen.gui;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import gdi1shisen.gamecontroller.UserMoveMenu;
import gdi1shisen.template.ui.GameWindow;

public class ShisenFrame extends GameWindow{
	
	private static final long serialVersionUID = -5577672970772938741L;
	//Das Spielfeld Objekt was in diesem Fenster angezeigt wird
	private ShisenFrameBoard gamePanel;
	
	//Die InfoBar die in diesem Fenster angezeigt wird
	private ShisenFrameInfoBar infobar = new ShisenFrameInfoBar();
	
	//SplashScreen Objekt, was angezeigt werden kann wenn kein Spielfeld aktiv ist
	private SplashScreen splashScreen = new SplashScreen();
	
	private ShisenFrameIconBar iconbar = new ShisenFrameIconBar(gamePanel);
	
	/**
	 * Konstruktor für ein Spielfenster
	 * @param ds DataStore Objekt das alle relevanten Daten beinhaltet
	 */
	public ShisenFrame(String caption)
	{
		super(caption);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// setze Fokusierbar und setze InputFokus auf ShisenFrame
		this.setFocusable(true);
		this.requestFocusInWindow();
		
		// sorgt dafür das Shisen Frame Root für Fokus ist
		this.setFocusCycleRoot(true);
	}
		
	/**
	 * Fügt dem Spielfenster eine Menüleiste hinzu
	 * @param moveMenuController Controller Objekt für Nutzereingaben
	 */
	public void createMenuBar(UserMoveMenu moveMenuController)
	{
		this.setJMenuBar(new ShisenFrameMenuBar(moveMenuController));
	}
	
	/**
	 * Entfernt das aktuelle SplashScreen Panel vom Spielfenster
	 */
	public void removeSplashScreen()
	{
		this.remove(this.splashScreen);
	}
	
	/**
	 * Entfernt das aktuelle Spielfeld-Panel vom Spielfenster
	 */
	public void removeGamePanel()
	{
		this.remove(this.gamePanel);
	}
	
	public void addIconBar()
	{
		this.add(this.iconbar, BorderLayout.NORTH);
	}
	
	/**
	 * Hinzufügen des SplashScreens zum Fenster
	 */
	public void addSplashScreen()
	{
		this.add(this.splashScreen, BorderLayout.CENTER);
	}
	
	/**
	 * Hinzufügen eines Spielfeldes zum Fenster
	 */
	public void addGamePanel()
	{
		this.add(this.gamePanel, BorderLayout.CENTER);

		// setzt game panel als root für Fokus 
		this.gamePanel.setFocusCycleRoot(true);
	}
	
	/**
	 * Hinzufügen der InfoLeiste zum Fenster
	 */
	public void addInfoBar()
	{
		this.add(this.infobar, BorderLayout.SOUTH);
	}
	
	/**
	 * Zeigt das erzeugte Fenster (frame) auf dem Bildschirm an
	 */
	public void showFrame()
	{
		this.setLocation(100, 100);
		this.pack();
		this.setVisible(true);
	}
	
	/**
	 * Erstellen eines neuen GamePanels zur Anzeige des Spielfeldes
	 * @return ShisenFrameBoard Objekt des Gamepanels
	 */
	public ShisenFrameBoard createGamePanel()
	{
		ShisenFrameBoard gp = new ShisenFrameBoard(this);
		return gp;
	}
	
	/**
	 * Setzt ein neues Spielfeld für das Fenster
	 * @param gamePanel ShisenFrameBoard Objekt (Spielfeld)
	 */
	public void setGamePanel(ShisenFrameBoard gamePanel)
	{
		this.gamePanel = gamePanel;
	}
	
	/**
	 * Getter für das GamePanel des Spielfensters
	 * @return ShisenFrameBoard Objekt des Gamepanels
	 */
	public ShisenFrameBoard getGamePanel()
	{
		return this.gamePanel;
	}
	
	/**
	 * Getter für die Infobar des Spielfensters
	 * @return ShisenFrameInfoBar Objekt der Infobar
	 */
	public ShisenFrameInfoBar getInfoBar()
	{
		return this.infobar;
	}
	
	/**
	 * Getter für die Iconbar des Spielfensters
	 * @return ShisenFrameIconBar Objekt der Iconbar
	 */
	public ShisenFrameIconBar getIconBar()
	{
		return this.iconbar;
	}
	
	public void keyLeftPressed()
	{
		if(gamePanel!=null)
		gamePanel.keyPressed(0);
	}
	
	public void keyDownPressed()
	{
		if(gamePanel!=null)
		gamePanel.keyPressed(1);
	}
	
	public void keyRightPressed()
	{
		if(gamePanel!=null)
		gamePanel.keyPressed(2);
	}

	public void keyUpPressed()
	{
		if(gamePanel!=null)
		gamePanel.keyPressed(3);
	}
	
	protected void keyNewGamePressed() {
		/* ShisenFrame frame = new ShisenFrame("Little Buddha Shisen");
		frame.removeGamePanel();
		frame.setGamePanel(createGamePanel());
	    frame.addGamePanel(); */
		if(gamePanel!=null)
	    gamePanel.keyPressed(7);
	    /*
		//Erstellen eines neuen Fensters 
		ShisenFrame frame = new ShisenFrame("Little Buddha Shisen");
		
		//Erstellen eines neuen UserMoveMenu Objekts zur Steuerung des Fenster Menüs
		UserMoveMenu moveMenuController = new UserMoveMenu(frame);
		
		//Hinzufügen wichtiger Elemente zum Spielfenster
		//und Anzeige auf dem Bildschirm
		frame.createMenuBar(moveMenuController);
		frame.addSplashScreen();
		frame.showFrame();
*/
		//schmog System.load("002.lvl");
		/*try {
		 * 
		 * (MINABS IST hier noch zu Ã¤ndern, zu der klasse die am ende die meisten rechte hat)
		 * SOUND ABSPIELE(NOCH ZU IMPL)
		 * MinAbs.resetAllValues();
		 * PAUSE(noch zu implementieren)= false
		 * clock.reset();
			time(NOCH ZU IMPL).setForeground(Color.black);
			time(NOCH ZU IMPL).setText("0");
			game(NOCH ZU IMPL) = new MinAbs();
			game(NOCH ZU IMPL).LevelParser();
			ShisenFrame.panel.redraw();
			//todo
		}
		catch (IOException e){
			System.out.println(e.getMessage());
		} catch (InternalFailureException e) {
			System.out.println(e.getMessage());
		} catch (SyntacticIncException e) {
			e.printStackTrace();
		} catch (ParameterOutOfRangeException e) {
			e.printStackTrace();
		}*/
	}
	public void keyQuitPressed()
	{
		

	
		System.exit(0);
	
	}
	public void keyRedoPressed()
	{
		if(gamePanel!=null)
		gamePanel.keyPressed(4);
	}
	
	public void keyUndoPressed()
	{
		if(gamePanel!=null)
		gamePanel.keyPressed(5);
	}
	
	public void keyOtherPressed(KeyEvent key)
	{
		if(gamePanel!=null)
			if(key.getKeyCode()==32)
			{
				gamePanel.keyPressed(6);
			}
			if(key.getKeyChar()=='h')
			{
				gamePanel.keyPressed(8);
			}
	}
}
