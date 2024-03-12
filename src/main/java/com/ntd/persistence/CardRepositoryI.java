package com.ntd.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio T_CARD
 * 
 * @author SLP
 */
public interface CardRepositoryI extends JpaRepository<Card, Long> {

	/**
	 * Comprobar existencia de tarjeta por su numero
	 * 
	 * @param cardNumber
	 * @return boolean
	 */
	public boolean existsByCardNumber(String cardNumber);

	/**
	 * Comprobar existencia por usuario
	 * 
	 * @param user
	 * @return boolean
	 */
	public boolean existsByUser(User user);

	/**
	 * Buscar las tarjetas de un usuario
	 * 
	 * @param user
	 * @return List
	 */
	public List<Card> findByUser(User user);

}
