package com.ntd.controllers;

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
import com.ntd.utils.Constants;
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
	 * @param model
	 * @return String
	 * @throws InternalException
	 */
	@PostMapping
	public String saveCategory(@RequestBody @Valid final ProductCategoryDTO categoryDto, final Model model)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Registrar Categoria");

		String result = null;

		// Guardar Report
		if (categoryMgmtService.insertProductCategory(categoryDto) != null) {
			result = Constants.MSG_SUCCESSFUL_OPERATION;
		} else {
			result = Constants.MSG_UNEXPECTED_ERROR;
		}

		model.addAttribute(Constants.MESSAGE_GROWL, result);

		return "VISTA MOSTRAR RESPUESTA DE guardar Category";
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
	@DeleteMapping
	public String deleteCategory(@RequestBody @NotNull final ProductCategoryDTO categoryDto, final Model model)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Eliminar Report");

		// Validar id
		ValidateParams.isNullObject(categoryDto.categoryId());

		// Eliminar Category
		categoryMgmtService.deleteProductCategory(categoryDto.categoryId());

		model.addAttribute(Constants.MESSAGE_GROWL, Constants.MSG_SUCCESSFUL_OPERATION);

		return "VISTA MOSTRAR RESPUESTA DE  Category ELIMINADO";
	}

	/**
	 * Buscar todos las Categorias
	 * 
	 * @param model
	 * @return String
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchAll")
	public String showCategory(final Model model) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Mostrar todos los Report");

		// Retornar lista de Category
		model.addAttribute("categoryDto", categoryMgmtService.searchAll());

		return "VISTA BUSCAR TODOS LOS Category";
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

		// Retornar Report
		model.addAttribute("categoryDto", categoryMgmtService.searchById(id));

		return "VISTA BUSCAR Category POR id";
	}
}
