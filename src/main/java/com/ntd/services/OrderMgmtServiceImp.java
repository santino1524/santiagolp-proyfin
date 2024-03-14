package com.ntd.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ntd.dto.OrderDTO;
import com.ntd.dto.ProductSoldDTO;
import com.ntd.dto.UserDTO;
import com.ntd.dto.mapper.DTOMapperI;
import com.ntd.dto.validators.OrderStatusValidator;
import com.ntd.exceptions.InternalException;
import com.ntd.persistence.Order;
import com.ntd.persistence.OrderRepositoryI;
import com.ntd.utils.Constants;
import com.ntd.utils.ValidateParams;

import lombok.extern.slf4j.Slf4j;

/**
 * Servicio de gestion de pedidos
 * 
 * @author SLP
 */
@Service
@Slf4j
public class OrderMgmtServiceImp implements OrderMgmtServiceI {

	/** Dependencia de OrderRepository */
	private final OrderRepositoryI orderRepository;

	/** Dependencia de OrderRepository */
	private final ProductSoldMgmtServiceI productSoldMgmtService;

	/**
	 * Constructor
	 * 
	 * @param orderRepository
	 * @param productSoldMgmtService
	 */
	public OrderMgmtServiceImp(final OrderRepositoryI orderRepository, ProductSoldMgmtServiceI productSoldMgmtService) {
		this.orderRepository = orderRepository;
		this.productSoldMgmtService = productSoldMgmtService;
	}

	@Override
	public String insertOrder(OrderDTO orderDto) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Insertar pedido");

		// Validar parametro
		ValidateParams.isNullObject(orderDto);

		// Extraer lista de productos vendidos
		List<ProductSoldDTO> soldProductsDto = orderDto.soldProductsDto();

		// Vaciar lista de productos vendidos para guardar pedido
		Order order = DTOMapperI.MAPPER.mapDTOToOrder(orderDto);
		order.getSoldProducts().clear();

		// Mapear DTO y guardar
		order = orderRepository.save(order);

		// Guardar lista de productos vendidos
		List<ProductSoldDTO> soldProductsDtoRegistered = productSoldMgmtService.insertAllProductSold(soldProductsDto);

		String result;
		if (order != null && soldProductsDtoRegistered.size() == soldProductsDto.size()) {
			result = Constants.MSG_SUCCESSFUL_OPERATION;
		} else {
			result = Constants.MSG_UNEXPECTED_ERROR;
		}

