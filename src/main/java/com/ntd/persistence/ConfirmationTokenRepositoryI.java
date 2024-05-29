package com.ntd.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio T_TOKEN_CONFIRMATION
 * 
 * @author slopezpi
 */
public interface ConfirmationTokenRepositoryI extends JpaRepository<ConfirmationToken, Long> {

	/**
	 * Buscar por token
	 * 
	 * @param token
	 * @return ConfirmationToken
	 */
	public ConfirmationToken findByToken(String token);
}
