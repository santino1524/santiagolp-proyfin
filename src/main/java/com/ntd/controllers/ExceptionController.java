package com.ntd.controllers;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
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
public class ExceptionController {

	/**
	 * Controlar excepciones no especificadas
	 * 
	 * @param e
	 * @param model
	 * @return String
	 */
	@ExceptionHandler()
	public String handleException(final Exception e, final Model model) {
		if (log.isErrorEnabled())
			log.error(e.getMessage());

		// Retornar mensaje del error
		StringBuilder builder = new StringBuilder();
		builder.append(Constants.MSG_UNEXPECTED_ERROR);
		builder.append(": ");
		builder.append(e.getMessage());
		model.addAttribute(Constants.VIEW_ERROR_MESSAGE, builder.toString());

		return Constants.URL_ERROR_VIEW;
	}

	/**
	 * Controlar excepciones de validacion de atributos de Beans
	 * 
	 * @param model
	 * @param br
	 * @return String
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public String handleException(final Model model, final BindingResult br) {

		for (ObjectError error : br.getAllErrors()) {
			if (error instanceof FieldError) {
				// Retornar mensaje de error
				model.addAttribute(Constants.VIEW_ERROR_MESSAGE, error.getDefaultMessage());

				if (log.isErrorEnabled())
					log.error(error.getDefaultMessage());

				break;
			}
		}

		return Constants.URL_ERROR_VIEW;
	}

	/**
	 * Controlar excepciones personalizadas
	 * 
	 * @param e
	 * @param model
	 * @return String
	 */
	@ExceptionHandler(CustomExceptions.class)
	public String handleException(final CustomExceptions e, final Model model) {

		// Retornar mensaje del error
		model.addAttribute(Constants.VIEW_ERROR_MESSAGE, e.getMessage());

		if (log.isErrorEnabled())
			log.error(e.getMessage());

		return Constants.URL_ERROR_VIEW;
	}

	/**
	 * Controlar excepciones por violacion de CONSTRAINTS
	 * 
	 * @param e
	 * @param model
	 * @return String
	 */
	@ExceptionHandler(DataIntegrityViolationException.class)
	public String handleException(final DataIntegrityViolationException e, final Model model) {

		// Retornar mensaje del error
		StringBuilder builder = new StringBuilder();
		builder.append(Constants.MSG_VIOLATION_CONSTRAINT);
		builder.append(": ");
		builder.append(e.getMessage());
		model.addAttribute(Constants.VIEW_ERROR_MESSAGE, builder.toString());

		if (log.isErrorEnabled())
			log.error(e.getMessage());

		return Constants.URL_ERROR_VIEW;
	}

	/**
	 * Controlar excepciones del Servidor
	 * 
	 * @param e
	 * @param model
	 * @return String
	 */
	@ExceptionHandler(ServletException.class)
	public String handleException(final ServletException e, final Model model) {

		// Retornar mensaje del error
		model.addAttribute(Constants.VIEW_ERROR_MESSAGE, Constants.MSG_ERR_SERVLET);

		if (log.isErrorEnabled())
			log.error(e.getMessage());

		return Constants.URL_ERROR_VIEW;
	}

}
