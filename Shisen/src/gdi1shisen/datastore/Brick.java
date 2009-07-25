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
			throws ParameterOutOfRangeException {
		super(pLevelObj, x, y);
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
		for (int i = 0; i < 4; i++) 
		{
			int[] lastMove = new int[] { xCoord, yCoord };
			for (int[] moved = doMoveInner(xCoord, yCoord, i); !moved
					.equals(new int[] { xCoord, yCoord })
					&& !(lastMove[0] == moved[0] && lastMove[1] == moved[1]); moved = doMoveInner(
					moved[0], moved[1], i)) {
				System.arraycopy(moved, 0, lastMove, 0, 2);
				if (!empty(moved[0], moved[1])) 
				{
					nachbarn[i] = mkPID(moved[0], moved[1],parsedLevel);
					switch (i) {
					case 0:
						nachbarO = mkPID(moved[0], moved[1],parsedLevel);
						break;
					case 1:
						nachbarR = mkPID(moved[0], moved[1],parsedLevel);
						break;
					case 2:
						nachbarU = mkPID(moved[0], moved[1],parsedLevel);
						break;
					case 3:
						nachbarL = mkPID(moved[0], moved[1],parsedLevel);
						break;
					}
					break;
				} else 
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
	public void addWayPoint(WayPoint wPoint) 
	{
		if (wayPointList == null) 
		{
			wayPointList = new WayPoint[] { wPoint };
		} else 
		{
			WayPoint[] wpLClone = wayPointList.clone();
			wayPointList = new WayPoint[wpLClone.length + 1];
			System.arraycopy(wpLClone, 0, wayPointList, 0, wpLClone.length);
			wayPointList[wpLClone.length] = wPoint;
		}
	}
}
