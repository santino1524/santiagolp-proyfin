package com.ntd.dto;

import java.time.YearMonth;

import org.hibernate.validator.constraints.CreditCardNumber;

import com.ntd.utils.Constants;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * DTO Tarjeta bancaria
 * 
 * @author SLP
 */
public record CardDTO(Long cardId,

		// Validar nombre en la tarjeta
		@NotBlank(message = Constants.MSG_NAME_NOT_VALID) @Pattern(regexp = Constants.REGEXP_NAMES, message = Constants.MSG_NAME_NOT_VALID) String titular,

		// Validar numero de tarjeta
		@CreditCardNumber(message = Constants.MSG_CARD_NUMBER_NOT_VALID) String cardNumber,

		// Validar fecha de expiracion
		@NotNull(message = Constants.MSG_CARD_EXPIRATION_NOT_VALID) @Future(message = Constants.MSG_CARD_EXPIRATION_NOT_VALID) YearMonth expiration,

		boolean favorite,

		// Validar usuario
		@NotNull(message = Constants.MSG_USER_NOT_VALID) UserDTO userDto

) {

}
