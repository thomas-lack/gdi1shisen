package gdi1shisen.gamecontroller;

import java.util.LinkedList;

import gdi1shisen.datastore.Brick;
import gdi1shisen.datastore.LevelParser;
import gdi1shisen.datastore.Point;
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
	

	private boolean solvable = false;
	private LinkedList<LevelParser> lpHistory = new LinkedList<LevelParser>();
	
	
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
		solverLogic();
		
	}
	
	
	
	
	public void solverLogic() throws ParameterOutOfRangeException, SyntacticIncException{
		boolean couldBeSolved = true;
		LevelParser lpObject = this.lpObject.clone();
		LinkedList<int[][]> apm;
		int positionToUse = 0;
		int rised = 1;
		int ebene = 1;
		while(!solvable || couldBeSolved)
		{
			apm = getActualPossibleMoves(lpObject.clone());
			if(apm.size()>0){
				if(positionToUse>apm.size())
				{
					couldBeSolved = false;
					break;
				}
				ebene++;
				LevelParser genL = easyMove(lpObject,apm.get(positionToUse));
				lpHistory.add(genL.clone());
				if(Move.isSolved(genL))
				{
					solvable = true;
					break;
				}else
				{
					lpObject = genL.clone();
				}
				if(positionToUse>0) {
					positionToUse--;
				}
			}else{
				ebene--;
				rised++;
				lpHistory.removeLast();
				LevelParser lpTMPObject = lpHistory.removeLast();
				lpObject = lpTMPObject.clone();
				positionToUse++;
			}
		}
		setSol(this.lpObject.clone(),lpHistory);
	}



	private LevelParser easyMove(LevelParser lpObject,int[][] move) throws SyntacticIncException{
		LevelParser lp = lpObject.clone();
		char[][] level = lp.getRawLevel();
		level[move[0][1]-1][move[0][0]-1] = '-';
		level[move[1][1]-1][move[1][0]-1] = '-';
		lp = new LevelParser(level);
		return lp;
	}
	
	@SuppressWarnings("unchecked")
	private void setSol(LevelParser lpObject,LinkedList<LevelParser> lpHistory) throws ParameterOutOfRangeException, SyntacticIncException{
		lpHistory = (LinkedList<LevelParser>) lpHistory.clone();
		LevelParser lp1 = lpObject;
		LevelParser lp2 = lpHistory.removeFirst().clone();
		while(lpHistory.size()>0){
			int difC = difCounter(lp1,lp2);
			if(difC==2){
				loesungsWeg.addLast(getMoveNormal(lp1,lp2));
			}else{
				LinkedList<int[][]> nn = getMultipleMove(lp1,lp2);
				while(nn.size()>0)
				{
					loesungsWeg.add(nn.removeFirst());
				}
			}
			lp1 = lp2.clone();
			lp2 = lpHistory.removeFirst().clone();
			if(lpHistory.size()==0){
				difC = difCounter(lp1,lp2);
				if(difC==2){
					loesungsWeg.addLast(getMoveNormal(lp1,lp2));
				}else{
					LinkedList<int[][]> nn = getMultipleMove(lp1,lp2);
					while(nn.size()>0)
					{
						loesungsWeg.add(nn.removeFirst());
					}
				}
				
			}
		}
	}
	
	
	private int difCounter(LevelParser lp1, LevelParser lp2){
		char[] lp1C = lp1.toString().toCharArray();
		char[] lp2C = lp2.toString().toCharArray();
		LinkedList<Integer> pids = new LinkedList<Integer>();
		int difCounter=0;
		for(int i=0;i<lp1C.length;i++){
			if(lp1C[i]!=lp2C[i]) difCounter++;
			pids.add(i);
		}
		return difCounter;
	}
	
	private int[][] getMoveNormal(LevelParser lp1, LevelParser lp2) throws ParameterOutOfRangeException{
		char[] lp1C = lp1.toString().toCharArray();
		char[] lp2C = lp2.toString().toCharArray();
		int pid1 = -1;
		int pid2 = -1;
		for(int i=0;i<lp1C.length;i++){
			if(lp1C[i]!=lp2C[i]){
				if(pid1==-1){
					pid1 = i;
				}else{
					pid2 = i;
				}
			}
		}
		int[] pCoord1 = Point.mkCoord(pid1, lp1.getRawLevel());
		int[] pCoord2 = Point.mkCoord(pid2, lp2.getRawLevel());
		int[][] output = new int[2][2];
		output[0] = new int[]{pCoord1[0]+1,pCoord1[1]+1};
		output[1] = new int[]{pCoord2[0]+1,pCoord2[1]+1};
		return output;
	}
	
	
	@SuppressWarnings("unchecked")
	private LinkedList<int[][]> getMultipleMove(LevelParser lp1, LevelParser lp2) throws ParameterOutOfRangeException, SyntacticIncException{
		LinkedList<int[][]> innerSol = new LinkedList<int[][]>();
		LinkedList<Integer> pidsTwo = new LinkedList<Integer>();
		
		char[] lp1C = lp1.toString().toCharArray();
		char[] lp2C = lp2.toString().toCharArray();
		
		for(int i=0;i<lp1C.length;i++){
			if(lp1C[i]!=lp2C[i]){
				pidsTwo.add(i);
			}
		}

		
		LinkedList<Integer> pClone1 = (LinkedList<Integer>) pidsTwo.clone();
		LevelParser lClone = lp1.clone();
		while(pidsTwo.size()>0){
			for(int i=0;i<pClone1.size();i++){
				for(int k=0;k<pClone1.size();k++){
					if(k!=i){
						int[] punktA = refakt(pClone1.get(i),lClone);
						int[] punktB = refakt(pClone1.get(k),lClone);
						if(Move.isWay(lClone, punktA, punktB)){
							innerSol.add(new int[][]{punktA,punktB});
							LevelParser  lClone2 = lClone.clone();
							lClone = easyMove(lClone2,new int[][]{punktA,punktB});
							LinkedList<Integer> tmpList = new LinkedList<Integer>();
							tmpList.add(pClone1.get(i));
							tmpList.add(pClone1.get(k));
							pClone1.removeAll(tmpList);
						}
					}
				}
			}
			pidsTwo = pClone1;
		}
		return innerSol;
	}
	

	public int[] refakt(int id,LevelParser lp) throws ParameterOutOfRangeException{
		int[] p = Point.mkCoord(id, lp.getRawLevel());
		int[] out = new int[2];
		out[0] = p[0]+1;
		out[1] = p[1]+1;
		return out;
	}
	
	public String getCode(int[][] i){
		StringBuffer sb = new StringBuffer();
		sb.append(i[0][1]);
		sb.append(":");
		sb.append(i[0][0]);
		sb.append(" - ");
		sb.append(i[1][1]);
		sb.append(":");
		sb.append(i[1][0]);
		return sb.toString();
	}
	
	
	
	
	/**
	 * Welche Züge sind aktuell möglich?
	 * @param lpObject	Das Level
	 * @return	die Liste der aktuell möglichen Züge
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