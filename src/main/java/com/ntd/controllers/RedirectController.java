package com.ntd.controllers;

import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
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
@RequestMapping("/")
public class RedirectController extends SavedRequestAwareAuthenticationSuccessHandler {

	/** Constante String administration */
	private static final String ADMINISTRATION = "administration";

	/**
	 * Mostrar AdminOrders
	 * 
	 * @return String
	 */
	@GetMapping(path = "adminOrders")
	public String showAdminOrders(final Model model) {
		if (log.isInfoEnabled())
			log.info("Mostrar AdminOrders");

		model.addAttribute("showOrders", true);

		return ADMINISTRATION;
	}

	/**
	 * Mostrar formulario de inicio
	 * 
	 * @return String
	 */
	@GetMapping(path = "login-page")
	public String showLoginForm() {
		if (log.isInfoEnabled())
			log.info("Mostrar pagina de login");

		return "login-page";
	}

	/**
	 * Mostrar pagina de pedidos
	 * 
	 * @return String
	 */
	@GetMapping(path = "myOrders")
	public String showMyOrders() {
		if (log.isInfoEnabled())
			log.info("Mostrar pagina de pedidos");

		return "my-orders";
	}

	/**
	 * Mostrar pagina perfil de usuario
	 * 
	 * @return String
	 */
	@GetMapping(path = "userProfile")
	public String showUserProfile() {
		if (log.isInfoEnabled())
			log.info("Mostrar pagina perfil de usuario");

		return "user-profile";
	}

	/**
	 * Mostrar AdminProducts
	 * 
	 * @return String
	 */
	@GetMapping(path = "adminProducts")
	public String showProducts(final Model model) {
		if (log.isInfoEnabled())
			log.info("Mostrar AdminProducts");

		model.addAttribute("showProducts", true);

		return ADMINISTRATION;
	}

	/**
	 * Mostrar Pagina de pagar
	 * 
	 * @return String
	 */
	@GetMapping(path = "pay")
	public String showPay() {
		if (log.isInfoEnabled())
			log.info("Mostrar Pagina de pagar");

		return "pay";
	}

	/**
	 * Mostrar AdminComplaints
	 * 
	 * @return String
	 */
	@GetMapping(path = "adminComplaints")
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
	@GetMapping(path = "adminSendings")
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
	@GetMapping(path = "adminUsers")
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
	@GetMapping(path = "products")
	public String goEnrollCustomer() {
		if (log.isInfoEnabled())
			log.info("Redireccionar a pagina productos");

		return "products";
	}

	/**
	 * Redireccionar a pagina reset-password
	 * 
	 * @return String
	 */
	@GetMapping(path = "reset-password")
	public String goResetPassword() {
		if (log.isInfoEnabled())
			log.info("Redireccionar a pagina reset-password");

		return "reset-password";
	}

	/**
	 * Redireccionar a pagina Acerca de
	 * 
	 * @return String
	 */
	@GetMapping(path = "about")
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
	@GetMapping(path = "register")
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
	@GetMapping(path = "recoverPassword")
	public String goRecoverPassword() {
		if (log.isInfoEnabled())
			log.info("Redireccionar a pagina Reiniciar contrasena");

		return "recover-password";
	}

	/**
	 * Redireccionar a pagina Carrito de compras
	 * 
	 * @return String
	 */
	@GetMapping(path = "shoppingCart")
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
	@GetMapping(path = "admin")
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
	@GetMapping(path = "responsiveAdmin")
	public String restrictedResponsive(final Model model) {
		if (log.isErrorEnabled())
			log.error(Constants.MSG_ADMIN_RESPONSIVE_EXC);

		// Retornar mensaje de error
		model.addAttribute(Constants.VIEW_ERROR_MESSAGE, Constants.MSG_ADMIN_RESPONSIVE_EXC);

		return Constants.URL_ERROR_VIEW;
	}

	/**
	 * Error interno
	 * 
	 * @param model
	 * @return String
	 */
	@GetMapping(path = "internalError")
	public String redInternalError(final Model model) {
		if (log.isErrorEnabled())
			log.error(Constants.MSG_ERR_SERVLET);

		// Retornar mensaje de error
		model.addAttribute(Constants.VIEW_ERROR_MESSAGE, Constants.MSG_ERR_SERVLET);

		return Constants.URL_ERROR_VIEW;
	}

	/**
	 * Error al intentar acceder a una url sin permisos
	 * 
	 * @param model
	 * @return String
	 */
	@GetMapping(path = "restricted")
	public String restrictedPage(final Model model) {
		if (log.isErrorEnabled())
			log.error("Constants.ACCESS_DENIEG");

		// Retornar mensaje de error
		model.addAttribute(Constants.VIEW_ERROR_MESSAGE, Constants.ACCESS_DENIEG);

		return Constants.URL_ERROR_VIEW;
	}
}
