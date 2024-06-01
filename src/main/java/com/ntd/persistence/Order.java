package com.ntd.persistence;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Tabla Pedido
 * 
 * @author SLP
 */
@Entity
@Setter
@Getter
@Table(name = "T_ORDER")
public class Order implements Serializable {

	/** Serial Version */
	private static final long serialVersionUID = -595996262186024802L;

	/** Identificador (PK) */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "C_ORDER_ID")
	private Long orderId;

	/** Numero de Pedido */
	@Column(name = "C_NUMBER_ORDER", length = 13, nullable = false, unique = true)
	private String orderNumber;

	/** Fecha del pedido */
	@Column(name = "C_ORDER_DATE", nullable = false)
	private LocalDateTime orderDate;

	/** Total del pedido */
	@Column(name = "C_TOTAL", nullable = false, precision = 12, scale = 2)
	private BigDecimal total;

	/** Estado del pedido */
	@Column(name = "C_STATUS", nullable = false, length = 25)
	private String status;

	/** Id de la transaccion */
	@Column(name = "C_TRANSACTION", nullable = false, length = 25)
	private String transactionId;

	/** Usuario */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "C_USER_ID", nullable = false)
	private User user;

	/** Lista de productos vendidos */
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<ProductSold> soldProducts;

	/** Direccion de envío */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "C_ADDRESS_ID", nullable = false)
	private PostalAddress shippingAddress;

}
