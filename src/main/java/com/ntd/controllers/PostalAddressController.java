package com.ntd.controllers;

import java.util.Collections;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ntd.dto.PostalAddressDTO;
import com.ntd.exceptions.InternalException;
import com.ntd.persistence.User;
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

	private static final String ADDRESS = "address";
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
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@PostMapping(path = "/save")
	public ResponseEntity<Object> savePostalAddress(@ModelAttribute @Valid final PostalAddressDTO postalAddressDto)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Guardar direccion");

		ResponseEntity<Object> result = null;

		// Comprobar si el usuario contiene esa direccion
		PostalAddressDTO addressDto = postalAddressMgmtService.findByCityDirectionLineProvinceUser(
				postalAddressDto.directionLine(), postalAddressDto.city(), postalAddressDto.province(),
				new User(postalAddressDto.userId(), null, null, null, null, null, null, null, null, false, true, null,
						null, null, null, null));

		if (addressDto != null && addressDto.addressId() != null) {
			// Si existe para el usuario devolver codigo 422
			return ResponseEntity.unprocessableEntity().build();
		}

		// Guardar direccion
		PostalAddressDTO newAddressDto = postalAddressMgmtService.insertPostalAddress(postalAddressDto);
		if (newAddressDto != null) {
			return ResponseEntity.ok().body(Collections.singletonMap(ADDRESS, newAddressDto));
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
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@Transactional
	@DeleteMapping("/delete")
	public ResponseEntity<Void> deleteRelationPostalAddress(@RequestParam @NotNull final Long addressId)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Eliminar direccion");

		// Eliminar direccion
		postalAddressMgmtService.deletePostalAddress(addressId);

		return ResponseEntity.ok().build();
	}

	/**
	 * Buscar todos las direcciones
	 * 
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByUser")
	public ResponseEntity<Object> searchByUser(@RequestParam @NotNull final Long userId) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Mostrar las direcciones por usuario");

		// Retornar lista de direcciones
		return ResponseEntity.ok()
				.body(Collections.singletonMap("addresses", postalAddressMgmtService.searchByUser(new User(userId, null,
						null, null, null, null, null, null, null, false, true, null, null, null, null, null))));
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
				.body(Collections.singletonMap(ADDRESS, postalAddressMgmtService.searchById(addressId)));
	}
}
