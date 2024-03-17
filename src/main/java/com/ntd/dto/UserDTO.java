package com.ntd.dto;

import java.util.List;

import com.ntd.dto.validators.ValidUserRol;
import com.ntd.utils.Constants;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * DTO Usuario
 * 
 * @author SLP
 */
public record UserDTO(Long userId,

		// Validar nombre y apellidos
		@NotBlank(message = Constants.MSG_NAME_NOT_VALID) @Pattern(regexp = Constants.REGEXP_NAMES, message = Constants.MSG_NAME_NOT_VALID) String name,
		@NotBlank(message = Constants.MSG_SURNAMES_NOT_VALID) @Pattern(regexp = Constants.REGEXP_NAMES, message = Constants.MSG_SURNAMES_NOT_VALID) String surname,
		@NotBlank(message = Constants.MSG_SURNAMES_NOT_VALID) @Pattern(regexp = Constants.REGEXP_NAMES, message = Constants.MSG_SURNAMES_NOT_VALID) String secondSurname,

		boolean blocked,

		// Validar DNI
		@NotBlank(message = Constants.MSG_DNI_NOT_VALID) @Pattern(regexp = Constants.REGEXP_DNI, message = Constants.MSG_DNI_NOT_VALID) String dni,

		// Validar email
		@NotBlank(message = Constants.MSG_EMAIL_NOT_VALID) @Email(message = Constants.MSG_EMAIL_NOT_VALID) String email,

		// Validar contrasena
		@NotBlank(message = Constants.MSG_PASSWD_NOT_VALID) @Pattern(regexp = Constants.REGEXP_PASSWD, message = Constants.MSG_PASSWD_NOT_VALID) String passwd,

		// Validar numero de telefono
		@NotBlank(message = Constants.MSG_PHONE_NUMBER_NOT_VALID) @Pattern(regexp = Constants.REGEXP_PHONE_NUMBER, message = Constants.MSG_PHONE_NUMBER_NOT_VALID) String phoneNumber,

		// Validar rol
		@ValidUserRol String role,

		List<PasswordResetQuestionDTO> questionsDto,

		List<PostalAddressDTO> addressesDto,

		List<OrderDTO> ordersDto) {
}
