package com.ntd.security.filters;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntd.persistence.User;
import com.ntd.security.jwt.JwtUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase para la autenticacion
 */
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	/** Dependencia JwtUtils */
	private JwtUtils jwtUtils;

	/**
	 * Constructor
	 * 
	 * @param jwtUtils
	 */
	public JwtAuthenticationFilter(JwtUtils jwtUtils) {
		this.jwtUtils = jwtUtils;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		User userEntity = null;
		String email = "";
		String password = "";

		try {
			// Intento de leer el cuerpo de la solicitud HTTP y convertirlo a un objeto User
			userEntity = new ObjectMapper().readValue(request.getInputStream(), User.class);

			if (userEntity != null) {
				// Si se obtiene un objeto User, se extraen el email y la contrasenna
				email = userEntity.getEmail();
				password = userEntity.getPasswd();
			}

		} catch (IOException e) {
			// Captura de excepcion en caso de error al leer el cuerpo de la solicitud
			if (log.isErrorEnabled())
				log.error(e.getMessage());
		}

		// Creacion del objeto de autenticacion UsernamePasswordAuthenticationToken con
		// el email y la contrasenna obtenidos
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email,
				password);

		// Retorna el resultado de la autenticacion utilizando el AuthenticationManager
		return getAuthenticationManager().authenticate(authenticationToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		// Obtiene el usuario autenticado
		org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authResult
				.getPrincipal();

		// Genera un token de acceso utilizando el nombre de usuario del usuario
		// autenticado
		String token = jwtUtils.generateAccesToken(user.getUsername());

		// Agrega el token al encabezado de la respuesta HTTP
		response.addHeader("Authorization", token);

		// Prepara la respuesta JSON con el token y otros detalles de la autenticacion
		Map<String, Object> httpResponse = new HashMap<>();
		httpResponse.put("token", token);
		httpResponse.put("Message", "Autenticación correcta");
		httpResponse.put("Username", user.getUsername());

		// Escribe la respuesta JSON en el cuerpo de la respuesta HTTP
		response.getWriter().write(new ObjectMapper().writeValueAsString(httpResponse));

		// Establece el estado y el tipo de contenido de la respuesta HTTP
		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().flush();

		// Llama al método successfulAuthentication de la superclase para continuar la
		// cadena de filtros
		super.successfulAuthentication(request, response, chain, authResult);
	}

}
