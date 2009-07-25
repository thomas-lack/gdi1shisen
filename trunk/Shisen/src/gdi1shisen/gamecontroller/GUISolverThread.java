package gdi1shisen.gamecontroller;

import java.util.LinkedList;
import gdi1shisen.gui.ShisenFrameBoard;

/**
 * Parallele Thread Klasse um das automatische Lösen
 * eines Levels zu visualisieren.
 * @author Thomas
 */
public class GUISolverThread extends Thread
{
	private ShisenFrameBoard gamePanel;
	private LinkedList<int[][]> solution;
	private UserMove userMoveController;
	
	/**
	 * Konstruktor der Klasse
	 * @param userMoveController
	 * @param gamePanel
	 * @param solution
	 */
	public GUISolverThread(UserMove userMoveController, ShisenFrameBoard gamePanel, 
			LinkedList<int[][]> solution)
	{
		this.userMoveController = userMoveController;
		this.gamePanel = gamePanel;
		this.solution = solution;
	}
	
	/**
	 * Paralleler Thread, in dem die einzelnen Schritte einer gefundenen
	 * Lösung abgearbeitet werden. (Simulierte Mausklicks)
	 */
	public void run()
	{
		for (int i=0; i<solution.size(); i++)
		{
			//markiere die beiten aktuellen Spielsteine aus der Lösungsliste
			gamePanel.markBrick(solution.get(i)[0][0], solution.get(i)[0][1]);
			gamePanel.markBrick(solution.get(i)[1][0], solution.get(i)[1][1]);
			
			//warte eine Sekunde
			try 
			{
				Thread.sleep(1000);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			
			userMoveController.newBrickClicked(solution.get(i)[0][0], solution.get(i)[0][1]);
			userMoveController.newBrickClicked(solution.get(i)[1][0], solution.get(i)[1][1]);
		}
	}
}
