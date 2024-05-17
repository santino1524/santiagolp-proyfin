package com.ntd.services;

import java.util.List;

import com.ntd.dto.ProductReviewDTO;
import com.ntd.exceptions.InternalException;
import com.ntd.persistence.Product;

/**
 * Servicio de gestion de Criticas de productos
 * 
 * @author SLP
 */
public interface ProductReviewMgmtServiceI {

	/**
	 * Insertar nuevo ProductReview
	 * 
	 * @param productReviewDto
	 * @return ProductReviewDTO
	 * @throws InternalException
	 */
	public ProductReviewDTO insertProductReview(final ProductReviewDTO productReviewDto) throws InternalException;

	/**
	 * Eliminar ProductReview
	 * 
	 * @param id
	 * @throws InternalException
	 */
	public void deleteProductReview(final Long id) throws InternalException;

	/**
	 * Buscar todos los ProductReview
	 * 
	 * @return List
	 */
	public List<ProductReviewDTO> searchAll();

	/**
	 * Buscar el ProductReview por id
	 * 
	 * @param id
	 * @return ProductReviewDTO
	 * @throws InternalException
	 */
	public ProductReviewDTO searchById(final Long id) throws InternalException;

	/**
	 * Buscar resennas por producto
	 * 
	 * @param product
	 * @return List
	 * @throws InternalException
	 */
	public List<ProductReviewDTO> findByProduct(Product product) throws InternalException;

}
