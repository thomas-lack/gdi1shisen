package gdi1shisen.gamecontroller;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import gdi1shisen.datastore.Brick;
import gdi1shisen.datastore.LevelManipulation;
import gdi1shisen.datastore.LevelParser;
import gdi1shisen.datastore.MoveData;
import gdi1shisen.datastore.Point;
import gdi1shisen.datastore.UserMoveHistory;
import gdi1shisen.datastore.WayPoint;
import gdi1shisen.exceptions.InternalFailureException;
import gdi1shisen.exceptions.ParameterOutOfRangeException;
import gdi1shisen.exceptions.SyntacticIncException;

public class Move {
	private static int minSolutionType;
	private static int solutionCounter;
	private static int[][] solutionSimple;
	private static int[][] solutionMedium;
	private static int[][] solutionComplex;
	
	
	/**
	 * setter für den parameter, der den leichtesten weg angibt
	 * @param minSolutionType
	 */
	private static void setMinSolutionType(int minSolutionTypeExt) {
		minSolutionType = minSolutionTypeExt;
	}

	/**
	 * getter für den parameter, der den leichtesten weg angibt
	 * @return
	 */
	private static int getMinSolutionType() {
		return minSolutionType;
	}
	
	/**
	 * fügt einen weg ohne richtungsänderung hinzug
	 * @param solution
	 */
	private static void addSimpleWay(int[] solution) {
		solutionCounter++;
		if (solutionSimple == null) 
		{
			solutionSimple = new int[1][];
			solutionSimple[0] = solution;
		} else 
		{
			int[][] solutionSimpleClone = solutionSimple.clone();
			solutionSimple = new int[solutionSimpleClone.length + 1][];
			System.arraycopy(solutionSimpleClone, 0, solutionSimple, 0,
					solutionSimpleClone.length);
			solutionSimple[solutionSimple.length - 1] = solution;
		}
	}
	
	
	/**
	 * fügt einen weg mit einer richtungsänderung hinzu
	 * @param solution
	 */
	private static void addMediumWay(int[] solution) {
		solutionCounter++;
		if (solutionMedium == null) {
			solutionMedium = new int[1][];
			solutionMedium[0] = solution;
		} else {
			int[][] solutionMediumClone = solutionMedium.clone();
			solutionMedium = new int[solutionMediumClone.length + 1][];
			System.arraycopy(solutionMediumClone, 0, solutionMedium, 0,
					solutionMediumClone.length);
			solutionMedium[solutionMedium.length - 1] = solution;
		}
	}
	

	/**
	 * fügt einen weg mit 2 richtungsänderungen hinzu
	 * @param solution
	 */
	private static void addComplexWay(int[] solution) 
	{
		if (solutionComplex == null) 
		{
			solutionComplex = new int[1][];
			solutionComplex[0] = solution;
		} else 
		{
			int[][] solutionComplexClone = solutionComplex.clone();
			solutionComplex = new int[solutionComplexClone.length + 1][];
			System.arraycopy(solutionComplexClone, 0, solutionComplex, 0,
					solutionComplexClone.length);
			solutionComplex[solutionComplex.length - 1] = solution;
		}
	}
	
	
	/**
	 * gibt es einen weg ohne abzubiegen zwischen zwei spielsteinen?
	 * @param firstBrick	erster spielstein
	 * @param secondBrick	zweiter spielstein
	 * @return	true wenn es einen weg gibt
	 */
	private static boolean isDirectWay(Brick firstBrick, Brick secondBrick) 
	{
		boolean found = false;
		int firstBrickID = firstBrick.pid;
		int[] nachbarnSecondBrick = secondBrick.nachbarn;
		int[] bruederSecondBrick = secondBrick.brueder;
		for (int einBruder : bruederSecondBrick) 
		{
			for (int einNachbar : nachbarnSecondBrick) 
			{
				if (einNachbar == einBruder && einBruder == firstBrickID) 
				{
					addSimpleWay(new int[] { secondBrick.pid, firstBrickID });
					found = true;
				}
			}
		}
		return found;
	}


