package gdi1shisen.datastore;

import java.io.Serializable;
import java.util.LinkedList;

public class UserMoveHistory implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5173953495731259515L;
	private int actualMove;
	private LinkedList<int[]> clickedBricksCoords;
	private LinkedList<char[]> clickedBricksSymbol;
	
	/**
	 * Konstruktor
	 */
	public UserMoveHistory()
	{
		actualMove = -1;
		clickedBricksCoords = new LinkedList<int[]>();
		clickedBricksSymbol = new LinkedList<char[]>();
	}
	
	/**
	 * Fügt einen neuen Spielzug zur UserMoveHistory hinzu.
	 * (Ggf. löschen von Zügen bei passender Undo/Redo Konstellation)
	 * @param coordsBrickOne x,y Koordinaten von Spielstein 1
	 * @param coordsBrickTwo x,y Koordinaten von Spielstein 2
	 * @param symbol char Spielstein der vom Brett genommen wurde
	 */
	public void addMove(int[] coordsBrickOne, int[] coordsBrickTwo, char symbol)
	{
		actualMove++;
		
		//falls der aktuelle Zug nicht auf das letzte Listenelement zeigt...
		if (actualMove != clickedBricksCoords.size())
		{
			//...lösche alle Züge die für eine redo-Funktion gespeichert
			//wurden (sonst inkonsistente Daten, da in diesem Fall ein
			//neuer Zug nach mehreren undo's getätigt wurde)
			while(clickedBricksCoords.size() != actualMove)
			{
				clickedBricksCoords.removeLast();
				clickedBricksSymbol.removeLast();
			}
		}
								
		int[] tmpCoords = { coordsBrickOne[0], coordsBrickOne[1], 
				coordsBrickTwo[0], coordsBrickTwo[1] };
		char[] tmpSymbol = { symbol };
		
		clickedBricksCoords.add(actualMove, tmpCoords);
		clickedBricksSymbol.add(actualMove, tmpSymbol);
	}
	
	/**
	 * Gibt zurück, ob ein undo möglich ist
	 * @return boolean
	 */
	public boolean undoIsPossible()
	{
		if (actualMove > -1)
			return true;
		else
			return false;
	}
	
	/**
	 * Gibt die Koordinaten des aktuell gesetzten Zuges zurück
	 * @return int[] x,y Koordinaten beider Spielsteine
	 */
	public int[] getLastMoveCoords()
	{
		return clickedBricksCoords.get(actualMove);
	}
	
	/**
	 * Gibt das Symbol des Spielsteins des aktuell gesetzten Zuges
	 * zurück
	 * @return char Spielstein Symbol
	 */
	public char getLastMoveSymbol()
	{
		char tmpSymbol = clickedBricksSymbol.get(actualMove)[0];
		return tmpSymbol;
	}
	
	/**
	 * Teilt dem UserMoveHistory Objekt mit, dass ein undo
	 * durchgeführt wurde
	 */
	public void undoProposed()
	{
		actualMove--;
	}
	
	/**
	 * Gibt zurück, ob ein redo möglich ist
	 * @return boolean
	 */
	public boolean redoIsPossible()
	{
		if (actualMove < clickedBricksCoords.size()-1)
			return true;
		else
			return false;
	}
	
	/**
	 * Teilt dem UserMoveHistory Objekt mit, dass ein redo
	 * durchgeführt werden soll
	 */
	public void redoProposed()
	{
		actualMove++;
	}
	
	/**
	 * Getter Funktion für aktuellen Move Index (für Testadapter)
	 * @return int Aktueller Move Index
	 */
	public int getActualMove()
	{
		return actualMove;
	}
	
	/**
	 * Getter für die Anzahl insgesamt gespeicherter Spielzüge
	 * (für Testadapter)
	 * @return int
	 */
	public int getHistorySize()
	{
		return clickedBricksCoords.size();
	}
}
