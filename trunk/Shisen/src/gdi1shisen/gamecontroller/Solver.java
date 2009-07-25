package gdi1shisen.gamecontroller;

import java.util.LinkedList;

import gdi1shisen.datastore.Brick;
import gdi1shisen.datastore.LevelParser;
import gdi1shisen.exceptions.ParameterOutOfRangeException;
import gdi1shisen.exceptions.SyntacticIncException;

/**
 * Diese Klasse findet heraus ob ein Level Lösbar ist und stellt den Lösungsweg bereit
 * @author chandra
 *
 */
public class Solver
{
	private LevelParser lpObject;
	private LinkedList<int[][]> loesungsWeg = new LinkedList<int[][]>();
	private int counter = 0;
	private boolean solvable = false;
	
	/**
	 * Initialisiert das Object und überprüft das level auf Lösbarkeit und 
	 * Speichert den Lösungsweg
	 * @param lpObject	Das LevelParser-Object auf dem Gearbeitet werden soll
	 * @throws ParameterOutOfRangeException
	 * @throws SyntacticIncException
	 */
	public Solver(LevelParser lpObject) 
	throws ParameterOutOfRangeException, SyntacticIncException
	{
		this.lpObject =lpObject.clone();
		solverLogic(this.lpObject);
	}
	
	/**
	 * Sucht nach einem Lösungsweg nach dem Prinzip der Tiefensuche und speichert diesen in loesungsWeg ab
	 * @param lpObject	Das zu lösende Level
	 * @throws ParameterOutOfRangeException
	 * @throws SyntacticIncException
	 */
	private void solverLogic(LevelParser lpObject) 
	throws ParameterOutOfRangeException, SyntacticIncException
	{
		if(!solvable){
			LinkedList<int[][]> apm = getActualPossibleMoves(lpObject);
			for(int i=0;i<apm.size();i++)
			{
				if(solvable) break;
				LevelParser lp = easyMove(lpObject,apm.get(i));
				loesungsWeg.add(counter, apm.get(i));
				counter++;
				if(Move.isSolved(lp))
				{
					solvable = true;
				}else
				{
					solverLogic(lp);
				}
				if(!solvable)
				{
					loesungsWeg.removeLast();
					counter--;
				}
			}
		}
	}

	/**
	 * löscht zwei steine aus einem LevelParser-Object und liefert ein neues verändertes zurück
	 * @param lpObject	Das LevelParser-Object
	 * @param move		koordinaten der zwei punkte in int[][]{{x0,y0},{x1,y1}}
	 * @return			das neue LevelParser-Object
	 * @throws SyntacticIncException
	 */
	private LevelParser easyMove(LevelParser lpObject,int[][] move) 
	throws SyntacticIncException
	{
		LevelParser lp = lpObject.clone();
		char[][] level = lp.getRawLevel();
		level[move[0][1]-1][move[0][0]-1] = '-';
		level[move[1][1]-1][move[1][0]-1] = '-';
		lp = new LevelParser(level);
		return lp;
	}

	
	/**
	 * Welche Züge sind aktuell möglich?
	 * @param lpObject	Das Level
	 * @return			die Liste der aktuell möglichen Züge
	 * @throws ParameterOutOfRangeException
	 */
	private LinkedList<int[][]> getActualPossibleMoves(LevelParser lpObject) 
	throws ParameterOutOfRangeException
	{
		char[][] rawLevel = lpObject.getRawLevel();
		int y = 0;
		Brick stein = null;
		LinkedList<int[][]> possibleMoves = new LinkedList<int[][]>();
		for (char[] line : rawLevel) 
		{
			for (int x = 0; x < line.length; x++) 
			{
				if (rawLevel[y][x] != '-') 
				{
					stein = new Brick(lpObject, x + 1, y + 1);
					
					for (int bruder : stein.brueder) 
					{
						int[] bruderCoords = Brick.mkCoord(bruder,lpObject.getLevel());
						
						if (Move.isWay(lpObject,stein.coord, bruderCoords)) 
						{
							int[][] coords = new int[][]{stein.coord,bruderCoords};
							int[][] coordsInverse = new int[][]{bruderCoords,stein.coord};
							if(!possibleMoves.contains(coords) && 
									!possibleMoves.contains(coordsInverse))
							{
								
								possibleMoves.addLast(coords);
							}
						}
					}
				}
			}
			y++;
		}
		return possibleMoves;
	}
	
	
	/**
	 * Getter für: Ist das Level lösbar?
	 * @return	true wenn lösbar
	 */
	public boolean isSolvable()
	{
		return solvable;
	}
	
	/**
	 * Getter für den Lösungsweg
	 * @return	eine Liste mit Koordinaten in einem Zweidimensionalem Array
	 */
	public LinkedList<int[][]> getSolution()
	{
		return loesungsWeg;
	}
}