package com.ntd.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer.SessionFixationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ntd.security.filters.JwtAuthenticationFilter;
import com.ntd.security.filters.JwtAuthorizationFilter;
import com.ntd.security.jwt.JwtUtils;
import com.ntd.security.service.UserDetailsServiceImpl;
import com.ntd.utils.Constants;

import lombok.extern.slf4j.Slf4j;

/**
 * Configuracion de seguridad
 * 
 * @author SLP
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	/** Dependencia de UserDetailsService */
	private UserDetailsServiceImpl userDetailsService;

	/** Dependencia JwtUtils */
	private JwtUtils jwtUtils;

	/** Dependencia JwtAuthorizationFilter */
	private JwtAuthorizationFilter authorizationFilter;

	/**
	 * Constructor
	 * 
	 * @param jwtUtils
	 * @param userDetailsService
	 */
	public SecurityConfig(UserDetailsServiceImpl userDetailsService, JwtUtils jwtUtils,
			JwtAuthorizationFilter authorizationFilter) {
		this.userDetailsService = userDetailsService;
		this.jwtUtils = jwtUtils;
		this.authorizationFilter = authorizationFilter;
	}

	/**
	 * Configurar seguridad de endpoints
	 * 
	 * @param httpSecurity
	 * @return SecurityFilterChain
	 * @throws Exception
	 */
	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager)
			throws Exception {

		if (log.isInfoEnabled())
			log.info("Configurar seguridad de endpoints");

		// Crear y configurar el filtro de autenticacion JWT
		JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtils);
		jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);

		// Configurar la seguridad de los endpoints HTTP
		httpSecurity.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
				// .requestMatchers(Constants.getEndpoints()).authenticated().anyRequest().permitAll())
				.requestMatchers(Constants.getEndpointsAdmin()).hasRole("SELLER")
				.requestMatchers(Constants.getEndpointsAuth()).authenticated().anyRequest().permitAll())
				.csrf(AbstractHttpConfigurer::disable)
				.formLogin(formLogin -> formLogin.loginPage(Constants.LOGIN_PAGE).loginProcessingUrl("/authentication")
						.permitAll())
				.sessionManagement(sess -> sess.sessionFixation(SessionFixationConfigurer::migrateSession)
						.sessionCreationPolicy(SessionCreationPolicy.ALWAYS).invalidSessionUrl(Constants.LOGIN_PAGE)
						.maximumSessions(2).expiredUrl(Constants.LOGIN_PAGE).sessionRegistry(sessionRegistry()))
				.addFilter(jwtAuthenticationFilter)
				.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
				.exceptionHandling(handling -> handling.accessDeniedPage("/restricted"));

		return httpSecurity.build();
	}

	/**
	 * Retorna un objeto SessionRegistry para manejar sesiones
	 * 
	 * @return SessionRegistry creado
	 */
	@Bean
	protected SessionRegistry sessionRegistry() {
		if (log.isInfoEnabled())
			log.info("Retorna un objeto SessionRegistry para manejar sesiones");

		return new SessionRegistryImpl();
	}

	/**
	 * Define un manejador para redirigir despues de una autenticacion exitosa
	 * 
	 * @return AuthenticationSuccessHandler
	 */
	public AuthenticationSuccessHandler successHandler() {
		if (log.isInfoEnabled())
			log.info("Define un manejador para redirigir despues de una autenticacion exitosa");

		return ((request, response, authentication) -> response.sendRedirect("/pay"));
	}

	/**
	 * Define el PasswordEncoder a utilizar para encriptar contrasennas
	 * 
	 * @return PasswordEncoder configurado
	 */
	@Bean
	protected PasswordEncoder passwordEncoder() {
		if (log.isInfoEnabled())
			log.info("Define el PasswordEncoder a utilizar para encriptar contrasennas");

		return new BCryptPasswordEncoder();
	}

	/**
	 * Configura y devuelve un AuthenticationManager utilizando el
	 * AuthenticationManagerBuilder
	 * 
	 * @param httpSecurity    Configuracion de seguridad HTTP
	 * @param passwordEncoder Encoder de contraseñas a utilizar
	 * @return AuthenticationManager configurado
	 * @throws Exception Si ocurre algun error durante la configuracion
	 */
	@SuppressWarnings("removal")
	@Bean
	protected AuthenticationManager authenticationManager(HttpSecurity httpSecurity, PasswordEncoder passwordEncoder)
			throws Exception {
		if (log.isInfoEnabled())
			log.info("Configura y devuelve un AuthenticationManager utilizando el AuthenticationManagerBuilder");

		// Configura el AuthenticationManager utilizando el AuthenticationManagerBuilder
		// y lo devuelve
		return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class).userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder).and().build();
	}

}
