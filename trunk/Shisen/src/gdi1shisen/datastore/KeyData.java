package gdi1shisen.datastore;

public class KeyData 
{
	// speichert x,y Koordinaten ddes Cursors
	private int[] cursor;
	
	public KeyData()
	{
		cursor = new int [] {1,1};
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
