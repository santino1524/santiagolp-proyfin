
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
	public static final String STORE_NAME = "Tienda Luz Fuego y Destrucción";

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

	/** Constante para mensaje growl */
	public static final String MESSAGE_GROWL = "error";

	/** Separador para rutas */
	public static final String SEPARATOR = "/";

	/** Dir de imagenes del producto */
	public static final String PRODUCT_IMAGES = "product_images";

	/** MSG Categoria creada */
	public static final String MSG_SAVE_CATEGORY = "La categoría se ha creado";

	/** MSG Categoria existente */
	public static final String MSG_EXISTS_CATEGORY = "Ya está registrada una categoría con ese nombre";

	/** MSG Categoria eliminada */
	public static final String MSG_DELETE_CATEGORY = "La categoría se ha eliminado";

	/** MSG Categoria no encontrada */
	public static final String MSG_NOT_FOUND_CATEGORY = "No se ha encontrado ninguna categoría con ese nombre";

	/** Variable para mensaje vista ERROR */
	public static final String VIEW_ERROR_MESSAGE = "message";

	/** Msg ExceptionDeleteFiles */
	public static final String MSG_DELETE_FILES_EXC = "Error al eliminar los archivo correspondientes a las imágenes de productos";

	/** Msg InternalException */
	public static final String MSG_INTERNAL_EXC = "Error interno, contacta con el administrador del sistema.";

	/** Msg Pagina de Admin en movil */
	public static final String MSG_ADMIN_RESPONSIVE_EXC = "La página de Administración no se puede abrir desde un dispositivo móvil.";

	/** Msg IllegalDatesException */
	public static final String MSG_ILLEGAL_DATES = "Los datos introducidos son incorrectos.";

	/** Msg de operacion exitosa */
	public static final String MSG_SUCCESSFUL_OPERATION = "La operación se ha completado con éxito";

	/** Msg de usuario registrado */
	public static final String MSG_REGISTER_USER = "Se ha registrado el usuario satisfactoriamente";

	/** Msg de error inesperado */
	public static final String MSG_UNEXPECTED_ERROR = "Ha ocurrido un error inesperado";

	/** URL buscar clientes */
	public static final String URL_SEARCH_CUSTOMERS = "searchCustomers";

	/** URL mostrar todos los clientes */
	public static final String URL_ALL_CUSTOMERS = "showCustomers";

	/** URL guardar nuevo cliente */
	public static final String URL_SAVE_CUSTOMER = "enrollCustomer";

	/** Msg de DNI Duplicado */
	public static final String MSG_DNI_EXISTS = "Existe un cliente registrado con ese DNI";

	/** Msg de datos Duplicado */
	public static final String MSG_USER_DATA_EXISTS = "Existe un cliente registrado con los datos introducidos";

	/** Msg de direccion duplicada */
	public static final String MSG_POSTAL_ADDRESS_EXISTS = "La dirección introducida ya está registrada";

	/** Msg de pedido duplicado */
	public static final String MSG_ORDER_EXISTS = "El número de pedido introducido ya está registrado";

	/** Msg de email duplicado */
	public static final String MSG_EMAIL_EXISTS = "El email introducido ya está registrado";

	/** Msg de telefono duplicado */
	public static final String MSG_PHONE_EXISTS = "El número de teléfono introducido ya está registrado";

	/** Msg de producto duplicado */
	public static final String MSG_PRODUCT_DATA_EXISTS = "Existe más de un producto registrado con los datos introducidos";

	/** Msg de Productreview no registrado */
	public static final String MSG_PRODUCT_REVIEW_NON_EXISTENT = "La crítica no está registrada";

	/** Msg de nombre de producto duplicado */
	public static final String MSG_PRODUCT_NAME_EXISTS = "El nombre de producto introducido ya está registrado";

	/** Msg de numero de producto duplicado */
	public static final String MSG_PRODUCT_NUMBER_EXISTS = "El número de producto introducido ya está registrado";

	/** Msg de preguntas NotValid */
	public static final String MSG_QUESTIONS_NOT_VALID = "Debe introducir las preguntas";

	/** Msg de name NotValid */
	public static final String MSG_NAME_NOT_VALID = "El nombre introducido no es válido";

	/** Msg de reason NotValid */
	public static final String MSG_REASON_NOT_VALID = "Debe introducir la razón de la denuncia";

	/** Msg de pregunta NotValid */
	public static final String MSG_QUESTION_NOT_VALID = "Debe introducir la pregunta";

	/** Msg de respuesta NotValid */
	public static final String MSG_ANSWER_NOT_VALID = "Debe introducir la respuestas";

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

	/** Msg de cardList NotValid */
	public static final String MSG_CARD_LIST_NOT_VALID = "Debe registrar al menos una tarjeta bancaria";

	/** Msg de passwd NotValid */
	public static final String MSG_PASSWD_NOT_VALID = "La contraseña introducida no es válida";

	/** Msg de surnames NotValid */
	public static final String MSG_SURNAMES_NOT_VALID = "Los apellidos introducidos no son válidos";

	/** Msg de birhdate NotValid */
	public static final String MSG_BIRTHDATE_NOT_VALID = "La fecha introducida no es válido";

	/** Msg de DNI NotValid */
	public static final String MSG_DNI_NOT_VALID = "DNI introducido no es válido";

	/** Msg de cardnumber NotValid */
	public static final String MSG_CARD_NUMBER_NOT_VALID = "El número de tarjeta introducido no es válido";

	/** Msg de cardExpiration NotValid */
	public static final String MSG_CARD_EXPIRATION_NOT_VALID = "La fecha de expiración introducida no es válida";

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

	/** Pattern fecha de expiracion */
	public static final String PATTERN_CARD_EXPIRATION = "MM/yy";

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
	private static final String[] AUTHENTICATED_ENDPOINTS = { "/pay", "/user-profile", "/userProfile", "/myOrders",
			"/my-orders" };

	/** Endpoints ADMIN */
	private static final String[] ADMIN_ENDPOINTS = { "/admin", "/adminUsers", "/adminSendings", "/adminComplaints",
			"/adminProducts", "/adminOrders", "/responsiveAdmin" };

	/** Msg de order NotValid */
	public static final String MSG_ORDER_NOT_VALID = "Producto vendido sin asignar a un pedido";

	/** Msg de product NotValid */
	public static final String MSG_PRODUCT_NOT_VALID = "Producto vendido sin referencia en stock";

	/** Msg de productNumber NotValid */
	public static final String MSG_PRODUCT_NUMBER_NOT_VALID = "Debe introducir un número de producto";

	/** Msg de orderNumber NotValid */
	public static final String MSG_ORDER_NUMBER_NOT_VALID = "Debe introducir un número de pedido";

	/** Msg de orderDate NotValid */
	public static final String MSG_ORDER_DATE_NOT_VALID = "La fecha del pedido debe ser anterior a la actual";

	/** Msg de alta cliente */
	public static final String MSG_ENROLL_CUSTOMER = "Cliente dado de alta";

	/** URL actualizar cliente */
	public static final String URL_UPDATE_CUSTOMER = "updateCustomer";

	/** URL modificar cliente */
	public static final String URL_MODIFY_CUSTOMER = "modifyCustomer";

	/** URL buscar por criterio */
	public static final String URL_SEARCH_BY_CRITERIO = "searchByCriterio";

	/** URL redireccionar a modifyCustomer */
	public static final String URL_REDIRECT_MODIFY_CUSTOMER = "redirectToModifyCustomer";

	/** URL eliminar cliente */
	public static final String URL_DELETE_CUSTOMER = "deleteCustomer";

	/** REGEXP para nombres */
	public static final String REGEXP_NAMES = "^[a-zA-ZÀ-ÖØ-öø-ÿ\s]*$";

	/** REGEXP para DNI */
	public static final String REGEXP_DNI = "^[a-zA-Z0-9]{9}$";

	/** REGEXP para Contrasena */
	public static final String REGEXP_PASSWD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!*])(?=\\S+$).{7,}$";

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
