package gdi1shisen.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import gdi1shisen.datastore.Highscore;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

public class HighscoreFrame extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 7762283860057909118L;
	
	private JTable table;
	private JButton exitButton = new JButton();
	private JLabel headline = new JLabel();
	private Highscore highscore;
	private String columnNames[] = {"#", "Name", "Spielzeit"};
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
		this.highscore = highscore;
		data = new JLabel[highscore.size()][3];
		
		//Standardwerte setzen
		ImageIcon highscoreHeadline = new ImageIcon("Images/highscore.png");
		headline.setIcon(highscoreHeadline);
		exitButton.setText("Highscore Liste schließen");
		exitButton.addActionListener(this);
		dataPanel.setLayout(new GridLayout(highscore.size(),3));
		
		//Befüllung der Highscore Tabelle
		//fillHighscoreTable();
		fillDataPanel();
		
		//hinzufügen der einzelnen Bauteile zum Frame
		add(headline, BorderLayout.NORTH);
		add(table, BorderLayout.CENTER);
		add(exitButton, BorderLayout.SOUTH);
		
		//Fenstergröße anpassen und Fenster anzeigen
		pack();
		setVisible(true);
	}
	
	private void fillDataPanel()
	{
		int countRows = highscore.size();
		String rowData[] = new String[3];
		
		for (int i=0; i<countRows; i++)
		{
			rowData = highscore.getRowData(i);
			
		}
	}
	
	/**
	 * Befüllung der Tabelle aus dem Highscore Objekt
	 */
	private void fillHighscoreTable()
	{
		int countRows = highscore.size();
				
		String rowData[][] = new String[countRows][3];
		
		for (int i=0; i<countRows; i++)
		{
			rowData[i] = highscore.getRowData(i);			
		}
		table = new JTable(new HighscoreTableModel(rowData, columnNames));
		table.setDefaultRenderer(Color.class, new HighscoreTableCellRenderer());
		table.setBackground(Color.BLACK);
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
