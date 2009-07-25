package gdi1shisen.gui;


	import java.awt.Dimension;
	import java.io.IOException;
	import java.io.InputStream;
	import java.io.InputStreamReader;

	import javax.swing.JFrame;
	import javax.swing.JScrollPane;
	import javax.swing.JTextArea;

	
	//Das Ganze benötigt eine weiter Datei "Spielregeln.txt"
	public class SpielRegeln  extends JFrame {

	    /**
		 * 
		 */
		private static final long serialVersionUID = -123;

		public SpielRegeln() {
	    	
	    	//Name wird gesetzt
	        super("SpielRegeln");
	        //nur das Fenster wird versteckt
	        setDefaultCloseOperation(HIDE_ON_CLOSE);
            
	        //Datei wird eingeladen
	        InputStream in = getClass().getResourceAsStream("Spielregeln.txt");
	        JTextArea spielregeln = new JTextArea();
	        try {
	            spielregeln.read(new InputStreamReader(in), null);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	        //Scrollbalken wird definiert
	        JScrollPane scrollPane = new JScrollPane(spielregeln);
	        //Größe des Fensters 
	        scrollPane.setPreferredSize(new Dimension(300, 300));
	       
	        //nicht editierbar
	        spielregeln.setEditable(false);
	   
	        add(scrollPane);
	        pack();
	       
	    }
 }
