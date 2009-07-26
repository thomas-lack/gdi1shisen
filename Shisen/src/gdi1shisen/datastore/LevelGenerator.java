package gdi1shisen.datastore;

import gdi1shisen.exceptions.ParameterOutOfRangeException;
import gdi1shisen.exceptions.SyntacticIncException;
import gdi1shisen.gamecontroller.Move;

import java.util.Random;
/**
 * liefert public static methoden zum generieren von outer/RAW-Leveln char[8][18] 
 * @author chandra Gruppe 3 lo08itaq
 *	Hilfestellung durch: http://forum.chip.de/java-delphi-pascal/array-mischen-401401.html#post2570609
 */
public class LevelGenerator {
	/**
	 * Randomobjekt für shuffle
	 */
	private static Random rnd = new Random();
	
	/**
	 * vertauscht arrayelemente
	 * @param x
	 * @param a
	 * @param b
	 */
	private static void swap(Object[] x, int a, int b) {
		// erstellt die temporäre Variable t und füllt diese mit x[a]. 
		// setzt den inhalt von x[a] auf den inhalt von x[b]
		// und ersetzt den inhalt von x[b] durch t und somit durch den 
		// alten wert von x[a]
		Object 
		t = x[a];
		x[a] = x[b];
		x[b] = t;
	}
	
	/**
	 * mischt ein array von objekten - nur von objekten!
	 * @param x	das array an objekten deren reihenfolge geändert werden soll
	 */
	private static void shuffle(Object[] x) {
		for(int i = x.length; i > 1; i--) swap(x, i-1, rnd.nextInt(i));
	}
	
	/**
	 * generiert ein Valides (nicht unbedingt lösbare) level
	 * @return level im outer/RAW Format char[8][18]
	 * @throws SyntacticIncException 
	 */
	public static char[][] generateValid() throws SyntacticIncException{
		return generateValid(8,18);
	}
	
	/**
	 * generiert ein Valides (nicht unbedingt lösbare) level - spalten*zeilen muss 144 ergeben
	 * @param zeilen - wie viele spalten soll das Level haben
	 * @param spalten - wie viele zeilen soll das level haben
	 * @return	im outer/RAW Format char[zeilen][spalten]
	 * @throws SyntacticIncException 
	 */
	public static char[][] generateValid(int zeilen,int spalten) 
	throws SyntacticIncException
	{
		if(zeilen*spalten==144){
			String alleChars = new String("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");
			char[] allChars = alleChars.toCharArray();
			char[][] level = new char[zeilen][spalten];
			char[][] alleZeichen = new char[144][1];
			for(int i = 0;i<144;i++){
				alleZeichen[i][0] = allChars[i%36];
			}
			shuffle(alleZeichen);
			int x = 0;
			for(int i=0;i<zeilen;i++){
				for(int k=0;k<spalten;k++){
					level[i][k] = alleZeichen[x][0];
					x++;
				}
			}
			return level;
		}else{
			throw new SyntacticIncException("Zeilenzahl * Spaltenzahl muss 144 ergeben");
		}
	}
	/**
	 * Erzeugt ein lösbares Level in den Dimensionen 8*18
	 * @return	ein lösbares Level im outer/RAW-Format char[8][18]
	 * @throws SyntacticIncException
	 * @throws ParameterOutOfRangeException
	 */
	public static char[][] generateSolvable() 
	throws SyntacticIncException, ParameterOutOfRangeException
	{
		return generateSolvable(8,18);
	}
	
	/**
	 * Erzeugt ein lösbares Level in den Dimensionen zeilen*spalten (muss 144 ergeben)
	 * @param zeilen	- anzahl der zeilen
	 * @param spalten	- anzahl der spalten
	 * @return	ein lösbares Level im outer/RAW-Format char[zeilen][spalten]
	 * @throws SyntacticIncException
	 * @throws ParameterOutOfRangeException
	 */
	public static char[][] generateSolvable(int zeilen,int spalten) throws SyntacticIncException, ParameterOutOfRangeException{
		if(zeilen*spalten==144){
			char[][] level = generateValid(zeilen,spalten);
			LevelParser lpObject = new LevelParser(level);
			while(!Move.isSolvable(lpObject)){
				level = generateValid();
				lpObject = new LevelParser(level);
			}
			return level;
		}else{
			throw new SyntacticIncException("Zeilenzahl * Spaltenzahl muss 144 ergeben");
		}
	}
}
