package com.ntd.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ntd.dto.OrderDTO;
import com.ntd.dto.ProductDTO;
import com.ntd.dto.ProductSoldDTO;
import com.ntd.dto.UserDTO;
import com.ntd.exceptions.InternalException;
import com.ntd.services.OrderMgmtServiceI;
import com.ntd.services.ProductMgmtServiceI;
import com.ntd.services.ProductSoldMgmtServiceI;
import com.ntd.utils.Constants;
import com.ntd.utils.ValidateParams;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador de Pedidos
 * 
 * @author SLP
 */
@Slf4j
@Controller
@RequestMapping("/orders")
public class OrderController {

	/** Constante String ordersDto */
	private static final String ORDERS_DTO = "ordersDto";

	/** Dependencia del servicio de gestion de pedidos */
	private final OrderMgmtServiceI orderMgmtService;

	/** Dependencia del servicio de gestion de productos */
	private final ProductMgmtServiceI productMgmtService;

	/** Dependencia del servicio de gestion de productos vendidos */
	private final ProductSoldMgmtServiceI productSoldMgmtService;

	/**
	 * Constructor
	 * 
	 * @param orderMgmtService
	 * @param productMgmtService
	 * @param productSoldMgmtService
	 */
	public OrderController(final OrderMgmtServiceI orderMgmtService, ProductMgmtServiceI productMgmtService,
			ProductSoldMgmtServiceI productSoldMgmtService) {
		this.orderMgmtService = orderMgmtService;
		this.productMgmtService = productMgmtService;
		this.productSoldMgmtService = productSoldMgmtService;
	}

	/**
	 * Buscar todos los pedidos
	 * 
	 * @param model
	 * @return String
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchAll")
	public String showOrders(final Model model) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Mostrar todos los pedidos");

		// Insertar en el Model la lista de clientes
		model.addAttribute(ORDERS_DTO, orderMgmtService.searchAllOrders());

		// Retornar lista de pedidos
		return "VISTA MOSTRAR PEDIDOS";
	}

	/**
	 * Realizar pedido
	 * 
	 * @param orderDto
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@PostMapping(path = "/save")
	public ResponseEntity<Object> saveOrder(@ModelAttribute @Valid final OrderDTO orderDto) throws InternalException {
		log.info("Guardar pedido");

		// Validar datos de productos a comprar
		productSoldMgmtService.validateProductsToBuy(orderDto.soldProductsDto());

		ResponseEntity<Object> result = null;

		// Comprobar si el pedido existe
		if (orderMgmtService.existsOrder(orderDto.orderNumber())) {
			// Retornar 400 si existe el numero de pedido
			result = ResponseEntity.badRequest().build();
		} else {

			// Confirmar productos a comprar
			final List<ProductDTO> productsDtoNotFound = productMgmtService.confirmOrder(orderDto.soldProductsDto(),
					false);
			if (productsDtoNotFound.isEmpty()) {
				// Guardar pedido
				result = orderMgmtService.insertOrder(orderDto);
			} else {
				log.info("Retorno de productos con stock insuficiente");

				// Retornar 422 para productos faltantes
				result = ResponseEntity.unprocessableEntity()
						.body(Collections.singletonMap("products", productsDtoNotFound));
			}
		}

		// Retornar respuesta
		return result;
	}

	/**
	 * Chequear productos a vender
	 * 
	 * @param productsToSold
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@PostMapping(path = "/checkProducts")
	public ResponseEntity<Object> checkProductsToSold(@RequestBody @NotNull List<Map<String, Object>> productsToSold)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Chequear los productos a vender");

		ResponseEntity<Object> result = null;

		ValidateParams.isNullObject(productsToSold);

		if (productsToSold.isEmpty()) {
			throw new InternalException();
		}

		// Convertir objeto en ProductSoldDTO
		List<ProductSoldDTO> productsDtoToSold = new ArrayList<>();
		for (Map<String, Object> object : productsToSold) {

			productsDtoToSold.add(new ProductSoldDTO(null, null, Long.parseLong((String) object.get("productId")),
					(Integer) object.get("quantity")));
		}

		// Confirmar productos a comprar
		final List<ProductDTO> productsDtoNotFound = productMgmtService.confirmOrder(productsDtoToSold, true);

		if (productsDtoNotFound.isEmpty()) {
			// Devolver una respuesta con codigo de estado 204
			result = ResponseEntity.noContent().build();
		} else {
			// Eliminar datos innecesarios
			final List<ProductSoldDTO> returnProducts = new ArrayList<>();
			for (ProductDTO productDTO : productsDtoNotFound) {
				returnProducts
						.add(new ProductSoldDTO(productDTO.productId(), null, null, productDTO.productQuantity()));
			}

			// Devolver una respuesta con codigo de estado 422
			result = ResponseEntity.unprocessableEntity().body(Collections.singletonMap("products", returnProducts));
		}

		return result;
	}

	/**
	 * Actualizar estado de pedido. El atributo Status es el unico campo que en
	 * principio sera actualizado
	 * 
	 * @param orderId
	 * @param status
	 * @param model
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@PutMapping
	public String updateOrderStatus(@RequestParam @NotNull final Long orderId,
			@RequestParam @NotBlank final String status, final Model model) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Actualizar estado de pedido");

		String result = null;

		// Actualizar pedido
		final OrderDTO orderUpdated = orderMgmtService.updateOrderStatus(orderId, status);

		// Verificar retorno de actualizacion
		if (orderUpdated != null) {
			result = Constants.MSG_SUCCESSFUL_OPERATION;
		} else {
			result = Constants.MSG_UNEXPECTED_ERROR;
		}

		model.addAttribute(Constants.MESSAGE_GROWL, result);

		// Retornar respuesta
		return "VISTA MOSTRAR RESPUESTA DE PEDIDO ACTUALIZADO";
	}

	/**
	 * Eliminar pedido
	 * 
	 * @param orderDto
	 * @param model
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@Transactional
	@DeleteMapping
	public String deleteOrder(@RequestBody @NotNull final OrderDTO orderDto, final Model model)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Eliminar pedido");

		// Validar id
		ValidateParams.isNullObject(orderDto.orderId());

		// Eliminar pedido
		orderMgmtService.deleteOrder(orderDto.orderId());

		model.addAttribute(Constants.MESSAGE_GROWL, Constants.MSG_SUCCESSFUL_OPERATION);

		// Retornar respuesta
		return "VISTA MOSTRAR RESPUESTA DE PEDIDO eliminado";
	}

	/**
	 * Buscar pedidos entre dos fechas
	 * 
	 * @param startDate
	 * @param endDate
	 * @param model
	 * @return List
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByDates")
	public String searchByOrderDateBetween(@RequestParam @NotNull final LocalDateTime startDate,
			@NotNull @RequestParam final LocalDateTime endDate, final Model model) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar por numero de pedido");

		// Retornar pedidos
		model.addAttribute(ORDERS_DTO, orderMgmtService.searchByOrderDateBetween(startDate, endDate));

		return "VISTA MOSTRAR Buscar pedidos entre dos fechas";
	}

	/**
	 * Buscar pedidos por fecha por orden ASC
	 * 
	 * @param model
	 * @return List
	 * @throws InternalException
	 */
	@GetMapping(path = "/sortDatesAsc")
	public String searchByOrderByOrderDateAsc(final Model model) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Mostrar todos los pedidos ordenados por fecha ASC");

