
package com.ntd.services;

import com.ntd.dto.UserDTO;
import com.ntd.persistence.ConfirmationToken;

/**
 * Servicio token
 * 
 * @author slopezpi
 */
public interface TokenMgmtServiceI {

	/**
	 * Generar token
	 * 
	 * @return String
	 */
	public String generateToken();

	/**
	 * Guardar token y usuario
	 * 
	 * @param user
	 */
	public String save(final UserDTO userDto);

	/**
	 * Buscar token por ID
	 * 
	 * @param token
	 * @return ConfirmationToken
	 */
	public ConfirmationToken findByToken(final String token);
}
