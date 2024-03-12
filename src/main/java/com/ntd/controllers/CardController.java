package com.ntd.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ntd.dto.CardDTO;
import com.ntd.dto.UserDTO;
import com.ntd.exceptions.InternalException;
import com.ntd.services.CardMgmtServiceI;
import com.ntd.utils.Constants;
import com.ntd.utils.ValidateParams;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador de Tarjetas bancarias
 * 
 * @author SLP
 */
@Slf4j
@RestController
@RequestMapping("/cards")
public class CardController {

	/** Dependencia del servicio de gestion de tarjetas */
	private final CardMgmtServiceI cardMgmtService;

	/**
	 * Constructor
	 * 
	 * @param cardMgmtService
	 */
	public CardController(final CardMgmtServiceI cardMgmtService) {
		this.cardMgmtService = cardMgmtService;
	}

	/**
	 * Buscar todas las tarjetas bancarias
	 * 
	 * @return List
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchAll")
	public List<CardDTO> showCards() throws InternalException {
		if (log.isInfoEnabled())
			log.info("Mostrar todas las tarjetas");

		// Retornar la lista de tarjetas
		return cardMgmtService.searchAllCard();
	}

	/**
	 * Guardar nueva tarjeta
	 * 
	 * @param cardDto
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@PostMapping
	public ResponseEntity<String> saveCard(@RequestBody @Valid final CardDTO cardDto) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Guardar nueva tarjeta");

		ResponseEntity<String> result = null;

		// Comprobar si la tarjeta existe
		if (cardMgmtService.existsCard(cardDto.cardNumber()) && cardMgmtService.existsByUser(cardDto.userDto())) {
			// Devolver una respuesta con codigo de estado 422
			result = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Constants.MSG_CARD_EXISTS);
		} else {
			// Guardar tarjeta
			if (cardMgmtService.insertCard(cardDto).cardId() != null) {
				// Devolver una respuesta con codigo de estado 202
				result = ResponseEntity.status(HttpStatus.ACCEPTED).body(Constants.MSG_SUCCESSFUL_OPERATION);
			} else {
				// Devolver una respuesta con codigo de estado 422
				result = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Constants.MSG_UNEXPECTED_ERROR);
			}
		}

		// Retornar respuesta
		return result;
	}

	/**
	 * Eliminar tarjeta
	 * 
	 * @param cardDto
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@Transactional
	@DeleteMapping
	public ResponseEntity<String> deleteCard(@RequestBody @NotNull final CardDTO cardDto) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Eliminar tarjeta bancaria");

		// Validar id
		ValidateParams.isNullObject(cardDto.cardId());

		// Eliminar tarjeta
		cardMgmtService.deleteCard(cardDto.cardId());

		// Devolver una respuesta con codigo de estado 202
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(Constants.MSG_SUCCESSFUL_OPERATION);
	}

	/**
	 * Buscar las tarjetas de un usuario
	 * 
	 * @param userDto
	 * @return List
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByUser")
	public List<CardDTO> searchByUser(@RequestBody @NotNull final UserDTO userDto) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar las tarjetas de un usuario");

		// Retornar tarjetas
		return cardMgmtService.searchByUser(userDto);
	}

}
