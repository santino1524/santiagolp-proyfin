package com.ntd.controllers;

import java.util.Collections;

import org.springframework.http.ResponseEntity;
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
import com.ntd.persistence.Product;
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
	 * Contar cantidad de resennas denunciadas
	 * 
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@GetMapping(path = "/countReviewReporter")
	public ResponseEntity<Integer> countByReporter() throws InternalException {
		log.info("Contar cantidad de resennas denunciadas");

		return ResponseEntity.ok(productReviewMgmtService.countByReportedEquals(true));
	}

	/**
	 * Buscar resenna por producto
	 * 
	 * @param productId
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByProduct")
	public ResponseEntity<Object> searchByProduct(@RequestParam @NotNull final Long productId)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar resenna por producto");

		ValidateParams.isNullObject(productId);

		Product product = new Product(productId, null, null, null, null, null, 0, null, null, null, null, null, null);

		// Retornar lista de resennas
		return ResponseEntity.ok()
				.body(Collections.singletonMap("productReviews", productReviewMgmtService.findByProduct(product)));
	}

	/**
	 * Registrar ProductReview
	 * 
	 * @param productReviewDto
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@PostMapping(path = "/save")
	public ResponseEntity<Object> saveProductReview(@RequestBody @Valid final ProductReviewDTO productReviewDto)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Registrar critica de producto");

		return ResponseEntity.ok().body(Collections.singletonMap("productReview",
				productReviewMgmtService.insertProductReview(productReviewDto)));
	}

	/**
	 * Buscar por id
	 * 
	 * @param id
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchById")
	public ResponseEntity<Object> searchById(@RequestParam @NotNull final Long id) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar ProductReview por id");

		return ResponseEntity.ok()
				.body(Collections.singletonMap("productReview", productReviewMgmtService.searchById(id)));
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

}