		// Retornar lista de pedidos
		model.addAttribute(ORDERS_DTO, orderMgmtService.searchByOrderByOrderDateAsc());

		return "VISTA MOSTRAR Buscar pedidos por fecha por orden ASC";
	}

	/**
	 * Buscar pedidos por fecha por orden DESC
	 * 
	 * @param model
	 * @return List
	 * @throws InternalException
	 */
	@GetMapping(path = "/sortDatesDesc")
	public String searchByOrderByOrderDateDesc(final Model model) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Mostrar todos los pedidos ordenados por fecha DESC");

		// Retornar lista de pedidos
		model.addAttribute(ORDERS_DTO, orderMgmtService.searchByOrderByOrderDateDesc());

		return "VISTA MOSTRAR pedidos por fecha por orden DESC";
	}

	/**
	 * Buscar por numero de pedido
	 *
	 * @param model
	 * @param orderNumber
	 * @return OrderDTO
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByOrderNumber")
	public String searchByOrderNumber(@RequestParam @NotNull final String orderNumber, final Model model)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar por numero de pedido");

		// Retornar pedido
		model.addAttribute("orderDto", orderMgmtService.searchByOrderNumber(orderNumber));

		return "VISTA MOSTRAR pedido por numero";
	}

	/**
	 * Buscar por estado de pedido
	 * 
	 * @param model
	 * @param status
	 * @return List
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByStatus")
	public String searchByStatus(@RequestParam @NotNull final String status, final Model model)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar por estado de pedido");

		// Retornar lista de pedidos
		model.addAttribute(ORDERS_DTO, orderMgmtService.searchByStatus(status));

		return "VISTA MOSTRAR pedidos por estado";
	}

	/**
	 * Buscar pedido por usuario
	 * 
	 * @param model
	 * @param userDto
	 * @return List
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByUser")
	public String searchByUser(@RequestBody @NotNull final UserDTO userDto, final Model model)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar pedido por usuario");

		// Retornar lista de pedidos
		model.addAttribute(ORDERS_DTO, orderMgmtService.searchByUser(userDto));

		return "VISTA MOSTRAR pedidos por usuario";
	}

}