	/**
	 * gibt es einen weg mit einer richtungsänderung?
	 * @param firstBrick	erster spielstein
	 * @param secondBrick	zweiter spielstein
	 * @return	true wenn es einen weg gibt
	 */
	private static boolean isOneCurvedWay(Brick firstBrick, Brick secondBrick) 
	{
		boolean found = false;
		WayPoint[] wayPointsFirstBrick = firstBrick.wayPointList;
		WayPoint[] wayPointsSecondBrick = secondBrick.wayPointList;
		if (!(wayPointsFirstBrick == null || wayPointsSecondBrick == null)) 
		{
			for (WayPoint wpFirst : wayPointsFirstBrick) {
				for (WayPoint wpSecond : wayPointsSecondBrick) 
				{
					if (wpFirst.pid == wpSecond.pid) 
					{
						addMediumWay(new int[] { firstBrick.pid, wpSecond.pid,
								secondBrick.pid });
						found = true;
					}
				}
			}
		}

		return found;
	}

	/**
	 * gibt es einen weg mit zwei richtungsänderungen?
	 * @param firstBrick	erster spielstein
	 * @param secondBrick	zweitern spielstein
	 * @return	true wenn es einen weg gibt
	 */
	private static boolean isTwoCurvedWay(Brick firstBrick, Brick secondBrick) 
	{
		boolean found = false;

		WayPoint[] wayPointsFirstBrick = firstBrick.wayPointList;
		WayPoint[] wayPointsSecondBrick = secondBrick.wayPointList;
		if (!(wayPointsFirstBrick == null || wayPointsSecondBrick == null)) 
		{
			for (WayPoint wpFirst : wayPointsFirstBrick) {
				if (wpFirst.pointList != null) {
					for (WayPoint wpSecond : wayPointsSecondBrick) 
					{
						for (Point pFirst : wpFirst.pointList) 
						{
							if (pFirst.pid == wpSecond.pid) 
							{
								addComplexWay(new int[] { firstBrick.pid,
										wpFirst.pid, wpSecond.pid,
										secondBrick.pid });
								found = true;
							}
							if (found)
								break;
						}
					}
				}
			}
		}

		return found;
	}
	
	/**
	 * gibt es eine weg (mit keiner, einer oder zwei richtungsänderungen) zwischen zwei spielsteinen?
	 * @param firstBrick	erster spielstein
	 * @param secondBrick	zweiter spielstein
	 * @return	true wenn es einen weg gibt
	 */
	public static boolean getTheWay(Brick firstBrick, Brick secondBrick) {
		minSolutionType = -1;
		solutionCounter = 0;
		solutionSimple = null;
		solutionMedium = null;
		solutionComplex = null;
		boolean found = false;
		if (firstBrick != null && secondBrick != null && firstBrick.pid!=secondBrick.pid) 
		{
			if (firstBrick.content == secondBrick.content) 
			{
				if (isDirectWay(firstBrick, secondBrick)) 
				{
					if (!found) 
					{
						setMinSolutionType(0);
					}
					found = true;
				}
				if (isOneCurvedWay(firstBrick, secondBrick)) 
				{
					if (!found) 
					{
						setMinSolutionType(1);
					}
					found = true;
				}
				if (isTwoCurvedWay(firstBrick, secondBrick)) 
				{
					if (!found) 
					{
						setMinSolutionType(2);
					}
					found = true;
				}
			}
		}
		return found;
	}
	
	
	/**
	 * erzeugt einen spielstein
	 * @param levelObj	LeveParser Object des Levels
	 * @param selectedField	die x,y-Koordinaten eines Spielsteins
	 * @return	ein Brick Objekt der den Spielstein darstellt
	 * @throws ParameterOutOfRangeException
	 */
	private static Brick initBrick(LevelParser levelObj, int[] selectedField) throws ParameterOutOfRangeException 
	{
			return new Brick(levelObj, selectedField[0],selectedField[1]);
	}
	

	/**
	 * gibt es einen Weg zwischen zwei Punkten?
	 * @param lpObject	LevelParser Objekt des Levels
	 * @param punktA	x,y-Koordinaten des ersten Punktes
	 * @param punktB	x,y-Koordinaten des zweiten Punktes
	 * @return	true wenn es einen Weg gibt
	 * @throws ParameterOutOfRangeException
	 */
	public static boolean isWay(LevelParser lpObject, int[] punktA, int[] punktB) throws ParameterOutOfRangeException{
		return getTheWay(initBrick(lpObject, punktA), initBrick(lpObject, punktB));
	}
	
