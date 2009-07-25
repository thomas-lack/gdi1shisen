package gdi1shisen.exceptions;

public class SyntacticIncException extends Exception{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 6532200232845239937L;

	/**
	 * Creates a new instance of the SyntacticIncorrectException class
	 * 
	 * @param errorMessage
	 * 			A string description of the reason why this operation is
	 * 			invalid
	 */
	public SyntacticIncException(String errorMessage){
		super(errorMessage);
	}
}