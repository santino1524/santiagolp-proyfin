package com.ntd.persistence;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio T_ORDER
 * 
 * @author SLP
 */
public interface OrderRepositoryI extends JpaRepository<Order, Long> {

	/**
	 * Comprobar existencia por numero de pedido
	 * 
	 * @param orderNumber
	 * @return boolean
	 */
	public boolean existsByOrderNumber(String orderNumber);

	/**
	 * Buscar pedidos que esten entre dos fechas
	 * 
	 * @param startDate
	 * @param endDate
	 * @return List
	 */
	public List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);

	/**
	 * Ordenar pedidos por orden ASC
	 * 
	 * @return List
	 */
	public List<Order> findByOrderByOrderDateAsc();

	/**
	 * Ordenar pedidos por orden DESC
	 * 
	 * @return List
	 */
	public List<Order> findByOrderByOrderDateDesc();

	/**
	 * Buscar por numero de pedido
	 * 
	 * @param orderNumber
	 * @return Order
	 */
	public Order findByOrderNumber(String orderNumber);

	/**
	 * Buscar pedidos por estado
	 * 
	 * @param status
	 * @return List
	 */
	public List<Order> findByStatusIgnoreCase(String status);

	/**
	 * Buscar pedidos por usuario
	 * 
	 * @param user
	 * @return List
	 */
	public List<Order> findByUser(User user);

	/**
	 * Buscar pedidos por usuario por fecha
	 * 
	 * @param user
	 * @return Order
	 */
	public List<Order> findByUserOrderByDateDesc(User user);

	/**
	 * Buscar ultimo pedido por usuario
	 * 
	 * @param user
	 * @return Order
	 */
	public Order findTopByUserOrderByDateDesc(User user);

	/**
	 * Buscar por id y numero de pedido
	 * 
	 * @param orderId
	 * @param orderNumber
	 * @return List
	 */
	public List<Order> findByOrderIdOrOrderNumber(Long orderId, String orderNumber);

}
