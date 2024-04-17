package com.ntd.exceptions;

import lombok.extern.slf4j.Slf4j;

/**
 * Excepcion al subir las imagenes de producto
 * 
 * @author SLP
 */
@Slf4j
public class FileUploadException extends CustomExceptions {

	/** SerialVersion */
	private static final long serialVersionUID = -407761404114702848L;

	public FileUploadException() {
		super("Error al subir las imagenes del producto");

		if (log.isErrorEnabled())
			log.error("Error al subir las imagenes del producto");
	}
}
