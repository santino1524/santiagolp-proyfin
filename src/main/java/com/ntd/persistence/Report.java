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
 * Clase Producto
 * 
 * @author SLP
 */
@Entity
@Getter
@Setter
@Table(name = "T_REPORT")
public class Report implements Serializable {

	/** Serial Version */
	private static final long serialVersionUID = -8986203031939654575L;

	/** Identificador (PK) */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "C_REPORT_ID")
	private Long reportId;

	/** Critica de producto */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "C_PRODUCT_REVIEW_ID", nullable = false)
	private ProductReview review;

	/** Usuario que reporta */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "C_REPORTER_USER_ID", nullable = false)
	private User reporter;

	/** Es procesada */
	@Column(name = "C_PROCESSED", nullable = false)
	private boolean processed;

}
