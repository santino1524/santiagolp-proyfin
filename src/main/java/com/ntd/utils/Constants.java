
package com.ntd.utils;

import java.util.Arrays;
import java.util.List;

/**
 * Clase de constantes
 * 
 * @author SLP
 *
 */
public class Constants {

	/** Constructor privado */
	private Constants() {
	}

	/** Nombre de la tienda */
	public static final String STORE_NAME = "Tienda Luz Fuego Destrucción";

	/** Usuario predeterminado SELLER */
	public static final String DEFAULT_USER = "goku@mail.com";

	/** Contrasenna predeterminada del usuario goku */
	public static final String DEFAULT_PASSWD = "goku";

	/** URL vista error */
	public static final String URL_ERROR_VIEW = "error";

	/** URL vista login */
	public static final String LOGIN_PAGE = "/login-page";

	/** MSG Acceso denegado */
	public static final String ACCESS_DENIEG = "Acceso denegado. No tienes permiso para acceder a esta página.";

	/** Variable para mensaje vista ERROR */
	public static final String VIEW_ERROR_MESSAGE = "message";

	/** Msg InternalException */
	public static final String MSG_INTERNAL_EXC = "Error interno, contacta con el administrador del sistema.";

	/** Msg de usuario registrado */
	public static final String MSG_REGISTER_USER = "Registro exitoso. Por favor revise su correo electrónico para confirmar";

	/** Msg de error inesperado */
	public static final String MSG_UNEXPECTED_ERROR = "Ha ocurrido un error inesperado";

	/** Msg de DNI Duplicado */
	public static final String MSG_DNI_EXISTS = "Existe un cliente registrado con ese DNI";

	/** Msg de email duplicado */
	public static final String MSG_EMAIL_EXISTS = "El email introducido ya está registrado";

	/** Msg de telefono duplicado */
	public static final String MSG_PHONE_EXISTS = "El número de teléfono introducido ya está registrado";

	/** Msg de name NotValid */
	public static final String MSG_NAME_NOT_VALID = "El nombre introducido no es válido";

	/** Msg de respuesta NotValid */
	public static final String MSG_ANSWER_NOT_VALID = "Debe introducir las respuestas";

	/** Msg de productSizes NotValid */
	public static final String MSG_PRODUCT_SIZES_NOT_VALID = "Debe introducir la talla del producto";

	/** Msg de imageUrls NotValid */
	public static final String MSG_IMAGE_NOT_VALID = "El producto debe contener alguna imagen";

	/** Msg de category NotValid */
	public static final String MSG_CATEGORY_NOT_VALID = "La categoría introducida no es válida";

	/** Msg de quantity NotValid */
	public static final String MSG_QUANTITY_NOT_VALID = "La cantidad de producto introducida no es válida";

	/** Msg de email NotValid */
	public static final String MSG_EMAIL_NOT_VALID = "El email introducido no es válido";

	/** Msg de addressList NotValid */
	public static final String MSG_ADDRESS_LIST_NOT_VALID = "Debe registrar al menos una dirección";

	/** Msg de productList NotValid */
	public static final String MSG_PRODUCT_LIST_NOT_VALID = "Debe registrar al menos un producto";

	/** Msg de passwd NotValid */
	public static final String MSG_PASSWD_NOT_VALID = "La contraseña introducida no es válida";

	/** Msg de surnames NotValid */
	public static final String MSG_SURNAMES_NOT_VALID = "Los apellidos introducidos no son válidos";

	/** Msg de DNI NotValid */
	public static final String MSG_DNI_NOT_VALID = "DNI introducido no es válido";

	/** Msg de user NotValid */
	public static final String MSG_USER_NOT_VALID = "El usuario no puede ser nulo";

	/** Msg de CP NotValid */
	public static final String MSG_CP_NOT_VALID = "El código postal introducido no es válido";

	/** Msg de direction NotValid */
	public static final String MSG_DIRECTION_NOT_VALID = "Debe introducir la dirección";

	/** Msg de city NotValid */
	public static final String MSG_CITY_NOT_VALID = "Debe introducir la ciudad";

	/** Msg de province NotValid */
	public static final String MSG_PROVINCE_NOT_VALID = "Debe introducir la provincia";

