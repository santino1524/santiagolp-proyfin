
package com.ntd.services;

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
	 */
	public void sendEmail(String to, String subject, String text);
}
