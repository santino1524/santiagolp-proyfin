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
	 * Checkear autenticacion
	 * 
	 * @return String
	 */
	@GetMapping(path = "/checkAuth")
	public String checkAuth() {
		if (log.isInfoEnabled())
			log.info(" Checkear autenticacion");

		return "shopping-cart";
	}

	/**
	 * Mostrar pagina de enviar reportes
	 * 
	 * @return String
	 */
	@GetMapping(path = "sendReport")
	public String showSendReport() {
		if (log.isInfoEnabled())
			log.info("Mostrar pagina de enviar reportes");

		return "send-report";
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
	public String showProducts() {
		if (log.isInfoEnabled())
			log.info("Mostrar AdminProducts");

		return "admin-products";
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

		return "admin-complaints";
	}

	/**
	 * Mostrar AdminSendings
	 * 
	 * @return String
	 */
//	@GetMapping(path = "adminSendings")
//	public String showSendings(final Model model) {
//		if (log.isInfoEnabled())
//			log.info("Mostrar AdminSendings");
//
//		model.addAttribute("showSendings", true);
//
//		return "admin-sendings";
//	}

	/**
	 * Mostrar AdminSendings
	 * 
	 * @return String
	 */
	@GetMapping(path = "adminSendings")
	public String showSendings() {
		if (log.isInfoEnabled())
			log.info("Mostrar AdminSendings");

		return "admin-sendings";
	}

	/**
	 * Mostrar AdminUsers
	 * 
	 * @return String
	 */
	@GetMapping(path = "adminUsers")
	public String showUsers() {
		if (log.isInfoEnabled())
			log.info("Mostrar AdminUsers");

		return "admin-user";
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

//	/**
//	 * Redireccionar a pagina Reiniciar contrasena
//	 * 
//	 * @return String
//	 */
//	@GetMapping(path = "recoverPassword")
//	public String goRecoverPassword() {
//		if (log.isInfoEnabled())
//			log.info("Redireccionar a pagina Reiniciar contrasena");
//
//		return "recover-password";
//	}

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
