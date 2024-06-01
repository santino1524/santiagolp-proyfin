
package com.ntd.services;

import jakarta.mail.MessagingException;

/**
 * Servicio de correos
 * 
 * @author slopezpi
 */
public interface EmailMgmtServiceI {

	/**
	 * Enviar correo
	 * 
	 * @param to
	 * @param subject
	 * @param text
	 * @throws MessagingException
	 */
	public void sendEmail(String to, String subject, String text) throws MessagingException;
}
