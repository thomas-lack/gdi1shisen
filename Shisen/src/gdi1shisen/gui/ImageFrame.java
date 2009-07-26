package gdi1shisen.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Klasse zum Erzeugen eines "Über uns" Fensters
 * @author Thomas, Eugen
 */
public class ImageFrame extends JFrame implements ActionListener
{
	
	private static final long serialVersionUID = -2344L;
	
	private JLabel centerImage = new JLabel();
	private JButton exitButton = new JButton(); 
	
	/**
	 * Konstruktor für das Fenster
	 * @param windowTitle String
	 */
	public ImageFrame(String windowTitle, ShisenFrame frame, String imagePath)
	{
		super(windowTitle);
		setLayout(new BorderLayout());
		setLocation(frame.getLocation());
		
		//Befüllen des Labels und des JButtons mit Inhalten
		ImageIcon teamPicture = new ImageIcon(imagePath);
		centerImage.setIcon(teamPicture);
		exitButton.setText("Fenster schließen");
		exitButton.addActionListener(this);
		
		//Hinzufügen zum Fenster
		add(centerImage, BorderLayout.CENTER);
		add(exitButton, BorderLayout.SOUTH);
		
		//Fenster resizen und anzeigen
		pack();
		setVisible(true);
	}

	@Override
	/**
	 * Abfangen des Events zum schließen des Fensters
	 * @param e ActionEvent
	 */
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource().equals(exitButton))
		{
			dispose();
		}
	}
}	

