package gdi1shisen.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;


import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


public class HighscoreTableCellRenderer extends DefaultTableCellRenderer 
{
	private static final long serialVersionUID = 9217189333703359535L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object obj,
			boolean isSelected, boolean hasFocus, int row, int column) 
	{
		//Color newColor = (Color)color;
		//setOpaque(true);
		setBackground(Color.BLACK);
		setFont(new Font("SansSerif", Font.PLAIN, 15));
		
		return this;
	}

}
