package gdi1shisen.datastore;

import gdi1shisen.exceptions.SyntacticIncException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;

/**
 * Läd Level aus lvl-Dateien und stellt Levelbezogenen Methoden bereit
 * 
 * @author chandra (lo08itaq)
 * 
 */
public class LevelParser implements Serializable
{
	private static final long serialVersionUID = 8817013321205728180L;
	
	private String file;
	private char[][] level = null;
	private char[][] levelActive = null;
	private int[] counters = new int[37];
	private int maxCount;
	public boolean levelGeladen = false;

	/**
	 * Parst das Level 'file'
	 * 
	 * @param file
	 *            Dateipfad und Name des Levels
	 * @throws SyntacticIncException
	 */
	public LevelParser(String file) throws SyntacticIncException 
	{
		this.file = file;
		try 
		{
			BufferedReader in = new BufferedReader(new FileReader(this.file));
			BufferedReader in2 = new BufferedReader(new FileReader(this.file));
			String zeile = null;
			int rows = 0;
			int cols = 0;
			while((zeile = in2.readLine()) != null){
				cols = zeile.length();
				rows++;
			}
			level = new char[rows][cols];
			levelActive = new char[rows+2][cols+2];
			int x = 0;
			while ((zeile = in.readLine()) != null) 
			{
				level[x] = zeile.toCharArray();
				x++;
			}
			fillActiveLevels();
		} catch (IOException e) {
			e.printStackTrace();
			throw new SyntacticIncException("Syntaktisch Inkorrekt - Datei nicht gefunden");
		}
		if (!syntaktischRichtig()) 
		{
			level = null;
			levelActive = null;
			throw new SyntacticIncException("Syntaktisch Inkorrekt - datei fehlerhaft");
		} else 
		{
			levelGeladen = true;
		}
	}

	/**
	 * Initialisiert auf einem Char[8][18] ein LevelParser
	 * @param level ein level im RAW-Format char[8][18]
	 * @throws SyntacticIncException
	 */
	public LevelParser(char[][] level) throws SyntacticIncException{
		levelActive = new char[level.length+2][level[0].length+2];
		file = null;
		this.level = level.clone();
		fillActiveLevels();
		if (!syntaktischRichtig()) 
		{
			level = null;
			levelActive = null;
			throw new SyntacticIncException("Syntaktisch Inkorrekt");
		} else 
		{
			levelGeladen = true;
		}
	}
	
	/**
	 * Ist das Level in der Form 144 Steine, kein Steintyp mehr als 4 mal -
	 * ausnahme leerstein -
	 * 
	 * @return true wenn ok
	 */
	public boolean syntaktischRichtig() 
	{
		counters = new int[37];
		maxCount = 0;
		boolean breakIt = false;
		for (char[] zeile : level) 
		{
			for (char elm : zeile) 
			{
				int cti = charToInt(elm);
				if (cti == -1) 
				{
					breakIt = true;
					break;
				} else 
				{
					counters[cti]++;
					if (counters[cti] > 4 && elm != '-') {
						return false;
					}

				}
				maxCount++;
			}
			if (breakIt){
				return false;
			}
		}
		if (maxCount <= 144 && maxCount%2==0) 
		{
			for (int count : counters) 
			{
				if (!(count % 2 == 0 || count % 4 == 0)) 
				{
					return false;
				}
			}
		} else 
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Umrundet das Level mit dem definierten Leerzeichen - und setzt
	 * "levelActive"
	 */
	private void fillActiveLevels() 
	{
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<level[0].length+2;i++){
			sb.append("-");
		}
		String lineStartEnd = sb.toString();
		char[] startEnd = lineStartEnd.toCharArray();
		levelActive[0] = startEnd;
		levelActive[levelActive.length-1] = startEnd;
		for (int i = 1; i <= level.length; i++) 
		{
			levelActive[i][0] = '-';
			System.arraycopy(level[i - 1], 0, levelActive[i], 1, level[0].length);
			levelActive[i][level[0].length+1] = '-';
		}
	}

