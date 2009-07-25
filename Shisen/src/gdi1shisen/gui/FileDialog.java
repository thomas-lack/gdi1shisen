package gdi1shisen.gui;

import java.io.File;
import javax.swing.filechooser.*;
import javax.swing.JFileChooser;



public class FileDialog extends FileFilter
{
	private static final long serialVersionUID = -3359012011785067208L;
	
	private JFileChooser chooser = new JFileChooser("Levels/"); 
	private ShisenFrame frame;
	private String saveFileExtension = ".sve";
	
	/**
	 * Konstruktor für einen FileDialog, angepasst auf Shisen Spielstände
	 * (es werden nur .sav Dateien angezeigt)
	 * @param frame ShisenFrame Objekt des Spielfensters
	 */
	public FileDialog(ShisenFrame frame)
	{
		this.frame = frame;
	}
	
	/**
	 * Interner Konstruktor, der benötigt wird um den FileFilter zu 
	 * instanziieren
	 */
	private FileDialog(){}
	
	/**
	 * Aufruf eines "Speichern" FileDialog
	 * @return File Objekt des zu speichernden Levels
	 */
	public String saveFileDialog()
	{
		//Anzeige des Dialogfensters "speichern" mit Dateifilter
		FileFilter filter = new FileDialog();
		chooser.addChoosableFileFilter(filter);
		chooser.showSaveDialog(frame);
		
		//Rückgabestring für den Filename passend bereitstellen
		if (chooser.getSelectedFile() != null)
		{
			String ret = chooser.getSelectedFile().getAbsolutePath();
			if (!ret.endsWith(saveFileExtension))
				ret += saveFileExtension;
			return ret;
		}
		else
			return null;
	}
	
	/**
	 * Aufruf eines "Laden" FileDialog
	 * @return File Objekt des zu ladenden Levels
	 */
	public String loadFileDialog()
	{
		//Anzeige des Dialogfensters "laden" mit Dateifilter
		FileFilter filter = new FileDialog();
		chooser.addChoosableFileFilter(filter);
		chooser.showOpenDialog(frame);
		
		//Rückgabestring für den Filename bereitstellen
		if (chooser.getSelectedFile() != null)
			return chooser.getSelectedFile().getAbsolutePath();
		else
			return null;
		
	}

	/**
	 * Überschreibt die FileFilter Methode zur Auswahl
	 * angezeigter Dateien im FileDialog.
	 * In unserem Fall wollen wir nur Spielstandsdateien
	 * mit der in saveFileExtension gespeicherten Dateiendung
	 * anzeigen.
	 * @param f File Objekt
	 * @return boolean Gibt true/false zurück, abhängig von 
	 * 					der Dateiendung
	 */
	@Override
	public boolean accept(File f) {
		if (f.isDirectory())
			return true;
		else
		{
			int dotPos = f.toString().lastIndexOf(".");
			String extension = f.toString().substring(dotPos);
			if (extension.toLowerCase().equals(saveFileExtension))
				return true;
			else
				return false;
		}
			
	}

	@Override
	/**
	 * Gibt die Beschreibung für den auswählbaren Dateifilter im 
	 * FileDialog zurück
	 * @return String 
	 */
	public String getDescription() {
		return "Shisen Spielstand *.sve";
	}
}
