package gdi1shisen.datastore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.regex.Pattern;
/**
 * Highscoreklasse
 * zuständig für das lesen, schreiben, hinzufügen und 
 * herausfinden, ob das neue ergebnis ein Highscore ist 
 * @author chandra lo08itaq Gruppe 3
 *
 */
public class Highscore {
	/**
	 * LinkedList in der die Zeiten aufsteigend/der länge nach sortiert gespeichert sind
	 */
	private LinkedList<int[]> zeiten = null;
	
	/**
	 * LinkedList der Namen entsprechend der zeiten
	 */
	private LinkedList<String> namen = null;
	
	/**
	 * der Dateiname des Highscores
	 */
	private String file = null;
	

	/**
	 * Constructor mit dateiname
	 * @param file datei des Highscores
	 * @throws IOException 
	 */
	public Highscore(String file) throws IOException
	{
		this.file = file;
		read();
	}
	
	/**
	 * Liest die Highscoredatei ein
	 * @param file	Datei des Highscores
	 * @throws IOException
	 */
	public void read() throws IOException 
	{
		zeiten = new LinkedList<int[]>();
		namen = new LinkedList<String>();
		File theFile = new File(file);
		if(!theFile.exists()) theFile.createNewFile();
		BufferedReader in = new BufferedReader(new FileReader(file));
		String zeile;
		int i=0;
		while ((zeile = in.readLine()) != null && i<10)
		{
			if (Pattern.matches(".*;[0-9]+", zeile))
			{
				String[] parts = zeile.split(";");
				if(parts.length==2)
				{
					namen.add(parts[0]);
					zeiten.addLast(new int[] { Integer.valueOf(parts[1]) });
				}
			}
			i++;
		}
	}

	/**
	 * Schreibt den Highscore in die Datei
	 * @throws IOException
	 */
	public void write() throws IOException
	{
		if(zeiten!=null && namen!=null)
		{
			while(zeiten.size()>10){
				zeiten.removeLast();
				namen.removeLast();
			}
			Writer fw = null; 
			fw = new FileWriter( file );
			for(int i=0;i<zeiten.size();i++)
			{
				StringBuffer sb = new StringBuffer();
				sb.append(namen.get(i));
				sb.append(";");
				sb.append(zeiten.get(i)[0]);
				fw.write(sb.toString());
				fw.append("\n");
			}
			fw.close();
		}
	}
	
	/**
	 * Fügt ein ergebnis an die Highsoreliste an
	 * @param time	die benötigte zeit in Sekunden
	 * @param name	der Benutzernamen
	 * @return		true wenn eingetragen & highscoreergebnis - false wenn kein highscore
	 * @throws Exception	wenn highscoredatei nicht angegeben 
	 */
	public boolean add(int time, String name) throws Exception
	{
		if(zeiten==null || namen==null)
		{
			if(file!=null)
				read();
			else
			{ 
				throw new Exception("Kein level angegeben zu dem ein Highscore hingefuegt werden soll");
			}
		}
		int i=0;
		if(zeiten!=null)
		{
			while(i<zeiten.size())
			{
				if(zeiten.get(i)[0]>time)
				{
					zeiten.add(i,new int[]{time});
					namen.add(i, name);
					write();
					return true;
				}
				i++;
			}
			zeiten.addLast(new int[]{time});
			namen.addLast(name);
			write();
			return true;
		}else{
			zeiten.addLast(new int[]{time});
			namen.addLast(name);
			write();
			return true;
		}
	}
	
	/**
	 * Gibt die Zeiten in der Highscoreliste von klein nach groß aufsteigend
	 * @return	die zeiten in der Highscoreliste
	 */
	public LinkedList<int[]> getZeiten(){
		return zeiten;
	}
	
	/**
	 * Gibt die Namen der Highscoreliste - erster name bester, letzter schlechtester
	 * @return	die namen in der Highscoreliste
	 */
	public LinkedList<String> getNamen(){
		return namen;
	}
	
	public int size(){
		if(namen==null) return 0;
		else return zeiten.size();
	}
}
