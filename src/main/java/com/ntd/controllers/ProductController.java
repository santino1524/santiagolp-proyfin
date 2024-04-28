package com.ntd.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.ntd.dto.ProductCategoryDTO;
import com.ntd.dto.ProductDTO;
import com.ntd.exceptions.InternalException;
import com.ntd.services.ProductMgmtServiceI;
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
@RequestMapping("/products")
public class ProductController {

	/** Constante String productsDto */
	private static final String PRODUCTS = "products";

	/** Dependencia del servicio de gestion de productos */
	private final ProductMgmtServiceI productMgmtService;

	/**
	 * Constructor
	 * 
	 * @param productMgmtService
	 */
	public ProductController(final ProductMgmtServiceI productMgmtService) {
		this.productMgmtService = productMgmtService;
	}

	/**
	 * Registrar producto
	 * 
	 * @param productDto
	 * @param model
	 * @return ResponseEntity
	 * @throws InternalException
	 * @throws SQLException
	 */
	@PostMapping(path = "/save")
	public ResponseEntity<Object> saveProduct(@ModelAttribute @Valid final ProductDTO productDto)
			throws InternalException, SQLException {
		if (log.isInfoEnabled())
			log.info("Registrar producto");

		ResponseEntity<Object> result = null;

		// Comprobar si el producto existe por el nombre
		if (productMgmtService.existsByProductName(productDto.productName())) {
			// Devolver una respuesta con codigo de estado 422
			result = ResponseEntity.unprocessableEntity().build();
		} else {
			// Guardar producto
			ProductDTO savedProduct = productMgmtService.insertProduct(productDto);

			if (savedProduct.productId() != null) {
				// Devolver una respuesta con codigo de estado 200
				result = ResponseEntity.ok().body(Collections.singletonMap("productId", savedProduct.productId()));

			} else {
				// Devolver una respuesta con codigo de estado 500
				result = ResponseEntity.internalServerError().build();
			}
		}

		return result;
	}

	/**
	 * Cargar imagenes
	 * 
	 * @param files
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@PostMapping(path = "/upload")
	public ResponseEntity<Void> handleFileUpload(@RequestParam("productId") Long productId,
			@RequestPart("files") final List<MultipartFile> files) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Guardar imagenes");

		ValidateParams.isNullObject(productId);
		ValidateParams.isNullObject(files);

		ResponseEntity<Void> result = null;
		List<byte[]> images = new ArrayList<>();

		for (MultipartFile file : files) {
			try {
				images.add(file.getBytes());
			} catch (IOException e) {
				if (log.isErrorEnabled())
					log.error(e.getMessage());

				// Devolver una respuesta con codigo de estado 500
				result = ResponseEntity.internalServerError().build();

				break;
			}
		}

		if (result == null) {
			try {
				productMgmtService.insertImages(productId, images);

				// Devolver una respuesta con codigo de estado 200
				result = ResponseEntity.ok().build();
			} catch (SQLException e) {
				// Devolver una respuesta con codigo de estado 500
				result = ResponseEntity.internalServerError().build();
			}
		}

		return result;
	}

	/**
	 * Actualizar producto
	 * 
	 * @param model
	 * @param productDto
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@PostMapping(path = "/update")
	public ResponseEntity<Object> updateProduct(@ModelAttribute @Valid final ProductDTO productDto)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Actualizar producto");

		ResponseEntity<Object> result = null;

		// Comprobar si existe otro producto
		if (productMgmtService.searchByProductNameOrProductNumber(productDto.productName(), productDto.productNumber())
				.size() > 1) {
			// Devolver una respuesta con codigo de estado 422
			result = ResponseEntity.unprocessableEntity().build();
		} else {
			// Actualizar producto
			final ProductDTO productUpdated = productMgmtService.updateProduct(productDto);

			// Verificar retorno de actualizacion
			if (productUpdated.productId() != null) {
				// Devolver una respuesta con codigo de estado 200
				result = ResponseEntity.ok().body(Collections.singletonMap("productId", productUpdated.productId()));
			} else {
				// Devolver una respuesta con codigo de estado 500
				result = ResponseEntity.internalServerError().build();
			}
		}

		// Retornar respuesta
		return result;
	}

	/**
	 * Eliminar imagenes
	 * 
	 * @param productId
	 * @param model
	 * @return String
	 * @throws InternalException
	 */
	@Transactional
	@DeleteMapping(path = "/deleteImages/{productId}")
	public ResponseEntity<Void> deleteImages(@PathVariable final Long productId, final Model model)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Eliminar imagenes");

