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

import com.ntd.dto.ProductDTO;
import com.ntd.exceptions.InternalException;
import com.ntd.services.ProductMgmtServiceI;
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
@RequestMapping("/products")
public class ProductControllerRest {

	/** Dependencia del servicio de gestion de productos */
	private final ProductMgmtServiceI productMgmtService;

	/**
	 * Constructor
	 * 
	 * @param productMgmtService
	 */
	public ProductControllerRest(final ProductMgmtServiceI productMgmtService) {
		this.productMgmtService = productMgmtService;
	}

	/**
	 * Registrar producto
	 * 
	 * @param productDto
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@PostMapping
	public ResponseEntity<String> saveProduct(@RequestBody @Valid final ProductDTO productDto)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Registrar producto");

		ResponseEntity<String> result = null;

		// Comprobar si el producto existe
		if (productMgmtService.existsProductNumber(productDto.productNumber())) {
			// Devolver una respuesta con codigo de estado 422
			result = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Constants.MSG_PRODUCT_NUMBER_EXISTS);
		} else {
			// Guardar producto
			if (productMgmtService.insertProduct(productDto) != null) {
				// Devolver una respuesta con codigo de estado 202
				result = ResponseEntity.status(HttpStatus.ACCEPTED).body(Constants.MSG_SUCCESSFUL_OPERATION);
			} else {
				// Devolver una respuesta con codigo de estado 422
				result = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Constants.MSG_UNEXPECTED_ERROR);
			}
		}

		return result;
	}

	/**
	 * Actualizar producto
	 * 
	 * @param productDto
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@PutMapping
	public ResponseEntity<String> updateOrder(@RequestBody @Valid final ProductDTO productDto)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Actualizar producto");

		ResponseEntity<String> result = null;

		// Comprobar si existe otro producto
		if (productMgmtService.searchByProductNameOrProductNumber(productDto.productName(), productDto.productNumber())
				.size() > 1) {
			// Devolver una respuesta con codigo de estado 422
			result = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Constants.MSG_PRODUCT_DATA_EXISTS);
		} else {
			// Actualizar producto
			final ProductDTO productUpdated = productMgmtService.updateProduct(productDto);

			// Verificar retorno de actualizacion
			if (productUpdated != null) {
				// Devolver una respuesta con codigo de estado 202
				result = ResponseEntity.status(HttpStatus.ACCEPTED).body(Constants.MSG_SUCCESSFUL_OPERATION);
			} else {
				// Devolver una respuesta con codigo de estado 422
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
	public ResponseEntity<String> deleteProduct(@RequestBody @NotNull final ProductDTO productDto)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Eliminar producto");

		// Validar id
		ValidateParams.isNullObject(productDto.productId());

		// Eliminar producto
		productMgmtService.deleteProduct(productDto.productId());

		// Devolver una respuesta con codigo de estado 202
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(Constants.MSG_SUCCESSFUL_OPERATION);
	}

	/**
	 * Buscar todos los productos
	 * 
	 * @return List
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchAll")
	public List<ProductDTO> showProducts() throws InternalException {
		if (log.isInfoEnabled())
			log.info("Mostrar todos los productos");

		// Retornar lista de productos
		return productMgmtService.searchAll();
	}

	/**
	 * Buscar producto por categoria
	 * 
	 * @param category
	 * @return List
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByCategory")
	public List<ProductDTO> searchByCategory(@RequestParam @NotNull final String category) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar producto por categoria");

		// Retornar lista de productos
		return productMgmtService.searchByCategory(category);
	}

	/**
	 * Buscar producto por nombre
	 * 
	 * @param productName
	 * @return List
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByProductName")
	public List<ProductDTO> searchByProductName(@RequestParam @NotNull final String productName)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar producto por su nombre");

		// Retornar producto
		return productMgmtService.searchByName(productName);
	}

	/**
	 * Buscar producto por nombre ordenado por precio DESC
	 * 
	 * @param productName
	 * @return List
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByProductNameDesc")
	public List<ProductDTO> searchByNameOrderPvpPriceDesc(@RequestParam @NotNull final String productName)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar producto por nombre ordenado por precio DESC");

		// Retornar producto
		return productMgmtService.searchByNameOrderPvpPriceDesc(productName);
	}

	/**
	 * Buscar producto por nombre ordenado por precio ASC
	 * 
	 * @param productName
	 * @return List
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByProductNameAsc")
	public List<ProductDTO> searchByNameOrderPvpPriceAsc(@RequestParam @NotNull final String productName)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar producto por nombre ordenado por precio ASC");

		// Retornar producto
		return productMgmtService.searchByNameOrderPvpPriceAsc(productName);
	}

	/**
	 * Buscar por numero producto
	 * 
	 * @param productNumber
	 * @return List
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByProductNumber")
	public ProductDTO searchByProductNumber(@RequestParam @NotNull final String productNumber)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar producto por su numero");

		// Retornar producto
		return productMgmtService.searchByProductNumber(productNumber);
	}

}
