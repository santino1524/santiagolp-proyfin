package com.ntd.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ntd.dto.ProductDTO;
import com.ntd.dto.ProductSoldDTO;
import com.ntd.dto.mapper.DTOMapperI;
import com.ntd.exceptions.InternalException;
import com.ntd.persistence.Product;
import com.ntd.persistence.ProductRepositoryI;
import com.ntd.utils.ValidateParams;

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

	/**
	 * Constructor
	 * 
	 * @param productRepository
	 */
	public ProductMgmtServiceImp(final ProductRepositoryI productRepository) {
		this.productRepository = productRepository;
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
	public void deleteProduct(Long id) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Eliminar producto");

		// Validar parametro
		ValidateParams.isNullObject(id);

		// Eliminar por Id
		productRepository.deleteById(id);
	}

	@Override
	public List<ProductDTO> searchAll() {
		if (log.isInfoEnabled())
			log.info("Buscar todos los productos");

		// Buscar todos los productos
		final List<Product> products = productRepository.findAll();

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
	public List<ProductDTO> searchByCategory(String productCategory) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar productos por categoria");

		// Validar parametro
		ValidateParams.isNullObject(productCategory);

		// Buscar por categoria
		final List<Product> products = productRepository.findByProductCategoryIgnoreCase(productCategory);

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

		// Retornar DTO
		return DTOMapperI.MAPPER.mapProductToDTO(product);
	}

	@Override
	public boolean existsProductName(String productName) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Verificar si existe el nombre del producto");

		// Validar parametro
		ValidateParams.isNullObject(productName);

		// Retornar si existe el nombre del producto en BBDD
		return productRepository.existsByProductNameIgnoreCase(productName);
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
	public boolean existsProductNumber(String productNumber) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Verificar si existe el numero del producto");

		// Validar parametro
		ValidateParams.isNullObject(productNumber);

		// Retornar si existe el numero del producto en BBDD
		return productRepository.existsByProductNumber(productNumber);
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

}
