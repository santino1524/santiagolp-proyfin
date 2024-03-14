package com.ntd.controllers.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@RestController
@RequestMapping("/productReview")
public class ProductReviewControllerRest {

	/** Dependencia del servicio de gestion de productos */
	private final ProductReviewMgmtServiceI productReviewMgmtService;

	/**
	 * Constructor
	 * 
	 * @param productReviewMgmtService
	 */
	public ProductReviewControllerRest(final ProductReviewMgmtServiceI productReviewMgmtService) {
		this.productReviewMgmtService = productReviewMgmtService;
	}

	/**
	 * Registrar ProductReview
	 * 
	 * @param productReviewDto
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@PostMapping
	public ResponseEntity<String> saveProductReview(@RequestBody @Valid final ProductReviewDTO productReviewDto)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Registrar critica de producto");

		ResponseEntity<String> result = null;

		// Guardar ProductReview
		if (productReviewMgmtService.insertProductReview(productReviewDto) != null) {
			result = ResponseEntity.status(HttpStatus.ACCEPTED).body(Constants.MSG_SUCCESSFUL_OPERATION);
		} else {
			result = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Constants.MSG_UNEXPECTED_ERROR);
		}

		return result;
	}

	/**
	 * Actualizar ProductReview
	 * 
	 * @param productReviewDto
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@PutMapping
	public ResponseEntity<String> updateProductReview(@RequestBody @Valid final ProductReviewDTO productReviewDto)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Actualizar critica de producto");

		ResponseEntity<String> result = null;

		// Comprobar si existe la critica
		if (productReviewMgmtService.searchById(productReviewDto.productReviewId()) == null) {
			result = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
					.body(Constants.MSG_PRODUCT_REVIEW_NON_EXISTENT);
		} else {
			// Actualizar ProductReview
			final ProductReviewDTO productReviewUpdated = productReviewMgmtService
					.insertProductReview(productReviewDto);

			// Verificar retorno de actualizacion
			if (productReviewUpdated != null) {
				result = ResponseEntity.status(HttpStatus.ACCEPTED).body(Constants.MSG_SUCCESSFUL_OPERATION);
			} else {
				result = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Constants.MSG_UNEXPECTED_ERROR);
			}
		}

		// Retornar respuesta
		return result;
	}

	/**
	 * Eliminar producto
	 * 
	 * @param productDto
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@Transactional
	@DeleteMapping
	public ResponseEntity<String> deleteProductReview(@RequestBody @NotNull final ProductReviewDTO productReviewDto)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Eliminar ProductReview");

		// Validar id
		ValidateParams.isNullObject(productReviewDto.productReviewId());

		// Eliminar ProductReview
		productReviewMgmtService.deleteProductReview(productReviewDto.productReviewId());

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(Constants.MSG_SUCCESSFUL_OPERATION);
	}

	/**
	 * Buscar todos los productos
	 * 
	 * @return List
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchAll")
	public List<ProductReviewDTO> showProductReview() throws InternalException {
		if (log.isInfoEnabled())
			log.info("Mostrar todos los ProductReview");

		// Retornar lista de ProductReview
		return productReviewMgmtService.searchAll();
	}

	/**
	 * Buscar por id
	 * 
	 * @param id
	 * @return ProductReviewDTO
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchById")
	public ProductReviewDTO searchByProductNumber(@RequestParam @NotNull final Long id) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar ProductReview por id");

		// Retornar ProductReview
		return productReviewMgmtService.searchById(id);
	}
}
