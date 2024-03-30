package com.ntd.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer.SessionFixationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.ntd.utils.Constants;

/**
 * Configuracion de seguridad
 * 
 * @author SLP
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	/**
	 * Configurar seguridad de endpoints
	 * 
	 * @param httpSecurity
	 * @return SecurityFilterChain
	 * @throws Exception
	 */
	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

		httpSecurity
				.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
						.requestMatchers(Constants.getEndpoints()).authenticated().anyRequest().permitAll())
				.csrf(AbstractHttpConfigurer::disable)
				.formLogin(formLogin -> formLogin.loginPage("/login-page").loginProcessingUrl("/authentication")
						.permitAll())
				.sessionManagement(sess -> sess.sessionFixation(SessionFixationConfigurer::migrateSession)
						.sessionCreationPolicy(SessionCreationPolicy.ALWAYS).invalidSessionUrl("/login-page")
						.maximumSessions(2).expiredUrl("/login-page").sessionRegistry(sessionRegistry()));

		return httpSecurity.build();
	}

	/**
	 * Retorna datos de la sesion
	 * 
	 * @return SessionRegistry
	 */
	@Bean
	protected SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	/**
	 * Establecer pagina despues del inicio de sesion
	 * 
	 * @return AuthenticationSuccessHandler
	 */
	public AuthenticationSuccessHandler successHandler() {
		return ((request, response, authentication) -> response.sendRedirect("/pay"));
	}

}
