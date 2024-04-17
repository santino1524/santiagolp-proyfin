package com.ntd.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.ntd.dto.ProductCategoryDTO;
import com.ntd.dto.ProductDTO;
import com.ntd.exceptions.FileUploadException;
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
@Controller
@RequestMapping("/products")
public class ProductController {

	/** Constante String productsDto */
	private static final String PRODUCTS_DTO = "productsDto";

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
	 */
	@PostMapping(path = "/save")
	public ResponseEntity<Void> saveProduct(@ModelAttribute @Valid final ProductDTO productDto)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Registrar producto");

		ResponseEntity<Void> result = null;

		// Comprobar si el producto existe por el nombre
		if (productMgmtService.existsByProductName(productDto.productName())) {
			// Devolver una respuesta con codigo de estado 422
			result = ResponseEntity.unprocessableEntity().build();
		} else {
			// Guardar producto
			if (productMgmtService.insertProduct(productDto) != null) {
				// Devolver una respuesta con codigo de estado 204
				result = ResponseEntity.noContent().build();
			} else {
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
	 * @return String
	 * @throws InternalException
	 */
	@PutMapping
	public String updateOrder(@RequestBody @Valid final ProductDTO productDto, final Model model)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Actualizar producto");

		String result = null;

		// Comprobar si existe otro producto
		if (productMgmtService.searchByProductNameOrProductNumber(productDto.productName(), productDto.productNumber())
				.size() > 1) {
			result = Constants.MSG_PRODUCT_DATA_EXISTS;
		} else {
			// Actualizar producto
			final ProductDTO productUpdated = productMgmtService.updateProduct(productDto);

			// Verificar retorno de actualizacion
			if (productUpdated != null) {
				result = Constants.MSG_SUCCESSFUL_OPERATION;
			} else {
				result = Constants.MSG_UNEXPECTED_ERROR;
			}
		}

		model.addAttribute(Constants.MESSAGE_GROWL, result);

		// Retornar respuesta
		return "VISTA MOSTRAR RESPUESTA DE  PRODUCTO ACTUALIZADO";
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
	public String deleteProduct(@RequestBody @NotNull final ProductDTO productDto, final Model model)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Eliminar producto");

		// Validar id
		ValidateParams.isNullObject(productDto.productId());

		// Eliminar producto
		productMgmtService.deleteProduct(productDto.productId());

		model.addAttribute(Constants.MESSAGE_GROWL, Constants.MSG_SUCCESSFUL_OPERATION);

		return "VISTA MOSTRAR RESPUESTA DE  PRODUCTO ELIMINADO";
	}

	/**
	 * Buscar todos los productos
	 * 
	 * @param model
	 * @return String
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchAll")
	public String showProducts(final Model model) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Mostrar todos los productos");

		// Retornar lista de productos
		model.addAttribute(PRODUCTS_DTO, productMgmtService.searchAll());

		return "VISTA BUSCAR TODOS LOS PRODUCTOS";
	}

	/**
	 * Buscar producto por categoria
	 * 
	 * @param model
	 * @param category
	 * @return String
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByCategory")
	public String searchByCategory(@RequestParam @NotNull final ProductCategoryDTO categoryDto, final Model model)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar producto por categoria");

		// Retornar lista de productos
		model.addAttribute(PRODUCTS_DTO, productMgmtService.searchByCategory(categoryDto));

		return "VISTA BUSCAR PRODUCTOS POR CATEGORIA";
	}

	/**
	 * Buscar producto por nombre
	 * 
	 * @param model
	 * @param productName
	 * @return String
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByProductName")
	public String searchByProductName(@RequestParam @NotNull final String productName, final Model model)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar producto por su nombre");

		// Retornar producto
		model.addAttribute(PRODUCTS_DTO, productMgmtService.searchByName(productName));

		return "VISTA BUSCAR PRODUCTOS POR nombre";
	}

	/**
	 * Buscar producto por nombre ordenado por precio DESC
	 * 
	 * @param model
	 * @param productName
	 * @return String
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByProductNameDesc")
	public String searchByNameOrderPvpPriceDesc(@RequestParam @NotNull final String productName, final Model model)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar producto por nombre ordenado por precio DESC");

		// Retornar producto
		model.addAttribute(PRODUCTS_DTO, productMgmtService.searchByNameOrderPvpPriceDesc(productName));

		return "VISTA BUSCAR PRODUCTOS POR nombre ordenado por precio desc";
	}

	/**
	 * Buscar producto por nombre ordenado por precio ASC
	 * 
	 * @param model
	 * @param productName
	 * @return String
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByProductNameAsc")
	public String searchByNameOrderPvpPriceAsc(@RequestParam @NotNull final String productName, final Model model)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar producto por nombre ordenado por precio ASC");

		// Retornar producto
		model.addAttribute(PRODUCTS_DTO, productMgmtService.searchByNameOrderPvpPriceAsc(productName));

		return "VISTA BUSCAR PRODUCTOS POR nombre ordenado por precio asc";
	}

	/**
	 * Buscar por numero producto
	 * 
	 * @param model
	 * @param productDTO
	 * @return String
	 * @throws InternalException
	 */
	@PostMapping(path = "/searchByProductNumber")
	public String searchByProductNumber(@RequestBody @Valid final ProductDTO productDTO, final Model model)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar producto por su numero");

		// Retornar producto
		model.addAttribute("productDto", productMgmtService.searchByProductNumber(productDTO.productNumber()));
		model.addAttribute("showProducts", true);

		return "administration";
	}

	/**
	 * Cargar imagenes
	 * 
	 * @param files
	 * @return ResponseEntity
	 * @throws FileUploadException
	 */
	@PostMapping(path = "/upload")
	public ResponseEntity<Void> handleFileUpload(@RequestPart("files") final List<MultipartFile> files)
			throws FileUploadException {

		if (!files.isEmpty()) {
			try {
				// Obtener el directorio de recursos estaticos
				String staticDir = "src/main/resources/static/";

				// Crear el directorio de imagenes si no existe
				File imageDir = new File(staticDir + "product_images/");
				if (!imageDir.exists()) {
					imageDir.mkdirs();
				}

				// Guardar cada archivo en el directorio de imágenes
				for (MultipartFile file : files) {
					byte[] bytes = file.getBytes();

					StringBuilder builder = new StringBuilder();
					builder.append(imageDir.getAbsolutePath());
					builder.append("/");
					builder.append(file.getOriginalFilename());

					Path path = Paths.get(builder.toString());
					Files.write(path, bytes);
				}

				return ResponseEntity.ok().build();

			} catch (IOException e) {
				if (log.isErrorEnabled())
					log.error(e.getMessage());

				throw new FileUploadException();
			}
		} else {
			return ResponseEntity.notFound().build();

		}
	}
}
