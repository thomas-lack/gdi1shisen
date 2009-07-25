package gdi1shisen.gui;

import gdi1shisen.gamecontroller.UserMoveMenu;
import javax.swing.*;

import java.awt.event.*;
import gdi1shisen.gui.SpielRegeln;
import gdi1shisen.gui.SpielSteuerung;

/**
 * Namespacing für Menüleisten Eintrage im Spielfenster
 * @author Thomas Lack
 *
 */
public class ShisenFrameMenuBar 
extends JMenuBar
implements ActionListener
{
	private static final long serialVersionUID = -2814690862433818430L;
	
	//Verknüpfung zum Controller Objekt für Menü Eingaben
	private UserMoveMenu moveMenuController;
	
	/**
	 * 	Konstruktor für WindowFrameMenuBar
	 *  Fügt in dieser Klasse erstellte Menüs zur Menüleiste hinzu
	 *  und stellt Verknüpfung zum Controller für Menüeingaben her
	 *  @param UserMoveMenu Objekt für Menü Controller
	 */
	public ShisenFrameMenuBar(UserMoveMenu moveMenuController)
	{
		this.moveMenuController = moveMenuController;
		
		add(createGameMenu());
		add(createLevelMenu());
		add(createTileSetMenu());
		add(createHelpMenu());
	}
	
	/**
	 * Action Handler
	 */
	public void actionPerformed(ActionEvent event)
	{
		//TODO
		//richtige Handler für angeklickte Aktionen
		String e = event.getActionCommand();
		if (e.equals("Beenden"))
			System.exit(0);
		else if (e.equals("Über"))
		{
			About about = new About("Über uns");
			about.setVisible(true);
		}
		else if (e.equals("Spielregeln"))
		{
		      SpielRegeln spregeln = new SpielRegeln();
	          spregeln.setVisible(true);
		    }
		else if (e.equals("Spielsteuerung"))
		{
			 SpielSteuerung spsteuer = new SpielSteuerung();
	         spsteuer.setVisible(true);
		}
		else if (e.equals("Zufallslevel"))
		{
			try
			{
				this.moveMenuController.setNewLevel("randomLevel");
			}
			catch (Exception ex)
			{
				System.out.println("Fehler in ShisenFrameMenuBar - actionPerformed");
				System.err.println(ex);
			}
		}
		else if (e.equals("001") || e.equals("002") || e.equals("003") || e.equals("004")
				|| e.equals("005") || e.equals("006") || e.equals("007") || e.equals("008")
				|| e.equals("009") || e.equals("010"))
		{
			try
			{
				this.moveMenuController.setNewLevel("Levels/" + e + ".lvl");	
			}
			catch (Exception ex)
			{
				System.out.println("Fehler in ShisenFrameMenuBar - actionPerformed");
				System.err.println(ex);
			}
		}
		else if (e.equals("Default") || e.equals("Wood") || e.equals("Bricks"))
		{
			try
			{
				this.moveMenuController.setNewTileSet("Images/" + e + "/");
			}
			catch (Exception ex)
			{
				System.out.println("Fehler in actionPerformed() - ShisenFrameMenuBar");
				ex.printStackTrace();
			}
		}
		else if(e.equals("Spielername"))
		{
			this.moveMenuController.newPlayerName();
		}
		else if(e.equals("Spiel speichern"))
		{
			this.moveMenuController.saveLevel();
		}
		else if(e.equals("Spiel laden"))
		{
			this.moveMenuController.loadLevel();
		}
		else
			System.out.println(e);
	}
	
	/**
	 * Untermenü "Spiel" in der Menüleiste
	 * @return JMenu Menü "Spiel"
	 */
	private JMenu createGameMenu()
	{
		JMenu ret = new JMenu("Spiel");
		ret.setMnemonic('S');
		JMenuItem mi;
		
		//Namen setzen
		mi = new JMenuItem("Spielername", 'n');
		mi.addActionListener(this);
		ret.add(mi);
		
		//Separator
		ret.addSeparator();
		
		//Spiel laden
		mi = new JMenuItem("Spiel laden", 'l');
		mi.addActionListener(this);
		ret.add(mi);
		
		//Spiel speichern
		mi = new JMenuItem("Spiel speichern", 's');
		mi.addActionListener(this);
		ret.add(mi);
		
		//Separator
		ret.addSeparator();
		
		//Spiel beenden
		mi = new JMenuItem("Beenden", 'e');
		mi.addActionListener(this);
		ret.add(mi);
		return ret;
	}
	
	/**
	 * Untermenü "Level" in der Menüleiste
	 * @return JMenu Menü "Level"
	 */
	private JMenu createLevelMenu()
	{
		JMenu ret = new JMenu("Spielsteine");
		ret.setMnemonic('T');
		JMenuItem mi;
		
		//Bricks
		mi = new JMenuItem("Default", 'd');
		mi.addActionListener(this);
		ret.add(mi);
		
		//Wood
		mi = new JMenuItem("Wood", 'w');
		mi.addActionListener(this);
		ret.add(mi);
		
		//Default
		mi = new JMenuItem("Bricks", 'b');
		mi.addActionListener(this);
		ret.add(mi);
		
		return ret;
	}
	
	/**
	 * Untermenü "TileSet" in der Menüleiste
	 * @return JMenu Menü "TileSet"
	 */
	private JMenu createTileSetMenu()
	{
		JMenu ret = new JMenu("Level");
		ret.setMnemonic('L');
		JMenuItem mi;
		
		for (int i=1; i<= 10; i++)
		{
			String lvlText;
			if (i<10)
				lvlText = "00" + i;
			else
				lvlText = "0" + i;
			mi = new JMenuItem(lvlText, (char)i);
			mi.addActionListener(this);
			ret.add(mi);
			
		}
		/*
		//Level 001
		mi = new JMenuItem("001", '1');
		mi.addActionListener(this);
		ret.add(mi);
		
		//Level 002
		mi = new JMenuItem("002", '2');
		mi.addActionListener(this);
		ret.add(mi);
		
		//Level 003
		mi = new JMenuItem("003", '3');
		mi.addActionListener(this);
		ret.add(mi);
		*/
		
		//Separator
		ret.addSeparator();
		
		//Zufällig generiertes Level (lösbar)
		mi = new JMenuItem("Zufallslevel", 'z');
		mi.addActionListener(this);
		ret.add(mi);
		
		return ret;
	}
	
	
	/**
	 * Untermenü "Hilfe" in der Menüleiste
	 * @return JMenu Menü "Hilfe"
	 */
	private JMenu createHelpMenu()
	{
		JMenu ret = new JMenu("Hilfe");
		ret.setMnemonic('H');
		JMenuItem mi;
		//Spielregeln
		mi = new JMenuItem("Spielsteuerung", 'u');
		mi.addActionListener(this);
		ret.add(mi);
		//Separator
		ret.addSeparator();
		mi = new JMenuItem("Spielregeln", 'r');
		mi.addActionListener(this);
		ret.add(mi);
		//Separator
		ret.addSeparator();
		
		//Über uns
		mi = new JMenuItem("Über", 'b');
		mi.addActionListener(this);
		ret.add(mi);
		return ret;
	}
}