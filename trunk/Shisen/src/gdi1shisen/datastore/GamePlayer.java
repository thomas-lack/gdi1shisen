package gdi1shisen.datastore;

import java.io.Serializable;

/**
 * Klasse zur Spielernamensverwaltung
 * @author Thomas
 */
public class GamePlayer implements Serializable
{
	private static final long serialVersionUID = 5680142680829370266L;
	private String name;
	
	/**
	 * Allg. Konstruktor für Spieler Objekt
	 */
	public GamePlayer()
	{
		name = "Spieler 1";
	}
	
	/**
	 * Spez. Konstruktor für Spieler Objekt
	 * @param name String mit dem Namen des Spielers
	 */
	public GamePlayer(String name)
	{
		this.name = name;
	}
	
	/**
	 * Setter für den Spielernamen
	 * @param name String des Spielernamens
	 */
	public void setPlayerName(String name)
	{
		this.name = name;
	}
	
	/**
	 * Getter für den Spielernamen
	 * @return String des Spielernamens
	 */
	public String getPlayerName()
	{
		return name;
	}	
}

