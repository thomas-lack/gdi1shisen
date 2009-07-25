package gdi1shisen.test;


import java.io.*;
import java.util.LinkedList;
import gdi1shisen.datastore.Highscore;
import gdi1shisen.datastore.LevelParser;
import gdi1shisen.datastore.MoveData;
import gdi1shisen.datastore.UserMoveHistory;
import gdi1shisen.exceptions.ParameterOutOfRangeException;
import gdi1shisen.exceptions.SyntacticIncException;
import gdi1shisen.gamecontroller.Move;
import gdi1shisen.gamecontroller.Solver;

public class ShisenTestAdapter  implements ShisenTest
{
	private LevelParser lpObject;
	private Highscore hsObject;
	private UserMoveHistory userMoveHistory = new UserMoveHistory();
	
	public static void init () 
	{
		//nothing to do
	}

	/*===================================================================*/

	public void loadLevel (File lvl) throws Exception 
	{
		lpObject = new LevelParser(lvl.getAbsolutePath());
		String filename = lvl.getAbsolutePath();
		hsObject = new Highscore(filename.substring(0, filename.length()-3)+"hsc");
	}

	public String getBrickAt (int x, int y) 
	{
		return String.valueOf(lpObject.getSymbolOuter(new int[]{x,y}));
	}

	public String currentLevelToString () 
	{
		return lpObject.toString();
		//return LevelParser.levelToString(lpObject.getRawLevel());
	}

	public int getBrickCount () 
	{
		return lpObject.countBricks();
	}

	public int getLevelWidth () 
	{
		return lpObject.getRawLevel()[0].length;
	}

	public int getLevelHeight () 
	{
		return lpObject.getRawLevel().length;
	}

	/*===================================================================*/

	public int getHighscoreCount () 
	{
		return hsObject.size();
	}

	public String getBestPlayerName () 
	{
		return hsObject.getNamen().getFirst();
	}

	public void writeHighScoreFile () throws Exception 
	{
		hsObject.write();
	}

	public void createHighscoreEntry (String name, int time) 
	{
		try 
		{
			hsObject.add(time, name);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	/*===================================================================*/

	public void saveGame (File file) 
	{
		Move.saveLevel(file, lpObject, userMoveHistory);
	}

	public void loadGame (File file) 
	{
		//Da ich keine zusätzlichen Setter für den TestAdatpter
		//einbauen will, wurden die passenden Codezeilen einfach
		//aus UserMoveMenu kopiert
		try
		{
			//Objekte aus Datei auslesen und deserialisieren
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(fis);
			lpObject = (LevelParser)in.readObject();
			userMoveHistory = (UserMoveHistory)in.readObject();
		}
		catch (Exception ex)
		{
			System.out.println("Fehler beim Laden (LevelSaver)");
			ex.printStackTrace();
		}
	}

	public int getUndoCount ()
	{
		return userMoveHistory.getActualMove() + 1;
	}

	public int getRedoCount () {
		return userMoveHistory.getHistorySize() - ( userMoveHistory.getActualMove() + 1 );
	}

	public void undoLastMove () 
	{
		Move.undo(lpObject, userMoveHistory);
	}

	public void redoLastUndoneMove () 
	{
		Move.redo(lpObject, userMoveHistory);
	}

	/*===================================================================*/

	public boolean isSolved () 
	{
		//return false;
		//return doneLevel();
		return Move.isSolved(lpObject);
	}

	public boolean canBeRemoved (int x1, int y1, int x2, int y2) 
	{
		try 
		{
			return Move.isWay(lpObject, new int[]{x1+1,y1+1}, new int[]{x2+1,y2+1});
		} 
		catch (ParameterOutOfRangeException e) 
		{
			e.printStackTrace();
			return false;
		}
	}

	public void removeBricks (int x1, int y1, int x2, int y2) 
	{
		try 
		{
			MoveData doneMove = Move.doMoveInner(lpObject, new int[]{x1+1,y1+1}, new int[]{x2+1,y2+1});
			
			//eintragen in UserMoveHistory für undo/redo
			char symbol = lpObject.getSymbol(new int[]{x1+1,y1+1});
			userMoveHistory.addMove(new int[]{x1+1,y1+1}, new int[]{x2+1,y2+1}, symbol);
			
			lpObject = new LevelParser(doneMove.getLevelRaw());
		} 
		catch (ParameterOutOfRangeException e) 
		{
			e.printStackTrace();
		} 
		catch (SyntacticIncException e) 
		{
			e.printStackTrace();
		}
	}

	/*===================================================================*/

	public int[][] getHint () 
	{
		try 
		{
			int[] hint = Move.hintInt(lpObject);
			return new int[][]{ {hint[0]-1,hint[1]-1},{hint[2]-1,hint[3]-1} };
		} 
		catch (ParameterOutOfRangeException e) 
		{
			return null;
		}
	}

	public int[][][] getSolution () 
	{
		try 
		{
			Solver sv = new Solver(lpObject);
			LinkedList<int[][]> solution = sv.getSolution();
			int[][][] outputBuffer = new int[solution.size()][2][2];
			for(int i=0;i<solution.size();i++)
			{
				outputBuffer[i][0][0] = solution.get(i)[0][0]-1;
				outputBuffer[i][0][1] = solution.get(i)[0][1]-1;
				outputBuffer[i][1][0] = solution.get(i)[1][0]-1;
				outputBuffer[i][1][1] = solution.get(i)[1][1]-1;
			}
			return outputBuffer;
		} 
		catch (ParameterOutOfRangeException e) 
		{
			e.printStackTrace();
			return null;
		} 
		catch (SyntacticIncException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
}