package gdi1shisen.datastore;

import gdi1shisen.exceptions.ParameterOutOfRangeException;

public class WayPoint extends Point 
{
	public int direction;
	public Point[] pointList = null;

	/**
	 * Contrucor eines Waypoints
	 * 
	 * @param pLevelObj
	 *            ein Objekt der Klasse LevelParser wird uebergeben
	 * @param x
	 *            X-Koordinate des Punktes
	 * @param y
	 *            Y-Koordinate des Punktes
	 * @param direction
	 *            Die Richtung in die Point.move(..,..,..) gegangen war
	 * @throws ParameterOutOfRangeException
	 */
	public WayPoint(LevelParser pLevelObj, int x, int y, int direction)
			throws ParameterOutOfRangeException 
			{
		super(pLevelObj, x, y);
		this.direction = direction;
		getSisterPoints();
		content = getFieldContent(xCoord, yCoord);
	}

	/**
	 * !Contructor-Funktion! Setzt die Pointlist
	 * 
	 * @throws ParameterOutOfRangeException
	 */
	private void getSisterPoints() throws ParameterOutOfRangeException 
	{
		for (int i = 0; i < 4; i++) 
		{
			if (isAllowed(i)) {
				int[] lastMove = new int[] { xCoord, yCoord };
				for (int[] moved = doMoveInner(xCoord, yCoord, i); !moved
						.equals(new int[] { xCoord, yCoord })
						&& !(lastMove[0] == moved[0] && lastMove[1] == moved[1]); moved = doMoveInner(
						moved[0], moved[1], i)) {
					System.arraycopy(moved, 0, lastMove, 0, 2);
					if (empty(moved[0], moved[1])) 
						addPoint(new Point(level, moved[0], moved[1]));
					else 
						break;
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
	private void addPoint(Point point) 
	{
		if (pointList == null) 
		{
			pointList = new Point[] { point };
		} else 
		{
			Point[] pLClone = pointList.clone();
			pointList = new Point[pLClone.length + 1];
			System.arraycopy(pLClone, 0, pointList, 0, pLClone.length);
			pointList[pLClone.length] = point;
		}
	}

	/**
	 * Ist die Richtung der SchwesterPunkte erlaubt?
	 * 
	 * @param i
	 *            Richtung
	 * @return boolean
	 */
	private boolean isAllowed(int i) 
	{
		if (i < 4 && i >= 0) 
		{
			if (direction == 0 || direction == 2) 
			{
				if (i == 0 || i == 2) 
				{
					return false;
				} else 
				{
					return true;
				}
			} else 
			{
				if (i == 1 || i == 3) 
				{
					return false;
				} else 
				{
					return true;
				}
			}
		} else 
		{
			return false;
		}
	}
}
