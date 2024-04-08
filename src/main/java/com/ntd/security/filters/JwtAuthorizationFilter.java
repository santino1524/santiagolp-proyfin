package com.ntd.security.filters;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ntd.security.jwt.JwtUtils;
import com.ntd.security.service.UserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	/** Dependencia JwtUtils */
	private JwtUtils jwtUtils;

	private UserDetailsServiceImpl userDetailsService;

	/**
	 * Constructor
	 * 
	 * @param jwtUtils
	 */
	public JwtAuthorizationFilter(JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsService) {
		this.jwtUtils = jwtUtils;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {

		// Obtiene el token del encabezado "Authorization"
		String tokenHeader = request.getHeader("Authorization");

		// Verifica si el token está presente y comienza con "Bearer"
		if (tokenHeader != null && tokenHeader.startsWith("Bearer")) {
			// Extrae el token de autorización excluyendo el prefijo "Bearer"
			String token = tokenHeader.substring(7);

			// Verifica si el token es valido
			if (jwtUtils.isTokenValid(token)) {
				// Obtiene el nombre de usuario del token
				String username = jwtUtils.getUsernameFromToken(token);
				// Carga los detalles del usuario utilizando el servicio de detalles de usuario
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);

				// Crea una autenticación de token de nombre de usuario y contrasenna
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						username, null, userDetails.getAuthorities());

				// Establece la autenticacion en el contexto de seguridad
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		}

		filterChain.doFilter(request, response);

	}

}
