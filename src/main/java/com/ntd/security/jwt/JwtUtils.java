package com.ntd.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase de utileria JSON Web Token
 */
@SuppressWarnings("deprecation")
@Slf4j
@Component
public class JwtUtils {

	/** Clave secreta */
	@Value("${jwt.secret.key}")
	private String secretKey;

	/** Tiempo de validez del token */
	@Value("${jwt.time.expiration}")
	private String timeExpiration;

	/**
	 * Genera un token de acceso utilizando el nombre de usuario proporcionado.
	 * 
	 * @param username El nombre de usuario para el cual se genera el token.
	 * @return El token de acceso generado como una cadena de caracteres.
	 */
	public String generateAccesToken(String username) {
		if (log.isInfoEnabled())
			log.info("Generando token de acceso");

		// Construye y retorna un token JWT utilizando el nombre de usuario
		// proporcionado
		return Jwts.builder().setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(timeExpiration)))
				.signWith(getSignatureKey(), SignatureAlgorithm.HS256).compact();
	}

	/**
	 * Valida si un token de acceso es valido.
	 * 
	 * @param token El token de acceso a validar.
	 * @return true si el token es valido, false de lo contrario.
	 */
	public boolean isTokenValid(String token) {
		if (log.isInfoEnabled())
			log.info("Validando token de acceso");

		try {
			// Intenta validar el token parseando sus claims y verificando su firma
			Jwts.parser().setSigningKey(getSignatureKey()).build().parseClaimsJws(token).getBody();
			return true;
		} catch (Exception e) {
			// Si ocurre un error al validar el token, se registra un error y se retorna
			// false
			if (log.isErrorEnabled()) {
				log.error(e.getMessage());
			}
			return false;
		}
	}

	/**
	 * Obtiene la clave de firma utilizada para generar y validar tokens.
	 * 
	 * @return La clave de firma.
	 */
	public Key getSignatureKey() {
		if (log.isInfoEnabled())
			log.info("Obteniendo clave de firma del token");

		// Decodifica la clave secreta en bytes y crea una instancia de Key
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	/**
	 * Extrae todos los claims (reclamaciones) del token.
	 * 
	 * @param token El token del cual extraer los claims.
	 * @return Un objeto Claims que contiene todos los claims del token.
	 */
	public Claims extractAllClaims(String token) {
		if (log.isInfoEnabled())
			log.info("Extrayendo todos los claims del token");

		// Parsea y retorna todos los claims del token
		return Jwts.parser().setSigningKey(getSignatureKey()).build().parseClaimsJws(token).getBody();
	}

	/**
	 * Obtiene un claim específico del token utilizando una funcion de
	 * transformacion.
	 * 
	 * @param token           El token del cual obtener el claim.
	 * @param claimsTFunction La funcion que transforma los claims en un tipo
	 *                        especifico de objeto.
	 * @return El claim transformado.
	 */
	public <T> T getClaim(String token, Function<Claims, T> claimsTFunction) {
		if (log.isInfoEnabled())
			log.info("Obteniendo un solo claim del token");

		// Extrae todos los claims del token y aplica la funcion de transformacion para
		// obtener el claim especifico
		Claims claims = extractAllClaims(token);
		return claimsTFunction.apply(claims);
	}

	/**
	 * Obtiene el nombre de usuario almacenado en el token.
	 * 
	 * @param token El token del cual obtener el nombre de usuario.
	 * @return El nombre de usuario contenido en el token.
	 */
	public String getUsernameFromToken(String token) {
		if (log.isInfoEnabled())
			log.info("Obteniendo nombre de usuario del token");

		// Obtiene y retorna el claim 'subject' que contiene el nombre de usuario
		return getClaim(token, Claims::getSubject);
	}

}
