
package com.ntd.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * Servicio de Tokens para confirmacion de cuenta
 * 
 * @author slopezpi
 */
@Service
@Slf4j
public class TokenMgmtServiceImp implements TokenMgmtServiceI {

	@Override
	public String generateToken() {
		if (log.isInfoEnabled())
			log.info("Generar token");

		return UUID.randomUUID().toString();
	}

}
