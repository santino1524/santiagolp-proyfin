
package com.ntd.exceptions;

/**
 * Excepcion personalizada padre
 * 
 * @author SLP
 *
 */
public class CustomExceptions extends Exception {

	/**
	 * SerialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public CustomExceptions() {
	}

	/**
	 * Constructor con Msg
	 * 
	 * @param message
	 */
	public CustomExceptions(String message) {
		super(message);
	}

}
