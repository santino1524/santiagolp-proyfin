
package com.ntd.services;

import com.ntd.dto.UserDTO;
import com.ntd.exceptions.InternalException;
import com.ntd.persistence.ConfirmationToken;

import jakarta.transaction.Transactional;

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
	 * @param userDto
	 */
	public String save(final UserDTO userDto);

	/**
	 * Buscar token por ID
	 * 
	 * @param token
	 * @return ConfirmationToken
	 */
	public ConfirmationToken findByToken(final String token);

	/**
	 * Eliminar ConfirmationToken
	 * 
	 * @param confirmationToken
	 * @throws InternalException
	 */
	@Transactional
	public void deleteConfirmationToken(ConfirmationToken confirmationToken) throws InternalException;
}