	/**
	 * erzeugt eine Linie zwischen zwei direkt erreichbaren punkten in form eines array von Punkten
	 * @param punktA	startpunkt in x,y-Koordinatenform
	 * @param punktB	zielpunkt in x,y-Koordinatenform
	 * @return	ein array von koordinaten wenn es eine gerade linie gibt, sonst null
	 */
	private static int[][] line(int[] punktA, int[] punktB){
		if( punktB[0]==punktA[0] )
		{
			int ende;
			int start;
			if(punktB[1]<punktA[1]){
				start = punktB[1]+1;
				ende = punktA[1];
			}else{
				start = punktA[1]+1;
				ende = punktB[1];
			}
			int[][] line = new int[ende-start][2];
			for(int i=start;i<ende;i++){
				line[i-start] = new int[]{punktA[0],i};
			}
			return line;
		}else if(punktB[1]==punktA[1])
		{
			int ende;
			int start;
			if(punktB[0]<punktA[0]){
				start = punktB[0]+1;
				ende = punktA[0];
			}else{
				start = punktA[0]+1;
				ende = punktB[0];
			}
			int[][] line = new int[ende-start][2];
			for(int i=start;i<ende;i++){
				line[i-start] = new int[]{i,punktA[1]};
			}
			return line;
		}else
		{
			System.out.println("start und endwert der linie haben keine gemeinsame achse ("+punktA[0]+":"+punktA[1]+"-"+punktB[0]+":"+punktB[1]+")");
			return null;
		}
	}
	
