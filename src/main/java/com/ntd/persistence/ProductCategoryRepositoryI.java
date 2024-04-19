package com.ntd.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio T_PRODUCT_CATEGORY
 * 
 * @author SLP
 */
public interface ProductCategoryRepositoryI extends JpaRepository<ProductCategory, Long> {

	/**
	 * Buscar por nombre
	 * 
	 * @param categoryName
	 * @return ProductCategory
	 */
	public ProductCategory findByCategoryNameIgnoreCase(String categoryName);

	/**
	 * Comprobar existencia por nombre
	 * 
	 * @param categoryName
	 * @return boolean
	 */
	public boolean existsByCategoryNameIgnoreCase(String categoryName);

}