	/** Msg de phoneNumber NotValid */
	public static final String MSG_PHONE_NUMBER_NOT_VALID = "El número de teléfono introducido no es válido";

	/** Msg de iva NotValid */
	public static final String MSG_IVA_NOT_VALID = "El IVA introducido no es válido";

	/** Msg de price NotValid */
	public static final String MSG_PRICE_NOT_VALID = "El precio introducido no es válido";

	/** Msg de total NotValid */
	public static final String MSG_TOTAL_NOT_VALID = "El total del pedido no es válido";

	/** Estados de pedido */
	private static final List<String> ORDER_STATUSES = Arrays.asList("CREADO", "ENVIADO", "CANCELADO");

	/** Estados de los pedidos */
	private static final List<String> USER_ROLE = Arrays.asList("SELLER", "BUYER");

	/** Endpoints protegidos */
	private static final String[] AUTHENTICATED_ENDPOINTS = { "/pay", "/userProfile", "/myOrders", "/checkAuth",
			"/sendReport", "/orders/save", "/orders/validateOrder", "/orders/searchByUser", "/orders/searchTopByUser",
			"/orders/searchByUserDateDesc", "/addresses/save", "/addresses/delete", "/addresses/searchByUser",
			"/addresses/searchById", "/productReview/save", "/report/save", "/users/verifyOldPasswd", "/users/update" };

	/** Endpoints ADMIN */
	private static final String[] ADMIN_ENDPOINTS = { "/admin", "/adminUsers", "/adminSendings", "/adminComplaints",
			"/adminProducts", "/orders/generateShippingLabel", "/orders/countByStatus", "/orders/updateStatusEnviado",
			"/orders/updateStatusCancelado", "/orders/searchByCreado", "/category/save", "/category/delete",
			"/products/generateProductLabel", "/products/deleteImages", "/products/delete",
			"/productReview/countReviewReporter", "/report/countByReporter", "/report/searchByWithoutProcessing",
			"/report/delete", "/users/searchByCriterio" };

	/** Msg de productNumber NotValid */
	public static final String MSG_PRODUCT_NUMBER_NOT_VALID = "Debe introducir un número de producto";

	/** Msg de orderNumber NotValid */
	public static final String MSG_ORDER_NUMBER_NOT_VALID = "Debe introducir un número de pedido";

	/** Msg de orderDate NotValid */
	public static final String MSG_ORDER_DATE_NOT_VALID = "La fecha del pedido debe ser anterior a la actual";

	/** REGEXP para nombres */
	public static final String REGEXP_NAMES = "^[a-zA-ZÀ-ÖØ-öø-ÿ\s]*$";

	/** REGEXP para DNI */
	public static final String REGEXP_DNI = "^[a-zA-Z0-9]{9}$";

	/** REGEXP para CP */
	public static final String REGEXP_CP = "^\\d{5}$";

	/** REGEXP para numero telefonico */
	public static final String REGEXP_PHONE_NUMBER = "^\\d{9}$";

	/** Msg violacion de CONSTRAINT EN BBDD */
	public static final String MSG_VIOLATION_CONSTRAINT = "No se pudo completar la operación debido a un problema con los datos proporcionados";

	/** Msg status NotValid */
	public static final String MSG_STATUS_NOT_VALID = "Estado de pedido no válido";

	/** Msg user role NotValid */
	public static final String MSG_USER_ROLE_NOT_VALID = "Rol de usuario no válido";

	/** Msg error del servidor */
	public static final String MSG_ERR_SERVLET = "Ha ocurrido un error en el servidor";

	/**
	 * Devuelve los tipos de estados
	 * 
	 * @return List
	 */
	public static List<String> getOrderStatuses() {
		return ORDER_STATUSES;
	}

	/**
	 * Devuelve los roles de usuario
	 * 
	 * @return List
	 */
	public static List<String> getRolesUser() {
		return USER_ROLE;
	}

	/**
	 * Devuelve los endpoints a autenticar
	 * 
	 * @return String[]
	 */
	public static String[] getEndpointsAuth() {
		return AUTHENTICATED_ENDPOINTS;
	}

	/**
	 * Devuelve los endpoints de administracion
	 * 
	 * @return String[]
	 */
	public static String[] getEndpointsAdmin() {
		return ADMIN_ENDPOINTS;
	}
}
