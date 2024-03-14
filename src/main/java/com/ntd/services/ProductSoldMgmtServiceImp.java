package com.ntd.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ntd.dto.ProductSoldDTO;
import com.ntd.dto.mapper.DTOMapperI;
import com.ntd.exceptions.InternalException;
import com.ntd.persistence.ProductSold;
import com.ntd.persistence.ProductSoldRepositoryI;
import com.ntd.utils.ValidateParams;

import lombok.extern.slf4j.Slf4j;

/**
 * Servicio de gestion de productos vendidos
 * 
 * @author SLP
 */
@Slf4j
@Service
public class ProductSoldMgmtServiceImp implements ProductSoldMgmtServiceI {

	/** Dependencia de ProductSoldRepository */
	private final ProductSoldRepositoryI productSoldRepository;

	/**
	 * Constructor
	 * 
	 * @param productSoldRepository
	 */
	public ProductSoldMgmtServiceImp(final ProductSoldRepositoryI productSoldRepository) {
		this.productSoldRepository = productSoldRepository;
	}

	@Override
	public void validateProductsToBuy(List<ProductSoldDTO> soldProductsDto) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Validar productos a comprar");

		// Validar parametro
		ValidateParams.isNullObject(soldProductsDto);

		// Validar datos
		for (ProductSoldDTO productSoldDTO : soldProductsDto) {
			ValidateParams.isNullObject(productSoldDTO.productDto());
			ValidateParams.isNullObject(productSoldDTO.quantitySold());
		}

	}

	@Override
	public List<ProductSoldDTO> insertAllProductSold(List<ProductSoldDTO> soldProductsDto) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Insertar productos vendido");

		// Validar parametro
		ValidateParams.isNullObject(soldProductsDto);

		// Mapear DTO y guardar
		List<ProductSold> soldProducts = productSoldRepository
				.saveAllAndFlush(DTOMapperI.MAPPER.dtoToListProductSold(soldProductsDto));

		// Retornar DTO
		return DTOMapperI.MAPPER.listProductSoldToDTO(soldProducts);
	}
}
