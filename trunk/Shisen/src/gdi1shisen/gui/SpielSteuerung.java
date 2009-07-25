
	package gdi1shisen.gui;


	import java.awt.Dimension;
	import java.io.IOException;
	import java.io.InputStream;
	import java.io.InputStreamReader;

	import javax.swing.JFrame;
	import javax.swing.JScrollPane;
	import javax.swing.JTextArea;

	
	//Das Ganze benötigt eine weiter Datei "Spielsteuerung.txt"
	public class SpielSteuerung  extends JFrame {

	    /**
		 * 
		 */
		private static final long serialVersionUID = -126;

		public SpielSteuerung() {
	    	
	    	//Name wird gesetzt
	        super("SpielSteuerung");
	        //nur das Fenster wird versteckt
	        setDefaultCloseOperation(HIDE_ON_CLOSE);
            
	        //Datei wird eingeladen
	        InputStream in = getClass().getResourceAsStream("Spielsteuerung.txt");
	        JTextArea spielsteuer = new JTextArea();
	        try {
	        	spielsteuer.read(new InputStreamReader(in), null);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	        //Scrollbalken wird definiert
	        JScrollPane scrollPane = new JScrollPane(spielsteuer);
	        //Größe des Fensters 
	        scrollPane.setPreferredSize(new Dimension(300, 300));
	       
	        //nicht editierbar
	        spielsteuer.setEditable(false);
	   
	        add(scrollPane);
	        pack();
	       
	    }

}
