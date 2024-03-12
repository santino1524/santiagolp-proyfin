package com.ntd.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ntd.dto.OrderDTO;
import com.ntd.dto.ProductDTO;
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
@RestController
@RequestMapping("/orders")
public class OrderController {

	/** Dependencia del servicio de gestion de tarjetas */
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
	 * @return List
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchAll")
	public List<OrderDTO> showOrders() throws InternalException {
		if (log.isInfoEnabled())
			log.info("Mostrar todos los pedidos");

		// Retornar lista de pedidos
		return orderMgmtService.searchAllOrders();
	}

	/**
	 * Realizar pedido
	 * 
	 * @param orderDto
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@PostMapping
	public ResponseEntity<String> saveOrder(@RequestBody @Valid final OrderDTO orderDto) throws InternalException {
		log.info("Guardar pedido");

		// Validar datos de productos a comprar
		productSoldMgmtService.validateProductsToBuy(orderDto.soldProductsDto());

		ResponseEntity<String> result = null;

		// Comprobar si el pedido existe
		if (orderMgmtService.existsOrder(orderDto.orderNumber())) {
			// Devolver una respuesta con codigo de estado 422
			result = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Constants.MSG_ORDER_EXISTS);
		} else {

			// Confirmar productos a comprar
			final List<ProductDTO> productsDtoNotFound = productMgmtService.confirmOrder(orderDto.soldProductsDto());
			if (productsDtoNotFound.isEmpty()) {
				// Guardar pedido
				if (orderMgmtService.insertOrder(orderDto).orderId() != null) {
					// Devolver una respuesta con codigo de estado 202
					result = ResponseEntity.status(HttpStatus.ACCEPTED).body(Constants.MSG_SUCCESSFUL_OPERATION);
				} else {
					// Devolver una respuesta con codigo de estado 422
					result = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
							.body(Constants.MSG_UNEXPECTED_ERROR);
				}
			} else {
				log.info("Retorno de productos con stock insuficiente");

				StringBuilder builder = new StringBuilder();

				// Preparar respuesta con productos no disponibles
				builder.append("No hay sufcientes unidades en stock de:\n");

				for (ProductDTO productDto : productsDtoNotFound) {
					builder.append(productDto.productName());
					builder.append(" - Talla: ");
					builder.append(productDto.productSize());
					builder.append(" - Disponibilidad: ");
					builder.append(productDto.productQuantity());
					builder.append("\n");
				}

				// Devolver una respuesta con codigo de estado 404
				result = ResponseEntity.status(HttpStatus.NOT_FOUND).body(builder.toString());
			}

		}

		// Retornar respuesta
		return result;
	}

	/**
	 * Actualizar estado de pedido. El atributo Status es el unico campo que en
	 * principio sera actualizado
	 * 
	 * @param orderId
	 * @param status
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@PutMapping
	public ResponseEntity<String> updateOrderStatus(@RequestParam @NotNull final Long orderId,
			@RequestParam @NotBlank final String status) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Actualizar estado de pedido");

		ResponseEntity<String> result = null;

		// Actualizar pedido
		final OrderDTO orderUpdated = orderMgmtService.updateOrderStatus(orderId, status);

		// Verificar retorno de actualizacion
		if (orderUpdated.orderNumber() != null) {
			// Devolver una respuesta con codigo de estado 202
			result = ResponseEntity.status(HttpStatus.ACCEPTED).body(Constants.MSG_SUCCESSFUL_OPERATION);
		} else {
			// Devolver una respuesta con codigo de estado 422
			result = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Constants.MSG_UNEXPECTED_ERROR);
		}

		// Retornar respuesta
		return result;
	}

	/**
	 * Eliminar pedido
	 * 
	 * @param orderDto
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@Transactional
	@DeleteMapping
	public ResponseEntity<String> deleteOrder(@RequestBody @NotNull final OrderDTO orderDto) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Eliminar pedido");

		// Validar id
		ValidateParams.isNullObject(orderDto.orderId());

		// Eliminar pedido
		orderMgmtService.deleteOrder(orderDto.orderId());

		// Devolver una respuesta con codigo de estado 202
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(Constants.MSG_SUCCESSFUL_OPERATION);
	}

	/**
	 * Buscar pedidos entre dos fechas
	 * 
	 * @param startDate
	 * @param endDate
	 * @return List
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByDates")
	public List<OrderDTO> searchByOrderDateBetween(@RequestParam @NotNull final LocalDateTime startDate,
			@NotNull @RequestParam final LocalDateTime endDate) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar por numero de pedido");

		// Retornar la lista de clientes
		return orderMgmtService.searchByOrderDateBetween(startDate, endDate);
	}

	/**
	 * Buscar pedidos por fecha por orden ASC
	 * 
	 * @return List
	 * @throws InternalException
	 */
	@GetMapping(path = "/sortDatesAsc")
	public List<OrderDTO> searchByOrderByOrderDateAsc() throws InternalException {
		if (log.isInfoEnabled())
			log.info("Mostrar todos los pedidos ordenados por fecha ASC");

		// Retornar lista de pedidos
		return orderMgmtService.searchByOrderByOrderDateAsc();
	}

	/**
	 * Buscar pedidos por fecha por orden DESC
	 * 
	 * @return List
	 * @throws InternalException
	 */
	@GetMapping(path = "/sortDatesDesc")
	public List<OrderDTO> searchByOrderByOrderDateDesc() throws InternalException {
		if (log.isInfoEnabled())
			log.info("Mostrar todos los pedidos ordenados por fecha DESC");

		// Retornar lista de pedidos
		return orderMgmtService.searchByOrderByOrderDateDesc();
	}

	/**
	 * Buscar por numero de pedido
	 * 
	 * @param orderNumber
	 * @return List
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByOrderNumber")
	public OrderDTO searchByOrderNumber(@RequestParam @NotNull final String orderNumber) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar por numero de pedido");

		// Retornar pedido
		return orderMgmtService.searchByOrderNumber(orderNumber);
	}

	/**
	 * Buscar por estado de pedido
	 * 
	 * @param status
	 * @return List
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByStatus")
	public List<OrderDTO> searchByStatus(@RequestParam @NotNull final String status) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar por estado de pedido");

		// Retornar lista de pedidos
		return orderMgmtService.searchByStatus(status);
	}

	/**
	 * Buscar pedido por usuario
	 * 
	 * @param userDto
	 * @return List
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByUser")
	public List<OrderDTO> searchByUser(@RequestBody @NotNull final UserDTO userDto) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar pedido por usuario");

		// Retornar lista de pedidos
		return orderMgmtService.searchByUser(userDto);
	}

}
