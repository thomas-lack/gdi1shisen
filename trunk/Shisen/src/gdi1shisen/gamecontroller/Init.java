package gdi1shisen.gamecontroller;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import gdi1shisen.datastore.GamePlayer;
import gdi1shisen.gui.ShisenFrame;

public class Init 
{

	/**
	 * Hauptfunktion für Shisen - stößt Controllerfunktionen, 
	 * Spielfeldgenerierung und Data-Modeling an.
	 * 
	 * @param args
	 */
	public static void main(String[] args) 
	{
        //Umstellen des Look & Feel auf Systemabhängige Anzeige
		try 
		{
			UIManager.setLookAndFeel(
			    UIManager.getSystemLookAndFeelClassName());
		} 
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (InstantiationException e) 
		{
			e.printStackTrace();
		} 
		catch (IllegalAccessException e) 
		{
			e.printStackTrace();
		} 
		catch (UnsupportedLookAndFeelException e) 
		{
			e.printStackTrace();
		}
		//Erstellen eines neuen Fensters 
		ShisenFrame frame = new ShisenFrame("Little Buddha Shisen");
		
		//Erstellen eines neuen Spielers
		GamePlayer player = new GamePlayer();
		
		//Erstellen eines neuen UserMoveMenu Objekts zur Steuerung des Fenster Menüs
		UserMoveMenu moveMenuController = new UserMoveMenu(frame);
		moveMenuController.setGamePlayer(player);
		
		//Hinzufügen wichtiger Elemente zum Spielfenster
		//und Anzeige auf dem Bildschirm
		frame.createMenuBar(moveMenuController);
		frame.addSplashScreen();
		frame.showFrame();
	}
}
