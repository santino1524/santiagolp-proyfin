package com.ntd.controllers.rest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ntd.exceptions.CustomExceptions;
import com.ntd.utils.Constants;

import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador de excepciones
 * 
 * @author SLP
 */
@ControllerAdvice
@Slf4j
public class ExceptionControllerRest {

	/**
	 * Controlar excepciones no especificadas
	 * 
	 * @param e
	 * @return ResponseEntity
	 */
	@ExceptionHandler()
	public ResponseEntity<String> handleException(final Exception e) {
		if (log.isErrorEnabled())
			log.error(e.getMessage());

		// Devolver una respuesta con codigo de estado 500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	}

	/**
	 * Controlar excepciones de validacion de atributos de Beans
	 * 
	 * @param br
	 * @return ResponseEntity
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleException(final BindingResult br) {

		ResponseEntity<String> result = null;

		for (ObjectError error : br.getAllErrors()) {
			if (error instanceof FieldError) {
				// Devolver una respuesta con codigo de estado 400
				result = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.getDefaultMessage());

				break;
			}
		}

		// Retornar mensaje del error
		return result;
	}

	/**
	 * Controlar excepciones personalizadas
	 * 
	 * @param e
	 * @return ResponseEntity
	 */
	@ExceptionHandler(CustomExceptions.class)
	public ResponseEntity<String> handleException(final CustomExceptions e) {
		if (log.isErrorEnabled())
			log.error(e.getMessage());

		// Devolver una respuesta con codigo de estado 500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

	}

	/**
	 * Controlar excepciones por violacion de CONSTRAINTS
	 * 
	 * @param e
	 * @return ResponseEntity
	 */
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<String> handleException(final DataIntegrityViolationException e) {
		if (log.isErrorEnabled())
			log.error(e.getMessage());

		// Devolver una respuesta con codigo de estado 422
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Constants.MSG_VIOLATION_CONSTRAINT);

	}

	/**
	 * Controlar excepciones del Servidor
	 * 
	 * @param e
	 * @return ResponseEntity
	 */
	@ExceptionHandler(ServletException.class)
	public ResponseEntity<String> handleException(final ServletException e) {
		if (log.isErrorEnabled())
			log.error(e.getMessage());

		// Devolver una respuesta con codigo de estado 500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.MSG_ERR_SERVLET);

	}

}
