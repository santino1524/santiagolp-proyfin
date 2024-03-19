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

	/* Constante String administration */
	private static final String ADMINISTRATION = "administration";

	/**
	 * Mostrar AdminOrders
	 * 
	 * @return String
	 */
	@GetMapping(path = "/adminOrders")
	public String showAdminOrders(final Model model) {
		if (log.isInfoEnabled())
			log.info("Mostrar AdminOrders");

		model.addAttribute("showOrders", true);

		return ADMINISTRATION;
	}

	/**
	 * Mostrar AdminProducts
	 * 
	 * @return String
	 */
	@GetMapping(path = "/adminProducts")
	public String showProducts(final Model model) {
		if (log.isInfoEnabled())
			log.info("Mostrar AdminProducts");

		model.addAttribute("showProducts", true);

		return ADMINISTRATION;
	}

	/**
	 * Mostrar AdminComplaints
	 * 
	 * @return String
	 */
	@GetMapping(path = "/adminComplaints")
	public String showComplaints(final Model model) {
		if (log.isInfoEnabled())
			log.info("Mostrar AdminComplaints");

		model.addAttribute("showComplaints", true);

		return ADMINISTRATION;
	}

	/**
	 * Mostrar AdminSendings
	 * 
	 * @return String
	 */
	@GetMapping(path = "/adminSendings")
	public String showSendings(final Model model) {
		if (log.isInfoEnabled())
			log.info("Mostrar AdminSendings");

		model.addAttribute("showSendings", true);

		return ADMINISTRATION;
	}

	/**
	 * Mostrar AdminUsers
	 * 
	 * @return String
	 */
	@GetMapping(path = "/adminUsers")
	public String showUsers(final Model model) {
		if (log.isInfoEnabled())
			log.info("Mostrar AdminUsers");

		model.addAttribute("showUsers", true);

		return ADMINISTRATION;
	}

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
	 * Redireccionar a pagina Acerca de
	 * 
	 * @return String
	 */
	@GetMapping(path = "/about")
	public String goAbout() {
		if (log.isInfoEnabled())
			log.info("Redireccionar a pagina Acerca de");

		return "about";
	}

	/**
	 * Redireccionar a pagina Registro
	 * 
	 * @return String
	 */
	@GetMapping(path = "/register")
	public String goRegister() {
		if (log.isInfoEnabled())
			log.info("Redireccionar a pagina Registro");

		return "register";
	}

	/**
	 * Redireccionar a pagina Reiniciar contrasena
	 * 
	 * @return String
	 */
	@GetMapping(path = "/resetPassword")
	public String goResetPassword() {
		if (log.isInfoEnabled())
			log.info("Redireccionar a pagina Reiniciar contrasena");

		return "reset-password";
	}

	/**
	 * Redireccionar a pagina Carrito de compras
	 * 
	 * @return String
	 */
	@GetMapping(path = "/shoppingCart")
	public String goShoppingCart() {
		if (log.isInfoEnabled())
			log.info("Redireccionar a pagina Carrito de compras");

		return "shopping-cart";
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

		return ADMINISTRATION;
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