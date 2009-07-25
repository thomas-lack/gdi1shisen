package gdi1shisen.datastore;

import java.util.Random;

import gdi1shisen.exceptions.InternalFailureException;
import gdi1shisen.exceptions.SyntacticIncException;

/**
 * Modifikationen an einem Level werden durch diese Klasse geregelt
 * Insbesondere die Cheatfunktion des MixIt
 * @author chandra
 * 
 */
public class LevelManipulation 
{
	/**
	 * Das von Constructor uebergebene Level
	 */
	public char[][] level;
	/**
	 * Das von Constructor uebergebene RAW/OuterLevel
	 */
	public char[][] rawLevel;

	/**
	 * Contructor to enable level-manipulation
	 * 
	 * @param LevelParser Object
	 * @throws InternalFailureException
	 */
	public LevelManipulation(LevelParser lpObject) 
	throws InternalFailureException 
	{
		LevelParser lp = lpObject.clone();
		level = lp.getLevel();
		rawLevel = lp.getRawLevel();
	}
	
	
	
	/**
	 * Randomobjekt für shuffle
	 */
	private Random rnd = new Random();
	
	/**
	 * vertauscht arrayelemente
	 * @param x
	 * @param a
	 * @param b
	 */
	private void swap(Object[] x, int a, int b)
	{
		Object 
		t = x[a];
		x[a] = x[b];
		x[b] = t;
	}
	
	/**
	 * mischt ein array von objekten - nur von objekten!
	 * @param x	das array an objekten deren reihenfolge geändert werden soll
	 */
	private void shuffle(Object[] x)
	{
		for(int i = x.length; i > 1; i--) swap(x, i-1, rnd.nextInt(i));
	}
	
	
	/**
	 * Mischt an das Object übergebene Level
	 * @return das neue LevelParser Object
	 * @throws SyntacticIncException
	 */
	public LevelParser mixIt() 
	throws SyntacticIncException
	{		
		StringBuffer sb = new StringBuffer();
		for(char[] line:rawLevel)
		{
			for(char brk:line)
			{
				sb.append(brk);
			}
		}
		char[] cArr = sb.toString().toCharArray();
		char[][] toShuffle = new char[cArr.length][1];
		for(int i=0;i<cArr.length;i++)
		{
			toShuffle[i][0] =  cArr[i];
		}
		shuffle(toShuffle);
		for(int i=0;i<toShuffle.length;i++)
		{
			cArr[i] = toShuffle[i][0];
		}
		int x = 0;
		for(int i=0;i<rawLevel.length;i++)
		{
			for(int k=0;k<rawLevel[i].length;k++)
			{
				rawLevel[i][k] = 
					cArr[x];
				x++;
			}
		}
		return new LevelParser(rawLevel);
	}
	
}
