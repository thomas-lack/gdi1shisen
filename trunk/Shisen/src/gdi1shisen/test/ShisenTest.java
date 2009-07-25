package gdi1shisen.test;


import java.io.*;


/**
 * All public unit tests access your implementation using methods of this interface exclusively.
 * You have to provide an implementation of ShisenTestAdapter to map the methods of this interface 
 * to you implementation.
 */
public interface ShisenTest {

	public static final File LVLPUB = new File( "src/gdi1shisen/test/pub/levels" );
	public static final File LVLPRI = new File( "src/gdi1shisen/test/pri/levels" );

	/**
	 * Load a level and parse it into the internal representation.
	 * 
	 * @param lvl The file from which to load the level
	 * @throws Exception in case the level is not syntactically correct
	 */
	void loadLevel(File lvl) throws Exception;

	/**
	 * Returns the current brick value at the given position.
	 * Remember that the value is " " (space) if the brick has been removed.
	 * 
	 * @param x The X coordinate
	 * @param y The Y coordinate
	 * @return Brick value at the given position
	 */
	String getBrickAt(int x, int y);

	/**
	 * Returns the String representation of the <i>current</i> level.
	 * Note:	Remove all spaces and line feeds from the string. The
	 * 			resulting string is a concatenation of each row in the level.
	 * 
	 * @return the String representation
	 */
	String currentLevelToString();

	/**
	 * Returns the current number of bricks in game.
	 * 
	 * @return The current number of bricks
	 */
	int getBrickCount();

	/**
	 * Returns the current level's width
	 *
	 * @return The width of the current level
	 */
	int getLevelWidth();

	/**
	 * Returns the current level's height
	 * 
	 * @return The height of the current level
	 */
	int getLevelHeight();

	/*===================================================================*/

	/**
	 * Returns the count of entries in the highscore list
	 * 
	 * @return The count of entries in the highscore list
	 */
	int getHighscoreCount();

	/**
	 * Returns the name of the best player for the current level, accoring to the highscores.
	 * Returns null if the highscore list is empty.
	 * 
	 * @return The name of the best player for the current level
	 */
	String getBestPlayerName();

	/**
	 * Tells the Shisen implementation to write the current 
	 * highscores to disk, as detailed in the documentation.
	 * 
	 * @throws If an error occurred during writing.
	 */
	void writeHighScoreFile() throws Exception ;

	/**
	 * Creates a new entry in the highscore list with the specified name and time.
	 * The resulting list is sorted by the times (shortest time, first entry).
	 * 
	 * @param name Name of the player.
	 * @param time Time that the player needed to solve the level.
	 */
	void createHighscoreEntry (String name, int time);

	/*===================================================================*/

	/**
	 * Save game state to a file.
	 * 
	 * @param file The file to save the game state in. 
	 */
	void saveGame(File file);

	/**
	 * Load game state from a file.
	 * 
	 * @param file The file with the desired game state. 
	 */
	void loadGame(File file);

	/**
	 * Returns the current number of moves to be undone. 
	 * 
	 * @return Number of moves to be undone.
	 */
	int getUndoCount();

	/**
	 * Returns the current number of moves to be redone. 
	 * 
	 * @return Number of moves to be redone.
	 */
	int getRedoCount();

	/**
	 * Undo the last move.
	 */
	void undoLastMove();

	/**
	 * Redo the last undone move.
	 */
	void redoLastUndoneMove();

	/*===================================================================*/

	/**
	 * Returns whether the current level is solved. 
	 * 
	 * @return True if the current level is solved, otherwise false.
	 */
	boolean isSolved();

	/**
	 * Returns true, if the bricks at (x1,y1) and (x2,y2) can be legally removed.
	 * 
	 * @param x1 X coordinate of brick 1
	 * @param y1 Y coordinate of brick 1
	 * @param x2 X coordinate of brick 2
	 * @param y2 Y coordinate of brick 2
	 * @return True if the bricks at the given positions can be legally removed, otherwise false.
	 * */
	boolean canBeRemoved (int x1, int y1, int x2, int y2) ;

	/**
	 * Removes the two bricks at the given positions (x1,y1) and (x2,y2).
	 * 
	 * @param x1 X coordinate of brick 1
	 * @param y1 Y coordinate of brick 1
	 * @param x2 X coordinate of brick 2
	 * @param y2 Y coordinate of brick 2
	 * */
	void removeBricks (int x1, int y1, int x2, int y2) ;

	/*===================================================================*/

	/**
	 * Returns a pair of xy-coordinates (x1,y1) and (x2,y2) where the
	 * corresponding bricks can be legally removed within the current state.
	 * Returns null if no move is possible within the current state.
	 * 
	 * @return 2d-int-array A as follows: A[0][0]=x1, A[0][1]=y1, A[1][0]=x2, A[1][1]=y2, 
	 * */
	int[][] getHint () ;

	/**
	 * Returns a solution as a sequence of xy-coordinates (x1,y1) and (x2,y2).
	 * Returns null if the game is not solvable within the current state.
	 * 
	 * @return 3d-int-array A as follows: A[i][0][0]=x1, A[i][0][1]=y1, A[i][1][0]=x2, A[i][1][1]=y2 with i = 0,1,2,...,n
	 * */
	int[][][] getSolution () ;
}