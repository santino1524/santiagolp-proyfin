
package com.ntd.exceptions;

/**
 * Excepcion personalizada padre
 * 
 * @author SLP
 *
 */
public class CustomExceptions extends Exception {

	/** SerialVersion */
	private static final long serialVersionUID = -3040245221253683688L;

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