	/**
	 * erzeugt eine linie zwischen zwei punkten, zwischen den ein Zug möglich ist
	 * @return	ein array von x,y-KoordinatenPunkten wenn ein gültiger zug, sonst null
	 * @throws ParameterOutOfRangeException
	 */
	private static int[][] getLine(char[][] level) throws ParameterOutOfRangeException{
		switch(getMinSolutionType()){
		case 0: {
			int[] punktA = Point.mkCoord(solutionSimple[0][0],level);
			int[] punktB = Point.mkCoord(solutionSimple[0][1],level);
			int[][] preLine = line(punktA, punktB);
			int[][] outLine = new int[preLine.length+2][];
			outLine[0] = punktA;
			System.arraycopy(preLine, 0, outLine, 1, preLine.length);
			outLine[outLine.length-1] = punktB.clone();
			return outLine;
		}
		case 1: {
			int[] punktA = Point.mkCoord(solutionMedium[0][0],level);
			int[] punktB = Point.mkCoord(solutionMedium[0][1],level);
			int[] punktC = Point.mkCoord(solutionMedium[0][2],level);
			int[][] preLineOne = line(punktA,punktB);
			int[][] preLineTwo = line(punktB,punktC);
			
			int[][] outLinePreOne = new int[preLineOne.length+2][];
			outLinePreOne[0] = punktA;
			System.arraycopy(preLineOne, 0, outLinePreOne, 1, preLineOne.length);
			outLinePreOne[outLinePreOne.length-1] = punktB;
			
			int[][] outLinePreTwo = new int[preLineTwo.length+1][];
			System.arraycopy(preLineTwo, 0, outLinePreTwo, 0, preLineTwo.length);
			outLinePreTwo[outLinePreTwo.length-1] = punktC;
			
			int[][] outLine = new int[outLinePreTwo.length+outLinePreOne.length][];
			System.arraycopy(outLinePreOne, 0, outLine, 0, outLinePreOne.length);
			System.arraycopy(outLinePreTwo, 0, outLine, outLinePreOne.length, outLinePreTwo.length);
			outLine[outLine.length-1] = punktC.clone();
			return outLine;
		}
		case 2: {
			int[] punktA = Point.mkCoord(solutionComplex[0][0],level);
			int[] punktB = Point.mkCoord(solutionComplex[0][1],level);
			int[] punktC = Point.mkCoord(solutionComplex[0][2],level);
			int[] punktD = Point.mkCoord(solutionComplex[0][3],level);
			
			int[][] preLineOne = line(punktA,punktB);
			int[][] preLineTwo = line(punktB,punktC);
			int[][] preLineThree = line(punktC,punktD);
			
			int[][] outLinePreOne = new int[preLineOne.length+2][];
			outLinePreOne[0] = punktA;
			System.arraycopy(preLineOne, 0, outLinePreOne, 1, preLineOne.length);
			outLinePreOne[outLinePreOne.length-1] = punktB;
			//System.out.println(preLineTwo.length+1);
			int[][] outLinePreTwo = new int[preLineTwo.length+1][];
			System.arraycopy(preLineTwo, 0, outLinePreTwo, 0, preLineTwo.length);
			outLinePreTwo[outLinePreTwo.length-1] = punktC;
			
			int[][] outLinePreThree = new int[preLineThree.length+1][];
			System.arraycopy(preLineThree, 0, outLinePreThree, 0, preLineThree.length);
			outLinePreThree[outLinePreThree.length-1] = punktD;
			
			int[][] outLine = new int[outLinePreTwo.length+outLinePreOne.length+outLinePreThree.length][];
			System.arraycopy(outLinePreOne, 0, outLine, 0, outLinePreOne.length);
			System.arraycopy(outLinePreTwo, 0, outLine, outLinePreOne.length, outLinePreTwo.length);
			System.arraycopy(outLinePreThree, 0, outLine, outLinePreOne.length+outLinePreTwo.length, outLinePreThree.length);
			outLine[outLine.length-1] = punktD.clone();
			return outLine;
		}
		default: return null;
		}
	}
	
	
	/**
	 * für einen Zug auf der Logikebene durch und speichert diesen schritt in der history ab
	 * @param lpObject	das Level als LevelParser Objekt
	 * @param punktA	erster Punkt in x,y-Koordinatenform
	 * @param punktB	zweiter Punkt in x,y-Koordinatenform
	 * @return	ein MoveData Objekt mit allen Informationen zu dem durchgeführten Zug
	 * @throws ParameterOutOfRangeException
	 */
	public static MoveData doMove(LevelParser lpObject, int[] punktA, int[] punktB) throws ParameterOutOfRangeException{
		return doMoveCore(lpObject, punktA, punktB);
	}
	
	
	/**
	 * für einen Zug auf der Logikebene durch ohne ihn in der history zu registrieren
	 * @param lpObject	das Level als LevelParser Objekt
	 * @param punktA	erster Punkt in x,y-Koordinatenform
	 * @param punktB	zweiter Punkt in x,y-Koordinatenform
	 * @return	ein MoveData Objekt mit allen Informationen zu dem durchgeführten Zug
	 * @throws ParameterOutOfRangeException
	 */
	public static MoveData doMoveInner(LevelParser lpObject, int[] punktA, int[] punktB) throws ParameterOutOfRangeException{
		return doMoveCore(lpObject, punktA, punktB);
	}
	
	
	/**
	 * erledigt die arbeit von doMove und doMoveInner -(ohne die registrierung eines schrittes)
	 * @param lpObject	das Level als LevelParser Objekt
	 * @param punktA	erster Punkt in x,y-Koordinatenform
	 * @param punktB	zweiter Punkt in x,y-Koordinatenform
	 * @return	ein MoveData Objekt mit allen Informationen zu dem durchgeführten Zug
	 * @throws ParameterOutOfRangeException
	 */
	private static MoveData doMoveCore(LevelParser lpObject, int[] punktA, int[] punktB) throws ParameterOutOfRangeException{
		if(!isWay(lpObject, punktA, punktB)){
			return null;
		}else{
			MoveData theMove = new MoveData();
			theMove.setLevelRaw(lpObject.getRawLevel());
			theMove.setLevel(lpObject.getLevel());
			theMove.setPunktOne(punktA);
			theMove.setPunktTwo(punktB);
			theMove.setTheWay(getLine(lpObject.getLevel()));
			return theMove;
		}
	}
	
	
	
	
	/**
	 * überprüft ob das Level gelöst ist
	 * @param levelObj das Level als Levelparser Objekt
	 * @return true wenn gelöst
	 */
	public static boolean isSolved(LevelParser levelObj) {
		StringBuffer sb = new StringBuffer();
		StringBuffer line = new StringBuffer();
		for(int i=0;i<levelObj.getLevel()[0].length;i++){
			line.append('-');
		}
		for(int i=0;i<levelObj.getLevel().length;i++){
			
			sb.append(line);
			if(i<levelObj.getLevel().length-1){
				sb.append("\n");
			}
		}
		String obj = sb.toString();
		return LevelParser.levelToString(levelObj.getLevel()).equals(obj);
	}
	
	
	/**
	 * finden wenn möglich einen zug vom gewählten punkt aus
	 * @param lpObject	level als levelParser Objekt
	 * @param stein	der Spielstein von dem aus gesucht werden soll
	 * @return	der erste Spielstein zu dem ein Zug möglich ist
	 * @throws ParameterOutOfRangeException
	 */
	public static Brick findFromX(LevelParser lpObject,Brick stein) throws ParameterOutOfRangeException 
	{
		int[] alleBrueder = stein.brueder;
		Brick steinZwei;
		for (int bruder : alleBrueder) 
		{
			int[] bruderCoords = Brick.mkCoord(bruder,lpObject.getLevel());
			steinZwei = initBrick(lpObject, bruderCoords);
			
			if (isWay(lpObject,stein.coord, bruderCoords)) 
			{
				return steinZwei;
			}
		}
		return null;
	}
	
	

