package com.ntd.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
	 * @param model
	 * @return String
	 * @throws InternalException
	 */
	@PostMapping
	public String savePostalAddress(@RequestBody @Valid final PostalAddressDTO postalAddressDto, final Model model)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Guardar direccion");

		String result = null;

		// Guardar direccion
		if (postalAddressMgmtService.insertPostalAddress(postalAddressDto) != null) {
			result = Constants.MSG_SUCCESSFUL_OPERATION;
		} else {
			result = Constants.MSG_UNEXPECTED_ERROR;
		}

		model.addAttribute(Constants.MESSAGE_GROWL, result);

		// Retornar respuesta
		return "VISTA MOSTRAR RESULTADO DE DIRECCION GUARDADA";
	}

	/**
	 * Eliminar direccion
	 * 
	 * @param model
	 * @param postalAddressDto
	 * @return String
	 * @throws InternalException
	 */
	@Transactional
	@DeleteMapping("/delete")
	public String deletePostalAddress(@RequestBody @NotNull final PostalAddressDTO postalAddressDto,
			@NotNull final Long userId, final Model model) throws InternalException {
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

		// Devolver una respuesta
		model.addAttribute(Constants.MESSAGE_GROWL, Constants.MSG_SUCCESSFUL_OPERATION);

		return "VISTA MOSTRAR RESPUESTA DE DIRECCION eliminado";
	}

	/**
	 * Eliminar relacion de direccion con usuario
	 * 
	 * @param model
	 * @param postalAddressDto
	 * @return String
	 * @throws InternalException
	 */
	@Transactional
	@DeleteMapping("/deleteRelation")
	public String deleteRelationPostalAddress(@RequestBody @NotNull final PostalAddressDTO postalAddressDto,
			@NotNull final Long userId, final Model model) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Eliminar direccion");

		// Validar campos del id
		ValidateParams.isNullObject(postalAddressDto.city());
		ValidateParams.isNullObject(postalAddressDto.directionLine());
		ValidateParams.isNullObject(postalAddressDto.province());

		// Eliminar direccion
		postalAddressMgmtService.deleteRelationPostalAddress(userId, postalAddressDto.city(),
				postalAddressDto.directionLine(), postalAddressDto.province());

		// Devolver una respuesta
		model.addAttribute(Constants.MESSAGE_GROWL, Constants.MSG_SUCCESSFUL_OPERATION);

		return "VISTA MOSTRAR RESPUESTA DE RELACION DE DIRECCION eliminado";
	}

	/**
	 * Buscar todos las direcciones
	 * 
	 * @param model
	 * @return String
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchAll")
	public String showPostalAddress(final Model model) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Mostrar todos las direcciones");

		// Retornar lista de direcciones
		model.addAttribute("addresses", postalAddressMgmtService.searchAll());

		return "VISTA MOSTRAR todas las direcciones";
	}

	/**
	 * Buscar por direccion por id
	 * 
	 * @param model
	 * @param postalAddressDto
	 * @return PostalAddressDTO
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchById")
	public String searchById(@RequestBody @NotNull final PostalAddressDTO postalAddressDto, final Model model)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar por id");

		// Validar campos del id
		ValidateParams.isNullObject(postalAddressDto.city());
		ValidateParams.isNullObject(postalAddressDto.directionLine());
		ValidateParams.isNullObject(postalAddressDto.province());

		// Retornar direccion
		model.addAttribute("addresses", postalAddressMgmtService.searchById(postalAddressDto));

		return "VISTA MOSTRAR la direccion";
	}
}
