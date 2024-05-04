package com.ntd.controllers;

import java.util.Collections;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ntd.dto.PostalAddressDTO;
import com.ntd.exceptions.InternalException;
import com.ntd.services.PostalAddressMgmtServiceI;

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
@Controller
@RequestMapping("/addresses")
public class PostalAddressController {

	/** Dependencia del servicio de gestion de direcciones */
	private final PostalAddressMgmtServiceI postalAddressMgmtService;

	/**
	 * Constructor
	 * 
	 * @param postalAddressMgmtService
	 */
	public PostalAddressController(final PostalAddressMgmtServiceI postalAddressMgmtService) {
		this.postalAddressMgmtService = postalAddressMgmtService;
	}

	/**
	 * Guardar direccion
	 * 
	 * @param postalAddressDto
	 * @return String
	 * @throws InternalException
	 */
	@PostMapping
	public ResponseEntity<Void> savePostalAddress(@RequestParam @Valid final PostalAddressDTO postalAddressDto)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Guardar direccion");

		ResponseEntity<Void> result = null;

		// Comprobar si existe alguna direccion
		PostalAddressDTO addressDto = postalAddressMgmtService.findByCityDirectionLineProvince(
				postalAddressDto.directionLine(), postalAddressDto.city(), postalAddressDto.province());

		if (addressDto != null && addressDto.addressId() != null) {
			boolean exists = postalAddressMgmtService.existsByUser(postalAddressDto.users().get(0).getUserId(),
					addressDto.addressId());

			if (exists) {
				// Si existe para el usuario devolver codigo 422
				return ResponseEntity.unprocessableEntity().build();
			}

			postalAddressMgmtService.insertRelation(postalAddressDto.users().get(0).getUserId(),
					addressDto.addressId());

			// devolver codigo 200
			return ResponseEntity.ok().build();
		}

		// Guardar direccion
		if (postalAddressMgmtService.insertPostalAddress(postalAddressDto) != null) {
			result = ResponseEntity.ok().build();
		} else {
			// Devolver codigo 500 si no se ha guardado correctamente
			result = ResponseEntity.internalServerError().build();
		}

		// Retornar respuesta
		return result;
	}

	/**
	 * Eliminar relacion de direccion con usuario
	 * 
	 * @param addressId
	 * @param userId
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@Transactional
	@DeleteMapping("/deleteRelation")
	public ResponseEntity<Void> deleteRelationPostalAddress(@RequestParam @NotNull final Long addressId,
			@NotNull final Long userId) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Eliminar direccion");

		// Eliminar direccion
		postalAddressMgmtService.deleteRelationPostalAddress(userId, addressId);

		return ResponseEntity.ok().build();
	}

	/**
	 * Buscar todos las direcciones
	 * 
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByUser")
	public ResponseEntity<Object> searchByUser(@RequestParam @NotNull final Long userid) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Mostrar las direcciones por usuario");

		// Retornar lista de direcciones
		return ResponseEntity.ok()
				.body(Collections.singletonMap("addresses", postalAddressMgmtService.searchByUser(userid)));
	}

	/**
	 * Buscar por direccion por id
	 * 
	 * @param addressId
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchById")
	public ResponseEntity<Object> searchById(@RequestParam @NotNull final Long addressId) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar por id");

		// Retornar direccion
		return ResponseEntity.ok()
				.body(Collections.singletonMap("address", postalAddressMgmtService.searchById(addressId)));
	}
}
