package com.ntd.dto.validators;

import com.ntd.utils.Constants;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Clase Validador asociado a @ValidOrderStatus
 * 
 * @author SLP
 */
public class OrderStatusValidator implements ConstraintValidator<ValidOrderStatus, String> {

	@Override
	public void initialize(ValidOrderStatus constraintAnnotation) {
		// Este metodo no requiere inicializacion especifica.
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return value != null && Constants.getOrderStatuses().contains(value.toUpperCase());
	}
}
