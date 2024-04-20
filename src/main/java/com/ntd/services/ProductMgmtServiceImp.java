package com.ntd.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ntd.dto.ProductCategoryDTO;
import com.ntd.dto.ProductDTO;
import com.ntd.dto.ProductSoldDTO;
import com.ntd.dto.mapper.DTOMapperI;
import com.ntd.exceptions.DeleteFilesException;
import com.ntd.exceptions.InternalException;
import com.ntd.persistence.Product;
import com.ntd.persistence.ProductRepositoryI;
import com.ntd.utils.Constants;
import com.ntd.utils.ValidateParams;

import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio de gestion de productos
 * 
 * @author SLP
 */
@Service
@Slf4j
public class ProductMgmtServiceImp implements ProductMgmtServiceI {

	/** Dependencia de ProductRepository */
	private final ProductRepositoryI productRepository;

	/** ServletContext */
	private final ServletContext context;

	/**
	 * Constructor
	 * 
	 * @param context
	 * @param productRepository
	 */
	public ProductMgmtServiceImp(final ProductRepositoryI productRepository, final ServletContext context) {
		this.productRepository = productRepository;
		this.context = context;
	}

	@Override
	public ProductDTO insertProduct(ProductDTO productDto) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Insertar producto");

		// Validar parametro
		ValidateParams.isNullObject(productDto);

		// Mapear DTO y guardar
		final Product product = productRepository.save(DTOMapperI.MAPPER.mapDTOToProduct(productDto));