	/**
	 * sucht einen möglichen zug
	 * @param levelObj das level als LevelParser Objekt
	 * @return	ein array mit zwei steinen fall es einen möglichen zug gibt - sonst null
	 * @throws ParameterOutOfRangeException
	 */
	public static Brick[] hint(LevelParser levelObj) throws ParameterOutOfRangeException 
	{
		char[][] rawLevel = levelObj.getRawLevel();
		int y = 0;
		Brick brk;
		Brick stein = null;
		for (char[] line : rawLevel) {
			for (int x = 0; x < line.length; x++) 
			{
				brk = null;
				if (rawLevel[y][x] != '-') 
				{
					stein = initBrick(levelObj, new int[]{x + 1, y + 1});
					brk = findFromX(levelObj, stein);
				}
				if (brk != null && stein != null) 
				{
					return new Brick[] { stein, brk };
				}
			}
			y++;
		}
		return null;
	}
	
	/**
	 * sucht einen möglichen zug
	 * @param levelObj das level als LevelParser Objekt
	 * @return	ein array mit mit 4 koordinatenpunkten x,y,x,y - wenn nichts gefunden dann null
	 * @throws ParameterOutOfRangeException
	 */
	public static int[] hintInt(LevelParser lpObject) throws ParameterOutOfRangeException{
		Brick[] steine = hint(lpObject);
		if(steine==null) return null;
		else return new int[]{steine[0].coord[0],steine[0].coord[1],steine[1].coord[0],steine[1].coord[1]};
			
	}

	/**
	 * ist das level lösbar?
	 * @param levelObj das level als LevelParser Objekt
	 * @return	true wenn lösbar - sonst false
	 * @throws ParameterOutOfRangeException
	 * @throws SyntacticIncException
	 */
	public static boolean isSolvable(LevelParser levelObjInput) throws ParameterOutOfRangeException, SyntacticIncException{
		char[][] levelObjChar = levelObjInput.getRawLevel();
		char[][] levelObjCharCopy = new char[levelObjChar.length][levelObjChar[0].length];
		// ich hab hier kein clone und kein System.arraycopy verwendet, weil dadurch kein neues arrayobjekt erzeugt wurde!
		for(int i = 0;i<levelObjChar.length;i++){
			for(int k=0;k<levelObjChar[0].length;k++){
				levelObjCharCopy[i][k] = levelObjChar[i][k];
			}
		}
		
		LevelParser levelObj = new LevelParser(levelObjCharCopy);
		
		while(!isSolved(levelObj)){
			Brick[] hintBricks = hint(levelObj);
			if(hintBricks==null){
				return false;
			}else{
				MoveData mvdata = doMoveInner(levelObj, hintBricks[0].coord, hintBricks[1].coord);
				levelObj = new LevelParser(mvdata.getLevelRaw());
			}
		}
		return true;
	}
		
	
	/**
	 * cmpCounter steht für current-possible-moves Counter - er zählt die aktuell möglichen spielzüge
	 * @param levelObj Das LevelParser Objekts des Levels
	 * @return	die anzahl der möglichen züge
	 * @throws ParameterOutOfRangeException
	 */
	public static int cpmCounter(LevelParser levelObj) throws ParameterOutOfRangeException {
		char[][] rawLevel = levelObj.getRawLevel();
		int y = 0;
		int count = 0;
		Brick stein = null;
		for (char[] line : rawLevel) {
			for (int x = 0; x < line.length; x++) 
			{
				if (rawLevel[y][x] != '-') 
				{
					
					stein = initBrick(levelObj, new int[]{x + 1, y + 1});
					
					for (int bruder : stein.brueder) 
					{
						int[] bruderCoords = Brick.mkCoord(bruder,levelObj.getLevel());
						
						if (isWay(levelObj,stein.coord, bruderCoords)) 
						{
							count++;
						}
					}
				}
			}
			y++;
		}
		return count;
	}
	
