package gdi1shisen.gamecontroller;

import java.sql.Time;
import java.text.SimpleDateFormat;

import gdi1shisen.gui.ShisenFrameInfoBar;

/**
 * Timerobjekt
 * @author chandra, Thomas
 */
public class Timer extends Thread
{
	// wann wurde das spiel gestartet / beendet
	private long startTime;
	private long endTime;
	
	//InfoBar JToolBar, in der die Zeitanzeige aktualisiert werden soll
	private ShisenFrameInfoBar infoBar;
	
	private boolean stopTimer = false;
	
	/**
	 * Contructor initialisiert startzeit
	 */
	public Timer()
	{
		startTime = System.currentTimeMillis();
	}
	
	public Timer(Long startTime)
	{
		this.startTime = System.currentTimeMillis() - startTime;
	}
	
	/**
	 * wie lange lief das Spiel? 
	 * @return	spielzeit in millisekunden
	 */
	public String getGameTime()
	{
		return timeToString(endTime - startTime);
	}
	
	/**
	 * Methode um den Timer anzuhalten
	 */
	public void stopTimer()
	{
		stopTimer = true;
		endTime = System.currentTimeMillis();
	}
	
	public Long getCurrentGameTime()
	{
		return System.currentTimeMillis() - startTime;
	}
	
	/**
	 * Setter für die JToolBar in der die Zeit ausgegeben werden soll
	 * @param infoBar ShisenFrameInfoBar Objekt mit JPanel für Zeitanzeige 
	 */
	public void setInfoBar(ShisenFrameInfoBar infoBar)
	{
		this.infoBar = infoBar;
	}
	
	/**
	 * Übergibt neuen Anzeige String an Info Toolbar
	 * @param outputTime aufbereiteter String zur Ausgabe
	 */
	public void outputTimeAtToolbar(String outputTime)
	{
		infoBar.setPlaytime(outputTime);
	}
	
	/**
	 * Wandelt einen Timestamp in einen String mit dem Format h:mm:ss um
	 * @param timestamp Timestamp der als String ausgegeben werden soll
	 * @return String des übergebenen Timestamps im Format h:mm:ss
	 */
	private String timeToString(Long timestamp)
	{
		SimpleDateFormat sdfmt = new SimpleDateFormat(); 
		sdfmt.applyPattern( "hh:mm:ss" ); 
		String out = sdfmt.format(new Time(timestamp)); 
		return out;
	}
	
	/**
	 * Diese Methode wird als paralleler Thread ausgeführt
	 */
	public void run()
	{
		while (!stopTimer)
		{
			try
			{
				sleep(1000);
			}
			catch (Exception ex) {}
			
			//Ausgabe der aktuell vergangenen Zeit in der Infobar
			outputTimeAtToolbar(timeToString(System.currentTimeMillis() - startTime));
		}
	}
}
