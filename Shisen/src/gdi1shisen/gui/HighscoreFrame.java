package gdi1shisen.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import gdi1shisen.datastore.Highscore;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.SwingConstants;

public class HighscoreFrame extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 7762283860057909118L;
	
	private JButton exitButton = new JButton();
	private JLabel headline = new JLabel();
	private Highscore highscore;
	private JLabel[][] data;
	private JPanel dataPanel = new JPanel();
	
	/**
	 * Konstruktor für ein Highscore Fenster
	 * Ein übergebenes Highscore Objekt wird dabei
	 * in Tabellenform ausgegeben.
	 * @param highscore Highscore Objekt
	 */
	public HighscoreFrame(Highscore highscore, ShisenFrame frame)
	{
		super("Highscore Liste");
		setLayout(new BorderLayout());
		setLocation(frame.getLocation());
		setBackground(Color.BLACK);
		this.highscore = highscore;
				
		//Standardwerte setzen
		ImageIcon highscoreHeadline = new ImageIcon("Images/highscore.png");
		headline.setIcon(highscoreHeadline);
		headline.setHorizontalAlignment(SwingConstants.CENTER);
		headline.setBackground(Color.BLACK);
		headline.setOpaque(true);
		exitButton.setText("Highscore Liste schließen");
		exitButton.addActionListener(this);
		dataPanel.setLayout(new GridLayout(highscore.size(),3));
		dataPanel.setBackground(Color.BLACK);
		
		//Befüllung der Highscore Tabelle
		//fillHighscoreTable();
		fillDataPanel();
		
		//hinzufügen der einzelnen Bauteile zum Frame
		add(headline, BorderLayout.NORTH);
		add(dataPanel, BorderLayout.CENTER);
		add(exitButton, BorderLayout.SOUTH);
		
		//Fenstergröße anpassen und Fenster anzeigen
		pack();
		setVisible(true);
	}
	
	/**
	 * Befüllen das DatenPanels mit Einträgen aus der Highscore Liste
	 */
	private void fillDataPanel()
	{
		data = new JLabel[highscore.size()][3];
		
		int countRows = highscore.size();
		String rowData[] = new String[3];
		
		ImageIcon crown = new ImageIcon("Images/crown.png");
		
		//hinzufügen aller JLabel zum mittleren Anzeigepanel
		for (int i=0; i<countRows; i++)
			for (int j=0; j<3; j++)
			{
				data[i][j] = new JLabel();
				data[i][j].setForeground(Color.WHITE);
				data[i][j].setFont(new Font("SansSerif", Font.PLAIN, 15));
				data[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				dataPanel.add(data[i][j]);
			}
		
		//Alle JLabel befüllen
		for (int i=0; i<countRows; i++)
		{
			rowData = highscore.getRowData(i);
			if (i==0)
				data[i][0].setIcon(crown);
			else
				data[i][0].setText(rowData[0]);
			data[i][1].setText(rowData[1]);
			data[i][2].setText(rowData[2]);
		}
	}

	@Override
	/**
	 * Actionlistener für "Schließen" Button
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
