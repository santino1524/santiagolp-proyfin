package com.ntd.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ntd.dto.ProductReviewDTO;
import com.ntd.dto.mapper.DTOMapperI;
import com.ntd.exceptions.InternalException;
import com.ntd.persistence.ProductReview;
import com.ntd.persistence.ProductReviewRepositoryI;
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

	/** Dependencia de ReportRepository */
	private final ProductReviewRepositoryI productReviewRepository;

	/**
	 * Constructor
	 * 
	 * @param reportRepository
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

}
