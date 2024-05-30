package com.ntd.controllers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ntd.dto.AnswersDTO;
import com.ntd.dto.UserDTO;
import com.ntd.exceptions.InternalException;
import com.ntd.persistence.ConfirmationToken;
import com.ntd.security.EncryptionUtils;
import com.ntd.services.EmailMgmtServiceI;
import com.ntd.services.TokenMgmtServiceI;
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

	/** Dependencia AuthenticationManager */
	private AuthenticationManager authenticationManager;

	/** Dependencia EncryptionUtils */
	private final EncryptionUtils encryptionUtils;

	/** Depenencia EmailService */
	private EmailMgmtServiceI emailService;

	/** Depenencia TokenService */
	private TokenMgmtServiceI tokenService;

	/**
	 * Constructor
	 * 
	 * @param userMgmtService
	 * @param sessionRegistry
	 * @param authenticationManager
	 * @param encryptionUtils
	 * @param emailService
	 * @param tokenService
	 */
	public UserController(UserMgmtServiceI userMgmtService, SessionRegistry sessionRegistry,
			AuthenticationManager authenticationManager, EncryptionUtils encryptionUtils,
			EmailMgmtServiceI emailService, TokenMgmtServiceI tokenService) {
		this.userMgmtService = userMgmtService;
		this.sessionRegistry = sessionRegistry;
		this.authenticationManager = authenticationManager;
		this.encryptionUtils = encryptionUtils;
		this.emailService = emailService;
		this.tokenService = tokenService;
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
	 * Comprobar contrasenna vieja
	 * 
	 * @param userDto
	 * @return ResponseEntity
	 */
	@PostMapping("/verifyOldPasswd")
	public ResponseEntity<Void> verifyOldPasswd(@RequestBody @Valid final UserDTO userDto) {

		ResponseEntity<Void> result = null;
		// Obtener el nombre de usuario del usuario actualmente autenticado
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();

		// Autenticar al usuario para verificar la contraseña anterior
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, userDto.passwd());

		try {
			authenticationManager.authenticate(token);

			// retornar codigo 200
			result = ResponseEntity.ok().build();
		} catch (Exception e) {
			// retornar 422
			result = ResponseEntity.unprocessableEntity().build();
		}

		return result;
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
	public String saveUser(@Valid final UserDTO userDto, final Model model) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Guardar nuevo usuario");

		String result = null;

		// Comprobar si el usuario existe
		if (userMgmtService.existsDni(userDto.dni())) {
			result = Constants.MSG_DNI_EXISTS;
		} else if (userMgmtService.existsEmail(userDto.email())) {
			result = Constants.MSG_EMAIL_EXISTS;
		} else if (userMgmtService.existsPhoneNumber(userDto.phoneNumber())) {
			result = Constants.MSG_PHONE_EXISTS;
		} else {
			UserDTO savedUser = userMgmtService.insertUser(userDto);
			if (savedUser != null) {
				// Guardar el token en la base de datos junto con el usuario
				String token = tokenService.save(savedUser);

				// Enviar correo de confirmacion
				String confirmationUrl = "http://localhost:8080/users/confirm?token=" + token;
				emailService.sendEmail(userDto.email(), "Confirma su Email",
						"Haga clic en el siguiente enlace para confirmar su correo electrónico: " + confirmationUrl);

				result = Constants.MSG_REGISTER_USER;
			} else {
				result = Constants.MSG_UNEXPECTED_ERROR;
			}
		}

		// Retornar respuesta
		model.addAttribute("message", result);

		return result.equals(Constants.MSG_REGISTER_USER) ? "login-page" : "register";
	}

	/**
	 * Confirmar email
	 * 
	 * @param token
	 * @param model
	 * @return String
	 */
	@GetMapping(path = "/confirm")
	public String confirmEmail(@RequestParam @NotNull final String token, final Model model) {
		if (log.isInfoEnabled())
			log.info("Habilitar nuevo usuario");

		ConfirmationToken confirmationToken = tokenService.findByToken(token);

		if (confirmationToken == null) {
			model.addAttribute("message", "Confirmación fallada");
			return "error";
		}

		com.ntd.persistence.User user = confirmationToken.getUser();
		user.setEnabled(true);

		userMgmtService.enableUser(user);
		model.addAttribute("message", "La confirmación de su cuenta de usuario se ha completado");

		return "login-page";
	}

	/**
	 * Resetear contrasenna
	 * 
	 * @param answersDto
	 * @param model
	 * @return String
	 * @throws InternalException
	 */
	@PostMapping("resetPasswd")
	public String resetPasswd(@Valid final AnswersDTO answersDto, final Model model) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Resetear contrasenna");

		String result = null;
		com.ntd.persistence.User user = userMgmtService.resetPasswd(answersDto);

		if (user == null) {
			result = "Las respuestas proporcionadas son incorrectas";

			// Retornar respuestas y usuario
			UserDTO userDto = userMgmtService.searchById(answersDto.userId());
			model.addAttribute("userId", userDto.userId());
			model.addAttribute("question1", encryptionUtils.decrypt(userDto.questions().get(0)));
			model.addAttribute("question2", encryptionUtils.decrypt(userDto.questions().get(1)));
			model.addAttribute("question3", encryptionUtils.decrypt(userDto.questions().get(2)));
		} else {
			result = "La contraseña se ha restablecido correctamente";
		}

		// Retornar respuesta
		model.addAttribute("message", result);

		return user == null ? "recover-password" : "login-page";
	}

	/**
	 * Actualizar usuario
	 * 
	 * @param userDto
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@PostMapping(path = "/update")
	public ResponseEntity<Object> updateUser(@RequestBody @Valid final UserDTO userDto) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Actualizar usuario");

		ResponseEntity<Object> result = null;

		// Comprobar si existe otro usuario
		if (userMgmtService.searchByDniOrEmailOrPhoneNumber(userDto.dni(), userDto.email(), userDto.phoneNumber())
				.size() > 1) {
			// Devolver una respuesta con codigo de estado 422
			result = ResponseEntity.unprocessableEntity().build();
		} else {
			// Actualizar usuario
			final UserDTO userUpdated = userMgmtService.updateUser(userDto);

			// Verificar retorno de actualizacion
			if (userUpdated != null) {
				result = ResponseEntity.ok().body(Collections.singletonMap("user", userUpdated));
			} else {
				// Devolver una respuesta con codigo de estado 500
				result = ResponseEntity.internalServerError().build();
			}
		}

		return result;
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
	 * Retornar preguntas de usuario en pagina de recuperacion de contrasenna
	 * 
	 * @param model
	 * @return String
	 * @throws InternalException
	 */
	@GetMapping(path = "/recoverPassword")
	public String showUsers(@RequestParam @NotNull final Long userId, final Model model) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Retornar preguntas de usuario en pagina de recuperacion de contrasenna");

		UserDTO userDto = userMgmtService.searchById(userId);

		if (userDto != null && userDto.userId() != null && userDto.userId() != 1) {
			// Retornar respuestas y usuario
			model.addAttribute("userId", userDto.userId());
			model.addAttribute("question1", encryptionUtils.decrypt(userDto.questions().get(0)));
			model.addAttribute("question2", encryptionUtils.decrypt(userDto.questions().get(1)));
			model.addAttribute("question3", encryptionUtils.decrypt(userDto.questions().get(2)));
		}

		return "recover-password";
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
	 * Metodo para devolucion de los empleados buscados.
	 * 
	 * @param criterio
	 * @param value
	 * @throws InternalException
	 */
	@PostMapping("/searchByCriterio")
	public ResponseEntity<Object> searchEmployees(@RequestParam @NotNull final String criterio,
			@RequestParam @NotNull final String value) throws InternalException {

		return ResponseEntity.ok()
				.body(Collections.singletonMap("user", userMgmtService.searchUserByCriterio(criterio, value)));
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
	 * @param email
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByEmail")
	public ResponseEntity<Object> searchByEmail(@RequestParam @NotNull final String email) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar usuario por email");

		return ResponseEntity.ok().body(Collections.singletonMap("user", userMgmtService.searchByEmail(email)));
	}

	/**
	 * Buscar usuario por id
	 * 
	 * @param userId
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchById")
	public ResponseEntity<Object> searchById(@RequestParam @NotNull final Long userId) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar usuario por id");

		return ResponseEntity.ok().body(Collections.singletonMap("user", userMgmtService.searchById(userId)));
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
