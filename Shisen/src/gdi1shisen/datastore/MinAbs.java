package gdi1shisen.datastore;

import java.io.IOException;

/**
 * @author Thomas Lack, Eugen Grenz, Benjamin Mayer, Chandra Nicolaus Kirchrath
 *
 */
public class MinAbs {
	public int fieldWidth = 0;
	public int fieldHeight = 0;
	
	
	
	//Variable zur Speicherung des Levelinhalts
	private static char[][] levelinh;
	
	/**
	 * @return sets the values of the levelinh variable
	 */
	public static char[][] getLevelinh() {
		return levelinh;
	}

	/**
	 * @param levelinh
	 * @return sets the value of levelinh
	 */
	public void setLevelinh(char[][] levelinh) {
		this.levelinh = levelinh;
	}
	/**
	 * leere konstruktor methode
	 */
	public MinAbs() {}
	
	/**
	 * @param levelinh
	 * @return Constructor method
	 */
	public MinAbs(char[][] levelinh){
		setLevelinh(levelinh);
	}
	
	//To String Methode
	public String toString(){
		//höhe und breite
		int width = levelinh.length;
		int height = levelinh[0].length;
		StringBuilder strb = new StringBuilder();
		//fuegt jedes zeichen an den StringBuilder
		//und fuegt einen zeilenumbruch an nachdem die
		//"x-achse" am ende ihrer laenge ist
		for (int i = 0; i < height; i++){
			for (int j = 0; j < width; j++){
				strb.append(levelinh[j][i]);
			}
			//loescht alle leerzeichen am ende einer zeile
			for (int j = 0; j < width; j++){
				if (strb.charAt(strb.length()-1) == ' ')
					strb.deleteCharAt(strb.length()-1);
			}
			//einfügen eines Zeilenumbruchs
			strb.append("\n");
		}
		return strb.toString();
	}
	
	

	
	
	//erkennt das level als gelöst, sobald es genau dieser symetrie entspricht
	public boolean doneLevel(){
		if (toString().matches   ( "- - - - - - - - - - - - - - - - - - - -"+
				                   "- - - - - - - - - - - - - - - - - - - -"+ 
				                   "- - - - - - - - - - - - - - - - - - - -"+  
				                   "- - - - - - - - - - - - - - - - - - - -"+ 
				                   "- - - - - - - - - - - - - - - - - - - -"+  
				                   "- - - - - - - - - - - - - - - - - - - -"+ 
				                   "- - - - - - - - - - - - - - - - - - - -"+  
				                   "- - - - - - - - - - - - - - - - - - - -"+ 
				                   "- - - - - - - - - - - - - - - - - - - -"+  
				                   "- - - - - - - - - - - - - - - - - - - -" ))
			return true;
		else return false;
		//oder über hilfsmethode die a-z 0-9 beinhaltet und diese (string)
		//methode wird in contains aufgerufen
		//!toString().contains("0 1 2 3 4 5 6 7 8 9 " +
				   //"A B C D E F G H I J K L M N O P Q R S T U V W X Y Z ");
	}
	
	
	
	/*
	 * resettet nach neustart
	 */
	public static void resetAllValues() throws IOException {
		
		// resetet alle Werte
	    //ZU IMPL
	}
}
