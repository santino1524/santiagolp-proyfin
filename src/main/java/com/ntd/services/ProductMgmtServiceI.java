package com.ntd.services;

import java.sql.SQLException;
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
	 * @throws SQLException
	 */
	@Transactional
	public ProductDTO insertProduct(final ProductDTO productDto) throws InternalException, SQLException;

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
	 * Generar etiqueta de envio
	 * 
	 * @param productId
	 * @return byte[]
	 * @throws InternalException
	 */
	public byte[] generateLabel(final Long productId) throws InternalException;

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
	 * @param onlyCheck
	 * @return List
	 * @throws InternalException
	 */
	public List<ProductDTO> confirmOrder(final List<ProductSoldDTO> productsDtoToBuy, final boolean onlyCheck)
			throws InternalException;

	/**
	 * Contar las ocurrencias de productos de una categoria
	 * 
	 * @param productCategory
	 * @return long
	 * @throws InternalException
	 */
	public long countByProductCategory(ProductCategoryDTO productCategoryDto) throws InternalException;

	/**
	 * Buscar producto por id
	 * 
	 * @param id
	 * @return ProductDTO
	 * @throws InternalException
	 */
	public ProductDTO searchById(final Long id) throws InternalException;

	/**
	 * Buscar productos por ids
	 * 
	 * @param ids
	 * @return ProductDTO
	 * @throws InternalException
	 */
	public List<ProductDTO> searchByIds(final List<Long> ids) throws InternalException;

}
