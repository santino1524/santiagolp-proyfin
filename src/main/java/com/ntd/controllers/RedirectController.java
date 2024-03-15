package com.ntd.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ntd.utils.Constants;

import lombok.extern.slf4j.Slf4j;

/**
 * Controlador de Redireccionamientos
 * 
 * @author SLP
 */
@Slf4j
@Controller
@RequestMapping("/redirect")
public class RedirectController {

	/**
	 * Redireccionar a pagina productos
	 * 
	 * @return String
	 */
	@GetMapping(path = "/products")
	public String goEnrollCustomer() {
		if (log.isInfoEnabled())
			log.info("Redireccionar a pagina productos");

		return "products";
	}

	/**
	 * Redireccionar a pagina Admin
	 * 
	 * @return String
	 */
	@GetMapping(path = "/admin")
	public String goAdmin() {
		if (log.isInfoEnabled())
			log.info("Redireccionar a pagina Administracion");

		return "administration";
	}

	/**
	 * Error al intentar mostrarla pagina de administracion en un movil
	 * 
	 * @param model
	 * @return String
	 */
	@GetMapping(path = "/responsiveAdmin")
	public String handleException(final Model model) {
		if (log.isErrorEnabled())
			log.error(Constants.MSG_ADMIN_RESPONSIVE_EXC);

		// Retornar mensaje de error
		model.addAttribute(Constants.VIEW_ERROR_MESSAGE, Constants.MSG_ADMIN_RESPONSIVE_EXC);

		return Constants.URL_ERROR_VIEW;
	}
}
