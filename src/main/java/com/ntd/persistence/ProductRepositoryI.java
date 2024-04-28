package com.ntd.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;

/**
 * Repositorio T_PRODUCT
 * 
 * @author SLP
 */
public interface ProductRepositoryI extends JpaRepository<Product, Long> {

	/**
	 * Buscar producto por categoria
	 * 
	 * @param productCategory
	 * @return List
	 */
	public List<Product> findByProductCategory(ProductCategory productCategory);

	/**
	 * Contar las ocurrencias de productos de una categoria
	 * 
	 * @param productCategory
	 * @return long
	 */
	public long countByProductCategory(ProductCategory productCategory);

	/**
	 * Buscar por nombre de producto
	 * 
	 * @param productName
	 * @return List
	 */
	public List<Product> findByProductNameIgnoreCaseContaining(String productName);

	/**
	 * Buscar productos por el nombre y ordenar por precio DESC
	 * 
	 * @param productName
	 * @return List
	 */
	public List<Product> findByProductNameIgnoreCaseContainingOrderByPvpPriceDesc(String productName);

	/**
	 * Buscar productos por el nombre y ordenar por precio ASC
	 * 
	 * @param productName
	 * @return List
	 */
	public List<Product> findByProductNameIgnoreCaseContainingOrderByPvpPriceAsc(String productName);

	/**
	 * Buscar productos por la categoria y ordenar por precio DESC
	 * 
	 * @param productCategory
	 * @return List
	 */
	public List<Product> findByProductCategoryOrderByPvpPriceDesc(ProductCategory productCategory);

	/**
	 * Buscar productos por la categoria y ordenar por precio ASC
	 * 
	 * @param productCategory
	 * @return List
	 */
	public List<Product> findByProductCategoryOrderByPvpPriceAsc(ProductCategory productCategory);

	/**
	 * Buscar productos por nombre y la categoria y ordenar por precio Desc
	 * 
	 * @param productName
	 * @param productCategory
	 * @return List
	 */
	public List<Product> findByProductNameIgnoreCaseContainingAndProductCategoryOrderByPvpPriceDesc(String productName,
			ProductCategory productCategory);

	/**
	 * Buscar productos por nombre y la categoria
	 * 
	 * @param productName
	 * @param productCategory
	 * @return List
	 */
	public List<Product> findByProductNameIgnoreCaseContainingAndProductCategory(String productName,
			ProductCategory productCategory);

	/**
	 * Buscar productos por nombre y la categoria y ordenar por precio Asc
	 * 
	 * @param productName
	 * @param productCategory
	 * @return List
	 */
	public List<Product> findByProductNameIgnoreCaseContainingAndProductCategoryOrderByPvpPriceAsc(String productName,
			ProductCategory productCategory);

	/**
	 * Buscar productos y ordenar por precio DESC
	 * 
	 * @return List
	 */
	public List<Product> findAllByOrderByPvpPriceDesc();

	/**
	 * Buscar productos y ordenar por precio ASC
	 * 
	 * @return List
	 */
	public List<Product> findAllByOrderByPvpPriceAsc();

	/**
	 * Buscar por numero de producto
	 * 
	 * @param productNumber
	 * @return Product
	 */
	public Product findByProductNumber(String productNumber);

	/**
	 * Buscar imagenes por id
	 * 
	 * @param id
	 * @return Object[]
	 */
	@Query(value = "SELECT i.C_IMAGE FROM T_IMAGES i WHERE i.C_PRODUCT_ID = :id", nativeQuery = true)
	public Object[] findImagesById(Long id);

	/**
	 * Comprobar existencia por numero de producto
	 * 
	 * @param productNumber
	 * @return boolean
	 */
	public boolean existsByProductNumber(String productNumber);

	/**
	 * Comprobar existencia por nombre de producto
	 * 
	 * @param productName
	 * @return boolean
	 */
	public boolean existsByProductNameIgnoreCase(String productName);

	/**
	 * Buscar por numero de producto y nombre
	 * 
	 * @param productName
	 * @param productNumber
	 * @return List
	 */
	public List<Product> findByProductNameIgnoreCaseOrProductNumber(String productName, String productNumber);

	/**
	 * Buscar las imagenes
	 * 
	 * @param productId
	 * @return List
	 */
	@Query(value = "SELECT C_IMAGE_URL FROM T_IMAGES_URL WHERE C_PRODUCT_ID = :productId", nativeQuery = true)
	public List<String> findImageUrlsByProductId(Long productId);

	/**
	 * Eliminar imagenes
	 * 
	 * @param productId
	 * @return List
	 */
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM T_IMAGES WHERE C_PRODUCT_ID = :productId", nativeQuery = true)
	public void deleteImagesByProductId(Long productId);

	/**
	 * Metodo para guardar o actualizar un producto
	 * 
	 * @param product
	 * @return Product
	 */
	@SuppressWarnings("unchecked")
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	public Product save(Product product);

}
