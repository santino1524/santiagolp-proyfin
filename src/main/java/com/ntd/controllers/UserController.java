package com.ntd.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ntd.dto.UserDTO;
import com.ntd.exceptions.InternalException;
import com.ntd.services.UserMgmtServiceI;
import com.ntd.utils.Constants;
import com.ntd.utils.ValidateParams;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador de Usuarios
 * 
 * @author SLP
 */
@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {

	/** Dependencia del servicio de gestion de usuarios */
	private final UserMgmtServiceI userMgmtService;

	/** Dependencia de SessionRegistry */
	private final SessionRegistry sessionRegistry;

	/**
	 * Constructor
	 * 
	 * @param userMgmtService
	 */
	public UserController(final UserMgmtServiceI userMgmtService, final SessionRegistry sessionRegistry) {
		this.userMgmtService = userMgmtService;
		this.sessionRegistry = sessionRegistry;
	}

	/**
	 * Retorna los datos de la session
	 * 
	 * @return ResponseEntity
	 */
	@GetMapping("/session")
	public ResponseEntity<Map<String, Object>> getDetailSessions() {
		String sessionId = "";
		User userObject = null;

		List<Object> sessions = sessionRegistry.getAllPrincipals();

		for (Object session : sessions) {
			if (session instanceof User user) {
				userObject = user;
			}

			List<SessionInformation> sessionInformations = sessionRegistry.getAllSessions(session, false);

			for (SessionInformation sessionInformation : sessionInformations) {
				sessionId = sessionInformation.getSessionId();
			}
		}

		Map<String, Object> response = new HashMap<>();
		response.put("sessionId", sessionId);
		response.put("sessionUser", userObject);

		return ResponseEntity.ok(response);
	}

	/**
	 * Crear nuevo usuario
	 * 
	 * @param userDto
	 * @param model
	 * @return String
	 * @throws InternalException
	 */
	@PostMapping
	public String saveUser(@RequestBody @Valid final UserDTO userDto, final Model model) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Guardar nuevo ususrio");

		String result = null;

		// Comprobar si el usuario existe
		if (userMgmtService.existsDni(userDto.dni())) {
			result = Constants.MSG_DNI_EXISTS;
		} else if (userMgmtService.existsEmail(userDto.email())) {
			result = Constants.MSG_EMAIL_EXISTS;
		} else if (userMgmtService.existsPhoneNumber(userDto.phoneNumber())) {
			result = Constants.MSG_PHONE_EXISTS;
		} else {
			// Guardar usuario
			if (userMgmtService.insertUser(userDto) != null) {
				result = Constants.MSG_SUCCESSFUL_OPERATION;
			} else {
				result = Constants.MSG_UNEXPECTED_ERROR;
			}
		}

		// Retornar respuesta
		model.addAttribute(Constants.MESSAGE_GROWL, result);

		return "register";
	}

	/**
	 * Actualizar usuario
	 * 
	 * @param userDto
	 * @return String
	 * @param model
	 * @throws InternalException
	 */
	@PutMapping
	public String updateUser(@RequestBody @Valid final UserDTO userDto, final Model model) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Actualizar usuario");

		String result = null;

		// Comprobar si existe otro usuario
		if (userMgmtService.searchByDniOrEmailOrPhoneNumber(userDto.dni(), userDto.email(), userDto.phoneNumber())
				.size() > 1) {
			result = Constants.MSG_USER_DATA_EXISTS;
		} else {
			// Actualizar usuario
			final UserDTO userUpdated = userMgmtService.updateUser(userDto);

			// Verificar retorno de actualizacion
			if (userUpdated != null) {
				result = Constants.MSG_SUCCESSFUL_OPERATION;
			} else {
				result = Constants.MSG_UNEXPECTED_ERROR;
			}
		}

		// Retornar respuesta
		model.addAttribute(Constants.MESSAGE_GROWL, result);

		return "VISTA MOSTRAR RESPUESTA DE actualizar usuario";
	}

	/**
	 * Eliminar usuario
	 * 
	 * @param model
	 * @param userDto
	 * @return String
	 * @throws InternalException
	 */
	@Transactional
	@DeleteMapping
	public String deleteUser(@RequestBody @NotNull final UserDTO userDto, final Model model) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Eliminar usuario");

		// Validar id
		ValidateParams.isNullObject(userDto.userId());

		// Eliminar usuario
		userMgmtService.deleteUser(userDto.userId());

		// Retornar respuesta
		model.addAttribute(Constants.MESSAGE_GROWL, Constants.MSG_SUCCESSFUL_OPERATION);

		return "VISTA MOSTRAR RESPUESTA DE eliminar usuario";
	}

	/**
	 * Buscar todos los usuarios
	 * 
	 * @param model
	 * @return String
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchAll")
	public String showUsers(final Model model) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Mostrar todos los usuarios");

		// Retornar lista de usuarios
		model.addAttribute("users", userMgmtService.searchAll());

		return "VISTA MOSTRAR lista usuario";
	}

	/**
	 * Buscar usuario por DNI
	 * 
	 * @param model
	 * @param dni
	 * @return String
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByDni")
	public String searchByDNI(@RequestParam @NotNull final String dni, final Model model) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar usuario por dni");

		// Retornar usuario
		model.addAttribute("user", userMgmtService.searchByDni(dni));

		return "VISTA MOSTRAR usuario por dni";
	}

	/**
	 * Buscar usuario por email
	 * 
	 * @param model
	 * @param email
	 * @return String
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByEmail")
	public String searchByEmail(@RequestParam @NotNull final String email, final Model model) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar usuario por email");

		// Retornar usuario
		model.addAttribute("user", userMgmtService.searchByEmail(email));

		return "VISTA MOSTRAR usuario por email";
	}

	/**
	 * Buscar usuario por numero de telefono
	 * 
	 * @param phoneNumber
	 * @param model
	 * @return String
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByPhone")
	public String searchByPhoneNumber(@RequestParam @NotNull final String phoneNumber, final Model model)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar usuario por numero de telefono");

		// Retornar usuario
		model.addAttribute("user", userMgmtService.searchByPhoneNumber(phoneNumber));

		return "VISTA MOSTRAR usuario por email";
	}

	/**
	 * Buscar usuario por nombre y apellidos
	 * 
	 * @param name
	 * @param surname
	 * @param secondSurname
	 * @param model
	 * @return String
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByName")
	public String searchByName(@RequestParam @NotNull final String name, @NotNull final String surname,
			final Model model, @NotNull final String secondSurname) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar usuario por nombre y apellidos");

		// Retornar lista de usuarios
		model.addAttribute("users", userMgmtService.searchByNameOrSurnameOrSecondSurname(name, surname, secondSurname));

		return "VISTA MOSTRAR usuario por email";
	}
}
