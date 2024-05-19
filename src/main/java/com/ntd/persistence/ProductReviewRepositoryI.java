package com.ntd.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio T_PRODUCT_REVIEW
 * 
 * @author SLP
 */
public interface ProductReviewRepositoryI extends JpaRepository<ProductReview, Long> {

	/**
	 * Buscar resennas por producto
	 * 
	 * @param product
	 * @return List
	 */
	public List<ProductReview> findByProduct(Product product);

	/**
	 * Buscar resenna por producto y usuario
	 * 
	 * @param product
	 * @param user
	 * @return ProductReview
	 */
	public ProductReview findByProductAndUser(Product product, User user);
}
