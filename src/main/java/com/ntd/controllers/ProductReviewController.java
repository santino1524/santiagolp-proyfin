package com.ntd.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ntd.dto.ProductReviewDTO;
import com.ntd.exceptions.InternalException;
import com.ntd.services.ProductReviewMgmtServiceI;
import com.ntd.utils.Constants;

import jakarta.validation.Valid;
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

		// Guardar producto
		if (productReviewMgmtService.insertProductReview(productReviewDto).productReviewId() != null) {
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
}
