package gdi1shisen.gamecontroller;

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
