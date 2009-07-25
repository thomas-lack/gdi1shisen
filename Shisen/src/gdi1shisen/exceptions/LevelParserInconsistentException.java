package gdi1shisen.exceptions;

public class LevelParserInconsistentException extends Exception
{
	private static final long serialVersionUID = -7723039002885105731L;

	public LevelParserInconsistentException()
	{
		super("LevelParser Objekte sind inkonsistent. " +
				"Möglicherweise treten Fehler bei der Leveldarstellung " +
				"oder Berechnung auf.");
	}
	
	public LevelParserInconsistentException(String errorMsg)
	{
		super(errorMsg);
	}
}