		// Retornar DTO
		return DTOMapperI.MAPPER.mapProductToDTO(product);
	}

	@Override
	public ProductDTO updateProduct(ProductDTO productDto) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Actualizar producto");

		// Validar parametro
		ValidateParams.isNullObject(productDto);

		// Mapear DTO y actualizar
		final Product product = productRepository.save(DTOMapperI.MAPPER.mapDTOToProduct(productDto));

		// Retornar DTO
		return DTOMapperI.MAPPER.mapProductToDTO(product);
	}

	@Override
	public void deleteProduct(Long id) throws InternalException, DeleteFilesException {
		if (log.isInfoEnabled())
			log.info("Eliminar producto");

		// Validar parametro
		ValidateParams.isNullObject(id);

		// Eliminar imagenes
		deleteImages(id);

		// Eliminar por Id
		productRepository.deleteById(id);
	}

	@Override
	public List<ProductDTO> searchAll() throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar todos los productos");

		// Buscar todos los productos
		final List<Product> products = productRepository.findAll();

		// Agregar rutas a las imagenes
		if (!products.isEmpty()) {
			for (Product product : products) {
				addRoutes(product.getImageUrls());
			}
		}

		// Mapear DTO
		final List<ProductDTO> productsDto = new ArrayList<>();

		if (!products.isEmpty()) {
			for (Product product : products) {
				productsDto.add(DTOMapperI.MAPPER.mapProductToDTO(product));
			}
		}

		// Retornar lista DTO
		return productsDto;
	}

	@Override
	public List<ProductDTO> searchByCategory(ProductCategoryDTO productCategoryDto) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar productos por categoria");

		// Validar parametro
		ValidateParams.isNullObject(productCategoryDto);

		// Buscar por categoria
		final List<Product> products = productRepository
				.findByProductCategory(DTOMapperI.MAPPER.mapDTOtoProductCategory(productCategoryDto));

		// Agregar rutas a las imagenes
		if (!products.isEmpty()) {
			for (Product product : products) {
				addRoutes(product.getImageUrls());
			}
		}

		// Mapear DTO
		final List<ProductDTO> productsDto = new ArrayList<>();

		if (!products.isEmpty()) {
			for (Product product : products) {
				productsDto.add(DTOMapperI.MAPPER.mapProductToDTO(product));
			}
		}

		// Retornar lista DTO
		return productsDto;
	}

	@Override
	public List<ProductDTO> searchByName(String productName) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar productos por nombre");

		// Validar parametro
		ValidateParams.isNullObject(productName);

		// Buscar por nombre
		final List<Product> products = productRepository.findByProductNameIgnoreCaseContaining(productName);

		// Agregar rutas a las imagenes
		if (!products.isEmpty()) {
			for (Product product : products) {
				addRoutes(product.getImageUrls());
			}
		}

		// Mapear DTO
		final List<ProductDTO> productsDto = new ArrayList<>();

		if (!products.isEmpty()) {
			for (Product product : products) {
				productsDto.add(DTOMapperI.MAPPER.mapProductToDTO(product));
			}
		}

		// Retornar lista DTO
		return productsDto;
	}

	@Override
	public List<ProductDTO> searchByNameOrderPvpPriceDesc(String productName) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar productos por nombre ordenados descendentemente por el precio");

		// Validar parametro
		ValidateParams.isNullObject(productName);

		// Buscar por nombre
		final List<Product> products = productRepository
				.findByProductNameIgnoreCaseContainingOrderByPvpPriceDesc(productName);

		// Agregar rutas a las imagenes
		if (!products.isEmpty()) {
			for (Product product : products) {
				addRoutes(product.getImageUrls());
			}
		}

		// Mapear DTO
		final List<ProductDTO> productsDto = new ArrayList<>();

		if (!products.isEmpty()) {
			for (Product product : products) {
				productsDto.add(DTOMapperI.MAPPER.mapProductToDTO(product));
			}
		}

		// Retornar lista DTO
		return productsDto;
	}

	@Override
	public List<ProductDTO> searchByNameOrderPvpPriceAsc(String productName) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar productos por nombre ordenados ascendentemente por el precio");

		// Validar parametro
		ValidateParams.isNullObject(productName);

		// Buscar por nombre
		final List<Product> products = productRepository
				.findByProductNameIgnoreCaseContainingOrderByPvpPriceAsc(productName);

		// Agregar rutas a las imagenes
		if (!products.isEmpty()) {
			for (Product product : products) {
				addRoutes(product.getImageUrls());
			}
		}

		// Mapear DTO
		final List<ProductDTO> productsDto = new ArrayList<>();

		if (!products.isEmpty()) {
			for (Product product : products) {
				productsDto.add(DTOMapperI.MAPPER.mapProductToDTO(product));
			}
		}

		// Retornar lista DTO
		return productsDto;
	}

	@Override
	public ProductDTO searchByProductNumber(String productNumber) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar por numero de producto ");

		// Validar parametro
		ValidateParams.isNullObject(productNumber);

		// Buscar por numero de producto
		final Product product = productRepository.findByProductNumber(productNumber);

		// Agregar rutas a las imagenes
		if (product != null) {
			addRoutes(product.getImageUrls());
		}

		// Retornar DTO
		return DTOMapperI.MAPPER.mapProductToDTO(product);
	}

	@Override
	public List<ProductDTO> searchByProductNameOrProductNumber(String productName, String productNumber)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar por numero y nombre de producto ");

		// Validar parametros
		ValidateParams.isNullObject(productNumber);
		ValidateParams.isNullObject(productName);

		// Buscar por numero y nombre de producto
		final List<Product> products = productRepository.findByProductNameIgnoreCaseOrProductNumber(productName,
				productNumber);

		// Agregar rutas a las imagenes
		if (!products.isEmpty()) {
			for (Product product : products) {
				addRoutes(product.getImageUrls());
			}
		}

		// Mapear DTO
		final List<ProductDTO> productsDto = new ArrayList<>();

		if (!products.isEmpty()) {
			for (Product product : products) {
				productsDto.add(DTOMapperI.MAPPER.mapProductToDTO(product));
			}
		}

		// Retornar lista DTO
		return productsDto;
	}

	@Override
	public boolean existsByProductName(String productName) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Verificar si existe el nombre del producto");

		// Validar parametro
		ValidateParams.isNullObject(productName);

		// Retornar si existe
		return productRepository.existsByProductNameIgnoreCase(productName);
	}

	@Override
	public List<ProductDTO> confirmOrder(List<ProductSoldDTO> productsDtoToBuy) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Confirmar disponibilidad de producto y actualizar stock");

		// Validar parametro
		ValidateParams.isNullObject(productsDtoToBuy);

		List<ProductDTO> productsDtoNotFound = new ArrayList<>();
		List<Product> productsConfirm = new ArrayList<>();

		for (ProductSoldDTO productSoldDto : productsDtoToBuy) {
			// Buscar producto en Stock
			final Product productInStock = productRepository.findById(productSoldDto.productDto().productId())
					.orElseThrow(InternalException::new);

			// Verificar producto a comprar con el stock
			if (productInStock.getProductQuantity() >= productSoldDto.quantitySold()) {
				// Reducir cantidad de producto en stock
				productInStock.setProductQuantity(productInStock.getProductQuantity() - productSoldDto.quantitySold());

				// Añadir a lista de productos confirmados
				productsConfirm.add(productInStock);
			} else {
				// Si algun producto no esta disponible agregarlo a la lista de no disponibles
				productsDtoNotFound.add(DTOMapperI.MAPPER.mapProductToDTO(productInStock));

			}
		}

		// Actualizar stock solo si todos los productos estan disponibles
		if (productsDtoNotFound.isEmpty()) {
			if (log.isInfoEnabled())
				log.info("Actualizacion de stock por productos vendidos");

			for (Product product : productsConfirm) {

				// Actualizar stock
				productRepository.save(product);
			}
		}

		// Retornar lista
		return productsDtoNotFound;
	}

	@Override
	public void deleteImages(Long id) throws InternalException, DeleteFilesException {
		if (log.isInfoEnabled())
			log.info("Eliminar imagenes");

		// Validar parametro
		ValidateParams.isNullObject(id);

		// Buscar imagenes
		List<String> fileNames = productRepository.findImageUrlsByProductId(id);

		// Eliminar ficheros
		removeImages(fileNames);

		// Eliminar imagenes
		productRepository.deleteImageUrlsByProductId(id);
	}

	@Override
	public void removeImages(List<String> fileNames) throws DeleteFilesException, InternalException {
		// Crea un objeto File que representa el directorio
		String baseDirectory = context.getRealPath("") + Constants.SEPARATOR + Constants.PRODUCT_IMAGES;
		File directory = new File(baseDirectory);

		// Verifica si el directorio existe
		if (directory.exists() && directory.isDirectory()) {
			// Obtiene una lista de archivos en el directorio
			File[] files = directory.listFiles();

			// Itera sobre los archivos en el directorio
			for (File file : files) {
				// Verifica si el nombre del archivo coincide con alguno de los nombres de
				// imagen
				findFileNameForImageName(fileNames, baseDirectory, file);
			}
		} else {
			if (log.isErrorEnabled())
				log.error("El directorio de imagenes de productos debe existir");

			throw new InternalException();
		}
	}

	/**
	 * Verifica si el nombre del archivo coincide con alguno de los nombres de
	 * imagen
	 * 
	 * @param fileNames
	 * @param baseDirectory
	 * @param file
	 * @throws DeleteFilesException
	 */
	private void findFileNameForImageName(List<String> fileNames, String baseDirectory, File file)
			throws DeleteFilesException {

		for (String imageName : fileNames) {
			if (file.getName().equals(imageName)) {
				// Obtener el path
				Path filePath = Paths.get(baseDirectory, file.getName());

				// Elimina el archivo si coincide con el nombre de la imagen
				try {
					Files.delete(filePath);
				} catch (IOException e) {
					if (log.isErrorEnabled())
						log.error(e.getMessage());

					throw new DeleteFilesException();
				}
			}
		}
	}

	@Override
	public void addRoutes(List<String> fileNames) throws InternalException {

		// Validar parametro
		ValidateParams.isNullObject(fileNames);
		fileNames.replaceAll(filename -> Constants.PRODUCT_IMAGES + File.separator + filename);
//		fileNames
//				.replaceAll(filename -> context.getRealPath("") + Constants.PRODUCT_IMAGES + File.separator + filename);

	}
}
