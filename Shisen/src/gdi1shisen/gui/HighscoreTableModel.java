package gdi1shisen.gui;

import javax.swing.table.AbstractTableModel;

public class HighscoreTableModel extends AbstractTableModel 
{
	private static final long serialVersionUID = 5236382823626886653L;

	private String[] columnName;
	private String[][] data;
	
	public HighscoreTableModel(String[][] rowData, String[] columnName)
	{
		this.columnName = columnName;
		this.data = rowData;
	}
	
	@Override
	public int getColumnCount() 
	{
		return columnName.length;
	}

	@Override
	public int getRowCount() 
	{
		return data.length;
	}

	@Override
	public Object getValueAt(int row, int col) 
	{
		return data[row][col];
	}

}

