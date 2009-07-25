package gdi1shisen.gui;

import javax.swing.*;

import java.awt.FlowLayout;


public class ShisenFrameInfoBar extends JToolBar 
{
	private static final long serialVersionUID = 7010987003226461530L;
	
	private JPanel playerName = new JPanel();
	private JLabel playerNameLabel = new JLabel();
	
	private JPanel playtime = new JPanel();
	private JLabel playtimeLabel = new JLabel();
	
	private JPanel noValidMove = new JPanel();
	private JLabel noValidMoveLabel = new JLabel();
	
	private JPanel countRestBricks = new JPanel();
	private JLabel countRestBricksLabel = new JLabel();
	
	private JPanel movesPossible = new JPanel();
	private JLabel movesPossibleLabel = new JLabel();
	
	private ImageIcon player = new ImageIcon("Images/player.png");
	private ImageIcon clock = new ImageIcon("Images/clock.png");
	private ImageIcon happysmiley = new ImageIcon("Images/smile.png");
	private ImageIcon sadsmiley = new ImageIcon("Images/notgood.png");
	private ImageIcon stones = new ImageIcon("Images/stones.png");
	private ImageIcon attention = new ImageIcon("Images/attention.png");
	
	/**
	 * Kontstruktor für die Infoleiste
	 */
	public ShisenFrameInfoBar()
	{
		//User soll Toolbar nicht verschieben können
		this.setFloatable(false);
		
		//Layout festlegen
		this.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		
		//Anzeigefelder definieren
		playerName.add(playerNameLabel);
		playerNameLabel.setIcon(player);
		playerName.setOpaque(false);
		playerNameLabel.setOpaque(false);
		playerNameLabel.setText("Spieler 1");
		
		playtime.add(playtimeLabel);
		playtimeLabel.setIcon(clock);
		playtime.setOpaque(false);
		playtimeLabel.setOpaque(false);
						
		countRestBricks.add(countRestBricksLabel);
		countRestBricksLabel.setIcon(stones);
		countRestBricks.setOpaque(false);
		countRestBricksLabel.setOpaque(false);
		
		movesPossible.add(movesPossibleLabel);
		movesPossibleLabel.setIcon(happysmiley);
		movesPossible.setOpaque(false);
		movesPossibleLabel.setOpaque(false);
		
		noValidMove.add(noValidMoveLabel);
		noValidMoveLabel.setIcon(attention);
		noValidMove.setOpaque(false);
		noValidMoveLabel.setOpaque(false);
		noValidMoveLabel.setText("Kein möglicher Zug!");
		noValidMove.setVisible(false);
		
		//Anzeigefelder zur Toolbar hinzufügen
		this.add(playerName);
		this.addSeparator();
		this.add(playtime);
		this.addSeparator();
		this.add(movesPossible);
		this.addSeparator();
		this.add(countRestBricks);
		this.addSeparator();
		this.add(noValidMove);
	}
	
	/**
	 * Setter für den Spielernamen
	 * @param name String mit Namen des Spielers
	 */
	public void setPlayerName(String name)
	{
		this.playerNameLabel.setText(name);
	}
	
	/**
	 * Setter für Anzeige der Anzahl restlicher Spielsteine
	 * @param text String mit Anzahl der restlichen Spielsteine
	 */
	public void setCountRestMoves(String text)
	{
		this.countRestBricksLabel.setText(text);
	}
	
	/**
	 * Setter für die Anzeige der Anzahl aktuell möglicher Spielzüge
	 * @param countRestMoves int mit Anzahl möglicher Spielzüge
	 */
	public void setMovesPossible(int countRestMoves)
	{
		if (countRestMoves == 0)
		{
			movesPossibleLabel.setIcon(sadsmiley);
			movesPossibleLabel.setText("Kein Zug mehr möglich!");
		}
		else
		{
			movesPossibleLabel.setIcon(happysmiley);
			//Kleiner Hack: countResMoves wird durch 2 geteilt, um die Zugmöglichkeiten
			//zuerst A dann B oder zuerst B dann A zu einer Möglichkeit zusammenzufassen
			movesPossibleLabel.setText("Züge möglich: " + String.valueOf(countRestMoves / 2));
		}	
	}
	
	/**
	 * Meldung "Kein gültiger Zug" anzeigen
	 */
	public void showNoValidMove()
	{
		this.noValidMove.setVisible(true);
	}
	
	/**
	 * Meldung "Kein gültiger Zug" verstecken
	 */
	public void hideNoValidMove()
	{
		this.noValidMove.setVisible(false);
	}
	
	/**
	 * Setter für Anzeige der bisher vergangenen Spielzeit
	 * @param text String mit vergangener Spielzeit
	 */
	public void setPlaytime(String text)
	{
		this.playtimeLabel.setText(text);
	}
}
