package com.ntd.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ntd.dto.OrderDTO;
import com.ntd.dto.mapper.DTOMapperI;
import com.ntd.dto.validators.OrderStatusValidator;
import com.ntd.exceptions.InternalException;
import com.ntd.persistence.Order;
import com.ntd.persistence.OrderRepositoryI;
import com.ntd.persistence.ProductSold;
import com.ntd.persistence.ProductSoldRepositoryI;
import com.ntd.persistence.User;
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

	/**
	 * Constructor
	 * 
	 * @param orderRepository
	 * @param productSoldRepository
	 */
	public OrderMgmtServiceImp(OrderRepositoryI orderRepository, ProductSoldRepositoryI productSoldRepository) {
		this.orderRepository = orderRepository;
		this.productSoldRepository = productSoldRepository;
	}

	/** Dependencia de ProductSoldRepository */
	private final ProductSoldRepositoryI productSoldRepository;

	@Override
	public ResponseEntity<Object> insertOrder(OrderDTO orderDto) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Insertar pedido");

		// Validar parametro
		ValidateParams.isNullObject(orderDto);

		// Extraer lista de productos vendidos
		List<ProductSold> soldProducts = (DTOMapperI.MAPPER.dtoToListProductSold(orderDto.soldProductsDto()));

		// Vaciar lista de productos vendidos para guardar pedido
		Order order = DTOMapperI.MAPPER.mapDTOToOrder(orderDto);
		order.getSoldProducts().clear();

		// Mapear DTO y guardar
		order = orderRepository.save(order);

		// Agregar order a soldProducts
		for (ProductSold productSold : soldProducts) {
			productSold.setOrder(order);
		}

		// Guardar lista de productos vendidos
		List<ProductSold> soldProductsRegistered = productSoldRepository.saveAll(soldProducts);

		ResponseEntity<Object> result;
		if (order != null && soldProductsRegistered.size() == soldProducts.size()) {
			// Retornar 200 para operacion exitosa
			result = ResponseEntity.ok().build();
		} else {
			// Retornar 500 para error
			result = ResponseEntity.internalServerError().build();
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
	public List<OrderDTO> searchByUser(Long userId) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar pedidos por usuario");

		// Validar parametro
		ValidateParams.isNullObject(userId);

		// Buscar por usuario
		final List<Order> orders = orderRepository.findByUser(
				new User(userId, null, null, null, null, null, null, null, null, false, null, null, null, null, null));

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

	@Override
	public List<OrderDTO> searchByUserOrderDateDesc(Long userId) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar pedidos por usuario por fecha desc");

		// Validar parametro
		ValidateParams.isNullObject(userId);

		// Buscar por usuario
		final List<Order> orders = orderRepository.findByUserOrderByOrderDateDesc(
				new User(userId, null, null, null, null, null, null, null, null, false, null, null, null, null, null));

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
	public OrderDTO searchTopByUser(Long userId) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar ultimo pedido del usuario");

		// Validar parametro
		ValidateParams.isNullObject(userId);

		// Buscar por usuario
		final Order order = orderRepository.findTopByUserOrderByOrderDateDesc(
				new User(userId, null, null, null, null, null, null, null, null, false, null, null, null, null, null));

		// Retornar DTO
		return DTOMapperI.MAPPER.mapOrderToDTO(order);
	}

	@Override
	public int countByStatusEquals(String status) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar cantidad de pedidos creados");

		// Validar parametro
		ValidateParams.isNullObject(status);

		return orderRepository.countByStatusEquals(status);
	}

	@Override
	public List<Order> findByStatusEquals(String status) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar los pedidos en estado 'CREADO'");

		// Validar parametro
		ValidateParams.isNullObject(status);

		return orderRepository.findByStatusEqualsOrderByOrderDateAsc(status);
	}

	@Override
	public Order searchById(Long id) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar pedido por id");

		// Validar parametro
		ValidateParams.isNullObject(id);

		// Retornar DTO
		return orderRepository.findById(id).orElse(null);
	}

}
