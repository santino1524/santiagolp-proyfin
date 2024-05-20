package com.ntd.persistence;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase Producto
 * 
 * @author SLP
 */
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "T_PRODUCT")
public class Product implements Serializable {

	/** Serial Version */
	private static final long serialVersionUID = -7965646309397013032L;

	/** Identificador (PK) */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "C_PRODUCT_ID")
	private Long productId;

	/** Numero de producto */
	@Column(name = "C_PRODUCT_NUMBER", nullable = false, unique = true)
	private String productNumber;

	/** Nombre del producto */
	@Column(name = "C_PRODUCT_NAME", nullable = false)
	private String productName;

	/** Descripcion del producto */
	@Column(name = "C_PRODUCT_DESCRIPTION")
	private String productDescription;

	/** Categoria del producto */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "C_CATEGORY_ID", nullable = false)
	private ProductCategory productCategory;

	/** Talla del producto */
	@Column(name = "C_PRODUCT_SIZE", nullable = false)
	private String productSize;

	/** Cantidad de productos */
	@Column(name = "C_PRODUCT_QUANTITY", nullable = false)
	private int productQuantity;

	/** Imagenes */
	@ElementCollection
	@Cascade(value = { CascadeType.ALL })
	@CollectionTable(name = "T_IMAGES", joinColumns = @JoinColumn(name = "C_PRODUCT_ID"))
	@Column(name = "C_IMAGE", columnDefinition = "TEXT", nullable = false)
	private List<String> images;

	/** IVA */
	@Column(name = "C_IVA", nullable = false)
	private BigDecimal iva;

	/** Precio Base */
	@Column(name = "C_BASE_PRICE", nullable = false)
	private BigDecimal basePrice;

	/** Precio PVP */
	@Column(name = "C_PVP_PRICE", nullable = false)
	private BigDecimal pvpPrice;

	/** Productos vendidos FK */
	@OneToMany(mappedBy = "product")
	private List<ProductSold> soldProducts;

	/** Critica de producto */
	@OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
	@Cascade(CascadeType.ALL)
	private List<ProductReview> reviews;

}
