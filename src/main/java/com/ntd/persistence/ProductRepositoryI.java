package com.ntd.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import jakarta.persistence.LockModeType;

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
	public List<Product> findByProductCategoryIgnoreCase(String productCategory);

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
	 * Buscar por numero de producto
	 * 
	 * @param productNumber
	 * @return Product
	 */
	public Product findByProductNumber(String productNumber);

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
	 * Metodo para guardar o actualizar un producto
	 * 
	 * @param product
	 * @return Product
	 */
	@SuppressWarnings("unchecked")
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	public Product save(Product product);

}