		// Retornar mensaje de operacion
		return result;

	}

	@Override
	public OrderDTO updateOrderStatus(Long orderId, String status) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Actualizar estado de pedido");

		// Validar parametros
		ValidateParams.isNullObject(orderId);
		OrderStatusValidator orderStatusValid = new OrderStatusValidator();
		if (!orderStatusValid.isValid(status, null)) {
			throw new InternalException();
		}

		// Actualizar estado de pedido
		final Order order = orderRepository.findById(orderId).orElseThrow(InternalException::new);
		order.setStatus(status);

		// Persistir Pedido y retornar DTO
		return DTOMapperI.MAPPER.mapOrderToDTO(orderRepository.save(order));
	}

	@Override
	public void deleteOrder(Long id) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Eliminar pedido");

		// Validar parametro
		ValidateParams.isNullObject(id);

		// Eliminar por DNI
		orderRepository.deleteById(id);
	}

	@Override
	public List<OrderDTO> searchAllOrders() {
		if (log.isInfoEnabled())
			log.info("Buscar todos los pedidos");

		// Buscar todos las tarjetas
		final List<Order> orders = orderRepository.findAll();

		// Mapear DTO
		final List<OrderDTO> ordersDto = new ArrayList<>();

		if (!orders.isEmpty()) {
			for (Order order : orders) {
				ordersDto.add(DTOMapperI.MAPPER.mapOrderToDTO(order));
			}
		}

		// Retornar lista DTO
		return ordersDto;
	}

	@Override
	public boolean existsOrder(String orderNumber) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Verificar si existe el numero de pedido");

		// Validar parametro
		ValidateParams.isNullObject(orderNumber);

		// Retornar si existe el pedido en BBDD
		return orderRepository.existsByOrderNumber(orderNumber);
	}

	@Override
	public List<OrderDTO> searchByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar pedidos entre fechas");

		// Validar parametros
		ValidateParams.isNullObject(startDate);
		ValidateParams.isNullObject(endDate);

		// Buscar pedidos
		final List<Order> orders = orderRepository.findByOrderDateBetween(startDate, endDate);

		// Mapear DTO
		final List<OrderDTO> ordersDto = new ArrayList<>();

		if (!orders.isEmpty()) {
			for (Order order : orders) {
				ordersDto.add(DTOMapperI.MAPPER.mapOrderToDTO(order));
			}
		}

		// Retornar lista DTO
		return ordersDto;
	}

	@Override
	public List<OrderDTO> searchByOrderByOrderDateAsc() {
		if (log.isInfoEnabled())
			log.info("Buscar pedidos ordenados ascendentemente por fecha");

		// Buscar por pedidos
		final List<Order> orders = orderRepository.findByOrderByOrderDateAsc();

		// Mapear DTO
		final List<OrderDTO> ordersDto = new ArrayList<>();

		if (!orders.isEmpty()) {
			for (Order order : orders) {
				ordersDto.add(DTOMapperI.MAPPER.mapOrderToDTO(order));
			}
		}

		// Retornar lista DTO
		return ordersDto;
	}

	@Override
	public List<OrderDTO> searchByOrderByOrderDateDesc() {
		if (log.isInfoEnabled())
			log.info("Buscar pedidos ordenados descendentemente por fecha");

		// Buscar por pedidos
		final List<Order> orders = orderRepository.findByOrderByOrderDateDesc();

		// Mapear DTO
		final List<OrderDTO> ordersDto = new ArrayList<>();

		if (!orders.isEmpty()) {
			for (Order order : orders) {
				ordersDto.add(DTOMapperI.MAPPER.mapOrderToDTO(order));
			}
		}

		// Retornar lista DTO
		return ordersDto;
	}

	@Override
	public OrderDTO searchByOrderNumber(String orderNumber) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar por numero de pedido ");

		// Validar parametro
		ValidateParams.isNullObject(orderNumber);

		// Buscar por numero de pedido
		final Order order = orderRepository.findByOrderNumber(orderNumber);

		// Retornar DTO
		return DTOMapperI.MAPPER.mapOrderToDTO(order);
	}

	@Override
	public List<OrderDTO> searchByStatus(String status) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar pedidos por estado");

		// Validar parametro
		ValidateParams.isNullObject(status);

		// Buscar por pedidos
		final List<Order> orders = orderRepository.findByStatusIgnoreCase(status);

		// Mapear DTO
		final List<OrderDTO> ordersDto = new ArrayList<>();

		if (!orders.isEmpty()) {
			for (Order order : orders) {
				ordersDto.add(DTOMapperI.MAPPER.mapOrderToDTO(order));
			}
		}

		// Retornar lista DTO
		return ordersDto;
	}

	@Override
	public List<OrderDTO> searchByUser(UserDTO userDto) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar pedidos por usuario");

		// Validar parametro
		ValidateParams.isNullObject(userDto.userId());

		// Buscar por usuario
		final List<Order> orders = orderRepository.findByUser(DTOMapperI.MAPPER.mapDTOToUser(userDto));

		// Mapear DTO
		final List<OrderDTO> ordersDto = new ArrayList<>();

		if (!orders.isEmpty()) {
			for (Order order : orders) {
				ordersDto.add(DTOMapperI.MAPPER.mapOrderToDTO(order));
			}
		}

		// Retornar lista DTO
		return ordersDto;
	}

	@Override
	public List<OrderDTO> searchByOrderIdOrOrderNumber(Long orderId, String orderNumber) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar por id o numero de pedido");

		// Validar parametros
		ValidateParams.isNullObject(orderId);
		ValidateParams.isNullObject(orderNumber);

		// Buscar por id o numero de pedido
		final List<Order> orders = orderRepository.findByOrderIdOrOrderNumber(orderId, orderNumber);

		// Mapear DTO
		final List<OrderDTO> ordersDto = new ArrayList<>();

		if (!orders.isEmpty()) {
			for (Order order : orders) {
				ordersDto.add(DTOMapperI.MAPPER.mapOrderToDTO(order));
			}
		}

		// Retornar lista DTO
		return ordersDto;
	}

}
