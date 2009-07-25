package gdi1shisen.datastore;

/**
 * MoveData ist eine speicher Klasse für durchgeführte Züge
 * @author chandra projektgruppe3 lo08itaq
 *
 */
public class MoveData {
	// das Level im internen Format
	private char[][] level = null;
	// das Level im externen/RAW Format
	private char[][] levelRaw = null;
	// die Koordinaten von einem Punkt
	private int[] punktOne = null;
	// die Koordinaten von einem anderen Punkt
	private int[] punktTwo = null;
	// eine Koordinatenliste des Weges vom Startpunkt bis zum Zielpunkt
	private int[][] theWay = null;
	
	/**
	 * Initialisiert das objekt mit allen Daten
	 * @param lpObject 	- das Level als LevelParser Objekt
	 * @param pointOne 	- int[2]{x,y} - koordinaten des ersten Punktes
	 * @param pointTwo 	- int[2]{x,y} - koordinaten des zweiten Punktes
	 * @param way		- der Koordinatenweg vom Startpunkt bis zum Zielpunkt
	 */
	public MoveData(LevelParser lpObject ,int[] pointOne, int[] pointTwo, int[][] way){
		theWay = null;
		setLevel(lpObject.getLevel());
		setLevelRaw(lpObject.getRawLevel());
		setPunktOne(pointOne);
		setPunktTwo(pointTwo);
		setTheWay(way);
	}
	/**
	 * Initialisiert ein leeres Speicherobjekt
	 */
	public MoveData(){
		theWay = null;
	}
	
	
	/**
	 * getter für den ersten Punkt
	 * @return int[]{x,y}
	 */
	public int[] getPunktOne() {
		return punktOne;
	}
	
	/**
	 * setter für den ersten punkt
	 * @param punktOne	- int[2]{x,y} koordinate
	 */
	public void setPunktOne(int[] punktOne) {
		this.punktOne = punktOne;
	}
	
	/**
	 * getter für den zweiten punkt
	 * @return	int[]{x,y}
	 */
	public int[] getPunktTwo() {
		return punktTwo;
	}
	
	/**
	 * setter für den zweiten punkt
	 * @param punktTwo	int[]{x,y} koordinate
	 */
	public void setPunktTwo(int[] punktTwo) {
		this.punktTwo = punktTwo;
	}
	
	/**
	 * getter für den weg
	 * @return	eine liste von x,y koordinaten
	 */
	public int[][] getTheWay() {
		return theWay;
	}
	
	/**
	 * setter für den weg
	 * @param theWay	eine liste von x,y koordinaten
	 */
	public void setTheWay(int[][] theWay) {
		this.theWay = theWay;
	}
	
	/**
	 * getter für das level nachdem der zug durchgeführt wurde
	 * @return	ein level im internen Format char[10][20]
	 */
	public char[][] getLevel() {
		if(getPunktOne()!=null && getPunktTwo()!=null){
			char[][] newLevel = new char[level.length][level[0].length];
			System.arraycopy(level, 0, newLevel, 0, level.length);
			newLevel[punktOne[1]][punktOne[0]] = '-';
			newLevel[punktTwo[1]][punktTwo[0]] = '-';
			return newLevel;
		}else{
			return level;
		}
	}
	/**
	 * setter für das Level auf dem der Zug angewendet werden soll
	 * @param level	im internen Format char[10][20]
	 */
	public void setLevel(char[][] level) {
		this.level = level;
	}
	
	/**
	 * setter für das level auf das der Zug angewendet werden soll
	 * @param levelRaw	level im externen Format  - bei nicht variabler feldgröße char[8][18]
	 */
	public void setLevelRaw(char[][] levelRaw) {
		this.levelRaw = levelRaw;
	}
	
	/**
	 * getter für das level auf das der Zug angewendet wurde
	 * @return	das level im externen Format  - bei nicht variabler feldgröße char[8][18]
	 */
	public char[][] getLevelRaw() {
		if(getPunktOne()!=null && getPunktTwo()!=null){
			char[][] newLevelRaw = new char[levelRaw.length][levelRaw[0].length];
			System.arraycopy(levelRaw, 0, newLevelRaw, 0, levelRaw.length);
			newLevelRaw[punktOne[1]-1][punktOne[0]-1] = '-';
			newLevelRaw[punktTwo[1]-1][punktTwo[0]-1] = '-';
			return newLevelRaw;
		}else{
			return levelRaw;
		}
	}
}
