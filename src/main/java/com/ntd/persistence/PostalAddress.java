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
 * Clase Direccion postal
 * 
 * @author SLP
 */
@Entity
@Setter
@Getter
@Table(name = "T_POSTAL_ADDRESS")
public class PostalAddress implements Serializable {

	/** Serial Version */
	private static final long serialVersionUID = 3576682110116222618L;

	/** Identificador (PK) */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "C_ADDRESS_ID")
	private Long addressId;

	/** Linea de direccion */
	@Column(name = "C_DIRECTION_LINE", nullable = false)
	private String directionLine;

	/** Ciudad */
	@Column(name = "C_CITY", nullable = false)
	private String city;

	/** Provincia */
	@Column(name = "C_PROVINCE", nullable = false)
	private String province;

	/** Codigo postal */
	@Column(name = "C_CP", nullable = false)
	private String cp;

	/** Pais */
	@Column(name = "C_COUNTRY", nullable = false)
	private String country;

	/** Usuarios FK */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "C_USER_ID")
	private User user;

}
