package com.ntd.services;

import java.util.List;

import com.ntd.dto.CardDTO;
import com.ntd.dto.UserDTO;
import com.ntd.exceptions.InternalException;

/**
 * Servicio de gestion de tarjetas bancarias
 * 
 * @author SLP
 */
public interface CardMgmtServiceI {

	/**
	 * Insertar nueva tarjeta bancaria
	 * 
	 * @param cardDto
	 * @return CardDTO
	 * @throws InternalException
	 */
	public CardDTO insertCard(final CardDTO cardDto) throws InternalException;

	/**
	 * Buscar todas las tarjetas bancarias
	 * 
	 * @return List
	 */
	public List<CardDTO> searchAllCard();

	/**
	 * Eliminar tarjetas bancarias
	 * 
	 * @param id
	 * @throws InternalException
	 */
	public void deleteCard(final Long id) throws InternalException;

	/**
	 * Comprobar existencia por numero de tarjeta
	 * 
	 * @param cardNumber
	 * @return boolean
	 * @throws InternalException
	 */
	public boolean existsCard(final String cardNumber) throws InternalException;

	/**
	 * Comprobar existencia por usuario
	 * 
	 * @param userDto
	 * @return boolean
	 * @throws InternalException
	 */
	public boolean existsByUser(UserDTO userDto) throws InternalException;

	/**
	 * Buscar las tarjetas de un usuario
	 * 
	 * @param userDto
	 * @return List
	 * @throws InternalException
	 */
	public List<CardDTO> searchByUser(UserDTO userDto) throws InternalException;
}
