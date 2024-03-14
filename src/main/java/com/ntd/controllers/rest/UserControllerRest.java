package com.ntd.controllers.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@RestController
@RequestMapping("/rest/users")
public class UserControllerRest {

	/** Dependencia del servicio de gestion de usuarios */
	private final UserMgmtServiceI userMgmtService;

	/**
	 * Constructor
	 * 
	 * @param userMgmtService
	 */
	public UserControllerRest(final UserMgmtServiceI userMgmtService) {
		this.userMgmtService = userMgmtService;
	}

	/**
	 * Crear nuevo usuario
	 * 
	 * @param userDto
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@PostMapping
	public ResponseEntity<String> saveUser(@RequestBody @Valid final UserDTO userDto) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Guardar nuevo ususrio");

		ResponseEntity<String> result = null;

		// Comprobar si el usuario existe
		if (userMgmtService.existsDni(userDto.dni())) {
			// Devolver una respuesta con codigo de estado 422
			result = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Constants.MSG_DNI_EXISTS);
		} else if (userMgmtService.existsEmail(userDto.email())) {
			// Devolver una respuesta con codigo de estado 422
			result = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Constants.MSG_EMAIL_EXISTS);
		} else if (userMgmtService.existsPhoneNumber(userDto.phoneNumber())) {
			// Devolver una respuesta con codigo de estado 422
			result = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Constants.MSG_PHONE_EXISTS);
		} else {
			// Guardar usuario
			if (userMgmtService.insertUser(userDto) != null) {
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
	 * Actualizar usuario
	 * 
	 * @param userDto
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@PutMapping
	public ResponseEntity<String> updateUser(@RequestBody @Valid final UserDTO userDto) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Actualizar usuario");

		ResponseEntity<String> result = null;

		// Comprobar si existe otro usuario
		if (userMgmtService.searchByDniOrEmailOrPhoneNumber(userDto.dni(), userDto.email(), userDto.phoneNumber())
				.size() > 1) {
			// Devolver una respuesta con codigo de estado 422
			result = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Constants.MSG_USER_DATA_EXISTS);
		} else {
			// Actualizar usuario
			final UserDTO userUpdated = userMgmtService.updateUser(userDto);

			// Verificar retorno de actualizacion
			if (userUpdated != null) {
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
	 * Eliminar usuario
	 * 
	 * @param userDto
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@Transactional
	@DeleteMapping
	public ResponseEntity<String> deleteUser(@RequestBody @NotNull final UserDTO userDto) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Eliminar usuario");

		// Validar id
		ValidateParams.isNullObject(userDto.userId());

		// Eliminar usuario
		userMgmtService.deleteUser(userDto.userId());

		// Devolver una respuesta con codigo de estado 202
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(Constants.MSG_SUCCESSFUL_OPERATION);
	}

	/**
	 * Buscar todos los usuarios
	 * 
	 * @return List
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchAll")
	public List<UserDTO> showUsers() throws InternalException {
		if (log.isInfoEnabled())
			log.info("Mostrar todos los usuarios");

		// Retornar lista de usuarios
		return userMgmtService.searchAll();
	}

	/**
	 * Buscar usuario por DNI
	 * 
	 * @param dni
	 * @return UserDTO
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByDni")
	public UserDTO searchByDNI(@RequestParam @NotNull final String dni) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar usuario por dni");

		// Retornar usuario
		return userMgmtService.searchByDni(dni);
	}

	/**
	 * Buscar usuario por email
	 * 
	 * @param email
	 * @return UserDTO
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByEmail")
	public UserDTO searchByEmail(@RequestParam @NotNull final String email) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar usuario por email");

		// Retornar usuario
		return userMgmtService.searchByEmail(email);
	}

	/**
	 * Buscar usuario por numero de telefono
	 * 
	 * @param phoneNumber
	 * @return UserDTO
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByPhone")
	public UserDTO searchByPhoneNumber(@RequestParam @NotNull final String phoneNumber) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar usuario por numero de telefono");

		// Retornar usuario
		return userMgmtService.searchByPhoneNumber(phoneNumber);
	}

	/**
	 * Buscar usuario por nombre y apellidos
	 * 
	 * @param name
	 * @param surname
	 * @param secondSurname
	 * @return List
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByName")
	public List<UserDTO> searchByName(@RequestParam @NotNull final String name, @NotNull final String surname,
			@NotNull final String secondSurname) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar usuario por nombre y apellidos");

		// Retornar lista de usuarios
		return userMgmtService.searchByNameOrSurnameOrSecondSurname(name, surname, secondSurname);
	}
}