		// Validar id
		ValidateParams.isNullObject(productId);

		try {
			// Validar id
			ValidateParams.isNullObject(productId);

			// Eliminar imagen
			productMgmtService.deleteImages(productId);

		} catch (InternalException e) {
			throw new InternalException();
		}

		return ResponseEntity.ok().build();
	}

	/**
	 * Eliminar producto
	 * 
	 * @param productId
	 * @param model
	 * @return String
	 * @throws InternalException
	 */
	@Transactional
	@DeleteMapping(path = "/delete/{productId}")
	public ResponseEntity<Void> deleteProduct(@PathVariable final Long productId, final Model model)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Eliminar producto");

		try {
			// Validar id
			ValidateParams.isNullObject(productId);

			// Eliminar producto
			productMgmtService.deleteProduct(productId);

		} catch (InternalException e) {
			throw new InternalException();
		}

		return ResponseEntity.ok().build();
	}

	/**
	 * Buscar producto por categoria
	 * 
	 * @param model
	 * @param categoryId
	 * @return String
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByCategoryPageProducts/{categoryId}")
	public String searchByCategory(@PathVariable final Long categoryId, final Model model) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar producto por categoria");

		ValidateParams.isNullObject(categoryId);

		ProductCategoryDTO categoryDto = new ProductCategoryDTO(categoryId, null);

		model.addAttribute(PRODUCTS, productMgmtService.searchByCategory(categoryDto));

		return PRODUCTS;
	}

	/**
	 * Buscar producto por categoria
	 * 
	 * @param categoryId
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByCategory")
	public ResponseEntity<Object> searchByCategory(@RequestParam @NotNull final Long categoryId)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar producto por categoria");

		ValidateParams.isNullObject(categoryId);

		ProductCategoryDTO categoryDto = new ProductCategoryDTO(categoryId, null);

		// Retornar lista de productos
		return ResponseEntity.ok()
				.body(Collections.singletonMap(PRODUCTS, productMgmtService.searchByCategory(categoryDto)));
	}

	/**
	 * Buscar todos los productos
	 * 
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchAll")
	public ResponseEntity<Object> showProducts() throws InternalException {
		if (log.isInfoEnabled())
			log.info("Mostrar todos los productos");

		// Retornar lista de productos
		return ResponseEntity.ok().body(Collections.singletonMap(PRODUCTS, productMgmtService.searchAll()));
	}

	/**
	 * Buscar producto por nombre desde otra pagina
	 * 
	 * @param model
	 * @param productName
	 * @return String
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByProductNamePage")
	public String searchByProductName(@RequestParam @NotNull final String productName, final Model model)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar producto por su nombre");

		// Retornar producto
		model.addAttribute(PRODUCTS, productMgmtService.searchByName(productName));

		return PRODUCTS;
	}

	/**
	 * Buscar producto por nombre
	 * 
	 * @param productName
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByProductName")
	public ResponseEntity<Object> searchByProductName(@RequestParam @NotNull final String productName)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar producto por su nombre");

		// Retornar lista de productos
		return ResponseEntity.ok()
				.body(Collections.singletonMap(PRODUCTS, productMgmtService.searchByName(productName)));
	}

	/**
	 * Buscar producto por nombre ordenado por precio DESC
	 * 
	 * @param productName
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByProductNameDesc")
	public ResponseEntity<Object> searchByNameOrderPvpPriceDesc(@RequestParam @NotNull final String productName)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar producto por nombre ordenado por precio DESC");

		// Retornar lista de productos
		return ResponseEntity.ok().body(
				Collections.singletonMap(PRODUCTS, productMgmtService.searchByNameOrderPvpPriceDesc(productName)));
	}

	/**
	 * Ordenar productos por categoria por precio DESC
	 * 
	 * @param categoryId
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByProductCategoryDesc")
	public ResponseEntity<Object> searchBycategoryOrderPvpPriceDesc(@RequestParam @NotNull final Long categoryId)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar producto por categoria ordenado por precio DESC");

		ProductCategoryDTO productCategoryDto = new ProductCategoryDTO(categoryId, null);

		// Retornar lista de productos
		return ResponseEntity.ok().body(Collections.singletonMap(PRODUCTS,
				productMgmtService.searchByCategoryOrderPvpPriceDesc(productCategoryDto)));
	}

	/**
	 * Ordenar productos por categoria y nombre por precio DESC
	 * 
	 * @param categoryId
	 * @param productName
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByNameAndProductCategoryOrderDesc")
	public ResponseEntity<Object> searchByNameAndProductCategoryOrderDesc(@RequestParam @NotNull final Long categoryId,
			String productName) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar producto por categoria y nombre ordenado por precio DESC");

		ProductCategoryDTO productCategoryDto = new ProductCategoryDTO(categoryId, null);

		// Retornar lista de productos
		return ResponseEntity.ok().body(Collections.singletonMap(PRODUCTS,
				productMgmtService.searchByNameAndProductCategoryOrderDesc(productName, productCategoryDto)));
	}

	/**
	 * Ordenar productos por categoria y nombre
	 * 
	 * @param categoryId
	 * @param productName
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByNameAndProductCategory")
	public ResponseEntity<Object> searchByNameAndProductCategory(@RequestParam @NotNull final Long categoryId,
			String productName) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar producto por categoria y nombre");

		ProductCategoryDTO productCategoryDto = new ProductCategoryDTO(categoryId, null);

		// Retornar lista de productos
		return ResponseEntity.ok().body(Collections.singletonMap(PRODUCTS,
				productMgmtService.searchByNameAndProductCategory(productName, productCategoryDto)));
	}

	/**
	 * Ordenar productos por categoria y nombre por precio ASC
	 * 
	 * @param categoryId
	 * @param productName
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByNameAndProductCategoryOrderAsc")
	public ResponseEntity<Object> searchByNameAndProductCategoryOrderAsc(@RequestParam @NotNull final Long categoryId,
			String productName) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar producto por categoria y nombre ordenado por precio ASC");

		ProductCategoryDTO productCategoryDto = new ProductCategoryDTO(categoryId, null);

		// Retornar lista de productos
		return ResponseEntity.ok().body(Collections.singletonMap("products",
				productMgmtService.searchByNameAndProductCategoryOrderAsc(productName, productCategoryDto)));
	}

	/**
	 * Buscar producto por nombre ordenado por precio ASC
	 * 
	 * @param productName
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByProductNameAsc")
	public ResponseEntity<Object> searchByNameOrderPvpPriceAsc(@RequestParam @NotNull final String productName)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar producto por nombre ordenado por precio ASC");

		// Retornar lista de productos
		return ResponseEntity.ok()
				.body(Collections.singletonMap(PRODUCTS, productMgmtService.searchByNameOrderPvpPriceAsc(productName)));
	}

	/**
	 * Buscar producto por categoria ordenado por precio ASC
	 * 
	 * @param categoryId
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByProductCategoryAsc")
	public ResponseEntity<Object> searchByCategoryOrderPvpPriceAsc(@RequestParam @NotNull final Long categoryId)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar producto por categoria ordenado por precio ASC");

		ProductCategoryDTO productCategoryDto = new ProductCategoryDTO(categoryId, null);

		// Retornar lista de productos
		return ResponseEntity.ok().body(Collections.singletonMap(PRODUCTS,
				productMgmtService.searchByCategoryOrderPvpPriceAsc(productCategoryDto)));
	}

	/**
	 * Buscar productos ordenado por precio ASC
	 * 
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchAllProductAsc")
	public ResponseEntity<Object> searchAllProductAsc() throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar productos ordenado por precio ASC");

		// Retornar lista de productos
		return ResponseEntity.ok()
				.body(Collections.singletonMap(PRODUCTS, productMgmtService.searchAllOrderPvpPriceAsc()));
	}

	/**
	 * Buscar productos ordenado por precio DESC
	 * 
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchAllProductDesc")
	public ResponseEntity<Object> searchAllProductDesc() throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar productos ordenado por precio DESC");

		// Retornar lista de productos
		return ResponseEntity.ok()
				.body(Collections.singletonMap(PRODUCTS, productMgmtService.searchAllOrderPvpPriceDesc()));
	}

	/**
	 * Buscar por numero producto
	 * 
	 * @param productDTO
	 * @return String
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByProductNumber/{productNum}")
	public ResponseEntity<Object> searchByProductNumber(@PathVariable final String productNum)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar producto por su numero");

		ValidateParams.isNullObject(productNum);

		// Retornar producto
		return ResponseEntity.ok()
				.body(Collections.singletonMap("productDto", productMgmtService.searchByProductNumber(productNum)));
	}
}