	/**
	 * Statische undo Methode für den Testadapter
	 * (Die eigentliche undo Methode befindet sich im UserMove Controller
	 * und ist mit der Steuerungslogik der GUI verknüpft. Für den Testadapter
	 * wesentliche Operationen wurden deshalb an diese Stelle ausgelagert)
	 * @param levelParser
	 * @param historyData
	 */
	public static void undo(LevelParser levelParser, UserMoveHistory historyData)
	{
		//hole die Koordinaten der Steine und das Steinsymbol des letztes Zuges
		int[] coords = historyData.getLastMoveCoords();
		char symbol = historyData.getLastMoveSymbol();
		
		//erstellen eine Kopie des raw-Levels, fügen das passende Symbol an
		//entsprechender Stelle ein und erzeugen daraus ein neues LP-Objekt
		char[][] tmpLevel = levelParser.getRawLevel();
		tmpLevel[coords[1]-1][coords[0]-1] = symbol;
		tmpLevel[coords[3]-1][coords[2]-1] = symbol;
		try
		{
			levelParser = new LevelParser(tmpLevel);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		//Meldung an das UserMoveHistory Objekt, dass ein Zug rückgängig
		//gemacht wurde
		historyData.undoProposed();
	}
	
	/**
	 * Statische redo Methode für den Testadapter
	 * (Die eigentliche redo Methode befindet sich im UserMove Controller
	 * und ist mit der Steuerungslogik der GUI verknüpft. Für den Testadapter
	 * wesentliche Operationen wurden deshalb an diese Stelle ausgelagert)
	 * @param levelParser
	 * @param historyData
	 */
	public static void redo(LevelParser levelParser, UserMoveHistory historyData)
	{
		if (historyData.redoIsPossible())
		{
			//Meldung an UserMoveHistory Objekt, dass ein Zug wiederhergestellt
			//werden soll
			historyData.redoProposed();
			
			//hole die Koordinaten der Steine
			int[] coords = historyData.getLastMoveCoords();
						
			//erstellen eine Kopie des raw-Levels, fügen das passende Symbol an
			//entsprechender Stelle ein und erzeugen daraus ein neues LP-Objekt
			char[][] tmpLevel = levelParser.getRawLevel();
			tmpLevel[coords[1]-1][coords[0]-1] = '-';
			tmpLevel[coords[3]-1][coords[2]-1] = '-';
			try
			{
				levelParser = new LevelParser(tmpLevel);
			}
			catch (Exception ex)
			{
				System.out.println("Fehler in newBrickClicked() - UserMove.java");
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * Statische Levelspeicher Methode für den Testadapter
	 * (Die eigentliche saveLevel Methode befindet sich im UserMoveMenu Controller
	 * und ist mit der Steuerungslogik der GUI verknüpft. Für den Testadapter
	 * wesentliche Operationen wurden deshalb an diese Stelle ausgelagert)
	 * @param file
	 * @param levelParser
	 * @param userMoveHistory
	 */
	public static void saveLevel(File file, LevelParser levelParser, 
			UserMoveHistory userMoveHistory)
	{
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		
		try
		{
			fos = new FileOutputStream(file);
			out = new ObjectOutputStream(fos);
			out.writeObject(levelParser);
			out.writeObject(userMoveHistory);
			out.close();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	/**
	 * Statische Methode die, ein LevelParser Objekt entgegen nimmt
	 * und darin enthaltene Spielsteine durchmischt
	 * @param levelParser LevelParser
	 * @return LevelParser
	 */
	public static LevelParser shuffleBoard(LevelParser levelParser)
	{
		try 
		{
			LevelManipulation lm = new LevelManipulation(levelParser);
			return lm.mixIt();
		} 
		catch (InternalFailureException e) 
		{
			e.printStackTrace();
		} 
		catch (SyntacticIncException e) 
		{
			e.printStackTrace();
		}
		return null;
		
	}
}
