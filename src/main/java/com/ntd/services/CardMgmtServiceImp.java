package com.ntd.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ntd.dto.CardDTO;
import com.ntd.dto.UserDTO;
import com.ntd.dto.mapper.DTOMapperI;
import com.ntd.exceptions.InternalException;
import com.ntd.persistence.Card;
import com.ntd.persistence.CardRepositoryI;
import com.ntd.utils.ValidateParams;

import lombok.extern.slf4j.Slf4j;

/**
 * Servicio de gestion de tarjetas bancarias
 * 
 * @author SLP
 */
@Service
@Slf4j
public class CardMgmtServiceImp implements CardMgmtServiceI {

	/** Dependencia de CardRepository */
	private final CardRepositoryI cardRepository;

	/**
	 * Constructor
	 * 
	 * @param cardRepository
	 */
	public CardMgmtServiceImp(final CardRepositoryI cardRepository) {
		this.cardRepository = cardRepository;
	}

	@Override
	public CardDTO insertCard(CardDTO cardDto) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Insertar tarjeta bancaria");

		// Validar parametro
		ValidateParams.isNullObject(cardDto);

		// Mapear DTO y guardar
		final Card card = cardRepository.save(DTOMapperI.MAPPER.mapDTOToCard(cardDto));

		// Retornar DTO
		return DTOMapperI.MAPPER.mapCardToDTO(card);
	}

	@Override
	public List<CardDTO> searchAllCard() {
		if (log.isInfoEnabled())
			log.info("Buscar todos las tarjetas bancarias");

		// Buscar todos las tarjetas
		final List<Card> cards = cardRepository.findAll();

		// Mapear DTO
		final List<CardDTO> cardsDto = new ArrayList<>();

		if (!cards.isEmpty()) {
			for (Card card : cards) {
				cardsDto.add(DTOMapperI.MAPPER.mapCardToDTO(card));
			}
		}

		// Retornar lista DTO
		return cardsDto;

	}

	@Override
	public void deleteCard(Long id) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Eliminar tarjeta bancaria");

		// Validar parametro
		ValidateParams.isNullObject(id);

		// Eliminar por Id
		cardRepository.deleteById(id);
	}

	@Override
	public boolean existsCard(String cardNumber) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Verificar si existe el numero de tarjeta");

		// Validar parametro
		ValidateParams.isNullObject(cardNumber);

		// Retornar si existe la tarjeta en BBDD
		return cardRepository.existsByCardNumber(cardNumber);
	}

	@Override
	public boolean existsByUser(UserDTO userDto) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Verificar si existe por usuario");

		// Validar parametro
		ValidateParams.isNullObject(userDto);

		// Retornar si existe la tarjeta por usuario
		return cardRepository.existsByUser(DTOMapperI.MAPPER.mapDTOToUser(userDto));
	}

	@Override
	public List<CardDTO> searchByUser(UserDTO userDto) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar las tarjetas de un usuario");

		// Validar parametro
		ValidateParams.isNullObject(userDto);

		// Buscar tarjetas
		final List<Card> cards = cardRepository.findByUser(DTOMapperI.MAPPER.mapDTOToUser(userDto));

		// Mapear DTO
		final List<CardDTO> cardsDto = new ArrayList<>();

		if (!cards.isEmpty()) {
			for (Card card : cards) {
				cardsDto.add(DTOMapperI.MAPPER.mapCardToDTO(card));
			}
		}

		// Retornar lista DTO
		return cardsDto;
	}

}
