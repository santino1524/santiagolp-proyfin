package com.ntd.services;

import java.util.List;

import com.ntd.dto.ProductCategoryDTO;
import com.ntd.dto.ProductDTO;
import com.ntd.dto.ProductSoldDTO;
import com.ntd.exceptions.DeleteFilesException;
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
	 * @throws DeleteFilesException
	 */
	@Transactional
	public void deleteProduct(final Long id) throws InternalException, DeleteFilesException;

	/**
	 * Eliminar imagenes
	 * 
	 * @param id
	 * @throws InternalException
	 * @throws DeleteFilesException
	 */
	@Transactional
	public void deleteImages(final Long id) throws InternalException, DeleteFilesException;

	/**
	 * Buscar todos los productos
	 * 
	 * @return List
	 * @throws InternalException
	 */
	public List<ProductDTO> searchAll() throws InternalException;

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
	 * Buscar productos por categoria ordenados descendentemente por el precio
	 * 
	 * @param productCategoryDto
	 * @return List
	 * @throws InternalException
	 */
	public List<ProductDTO> searchByCategoryOrderPvpPriceDesc(final ProductCategoryDTO productCategoryDto)
			throws InternalException;

	/**
	 * Buscar productos por categoria ordenados ascendentemente por el precio
	 * 
	 * @param productCategoryDto
	 * @return List
	 * @throws InternalException
	 */
	public List<ProductDTO> searchByCategoryOrderPvpPriceAsc(final ProductCategoryDTO productCategoryDto)
			throws InternalException;

	/**
	 * Buscar productos por nombre y la categoria y ordenar por precio Desc
	 * 
	 * @param productName
	 * @param productCategoryDto
	 * @return List
	 * @throws InternalException
	 */
	public List<ProductDTO> searchByNameAndProductCategoryOrderDesc(final String productName,
			final ProductCategoryDTO productCategoryDto) throws InternalException;

	/**
	 * Buscar productos por nombre y la categoria
	 * 
	 * @param productName
	 * @param productCategoryDto
	 * @return List
	 * @throws InternalException
	 */
	public List<ProductDTO> searchByNameAndProductCategory(final String productName,
			final ProductCategoryDTO productCategoryDto) throws InternalException;

	/**
	 * Buscar productos por nombre y la categoria y ordenar por precio Asc
	 * 
	 * @param productName
	 * @param productCategoryDto
	 * @return List
	 * @throws InternalException
	 */
	public List<ProductDTO> searchByNameAndProductCategoryOrderAsc(final String productName,
			final ProductCategoryDTO productCategoryDto) throws InternalException;

	/**
	 * Buscar productos ordenados descendentemente por el precio
	 * 
	 * @return List
	 * @throws InternalException
	 */
	public List<ProductDTO> searchAllOrderPvpPriceDesc() throws InternalException;

	/**
	 * Buscar productos ordenados ascendentemente por el precio
	 * 
	 * @return List
	 * @throws InternalException
	 */
	public List<ProductDTO> searchAllOrderPvpPriceAsc() throws InternalException;

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
	 * @throws DeleteFilesException
	 * @throws InternalException
	 */
	public void removeImages(final List<String> imagesUrls) throws DeleteFilesException, InternalException;

	/**
	 * Agregar ruta base a cada archivo
	 * 
	 * @param fileNames
	 * @throws InternalException
	 */
	public void addRoutes(final List<String> fileNames) throws InternalException;

}
