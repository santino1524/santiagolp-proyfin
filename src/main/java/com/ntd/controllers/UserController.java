package com.ntd.controllers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
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

import jakarta.mail.MessagingException;
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

	/** String login-page */
	private static final String LOGIN_PAGE = "login-page";

	/** String message */
	private static final String MESSAGE_STRING = "message";

	/** Dependencia del servicio de gestion de usuarios */
	private final UserMgmtServiceI userMgmtService;

	/** Dependencia de SessionRegistry */
	private final SessionRegistry sessionRegistry;

	/** Dependencia AuthenticationManager */
	private final AuthenticationManager authenticationManager;

	/** Dependencia EncryptionUtils */
	private final EncryptionUtils encryptionUtils;

	/** Depenencia EmailService */
	private final EmailMgmtServiceI emailService;

	/** Depenencia TokenService */
	private final TokenMgmtServiceI tokenService;

	/** Dominio de la app */
	@Value("${app.domain}")
	private String domain;

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
	 * @throws MessagingException
	 */
	@PostMapping
	public String saveUser(@Valid final UserDTO userDto, final Model model)
			throws InternalException, MessagingException {
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

				StringBuilder builder = new StringBuilder();
				builder.append(
						"Haga clic en el siguiente enlace para confirmar su cuenta de usuario en la Tienda Luz Fuego Destrucción: ");
				builder.append(domain);
				builder.append("/users/confirm?token=");
				builder.append(token);

				// Enviar correo de confirmacion
				emailService.sendEmail(userDto.email(), "Confirmación de cuenta de usuario", builder.toString());

				result = Constants.MSG_REGISTER_USER;
			} else {
				result = Constants.MSG_UNEXPECTED_ERROR;
			}
		}

		// Retornar respuesta
		model.addAttribute(MESSAGE_STRING, result);

		return result.equals(Constants.MSG_REGISTER_USER) ? LOGIN_PAGE : "register";
	}

	/**
	 * Confirmar email
	 * 
	 * @param token
	 * @param model
	 * @return String
	 * @throws InternalException
	 */
	@GetMapping(path = "/confirm")
	public String confirmEmail(@RequestParam @NotNull final String token, final Model model) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Habilitar nuevo usuario");

		ConfirmationToken confirmationToken = tokenService.findByToken(token);

		if (confirmationToken == null) {
			model.addAttribute(MESSAGE_STRING, "Confirmación fallada");
			return "error";
		}

		com.ntd.persistence.User user = confirmationToken.getUser();
		user.setEnabled(true);

		userMgmtService.enableUser(user);
		model.addAttribute(MESSAGE_STRING, "La confirmación de su cuenta de usuario se ha completado");

		// Eliminar token de confirmacion
		tokenService.deleteConfirmationToken(confirmationToken);

		return LOGIN_PAGE;
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
		model.addAttribute(MESSAGE_STRING, result);

		return user == null ? "recover-password" : LOGIN_PAGE;
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
	 * Actualizar usuario
	 * 
	 * @param userDto
	 * @return ResponseEntity
	 * @throws InternalException
	 * @throws MessagingException
	 */
	@PostMapping(path = "/update")
	public ResponseEntity<Object> updateUser(@RequestBody @Valid final UserDTO userDto)
			throws InternalException, MessagingException {
		if (log.isInfoEnabled())
			log.info("Actualizar usuario");

		ResponseEntity<Object> result = null;

		// Comprobar si existe otro usuario
		if (userMgmtService.searchByDniOrEmailOrPhoneNumber(userDto.dni(), userDto.email(), userDto.phoneNumber())
				.size() > 1) {
			// Devolver una respuesta con codigo de estado 422
			result = ResponseEntity.unprocessableEntity().build();
		} else {
			boolean emailChange = false;

			UserDTO oldUser = userMgmtService.searchById(userDto.userId());

			// Usuario actualizado
			UserDTO userUpdated = null;

			if (oldUser != null && !oldUser.email().equals(userDto.email())) {
				emailChange = true;
				userUpdated = userMgmtService.updateUser(new UserDTO(userDto.userId(), userDto.name(),
						userDto.surname(), userDto.secondSurname(), userDto.blocked(), false, userDto.dni(),
						userDto.email(), userDto.passwd(), userDto.phoneNumber(), userDto.role(), userDto.questions(),
						userDto.answers(), userDto.addressesDto(), userDto.ordersDto()));
			} else {
				userUpdated = userMgmtService.updateUser(userDto);
			}

			// Verificar retorno de actualizacion
			if (userUpdated != null) {

				if (emailChange) {
					// Guardar el token en la base de datos junto con el usuario
					String token = tokenService.save(userUpdated);

					StringBuilder builder = new StringBuilder();
					builder.append(
							"Haga clic en el siguiente enlace para confirmar su cuenta de usuario en la Tienda Luz Fuego Destrucción: ");
					builder.append(domain);
					builder.append("/users/confirm?token=");
					builder.append(token);

					// Enviar correo de confirmacion
					emailService.sendEmail(userUpdated.email(), "Confirmación de cuenta de usuario",
							builder.toString());
				}

				result = ResponseEntity.ok().body(Collections.singletonMap("user", userUpdated));
			} else {
				// Devolver una respuesta con codigo de estado 500
				result = ResponseEntity.internalServerError().build();
			}
		}

		return result;
	}

	/**
	 * Devolucion de usuarios buscados
	 * 
	 * @param criterio
	 * @param value
	 * @throws InternalException
	 */
	@PostMapping("/searchByCriterio")
	public ResponseEntity<Object> searchUserByCriterio(@RequestParam @NotNull final String criterio,
			@RequestParam @NotNull final String value) throws InternalException {

		return ResponseEntity.ok()
				.body(Collections.singletonMap("user", userMgmtService.searchUserByCriterio(criterio, value)));
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
}
