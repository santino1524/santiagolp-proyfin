package com.ntd.dto.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ntd.utils.Constants;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Clase Anotacion personalizada para validar estados de pedidos
 * 
 * @author SLP
 */
@Documented
@Constraint(validatedBy = OrderStatusValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidOrderStatus {

	/**
	 * Mensaje de error
	 * 
	 * @return String
	 */
	public String message() default Constants.MSG_STATUS_NOT_VALID;

	/**
	 * Asignar la anotacion a uno o mas grupos de validacion
	 * 
	 * @return Class
	 */
	public Class<?>[] groups() default {};

	/**
	 * Asocia metadatos adicionales con la restriccion
	 * 
	 * @return Class
	 */
	public Class<? extends Payload>[] payload() default {};
}
