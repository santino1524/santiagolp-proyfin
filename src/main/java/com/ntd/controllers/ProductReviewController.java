package com.ntd.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ntd.dto.ProductReviewDTO;
import com.ntd.exceptions.InternalException;
import com.ntd.services.ProductReviewMgmtServiceI;
import com.ntd.utils.Constants;
import com.ntd.utils.ValidateParams;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador de Productos
 * 
 * @author SLP
 */
@Slf4j
@Controller
@RequestMapping("/productReview")
public class ProductReviewController {

	/** Dependencia del servicio de gestion de productos */
	private final ProductReviewMgmtServiceI productReviewMgmtService;

	/**
	 * Constructor
	 * 
	 * @param productReviewMgmtService
	 */
	public ProductReviewController(final ProductReviewMgmtServiceI productReviewMgmtService) {
		this.productReviewMgmtService = productReviewMgmtService;
	}

	/**
	 * Registrar ProductReview
	 * 
	 * @param productReviewDto
	 * @param model
	 * @return String
	 * @throws InternalException
	 */
	@PostMapping
	public String saveProductReview(@RequestBody @Valid final ProductReviewDTO productReviewDto, final Model model)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Registrar critica de producto");

		String result = null;

		// Guardar ProductReview
		if (productReviewMgmtService.insertProductReview(productReviewDto) != null) {
			result = Constants.MSG_SUCCESSFUL_OPERATION;
		} else {
			result = Constants.MSG_UNEXPECTED_ERROR;
		}

		model.addAttribute(Constants.MESSAGE_GROWL, result);

		return "VISTA MOSTRAR RESPUESTA DE guardar CRITICA DE PRODUCTO";
	}

	/**
	 * Actualizar ProductReview
	 * 
	 * @param model
	 * @param productReviewDto
	 * @return String
	 * @throws InternalException
	 */
	@PutMapping
	public String updateProductReview(@RequestBody @Valid final ProductReviewDTO productReviewDto, final Model model)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Actualizar critica de producto");

		String result = null;

		// Comprobar si existe la critica
		if (productReviewMgmtService.searchById(productReviewDto.productReviewId()) == null) {
			result = Constants.MSG_PRODUCT_REVIEW_NON_EXISTENT;
		} else {
			// Actualizar ProductReview
			final ProductReviewDTO productReviewUpdated = productReviewMgmtService
					.insertProductReview(productReviewDto);

			// Verificar retorno de actualizacion
			if (productReviewUpdated != null) {
				result = Constants.MSG_SUCCESSFUL_OPERATION;
			} else {
				result = Constants.MSG_UNEXPECTED_ERROR;
			}
		}

		model.addAttribute(Constants.MESSAGE_GROWL, result);

		// Retornar respuesta
		return "VISTA MOSTRAR RESPUESTA DE CRITICA DE PRODUCTO ACTUALIZADO";
	}

	/**
	 * Eliminar producto
	 * 
	 * @param productDto
	 * @param model
	 * @return String
	 * @throws InternalException
	 */
	@Transactional
	@DeleteMapping
	public String deleteProductReview(@RequestBody @NotNull final ProductReviewDTO productReviewDto, final Model model)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Eliminar ProductReview");

		// Validar id
		ValidateParams.isNullObject(productReviewDto.productReviewId());

		// Eliminar ProductReview
		productReviewMgmtService.deleteProductReview(productReviewDto.productReviewId());

		model.addAttribute(Constants.MESSAGE_GROWL, Constants.MSG_SUCCESSFUL_OPERATION);

		return "VISTA MOSTRAR RESPUESTA DE  ProductReview ELIMINADO";
	}

	/**
	 * Buscar todos los productos
	 * 
	 * @param model
	 * @return String
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchAll")
	public String showProductReview(final Model model) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Mostrar todos los ProductReview");

		// Retornar lista de ProductReview
		model.addAttribute("reviewsDto", productReviewMgmtService.searchAll());

		return "VISTA BUSCAR TODOS LOS ProductReview";
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
	public String searchByProductNumber(@RequestParam @NotNull final Long id, final Model model)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar ProductReview por id");

		// Retornar ProductReview
		model.addAttribute("productReviewDto", productReviewMgmtService.searchById(id));

		return "VISTA BUSCAR PRODUCTOS POR id";
	}
}