package gdi1shisen.datastore;

/**
 * Ein Step ist ein Spielzug - repraesentiert durch zwei koordinatenpaaren mit
 * selben Inhalt
 * 
 * @author chandra
 * 
 */
public class Step 
{
	/**
	 * inahtl der steinpaare
	 */
	public char content;
	/**
	 * Koordinaten des ersten Punkts 0=x,1=y
	 */
	public int[] pointOne;
	/**
	 * Koordinaten des zweiten Punktes
	 */
	public int[] pointTwo;

	/**
	 * Contructor eines Punktes
	 * 
	 * @param pointOne
	 *            Koordinatenpaar - erster Punkt
	 * @param pointTwo
	 *            Koordinatenpaar - zweiter Punkt
	 * @param content
	 *            Inhalt
	 */
	public Step(int[] pointOne, int[] pointTwo, char content) 
	{
		this.content = content;
		this.pointOne = pointOne.clone();
		this.pointTwo = pointTwo.clone();
	}
}
