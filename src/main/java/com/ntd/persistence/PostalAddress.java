package com.ntd.persistence;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
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
	@EmbeddedId
	private AddressId addressId;

	/** Codigo postal */
	@Column(name = "C_CP", nullable = false)
	private String cp;

	/** Pais */
	@Column(name = "C_COUNTRY")
	private String country;

	/** Usuarios FK */
	@ManyToMany(mappedBy = "addresses")
	private List<User> users;

}
