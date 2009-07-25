package gdi1shisen.datastore;

import java.io.Serializable;

/**
 * Klasse zur Spielerverwaltung, bestehend aus 
 * Name, gewähltem TileSet und aktuell gespieltem Level
 * @author Thomas
 */
public class GamePlayer implements Serializable
{
	private static final long serialVersionUID = 5680142680829370266L;
	private String name;
	private String tileSet = "Images/Default/";
	private String actualLevel;
	private String actualLevelHighscore;
	
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

	/**
	 * Setzen des Aktuellen Levelpfades als String
	 * @param actualLevel String
	 */
	public void setActualLevel(String actualLevel) {
		this.actualLevel = actualLevel;

		//gleichzeitig zum Speichern des aktuellen Levels
		//wird der Pfad zur Highscore Datei des aktuellen Levels
		//gespeichert
		setActualLevelHighscore(actualLevel.replace("lvl", "hsc"));
	}

	/**
	 * Getter für den aktuellen Levelpfad als String
	 * @return String
	 */
	public String getActualLevel() {
		return actualLevel;
	}

	/**
	 * Setzt das aktuell zu benutzende TileSet als Pfad
	 * @param tileSet String
	 */
	public void setTileSet(String tileSet) {
		this.tileSet = tileSet;
	}

	/**
	 * Getter für das aktuell benutzte TileSet als Pfad String
	 * @return String
	 */
	public String getTileSet() {
		return tileSet;
	}

	/**
	 * Setzt den Pfad für die aktuelle Highscore Datei
	 * @param actualLevelHighscore String
	 */
	public void setActualLevelHighscore(String actualLevelHighscore) {
		this.actualLevelHighscore = actualLevelHighscore;
	}

	/**
	 * Getter für den Pfad String der aktuellen Highscore Datei
	 * @return String
	 */
	public String getActualLevelHighscore() {
		return actualLevelHighscore;
	}
	
	
}

