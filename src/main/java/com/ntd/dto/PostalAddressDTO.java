package com.ntd.dto;

import java.util.List;

import com.ntd.persistence.User;
import com.ntd.utils.Constants;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

/**
 * DTO Direccion postal
 * 
 * @author SLP
 */
public record PostalAddressDTO(Long addressId,
		// Validar codigo postal
		@NotBlank(message = Constants.MSG_CP_NOT_VALID) @Pattern(regexp = Constants.REGEXP_CP, message = Constants.MSG_CP_NOT_VALID) String cp,

		// Validar direccion
		@NotBlank(message = Constants.MSG_DIRECTION_NOT_VALID) String directionLine,

		// Validar ciudad
		@NotBlank(message = Constants.MSG_CITY_NOT_VALID) String city,

		// Validar provincia
		@NotBlank(message = Constants.MSG_PROVINCE_NOT_VALID) String province,

		// Validar pais
		@NotBlank(message = Constants.MSG_PROVINCE_NOT_VALID) String country,

		@NotEmpty(message = Constants.MSG_USER_NOT_VALID) List<User> users

) {

}
