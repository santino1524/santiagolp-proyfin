package com.ntd.persistence;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Clase Producto vendido
 * 
 * @author SLP
 */
@Entity
@Setter
@Getter
@Table(name = "T_PRODUCT_SOLD")
public class ProductSold implements Serializable {

	/** Serial Version */
	private static final long serialVersionUID = 1L;

	/** Identificador (PK) */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "C_PRODUCT_SOLD_ID")
	private Long productSoldId;

	/** Pedido */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "C_ORDER_ID", nullable = false)
	private Order order;

	/** Producto */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "C_PRODUCT_ID", nullable = false)
	private Product product;

	/** Cantidad de productos vendidos */
	@Column(name = "C_QUANTITY_SOLD", nullable = false)
	private Integer quantitySold;
}
