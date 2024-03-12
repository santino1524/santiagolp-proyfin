package com.ntd.services;

import java.time.LocalDateTime;
import java.util.List;

import com.ntd.dto.OrderDTO;
import com.ntd.dto.UserDTO;
import com.ntd.exceptions.InternalException;

/**
 * Servicio de gestion de pedidos
 * 
 * @author SLP
 */
public interface OrderMgmtServiceI {

	/**
	 * Insertar nuevo pedido
	 * 
	 * @param orderDto
	 * @return OrderDTO
	 * @throws InternalException
	 */
	public OrderDTO insertOrder(final OrderDTO orderDto) throws InternalException;

	/**
	 * Actualizar pedido
	 * 
	 * @param orderId
	 * @param status
	 * @return
	 * @throws InternalException
	 */
	public OrderDTO updateOrderStatus(final Long orderId, final String status) throws InternalException;

	/**
	 * Eliminar pedido
	 * 
	 * @param id
	 * @throws InternalException
	 */
	public void deleteOrder(final Long id) throws InternalException;

	/**
	 * Buscar todos los pedidos
	 * 
	 * @return List
	 */
	public List<OrderDTO> searchAllOrders();

	/**
	 * Comprobar existencia por numero de pedido
	 * 
	 * @param orderNumber
	 * @return boolean
	 * @throws InternalException
	 */
	public boolean existsOrder(final String orderNumber) throws InternalException;

	/**
	 * Buscar pedidos que esten entre dos fechas
	 * 
	 * @param startDate
	 * @param endDate
	 * @return List
	 * @throws InternalException
	 */
	public List<OrderDTO> searchByOrderDateBetween(final LocalDateTime startDate, final LocalDateTime endDate)
			throws InternalException;

	/**
	 * Buscar pedidos por fecha por orden ASC
	 * 
	 * @return List
	 */
	public List<OrderDTO> searchByOrderByOrderDateAsc();

	/**
	 * Buscar pedidos por fecha por orden DESC
	 * 
	 * @return List
	 */
	public List<OrderDTO> searchByOrderByOrderDateDesc();

	/**
	 * Buscar por numero de pedido
	 * 
	 * @param orderNumber
	 * @return OrderDTO
	 * @throws InternalException
	 */
	public OrderDTO searchByOrderNumber(final String orderNumber) throws InternalException;

	/**
	 * Buscar pedidos por estado
	 * 
	 * @param status
	 * @return List
	 * @throws InternalException
	 */
	public List<OrderDTO> searchByStatus(final String status) throws InternalException;

	/**
	 * Buscar pedidos por usuario
	 * 
	 * @param userDto
	 * @return List
	 * @throws InternalException
	 */
	public List<OrderDTO> searchByUser(UserDTO userDto) throws InternalException;

	/**
	 * Buscar por id y numero de pedido
	 * 
	 * @param orderId
	 * @param orderNumber
	 * @return List
	 * @throws InternalException
	 */
	public List<OrderDTO> searchByOrderIdOrOrderNumber(final Long orderId, final String orderNumber)
			throws InternalException;

}
