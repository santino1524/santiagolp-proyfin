package com.ntd.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.extern.slf4j.Slf4j;

/**
 * Clase de utilidad para fechas
 * 
 * @author slopezpi
 */
@Slf4j
public class DateUtil {

	/** Constructor privado */
	private DateUtil() {
	}

	/**
	 * Formatear fecha
	 * 
	 * @param dateTime
	 * @return String
	 */
	public static String formatDate(LocalDateTime dateTime) {
		if (log.isInfoEnabled())
			log.info("Formatear fecha");

		// Formato de la fecha de salida
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

		// Formatear la fecha de salida
		return dateTime.format(outputFormatter);
	}

}
