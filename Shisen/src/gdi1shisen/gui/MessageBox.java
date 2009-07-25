package gdi1shisen.gui;

import javax.swing.JOptionPane;

/**
 * Erstellt eine Info-Dialogbox im übergebenen Spielfenster und mit 
 * dem übergebenen Inhalt
 * @author Thomas Lack
 */
public class MessageBox extends JOptionPane
{
	private static final long serialVersionUID = -1842180922419699507L;

	/**
	 * Erstellt eine Dialogbox im Spielfenster
	 * @param frame ShisenFrame Spielfenster Objekt
	 * @param title String Titel der Dialogbox
	 * @param message String Nachricht der Dialogbox
	 */
	public MessageBox(ShisenFrame frame, String title, String message)
	{
		JOptionPane.showMessageDialog(frame, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
}
