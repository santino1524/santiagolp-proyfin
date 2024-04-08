package com.ntd.security;

import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Clase utilitaria para encriptar y desencriptar datos
 */
@Slf4j
@Component
public class EncryptionUtils {

	/** Clave secreta */
	@Value("${encryption.secret.key}")
	private String secretKey;

	/** Algoritmo de encriptacion */
	@Value("${encryption.algorithm}")
	private String algorithm;

	/** Transformacion de cifrado */
	@Value("${encryption.transformation}")
	private String transformation;

	/** Caracteres para codificar */
	@Value("${encryption.charset}")
	private String charset;

	/**
	 * Metodo para encriptar informacion
	 * 
	 * @param data
	 * @return String
	 */
	public String encrypt(String data) {
		try {
			// Crea una clave secreta utilizando la clave proporcionada y el algoritmo
			// especificado
			Key key = new SecretKeySpec(secretKey.getBytes(), algorithm);
			// Crea una instancia de cifrado utilizando la transformacion especificada
			Cipher cipher = Cipher.getInstance(transformation);
			// Inicializa el cifrado en modo de encriptacion con la clave
			cipher.init(Cipher.ENCRYPT_MODE, key);
			// Encripta los datos y los convierte en una cadena Base64
			byte[] encryptedBytes = cipher.doFinal(data.getBytes(charset));

			return Base64.getEncoder().encodeToString(encryptedBytes);
		} catch (Exception e) {
			if (log.isErrorEnabled())
				log.error("Error al encriptar los datos: {}", e.getMessage());

			return null;
		}
	}

	/**
	 * Metodo para desencriptar informacion
	 * 
	 * @param encryptedData
	 * @return String
	 */
	public String decrypt(String encryptedData) {
		try {
			// Crea una clave secreta utilizando la clave proporcionada y el algoritmo
			// especificado
			Key key = new SecretKeySpec(secretKey.getBytes(), algorithm);
			// Crea una instancia de cifrado utilizando la transformacion especificada
			Cipher cipher = Cipher.getInstance(transformation);
			// Inicializa el cifrado en modo de desencriptación con la clave
			cipher.init(Cipher.DECRYPT_MODE, key);
			// Desencripta los datos y los convierte en una cadena utilizando el conjunto de
			// caracteres especificado
			byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));

			return new String(decryptedBytes, charset);
		} catch (Exception e) {
			if (log.isErrorEnabled())
				log.error("Error al desencriptar los datos: {}", e.getMessage());

			return null;
		}
	}
}
