package com.ntd.utils;

import java.util.List;

import com.ntd.dto.ProductSoldDTO;
import com.ntd.exceptions.InternalException;

import lombok.extern.slf4j.Slf4j;

/**
 * Clase de validacion de parametros
 * 
 * @author SLP
 */
@Slf4j
public class ValidateParams {

	/** Constructor privado */
	private ValidateParams() {
	}

	/**
	 * Comprobar nulidad de objetos
	 * 
	 * @param obj
	 * @throws InternalException
	 */
	public static void isNullObject(final Object obj) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Validar Object");

		// Validar nulidad
		if (obj == null) {
			throw new InternalException();
		}
	}

	/**
	 * Validar productos a comprar
	 * 
	 * @param soldProductsDto
	 * @throws InternalException
	 */
	public static void validateProductsToBuy(List<ProductSoldDTO> soldProductsDto) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Validar productos a comprar");

		// Validar parametro
		ValidateParams.isNullObject(soldProductsDto);

		// Validar datos
		for (ProductSoldDTO productSoldDTO : soldProductsDto) {
			ValidateParams.isNullObject(productSoldDTO.productId());
			ValidateParams.isNullObject(productSoldDTO.quantity());
		}

	}
}