	/**
	 * Das Inhaltszeichen kann wird durch eine String repraesentiert
	 * 
	 * @param c
	 *            Das Zeichen A-Z 0-9 und -
	 * @return Die String die das Zeichen repaesentiert 00-36, 00 wenn unerlaubtes
	 *         Zeichen
	 */
	public static String charToString(char c) 
	{
		switch (c) 
		{
		case '0':
			return "36";
		case '1':
			return "01";
		case '2':
			return "02";
		case '3':
			return "03";
		case '4':
			return "04";
		case '5':
			return "05";
		case '6':
			return "06";
		case '7':
			return "07";
		case '8':
			return "08";
		case '9':
			return "09";
		case 'A':
			return "10";
		case 'B':
			return "11";
		case 'C':
			return "12";
		case 'D':
			return "13";
		case 'E':
			return "14";
		case 'F':
			return "15";
		case 'G':
			return "16";
		case 'H':
			return "17";
		case 'I':
			return "18";
		case 'J':
			return "19";
		case 'K':
			return "20";
		case 'L':
			return "21";
		case 'M':
			return "22";
		case 'N':
			return "23";
		case 'O':
			return "24";
		case 'P':
			return "25";
		case 'Q':
			return "26";
		case 'R':
			return "27";
		case 'S':
			return "28";
		case 'T':
			return "29";
		case 'U':
			return "30";
		case 'V':
			return "31";
		case 'W':
			return "32";
		case 'X':
			return "33";
		case 'Y':
			return "34";
		case 'Z':
			return "35";
		case '-':
			return "37";
		default:
			return "00";
		}
	}

	/**
	 * Das Inhaltszeichen kann wird durch eine Zahl repraesentiert
	 * 
	 * @param c
	 *            Das Zeichen A-Z 0-9 und -
	 * @return Die Zahl die das Zeichen repaesentiert 0-36, -1 wenn unerlaubtes
	 *         Zeichen
	 */
	public static int charToInt(char c) 
	{
		switch (c) 
		{
		case '0':
			return 0;
		case '1':
			return 1;
		case '2':
			return 2;
		case '3':
			return 3;
		case '4':
			return 4;
		case '5':
			return 5;
		case '6':
			return 6;
		case '7':
			return 7;
		case '8':
			return 8;
		case '9':
			return 9;
		case 'A':
			return 10;
		case 'B':
			return 11;
		case 'C':
			return 12;
		case 'D':
			return 13;
		case 'E':
			return 14;
		case 'F':
			return 15;
		case 'G':
			return 16;
		case 'H':
			return 17;
		case 'I':
			return 18;
		case 'J':
			return 19;
		case 'K':
			return 20;
		case 'L':
			return 21;
		case 'M':
			return 22;
		case 'N':
			return 23;
		case 'O':
			return 24;
		case 'P':
			return 25;
		case 'Q':
			return 26;
		case 'R':
			return 27;
		case 'S':
			return 28;
		case 'T':
			return 29;
		case 'U':
			return 30;
		case 'V':
			return 31;
		case 'W':
			return 32;
		case 'X':
			return 33;
		case 'Y':
			return 34;
		case 'Z':
			return 35;
		case '-':
			return 36;
		default:
			return -1;
		}
	}

	/**
	 * 
	 * @return Das geparste Roh-Level in zwei Dimensionen Erste Dimension:
	 *         Zeilen Zweite Dimension: Zeilenelemente
	 *         Die Leerzeichen zwischen den chars dienen nur
	 *         zur besseren Darstellung
	 *         
	 *         S P J K A Q L T 6 O B A 7 C K L S 7
	 *         D H W N H I J 9 F R O Z G L 6 F P 2
	 *         B C D W Q 7 N J U G H A O I L M Q P
	 *         D O E Q 7 5 P C F G H I J M 1 D E I
	 *         S Y Z K 3 8 V W E X A 4 5 N B K 0 C
	 *         X Y F 8 T U V M R W 9 6 0 1 2 3 4 B
	 *         E X Y 9 3 S 4 5 6 R Z 0 1 U 8 T 2 V
	 *         T 9 2 8 3 4 5 X U V G N Y Z 0 R M 1
	 */
	public char[][] getRawLevel() 
	{
		return this.level;
	}

