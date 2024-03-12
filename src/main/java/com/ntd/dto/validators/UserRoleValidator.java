package com.ntd.dto.validators;

import com.ntd.utils.Constants;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Clase Validador asociado a @ValidUserRol
 * 
 * @author SLP
 */
public class UserRoleValidator implements ConstraintValidator<ValidUserRol, String> {

	@Override
	public void initialize(ValidUserRol constraintAnnotation) {
		// Este metodo no requiere inicializacion especifica.
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return value != null && Constants.getRolesUser().contains(value.toUpperCase());
	}
}
