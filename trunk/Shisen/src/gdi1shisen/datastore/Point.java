package gdi1shisen.datastore;

import gdi1shisen.exceptions.ParameterOutOfRangeException;

/**
 * Jedes Feld ist ein Objekt der Klasse Punkt - entweder Brick (Spielstein) oder
 * Waypoint
 * 
 * @author chandra kirchrath [lo08itaq] : Gruppe 3
 * 
 * 
 */
public class Point {
	public LevelParser level;
	public char[][] parsedLevel;
	public int xCoord;
	public int yCoord;
	public int[] coord;
	public int pid;
	public char content;

	/**
	 * Contructor des Punktes x=0;y=0
	 * 
	 * @param pLevelObj
	 *            ein Objekt der Klasse LevelParser wird uebergeben
	 * @throws ParameterOutOfRangeException
	 */
	public Point(LevelParser pLevelObj) throws ParameterOutOfRangeException 
	{
		construct(pLevelObj, 0, 0);
	}

	/**
	 * Contructor des Punktes x:y
	 * 
	 * @param pLevelObj
	 *            ein Objekt der Klasse LevelParser wird uebergeben
	 * @param x
	 *            X-Koordinate des Punktes
	 * @param y
	 *            Y-Koordinate des Punktes
	 * @throws ParameterOutOfRangeException
	 */
	public Point(LevelParser pLevelObj, int x, int y)
			throws ParameterOutOfRangeException 
			{
		construct(pLevelObj, x, y);
	}

	/**
	 * Der eigentlich Contructor eines Punktes
	 * 
	 * @param pLevelObj
	 *            ein Objekt der Klasse LevelParser wird uebergeben
	 * @param x
	 *            X-Koordinate des Punktes
	 * @param y
	 *            Y-Koordinate des Punktes
	 * @throws ParameterOutOfRangeException
	 */
	private void construct(LevelParser pLevelObj, int x, int y)
			throws ParameterOutOfRangeException 
			{
		if (!isOutOfBounds(x, y,pLevelObj.getLevel())) 
		{
			level = pLevelObj;
			parsedLevel = level.getLevel();
			xCoord = x;
			yCoord = y;
			coord = new int[] { x, y };
			pid = (y * parsedLevel[0].length) + x;
			content = getFieldContent(xCoord, yCoord);

		} else 
		{
			throw new ParameterOutOfRangeException(
					"Koordinaten ausserhalb der Grenzen");
		}
	}

	/**
	 * Berechnung der Koordinaten nach einer Bewegung
	 * 
	 * @param x
	 *            X-Koordinate des Startpunktes
	 * @param y
	 *            Y-Koordinate des Startpunktes
	 * @param richtung
	 *            Die Richtung in die gegangen wird:
	 *            0=oben,1=rechts,2=unten,3=links
	 * @return Die koordinaten des erreichten Punkts
	 */
	public int[] doMoveInner(int x, int y, int richtung) 
	{
		if (richtung == 0 && !isOutOfBounds(new int[] { x, y - 1 })) 
		{
			return new int[] { x, y - 1 };
		} else if (richtung == 1 && !isOutOfBounds(new int[] { x + 1, y })) 
		{
			return new int[] { x + 1, y };
		} else if (richtung == 2 && !isOutOfBounds(new int[] { x, y + 1 })) 
		{
			return new int[] { x, y + 1 };
		} else if (richtung == 3 && !isOutOfBounds(new int[] { x - 1, y })) 
		{
			return new int[] { x - 1, y };
		}
		return new int[] { x, y };
	}
	
	/**
	 * ist ein punkt leer?
	 * @param x koordinate
	 * @param y koordinate
	 * @return boolean true wenn leer
	 * @throws ParameterOutOfRangeException
	 */
	public boolean empty(int x, int y) throws ParameterOutOfRangeException 
	{
		if (getFieldContent(x, y) == '-')
			return true;
		else
			return false;
	}

	/**
	 * Sind die Koordinaten in den erlaubten Bereichen?
	 * 
	 * @param coord
	 *            KoordinatenArray
	 * @return boolean
	 */
	public boolean isOutOfBounds(int[] coord) 
	{
		return isOutOfBounds(coord[0], coord[1],parsedLevel);
	}

