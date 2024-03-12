
package com.ntd.exceptions;

import com.ntd.utils.Constants;

import lombok.extern.slf4j.Slf4j;

/**
 * Excepcion Interna Provocada por un mal funcionamiento de la app. Ejemplo:
 * Parametro con valor nulo cuando nunca debe serlo.
 * 
 * @author SLP
 */
@Slf4j
public class InternalException extends CustomExceptions {

	/** SerialVersion */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor con mensaje
	 */
	public InternalException() {
		super(Constants.MSG_INTERNAL_EXC);

		if (log.isErrorEnabled())
			log.error(Constants.MSG_INTERNAL_EXC);
	}

}
