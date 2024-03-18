package com.ntd.controllers.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ntd.dto.ProductCategoryDTO;
import com.ntd.exceptions.InternalException;
import com.ntd.services.ProductCategoryMgmtServiceI;
import com.ntd.utils.Constants;
import com.ntd.utils.ValidateParams;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador de Category
 * 
 * @author SLP
 */
@Slf4j
@RestController
@RequestMapping("/rest/category")
public class ProductCategoryControllerRest {

	/** Dependencia del servicio de gestion de categorias */
	private final ProductCategoryMgmtServiceI categoryMgmtService;

	/**
	 * Constructor
	 * 
	 * @param categoryMgmtService
	 */
	public ProductCategoryControllerRest(final ProductCategoryMgmtServiceI categoryMgmtService) {
		this.categoryMgmtService = categoryMgmtService;
	}

	/**
	 * Registrar Category
	 * 
	 * @param category
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@PostMapping
	public ResponseEntity<String> saveCategory(@RequestBody @Valid final ProductCategoryDTO categoryDto)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Registrar category");

		ResponseEntity<String> result = null;

		// Guardar category
		if (categoryMgmtService.insertProductCategory(categoryDto) != null) {
			result = ResponseEntity.status(HttpStatus.ACCEPTED).body(Constants.MSG_SUCCESSFUL_OPERATION);
		} else {
			result = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Constants.MSG_UNEXPECTED_ERROR);
		}

		return result;
	}

	/**
	 * Eliminar category
	 * 
	 * @param categoryDto
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@Transactional
	@DeleteMapping
	public ResponseEntity<String> deleteCategory(@RequestBody @NotNull final ProductCategoryDTO categoryDto)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Eliminar category");

		// Validar id
		ValidateParams.isNullObject(categoryDto.categoryId());

		// Eliminar category
		categoryMgmtService.deleteProductCategory(categoryDto.categoryId());

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(Constants.MSG_SUCCESSFUL_OPERATION);
	}

	/**
	 * Buscar todos los category
	 * 
	 * @return List
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchAll")
	public List<ProductCategoryDTO> showReport() throws InternalException {
		if (log.isInfoEnabled())
			log.info("Mostrar todos las categorias");

		// Retornar lista de category
		return categoryMgmtService.searchAll();
	}

	/**
	 * Buscar por id
	 * 
	 * @param id
	 * @return ProductCategoryDTO
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchById")
	public ProductCategoryDTO searchById(@RequestParam @NotNull final Long id) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar category por id");

		// Retornar category
		return categoryMgmtService.searchById(id);
	}

}
