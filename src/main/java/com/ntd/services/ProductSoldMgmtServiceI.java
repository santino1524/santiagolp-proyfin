package com.ntd.services;

import java.util.List;

import com.ntd.dto.ProductSoldDTO;
import com.ntd.exceptions.InternalException;

/**
 * Servicio de gestion de productos vendidos
 * 
 * @author SLP
 */
public interface ProductSoldMgmtServiceI {

	/**
	 * Validar productos a comprar
	 * 
	 * @param soldProductsDto
	 * @throws InternalException
	 */
	public void validateProductsToBuy(List<ProductSoldDTO> soldProductsDto) throws InternalException;

	/**
	 * Insertar nuevo producto vendido
	 * 
	 * @param soldProductsDto
	 * @return List
	 * @throws InternalException
	 */
	public List<ProductSoldDTO> insertAllProductSold(final List<ProductSoldDTO> soldProductsDto)
			throws InternalException;

}