	/**
	 * 
	 * @return Das Level geparste Level aufbereitet mit Umgebungsspacern Eine
	 *         zeilenweise Ausgabe mit Zeilennummern
	 *         Zeilen Zweite Dimension: Zeilenelemente
	 *         Die Leerzeichen zwischen den chars dienen nur
	 *         zur besseren Darstellung
	 * 
	 *         - - - - - - - - - - - - - - - - - - - -
	 *         - S P J K A Q L T 6 O B A 7 C K L S 7 -
	 *         - D H W N H I J 9 F R O Z G L 6 F P 2 -
	 *         - B C D W Q 7 N J U G H A O I L M Q P -
	 *         - D O E Q 7 5 P C F G H I J M 1 D E I -
	 *         - S Y Z K 3 8 V W E X A 4 5 N B K 0 C -
	 *         - X Y F 8 T U V M R W 9 6 0 1 2 3 4 B -
	 *         - E X Y 9 3 S 4 5 6 R Z 0 1 U 8 T 2 V -
	 *         - T 9 2 8 3 4 5 X U V G N Y Z 0 R M 1 -
	 *         - - - - - - - - - - - - - - - - - - - -
	 */
	public char[][] getLevel() 
	{
		return levelActive;
	}

	/**
	 * Konvertiert ein Level in eine den .lvl-Dateien gleichende String
	 * 
	 * @param level
	 *            - char[][] - ein (RAW)Level
	 * @return String des Levels
	 */
	public static String levelToString(char[][] level) 
	{
		StringBuffer sb = new StringBuffer();
		int i = 0;
		for (char[] zeile : level) 
		{
			for (char zeichen : zeile)
				sb.append(zeichen);
			
			i++;
			if(i!=level.length){
				sb.append("\n");
			}
		}
		return sb.toString();

	}
	
	/**
	 * Gibt einen Einzeiler des Levels als String zurück
	 * !!!WICHIGE ANMERKUNG!!!: die eigentlich vorhanden
	 * 							Zeilenumbrücke sind nicht
	 * 							in der String enthalten! 
	 * @return Zeichenkette des Levels
	 * 			!OHNE! Zeilenumbrüche
	 */
	public String toString(){
		StringBuffer sb = new StringBuffer();
		int i = 0;
		for (char[] zeile : level) 
		{
			for (char zeichen : zeile)
				sb.append(zeichen);
			
			i++;
		}
		return sb.toString();
	}
	
	/**
	 * Zählt die noch verbliebenen Steine in einem Level
	 * @return verbliebene Steine
	 */
	public int countBricks(){
		int c = 0;
		for(int y=0;y<level.length;y++)
			for(int x=0;x<level[0].length;x++)
				if(level[y][x]!='-') c++;
		return c;
	}
	
	/**
	 * Liefert das char-Symbol an einer beliebigen Position im 
	 * erweiterten Level
	 * @param coords int[] Array mit x und y Koordinaten
	 * @return char Symbol an den x,y Koordinaten von coords
	 */
	public char getSymbol(int[] coords)
	{
		return levelActive[coords[1]][coords[0]];
	}
	
	/**
	 * Liefert das char-Symbol an einer beliebigen Position im 
	 * äußerem/RAW Level
	 * @param coords int[] Array mit x und y Koordinaten
	 * @return char Symbol an den x,y Koordinaten von coords
	 */
	public char getSymbolOuter(int[] coords)
	{
		if(level[coords[1]][coords[0]]=='-') return ' ';
		else return level[coords[1]][coords[0]];
	}
	
	/**
	 * Gibt eine verkettete Liste mit den Koordinaten aller
	 * gleichen Spielsteine des aktuellen Levels zurück
	 * @param symbol char Symbol eines Spielsteins
	 * @return LinkedList<int[]> Liste aller relevanten Koordinaten
	 */
	public LinkedList<int[]> getCoordsOfSimilarSymbols(char symbol)
	{
		LinkedList<int[]> retList = new LinkedList<int[]>();
		
		for (int i=0; i < levelActive.length; i++)
			for (int j=0; j < levelActive[0].length; j++)
				if (levelActive[i][j] == symbol)
					retList.add(new int[] {j,i});
		
		return retList;
	}
	
	/**
	 * liefert einen Clone des aktuellen levels
	 */
	public LevelParser clone(){
		char[][] level = new char[this.level.length][this.level[0].length];
		for(int y=0;y<this.level.length;y++)
			for(int x=0;x<this.level[0].length;x++)
				level[y][x] = this.level[y][x];
		try {
			return new LevelParser(level);
		} catch (SyntacticIncException e) {
			e.printStackTrace();
			return null;
		}
	}
}
