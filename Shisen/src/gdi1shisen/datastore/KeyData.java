package gdi1shisen.datastore;

import java.io.Serializable;

public class KeyData implements Serializable
{
	private static final long serialVersionUID = 1517513107677297148L;
	
	// speichert x,y Koordinaten ddes Cursors
	private int[] cursor;
	
	public KeyData()
	{
		cursor = null;
	}
	
	/**
	 * getter für den Cursor
	 * @return 2D Integer Array {x,y} Koordinaten des Cursors
	 */
	public int[] getCursor() {
		return cursor;
	}
	/**
	 * setter für den Cursor
	 * @param cursor 2D Integer Array {x,y} Koordinaten des Cursors
	 */
	public void setCursor(int[] cursor) {
		this.cursor = cursor;
	}
	
	
}
