package gdi1shisen.datastore;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.*;

;

public class Sound extends Thread{
	// Playerklasse
	private Player sound;
	// bool der abfragt, ob wirklich eine datei geoeffnet wird
	private boolean isFile;

	/**
	 * Konstruktor
	 * @param s pfad der datei
	 */
	public Sound(String s) {
		InputStream inps;
		File fis = new File(s);
		// check auf datei
		if (fis.isFile() && !fis.isDirectory()) {
			isFile = true;
			try {
				inps = new FileInputStream(fis);
				sound = new Player(inps);
			} catch (FileNotFoundException e) {
				System.err.println(e.getMessage());
			} catch (JavaLayerException e) {
				System.err.println(e.getMessage());
			}
		} else
			isFile = false;
	}

	/**
	 * Spielt die Sounddatei ab
	 */
	public void play() {
		// sofern wahr true, wird sound abgespielt
		try {
			if (isFile)
				sound.play();
		} catch (JavaLayerException e) {
			System.err.println(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		play();
	}

}
