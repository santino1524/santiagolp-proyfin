
package com.ntd.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ntd.dto.UserDTO;
import com.ntd.dto.mapper.DTOMapperI;
import com.ntd.exceptions.InternalException;
import com.ntd.persistence.ConfirmationToken;
import com.ntd.persistence.ConfirmationTokenRepositoryI;
import com.ntd.utils.ValidateParams;

import lombok.extern.slf4j.Slf4j;

/**
 * Servicio de Tokens para confirmacion de cuenta
 * 
 * @author slopezpi
 */
@Service
@Slf4j
public class TokenMgmtServiceImp implements TokenMgmtServiceI {

	/** Dependencia ConfirmationTokenRepositoryI */
	private ConfirmationTokenRepositoryI tokenRepository;

	/**
	 * Constructor
	 * 
	 * @param tokenRepository
	 */
	public TokenMgmtServiceImp(ConfirmationTokenRepositoryI tokenRepository) {
		this.tokenRepository = tokenRepository;
	}

	@Override
	public String generateToken() {
		if (log.isInfoEnabled())
			log.info("Generar token");

		return UUID.randomUUID().toString();
	}

	@Override
	public String save(UserDTO userDto) {
		if (log.isInfoEnabled())
			log.info("Guardar token y usuario");

		// Generar token de confirmacion
		String token = generateToken();

		// Guardar
		tokenRepository.save(new ConfirmationToken(DTOMapperI.MAPPER.mapDTOToUser(userDto), token));

		return token;
	}

	@Override
	public ConfirmationToken findByToken(String token) {
		if (log.isInfoEnabled())
			log.info("Buscar pr token");

		return tokenRepository.findByToken(token);
	}

	@Override
	public void deleteConfirmationToken(ConfirmationToken confirmationToken) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Eliminar ConfirmationToken");

		// Validar parametro
		ValidateParams.isNullObject(confirmationToken);

		// Eliminar por Id
		tokenRepository.deleteById(confirmationToken.getConfirmId());
	}

}
