package com.ntd.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio T_PRODUCT_SOLD
 * 
 * @author SLP
 */
public interface ProductSoldRepositoryI extends JpaRepository<ProductSold, Long> {

	/**
	 * Buscar productos vendidos por producto
	 * 
	 * @param product
	 * @return List
	 */
	public List<ProductSold> findByProduct(Product product);
}
