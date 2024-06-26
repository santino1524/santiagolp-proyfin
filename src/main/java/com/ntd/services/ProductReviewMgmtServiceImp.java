package com.ntd.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ntd.dto.ProductReviewDTO;
import com.ntd.dto.mapper.DTOMapperI;
import com.ntd.exceptions.InternalException;
import com.ntd.persistence.Product;
import com.ntd.persistence.ProductReview;
import com.ntd.persistence.ProductReviewRepositoryI;
import com.ntd.persistence.User;
import com.ntd.utils.ValidateParams;

import lombok.extern.slf4j.Slf4j;

/**
 * Servicio de gestion de ProductReview
 * 
 * @author SLP
 */
@Service
@Slf4j
public class ProductReviewMgmtServiceImp implements ProductReviewMgmtServiceI {

	/** Dependencia de ProductReviewRepository */
	private final ProductReviewRepositoryI productReviewRepository;

	/**
	 * Constructor
	 * 
	 * @param productReviewRepository
	 */
	public ProductReviewMgmtServiceImp(final ProductReviewRepositoryI productReviewRepository) {
		this.productReviewRepository = productReviewRepository;
	}

	@Override
	public ProductReviewDTO insertProductReview(ProductReviewDTO productReviewDto) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Insertar ProductReview");

		// Validar parametro
		ValidateParams.isNullObject(productReviewDto);

		// Mapear DTO y guardar
		final ProductReview productReview = productReviewRepository
				.save(DTOMapperI.MAPPER.mapDTOToProductReview(productReviewDto));

		// Retornar DTO
		return DTOMapperI.MAPPER.mapProductReviewToDTO(productReview);
	}

	@Override
	public List<ProductReviewDTO> searchAll() {
		if (log.isInfoEnabled())
			log.info("Buscar todos los ProductReview");

		// Buscar todos los ProductReview
		final List<ProductReview> reviews = productReviewRepository.findAll();

		// Mapear DTO
		final List<ProductReviewDTO> reviewsDto = new ArrayList<>();

		if (!reviews.isEmpty()) {
			for (ProductReview productReview : reviews) {
				reviewsDto.add(DTOMapperI.MAPPER.mapProductReviewToDTO(productReview));
			}
		}

		// Retornar lista DTO
		return reviewsDto;
	}

	@Override
	public void deleteProductReview(Long id) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Eliminar ProductReview");

		// Validar parametro
		ValidateParams.isNullObject(id);

		// Eliminar por Id
		productReviewRepository.deleteById(id);
	}

	@Override
	public ProductReviewDTO searchById(Long id) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar ProductReview por id");

		// Validar parametro
		ValidateParams.isNullObject(id);

		// Buscar por id
		final ProductReview productReview = productReviewRepository.findById(id).orElse(null);

		// Retornar DTO
		return DTOMapperI.MAPPER.mapProductReviewToDTO(productReview);
	}

	@Override
	public List<ProductReviewDTO> findByProduct(Product product) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar ProductReview por producto");

		// Validar parametro
		ValidateParams.isNullObject(product);

		// Buscar por producto
		final List<ProductReview> productReviewsDto = productReviewRepository.findByProduct(product);

		// Retornar DTO
		return DTOMapperI.MAPPER.listProductReviewToDTO(productReviewsDto);
	}

	@Override
	public ProductReviewDTO findByProductAndUser(Product product, User user) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar ProductReview por producto y usuario");

		// Validar parametro
		ValidateParams.isNullObject(product);
		ValidateParams.isNullObject(user);

		// Buscar por producto y usuario
		final ProductReview productReviewDto = productReviewRepository.findByProductAndUser(product, user);

		// Retornar DTO
		return DTOMapperI.MAPPER.mapProductReviewToDTO(productReviewDto);
	}

	@Override
	public int countByReportedEquals(boolean status) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Contar cantidad de resennas denunciadas");

		// Validar parametro
		ValidateParams.isNullObject(status);

		return productReviewRepository.countByReportedEquals(status);
	}

}
