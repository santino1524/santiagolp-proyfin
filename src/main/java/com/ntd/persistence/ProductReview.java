package com.ntd.persistence;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
 * Clase Critica de producto
 * 
 * @author SLP
 */
@Entity
@Getter
@Setter
@Table(name = "T_PRODUCT_REVIEW")
public class ProductReview implements Serializable {

	/** Serial Version */
	private static final long serialVersionUID = 4095154141192932164L;

	/** Identificador (PK) */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "C_PRODUCT_REVIEW_ID")
	private Long productReviewId;

	/** Producto */
	@ManyToOne
	@JoinColumn(name = "C_PRODUCT_ID", nullable = false)
	private Product product;

	/** Usuario */
	@ManyToOne
	@JoinColumn(name = "C_USER_ID", nullable = false)
	private User user;

	/** Calificacion del producto */
	@Column(name = "C_RATING", nullable = false)
	private int rating;

	/** Comentario del usuario */
	@Column(name = "C_COMMENT")
	private String comment;

	/** Lista de reportes */
	@OneToMany(mappedBy = "review")
	private List<Report> reports;

	/** Indica si el comentario ha sido denunciado */
	@Column(name = "C_REPORTED", nullable = false)
	private boolean reported;
}
