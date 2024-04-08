package com.ntd.security.service;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ntd.persistence.User;
import com.ntd.persistence.UserRepositoryI;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

	/** Dependencia UserRepository */
	private UserRepositoryI userRepository;

	/**
	 * Constructor
	 * 
	 * @param userRepository
	 */
	public UserDetailsServiceImpl(UserRepositoryI userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = null;

		// Busca al usuario en la base de datos utilizando su email
		user = userRepository.findByEmailIgnoreCase(email);

		// Verifica si el usuario no fue encontrado
		if (user == null) {
			// Si el usuario no existe, se lanza una excepcion UsernameNotFoundException
			StringBuilder builder = new StringBuilder();
			builder.append("El usuario ");
			builder.append(email);
			builder.append(" no existe");

			// Registro de advertencia en caso de usuario no encontrado
			if (log.isWarnEnabled())
				log.warn(builder.toString());

			// Lanza la excepcion UsernameNotFoundException con un mensaje descriptivo
			throw new UsernameNotFoundException(builder.toString());
		}

		// Crear la autoridad con el rol del usuario
		GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());
		// Crear la coleccion de autoridades con la unica autoridad
		Collection<GrantedAuthority> authorities = Collections.singleton(authority);

		// Construye y retorna un objeto UserDetails utilizando los detalles del usuario
		// encontrado
		// Los detalles incluyen el email, la contrasenna, y los roles (authorities) del
		// usuario
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPasswd(), true, true,
				true, true, authorities);
	}

}
