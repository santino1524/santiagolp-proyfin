package com.ntd.controllers.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ntd.dto.PostalAddressDTO;
import com.ntd.exceptions.InternalException;
import com.ntd.services.PostalAddressMgmtServiceI;
import com.ntd.utils.Constants;
import com.ntd.utils.ValidateParams;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador de Direcciones postales
 * 
 * @author SLP
 */
@Slf4j
@RestController
@RequestMapping("/addresses")
public class PostalAddressControllerRest {

	/** Dependencia del servicio de gestion de direcciones */
	private final PostalAddressMgmtServiceI postalAddressMgmtService;

	/**
	 * Constructor
	 * 
	 * @param postalAddressMgmtService
	 */
	public PostalAddressControllerRest(final PostalAddressMgmtServiceI postalAddressMgmtService) {
		this.postalAddressMgmtService = postalAddressMgmtService;
	}

	/**
	 * Guardar direccion
	 * 
	 * @param postalAddressDto
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@PostMapping
	public ResponseEntity<String> savePostalAddress(@RequestBody @Valid final PostalAddressDTO postalAddressDto)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Guardar direccion");

		ResponseEntity<String> result = null;

		// Guardar direccion
		if (postalAddressMgmtService.insertPostalAddress(postalAddressDto) != null) {
			// Devolver una respuesta con codigo de estado 202
			result = ResponseEntity.status(HttpStatus.ACCEPTED).body(Constants.MSG_SUCCESSFUL_OPERATION);
		} else {
			// Devolver una respuesta con codigo de estado 422
			result = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Constants.MSG_UNEXPECTED_ERROR);
		}

		// Retornar respuesta
		return result;
	}

	/**
	 * Eliminar direccion
	 * 
	 * @param postalAddressDto
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@Transactional
	@DeleteMapping
	public ResponseEntity<String> deletePostalAddress(@RequestBody @NotNull final PostalAddressDTO postalAddressDto,
			@NotNull final Long userId) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Eliminar direccion");

		// Validar campos del id
		ValidateParams.isNullObject(postalAddressDto.city());
		ValidateParams.isNullObject(postalAddressDto.directionLine());
		ValidateParams.isNullObject(postalAddressDto.province());

		// Eliminar direccion
		postalAddressMgmtService.deleteRelationPostalAddress(userId, postalAddressDto.city(),
				postalAddressDto.directionLine(), postalAddressDto.province());
		postalAddressMgmtService.deletePostalAddress(postalAddressDto);

		// Devolver una respuesta con codigo de estado 202
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(Constants.MSG_SUCCESSFUL_OPERATION);
	}

	/**
	 * Eliminar relacion de direccion con usuario
	 * 
	 * @param postalAddressDto
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@Transactional
	@DeleteMapping
	public ResponseEntity<String> deleteRelationPostalAddress(
			@RequestBody @NotNull final PostalAddressDTO postalAddressDto, @NotNull final Long userId)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Eliminar direccion");

		// Validar campos del id
		ValidateParams.isNullObject(postalAddressDto.city());
		ValidateParams.isNullObject(postalAddressDto.directionLine());
		ValidateParams.isNullObject(postalAddressDto.province());

		// Eliminar direccion
		postalAddressMgmtService.deleteRelationPostalAddress(userId, postalAddressDto.city(),
				postalAddressDto.directionLine(), postalAddressDto.province());

		// Devolver una respuesta con codigo de estado 202
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(Constants.MSG_SUCCESSFUL_OPERATION);
	}

	/**
	 * Buscar todos las direcciones
	 * 
	 * @return List
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchAll")
	public List<PostalAddressDTO> showOrders() throws InternalException {
		if (log.isInfoEnabled())
			log.info("Mostrar todos las direcciones");

		// Retornar lista de direcciones
		return postalAddressMgmtService.searchAll();
	}

	/**
	 * Buscar por direccion por id
	 * 
	 * @param postalAddressDto
	 * @return PostalAddressDTO
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchById")
	public PostalAddressDTO searchById(@RequestBody @NotNull final PostalAddressDTO postalAddressDto)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar por id");

		// Validar campos del id
		ValidateParams.isNullObject(postalAddressDto.city());
		ValidateParams.isNullObject(postalAddressDto.directionLine());
		ValidateParams.isNullObject(postalAddressDto.province());

		// Retornar direccion
		return postalAddressMgmtService.searchById(postalAddressDto);
	}
}
