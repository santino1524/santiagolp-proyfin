package com.ntd.controllers;

import java.util.Collections;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ntd.dto.ProductCategoryDTO;
import com.ntd.exceptions.InternalException;
import com.ntd.services.ProductCategoryMgmtServiceI;
import com.ntd.utils.ValidateParams;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador de Categorias de productos
 * 
 * @author SLP
 */
@Slf4j
@Controller
@RequestMapping("/category")
public class ProductCategoryController {

	/** Dependencia del servicio de gategorias de productos */
	private final ProductCategoryMgmtServiceI categoryMgmtService;

	/**
	 * Constructor
	 * 
	 * @param categoryMgmtService
	 */
	public ProductCategoryController(final ProductCategoryMgmtServiceI categoryMgmtService) {
		this.categoryMgmtService = categoryMgmtService;
	}

	/**
	 * Registrar Categoria
	 * 
	 * @param categoryDto
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@PostMapping(path = "/save")
	public ResponseEntity<Object> saveCategory(@RequestBody @Valid final ProductCategoryDTO categoryDto)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Registrar Categoria");

		ResponseEntity<Object> result = null;

		// Guardar Categoria
		if (categoryMgmtService.existsCategoryName(categoryDto.categoryName())) {
			// Devolver una respuesta con codigo de estado 422
			result = ResponseEntity.unprocessableEntity().build();
		} else {
			ProductCategoryDTO savedCategoryDto = categoryMgmtService.insertProductCategory(categoryDto);

			if (savedCategoryDto.categoryId() != null) {
				// Devolver una respuesta con codigo de estado 200
				result = ResponseEntity.ok().body(Collections.singletonMap("productCategoryDto", savedCategoryDto));
			} else {
				// Devolver una respuesta con codigo de estado 500
				result = ResponseEntity.internalServerError().build();
			}
		}

		// Retornar respuesta
		return result;
	}

	/**
	 * Eliminar Categoria
	 * 
	 * @param categoryDto
	 * @param model
	 * @return String
	 * @throws InternalException
	 */
	@Transactional
	@DeleteMapping(path = "/delete")
	public ResponseEntity<Void> deleteCategory(@RequestBody @NotNull final ProductCategoryDTO categoryDto)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Eliminar Categoria");

		// Validar id
		ValidateParams.isNullObject(categoryDto.categoryId());

		// Eliminar Category
		categoryMgmtService.deleteProductCategory(categoryDto.categoryId());

		return ResponseEntity.noContent().build();
	}

	/**
	 * Buscar todas las Categorias
	 * 
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchAll")
	public ResponseEntity<Object> showCategory(final Model model) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Mostrar todas las Categorias");

		// Retornar lista de Category
		return ResponseEntity.ok()
				.body(Collections.singletonMap("productCategoryDto", categoryMgmtService.searchAll()));
	}

	/**
	 * Buscar por nombre
	 * 
	 * @param categoryDto
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@PostMapping(path = "/searchByName")
	public ResponseEntity<Object> searchByName(@RequestBody @NotNull final ProductCategoryDTO categoryDto)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar categoria por nombre");

		ResponseEntity<Object> result = null;

		ProductCategoryDTO categoryDtoFound = categoryMgmtService.searchByName(categoryDto.categoryName());

		if (categoryDtoFound != null && categoryDtoFound.categoryId() != null) {
			result = ResponseEntity.ok().body(Collections.singletonMap("categoryId", categoryDtoFound.categoryId()));
		} else {
			result = ResponseEntity.ok().body(Collections.singletonMap("categoryId", null));
		}

		return result;
	}

	/**
	 * Buscar por id
	 * 
	 * @param model
	 * @param id
	 * @return String
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchById")
	public String searchById(@RequestParam @NotNull final Long id, final Model model) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar Category por id");

		// Retornar categoria
		model.addAttribute("categoryDto", categoryMgmtService.searchById(id));

		return "VISTA BUSCAR Category POR id";
	}

}
