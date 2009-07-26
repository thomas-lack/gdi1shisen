package gdi1shisen.datastore;

import gdi1shisen.exceptions.ParameterOutOfRangeException;

/**
 * Ein (Spiel-)Feld/Stein
 * 
 * @author chandra
 * 
 */
public class Brick extends Point 
{
	public int[] brueder = new int[3];
	public int nachbarO;
	public int nachbarR;
	public int nachbarU;
	public int nachbarL;
	public int[] nachbarn = new int[4];
	public WayPoint[] wayPointList = null;

	/**
	 * Contructor eines Brick (-Points)
	 * 
	 * @param pLevelObj
	 *            ein Objekt der Klasse LevelParser wird uebergeben
	 * @param x
	 *            X-Koordinate des Punktes
	 * @param y
	 *            Y-Koordinate des Punktes
	 * @throws ParameterOutOfRangeException
	 */
	public Brick(LevelParser pLevelObj, int x, int y)
	throws ParameterOutOfRangeException 
	{
		//initialisiere brick auf die selbe weise wie jeden Point
		super(pLevelObj, x, y);
		//berechne die Koordinaten der Brüderstein (Brüder sind solche mit unterschiedlichen koordinaten
		// aber selben inhalt)
		int[][] tmpBrothers = getBrothers(xCoord, yCoord);
		if (tmpBrothers != null) 
		{
			for (int i = 0; i < tmpBrothers.length; i++) 
			{
				try 
				{
					brueder[i] = mkPID(tmpBrothers[i][0], tmpBrothers[i][1],pLevelObj.getLevel());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		//berechne die nachbar
		//nachbarn sie die bricks die über leersteine/waypoints ohne richtungsänderung erreichbar sind
		getNeighbours();
	}

	/**
	 * !Contructor-Funktion! Was sind die Nachbarn (in direkter richtung
	 * erreichbare nicht Waypoints) des Punktes
	 * 
	 * @throws ParameterOutOfRangeException
	 */
	private void getNeighbours() throws ParameterOutOfRangeException 
	{
		//vier mal, da es maximal 4 nachbarn geben kann
		for (int i = 0; i < 4; i++) 
		{
			int[] lastMove = new int[] { xCoord, yCoord };
			//gehe jede koordinaten einzeln weiter in eine richtung
			for (int[] moved = doMoveInner(xCoord, yCoord, i); !moved
					.equals(new int[] { xCoord, yCoord })
					&& !(lastMove[0] == moved[0] && lastMove[1] == moved[1]); moved = doMoveInner(
					moved[0], moved[1], i)) {
				System.arraycopy(moved, 0, lastMove, 0, 2);
				// wenn der inhalt des moves nicht leer ist, wurde ein nachbar gefunden
				if (!empty(moved[0], moved[1])) 
				{
					nachbarn[i] = mkPID(moved[0], moved[1],parsedLevel);
					switch (i) {
					case 0:
						nachbarO = nachbarn[i];
						break;
					case 1:
						nachbarR = nachbarn[i];
						break;
					case 2:
						nachbarU = nachbarn[i];
						break;
					case 3:
						nachbarL = nachbarn[i];
						break;
					}
					break;
				} else
				// andernfalls ist es ein WayPoint da der stein leer ist
				// und über ihn gezogen werden kann 
				{
					nachbarn[i] = -1;
					addWayPoint(new WayPoint(level, moved[0], moved[1], i));
				}
			}
		}

	}

	/**
	 * Fuegt einen neuen Wegpunkt zur Wegepunkt Liste hinzu
	 * 
	 * @param wPoint
	 *            der Wegpunkt der zur wayPointList hinzugefuegt werden soll
	 */
	// verwendet keine Liste sondern ein array, da ich urspünglich auf listen
	// komplett verzichten wollte
	public void addWayPoint(WayPoint wPoint) 
	{
		// wenn wayPointList leer ist, dann wird sie neu initialisiert
		if (wayPointList == null) 
		{
			wayPointList = new WayPoint[] { wPoint };
		} else 
		// ansonsten wird ein eintrag hinten angehängt
		{
			WayPoint[] wpLClone = wayPointList.clone();
			wayPointList = new WayPoint[wpLClone.length + 1];
			System.arraycopy(wpLClone, 0, wayPointList, 0, wpLClone.length);
			wayPointList[wpLClone.length] = wPoint;
		}
	}
}
