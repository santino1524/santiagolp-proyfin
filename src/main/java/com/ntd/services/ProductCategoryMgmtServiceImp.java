package com.ntd.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ntd.dto.ProductCategoryDTO;
import com.ntd.dto.mapper.DTOMapperI;
import com.ntd.exceptions.InternalException;
import com.ntd.persistence.ProductCategory;
import com.ntd.persistence.ProductCategoryRepositoryI;
import com.ntd.utils.ValidateParams;

import lombok.extern.slf4j.Slf4j;

/**
 * Servicio de gestion de Categorias de productos
 * 
 * @author SLP
 */
@Service
@Slf4j
public class ProductCategoryMgmtServiceImp implements ProductCategoryMgmtServiceI {

	/** Dependencia de ProductCategory */
	private final ProductCategoryRepositoryI categoryRepository;

	/**
	 * Constructor
	 * 
	 * @param categoryRepository
	 */
	public ProductCategoryMgmtServiceImp(final ProductCategoryRepositoryI categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Override
	public ProductCategoryDTO insertProductCategory(ProductCategoryDTO productCategoryDto) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Insertar categoria");

		// Validar parametro
		ValidateParams.isNullObject(productCategoryDto);

		// Mapear DTO y guardar
		final ProductCategory productCategory = categoryRepository
				.save(DTOMapperI.MAPPER.mapDTOtoProductCategory(productCategoryDto));

		// Retornar DTO
		return DTOMapperI.MAPPER.mapProductCategoryToDTO(productCategory);
	}

	@Override
	public void deleteProductCategory(Long id) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Eliminar Categoria");

		// Validar parametro
		ValidateParams.isNullObject(id);

		// Eliminar por Id
		categoryRepository.deleteById(id);
	}

	@Override
	public List<ProductCategoryDTO> searchAll() {
		if (log.isInfoEnabled())
			log.info("Buscar todas las Categorias");

		// Buscar todos los ProductCategory
		final List<ProductCategory> categories = categoryRepository.findAll();

		// Mapear DTO
		final List<ProductCategoryDTO> categoriesDto = new ArrayList<>();

		if (!categories.isEmpty()) {
			for (ProductCategory productCategory : categories) {
				categoriesDto.add(DTOMapperI.MAPPER.mapProductCategoryToDTO(productCategory));
			}
		}

		// Retornar lista DTO
		return categoriesDto;
	}

	@Override
	public ProductCategoryDTO searchById(Long id) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar Categoria por id");

		// Validar parametro
		ValidateParams.isNullObject(id);

		// Buscar por id
		final ProductCategory productCategory = categoryRepository.findById(id).orElse(null);

		// Retornar DTO
		return DTOMapperI.MAPPER.mapProductCategoryToDTO(productCategory);
	}

	@Override
	public ProductCategoryDTO searchByName(String categoryName) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar Categoria por nombre");

		// Validar parametro
		ValidateParams.isNullObject(categoryName);

		// Buscar por id
		final ProductCategory productCategory = categoryRepository.findByCategoryNameIgnoreCase(categoryName);

		// Retornar DTO
		return DTOMapperI.MAPPER.mapProductCategoryToDTO(productCategory);

	}

	@Override
	public boolean existsCategoryName(String categoryName) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Comprobar existencia de categoria");

		// Validar parametro
		ValidateParams.isNullObject(categoryName);

		// Retornar si existe la categoria
		return categoryRepository.existsByCategoryNameIgnoreCase(categoryName);
	}

}
