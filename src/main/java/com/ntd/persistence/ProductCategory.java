package com.ntd.persistence;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Clase Categoria de productos
 * 
 * @author SLP
 */
@Entity
@Setter
@Getter
@Table(name = "T_PRODUCT_CATEGORY")
public class ProductCategory implements Serializable {

	/** Serial Version */
	private static final long serialVersionUID = 3900678477330076906L;

	/** Identificador (PK) */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "C_CATEGORY_ID")
	private Long categoryId;

	/** Nombre de categoria */
	@Column(name = "C_CATEGORY_NAME")
	private String categoryName;

}