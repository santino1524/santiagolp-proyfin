package com.ntd.exceptions;

import com.ntd.utils.Constants;

/**
 * Error al eliminar las imagenes de los productos
 * 
 * @author SLP
 */
public class DeleteFilesException extends CustomExceptions {

	/** SerialVersion */
	private static final long serialVersionUID = 5505731101444788198L;

	/**
	 * Constructor con mensaje
	 */
	public DeleteFilesException() {
		super(Constants.MSG_DELETE_FILES_EXC);
	}

}
