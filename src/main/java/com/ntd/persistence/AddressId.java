package com.ntd.persistence;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase ID para T_POSTAL_ADDRESS
 * 
 * @author SLP
 */
@EqualsAndHashCode
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class AddressId implements Serializable {

	/** Serial Version */
	private static final long serialVersionUID = 1L;

	/** Linea de direccion */
	@Column(name = "C_DIRECTION_LINE", nullable = false)
	private String directionLine;

	/** Ciudad */
	@Column(name = "C_CITY", nullable = false)
	private String city;

	/** Provincia */
	@Column(name = "C_PROVINCE", nullable = false)
	private String province;
}