	/**
	 * Sind die Koordinaten ausserhalb des erlaubten Bereichs? x:0-19 & y:0-9
	 * 
	 * @param x
	 *            X-Koordinate
	 * @param y
	 *            Y-Koordinate
	 * @return true wenn ausserhalb des bereichs
	 */
	public static boolean isOutOfBounds(int x, int y,char[][] level) 
	{
		if (x < 0 || x > level[0].length-1 || y < 0 || y > level.length-1)
			return true;
		else
			return false;
	}

	/**
	 * Erzeugt die PunktID - entspricht dem durchgezaehltem Index
	 * 
	 * @param x
	 *            X-Koordinate
	 * @param y
	 *            Y-Koordinate
	 * @return PunktID
	 * @throws ParameterOutOfRangeException
	 */
	public static int mkPID(int x, int y, char[][] levelChars) throws ParameterOutOfRangeException 
	{
		if (!isOutOfBounds(x, y,levelChars))
			return (y * levelChars[0].length) + x;
		else 
			throw new ParameterOutOfRangeException(
					"Die Koordinaten passen zu keinem Feld");

	}

	/**
	 * Erzeugt aus der Punkt-ID die Koordinaten
	 * 
	 * @param point
	 *            Punkt-ID
	 * @return int[] der neuen Koordinaten
	 * @throws ParameterOutOfRangeException
	 */
	public static int[] mkCoord(int point, char[][] levelChars) throws ParameterOutOfRangeException 
	{
		int x = point % levelChars[0].length;
		int y = (point - x) / levelChars[0].length;
		if(!isOutOfBounds(x, y,levelChars))
			return new int[] { x, y };
		else throw new ParameterOutOfRangeException("Der Punkt passt zu keinem Feld");
	}

	/**
	 * Jedes/r Feld/Punkt hat 1 oder 3 Brueder dessen Koordinaten ermittelt
	 * werden
	 * 
	 * @param x
	 *            X-Koordinate
	 * @param y
	 *            Y-Koordinate
	 * @return Koordinaten der Brueder
	 * @throws ParameterOutOfRangeException
	 */
	public int[][] getBrothers(int x, int y)
			throws ParameterOutOfRangeException 
			{
		int match = 0;
		int[][] brothers = new int[4][2];
		if (!isOutOfBounds(x, y,parsedLevel)) 
		{
			char val = getFieldContent(x, y);
			if (val != '-') 
			{
				for (int i = 1; i < parsedLevel.length-1 && match < 4; i++) 
				{
					for (int k = 1; k < parsedLevel[0].length-1 && match < 4; k++) {
						if (parsedLevel[i][k] == val && !(k == x && i == y)) 
						{
							brothers[match][0] = k;
							brothers[match][1] = i;
							match++;
						}
					}
				}
				if (match < 4) 
				{
					int[][] tmp = brothers.clone();
					brothers = new int[match][2];
					System.arraycopy(tmp, 0, brothers, 0, match);
					tmp = null;
				}
				return clearBrothers(brothers);
			} else
				return clearBrothers(brothers);
		} else
			// return brothers;
			throw new ParameterOutOfRangeException(
					"Die Koordinaten passen zu keinem Feld");
	}

	private int[][] clearBrothers(int[][] brothers) 
	{
		boolean allEmpty = true;
		for (int i = 0; i < brothers.length; i++) 
		{
			if (brothers[i][0] == 0 || brothers[i][1] == 0) 
			{
				brothers[i] = null;
			} else 
			{
				allEmpty = false;
			}
		}
		if (allEmpty) 
		{
			brothers = null;
		}
		return brothers;
	}

	/**
	 * Ermittelt den Inhalt eines Feldes/Punktes
	 * 
	 * @param x
	 *            x-Koordinate des Felds
	 * @param y
	 *            y-Koordninate des Felds
	 * @return Das Zeichen des Feldes
	 * @throws ParameterOutOfRangeException
	 */
	public char getFieldContent(int x, int y)
			throws ParameterOutOfRangeException 
			{
		if (!isOutOfBounds(x, y,parsedLevel))
			return parsedLevel[y][x];
		else
			throw new ParameterOutOfRangeException(
					"Die Koordinaten passen zu keinem Feld");// return '-';
	}
}
