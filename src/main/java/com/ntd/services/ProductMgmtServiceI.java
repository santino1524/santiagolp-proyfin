package com.ntd.services;

import java.util.List;

import com.ntd.dto.ProductCategoryDTO;
import com.ntd.dto.ProductDTO;
import com.ntd.dto.ProductSoldDTO;
import com.ntd.exceptions.InternalException;

import jakarta.transaction.Transactional;

/**
 * Servicio de gestion de productos
 * 
 * @author SLP
 */
public interface ProductMgmtServiceI {

	/**
	 * Insertar nuevo producto
	 * 
	 * @param productDto
	 * @return ProductDTO
	 * @throws InternalException
	 */
	@Transactional
	public ProductDTO insertProduct(final ProductDTO productDto) throws InternalException;

	/**
	 * Actualizar nuevo producto
	 * 
	 * @param productDto
	 * @return ProductDTO
	 * @throws InternalException
	 */
	@Transactional
	public ProductDTO updateProduct(final ProductDTO productDto) throws InternalException;

	/**
	 * Eliminar producto
	 * 
	 * @param id
	 * @throws InternalException
	 */
	@Transactional
	public void deleteProduct(final Long id) throws InternalException;

	/**
	 * Eliminar imagenes
	 * 
	 * @param id
	 * @throws InternalException
	 */
	@Transactional
	public void deleteImages(final Long id) throws InternalException;

	/**
	 * Buscar todos los productos
	 * 
	 * @return List
	 */
	public List<ProductDTO> searchAll();

	/**
	 * Buscar productos por categoria
	 * 
	 * @param productCategoryDto
	 * @return List
	 * @throws InternalException
	 */
	public List<ProductDTO> searchByCategory(final ProductCategoryDTO productCategoryDto) throws InternalException;

	/**
	 * Buscar productos por coincidencia en el nombre
	 * 
	 * @param productName
	 * @return List
	 * @throws InternalException
	 */
	public List<ProductDTO> searchByName(final String productName) throws InternalException;

	/**
	 * Buscar productos por nombre ordenados descendentemente por el precio
	 * 
	 * @param productName
	 * @return List
	 * @throws InternalException
	 */
	public List<ProductDTO> searchByNameOrderPvpPriceDesc(final String productName) throws InternalException;

	/**
	 * Buscar productos por nombre ordenados ascendentemente por el precio
	 * 
	 * @param productName
	 * @return List
	 * @throws InternalException
	 */
	public List<ProductDTO> searchByNameOrderPvpPriceAsc(final String productName) throws InternalException;

	/**
	 * Buscar por numero de producto
	 * 
	 * @param productNumber
	 * @return ProductDTO
	 * @throws InternalException
	 */
	public ProductDTO searchByProductNumber(final String productNumber) throws InternalException;

	/**
	 * Comprobar existencia por numero de producto
	 * 
	 * @param productNumber
	 * @return boolean
	 * @throws InternalException
	 */
	public boolean existsByProductName(final String productName) throws InternalException;

	/**
	 * Comprobar existencia por nombre de producto
	 * 
	 * @param productName
	 * @return boolean
	 * @throws InternalException
	 */
	public boolean existsProductName(final String productName) throws InternalException;

	/**
	 * Buscar por numero de producto y nombre
	 * 
	 * @param productName
	 * @param productNumber
	 * @return List
	 * @throws InternalException
	 */
	public List<ProductDTO> searchByProductNameOrProductNumber(final String productName, final String productNumber)
			throws InternalException;

	/**
	 * Confirmar disponibilidad de producto
	 * 
	 * @param productsDtoToBuy
	 * @return List
	 * @throws InternalException
	 */
	@Transactional
	public List<ProductDTO> confirmOrder(final List<ProductSoldDTO> productsDtoToBuy) throws InternalException;

	/**
	 * Eliminar ficheros
	 * 
	 * @param imagesUrls
	 */
	public void removeImages(List<String> imagesUrls);

}
