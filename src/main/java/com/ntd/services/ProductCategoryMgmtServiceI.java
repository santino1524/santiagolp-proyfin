package com.ntd.services;

import java.util.List;

import com.ntd.dto.ProductCategoryDTO;
import com.ntd.exceptions.InternalException;

/**
 * Servicio de gestion de Categorias de productos
 * 
 * @author SLP
 */
public interface ProductCategoryMgmtServiceI {

	/**
	 * Insertar nueva Categoria
	 * 
	 * @param productCategoryDto
	 * @return ProductCategoryDTO
	 * @throws InternalException
	 */
	public ProductCategoryDTO insertProductCategory(final ProductCategoryDTO productCategoryDto)
			throws InternalException;

	/**
	 * Eliminar Categoria
	 * 
	 * @param id
	 * @throws InternalException
	 */
	public void deleteProductCategory(final Long id) throws InternalException;

	/**
	 * Buscar todas las Categorias
	 * 
	 * @return List
	 */
	public List<ProductCategoryDTO> searchAll();

	/**
	 * Buscar Categoria por id
	 * 
	 * @param id
	 * @return ProductCategoryDTO
	 * @throws InternalException
	 */
	public ProductCategoryDTO searchById(final Long id) throws InternalException;

}
