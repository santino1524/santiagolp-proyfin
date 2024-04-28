package com.ntd.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Service;

import com.ntd.dto.ProductCategoryDTO;
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

	/** Dependencia de DataSource */
	private final DataSource dataSource;

	/**
	 * Constructor
	 * 
	 * @param productRepository
	 * @param dataSource
	 */
	public ProductMgmtServiceImp(ProductRepositoryI productRepository, DataSource dataSource) {
		this.productRepository = productRepository;
		this.dataSource = dataSource;
	}

	@Override
	public ProductDTO insertProduct(ProductDTO productDto) throws InternalException, SQLException {
		if (log.isInfoEnabled())
			log.info("Insertar producto");

		// Validar parametro
		ValidateParams.isNullObject(productDto);

		// Mapear DTO y guardar
		final Product product = productRepository.save(DTOMapperI.MAPPER.mapDTOToProduct(productDto));

		// Guardar las imágenes asociadas al producto
		if (productDto.images() != null && !productDto.images().isEmpty()) {
			insertImages(product.getProductId(), productDto.images());
		}

		// Retornar DTO
		return DTOMapperI.MAPPER.mapProductToDTO(product);
	}

	/**
	 * Insertar imagenes por lotes
	 * 
	 * @param productId
	 * @param images
	 * @throws SQLException
	 */
	public void insertImages(Long productId, List<byte[]> images) throws SQLException {
		if (log.isInfoEnabled())
			log.info("Insertar imagenes");

		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(
						"INSERT INTO T_IMAGES (C_PRODUCT_ID, C_IMAGE) VALUES (?, CAST(? AS bytea))")) {

			for (byte[] image : images) {
				statement.setLong(1, productId);
				statement.setBytes(2, image);
				statement.addBatch();
			}
			statement.executeBatch();

		} catch (SQLException e) {
			if (log.isErrorEnabled()) {
				log.error(e.getMessage());
			}

			throw new SQLException("Ha ocurrido un error al insertar las imágenes");
		}
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
	public List<ProductDTO> searchAll() throws InternalException {
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
	public List<ProductDTO> searchByCategory(ProductCategoryDTO productCategoryDto) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar productos por categoria");

		// Validar parametro
		ValidateParams.isNullObject(productCategoryDto);

		// Buscar por categoria
		final List<Product> products = productRepository
				.findByProductCategory(DTOMapperI.MAPPER.mapDTOtoProductCategory(productCategoryDto));

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

		// Buscar producto por numero de producto
		Product product = productRepository.findByProductNumber(productNumber);

		// Buscar imagenes por id
		Object[] objectProduct = productRepository.findImagesById(product.getProductId());

		// Inicializa una lista para las imágenes
		List<byte[]> images = new ArrayList<>();

		// Itera sobre los valores de result
		for (int i = 0; i < objectProduct.length; i++) {
			// Verifica si el valor en result[i] no es nulo
			if (objectProduct[i] != null) {
				// Agrega el valor a la lista de imagenes
				images.add((byte[]) objectProduct[i]);
			}
		}

		product.setImages(images);

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
	public void deleteImages(Long id) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Eliminar imagenes");

		// Validar parametro
		ValidateParams.isNullObject(id);

		// Eliminar imagenes
		productRepository.deleteImagesByProductId(id);
	}

	@Override
	public List<ProductDTO> searchByCategoryOrderPvpPriceDesc(ProductCategoryDTO productCategoryDto)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar productos por categoria ordenados descendentemente por el precio");

		// Validar parametro
		ValidateParams.isNullObject(productCategoryDto.categoryId());

		// Buscar por categoria
		final List<Product> products = productRepository.findByProductCategoryOrderByPvpPriceDesc(
				DTOMapperI.MAPPER.mapDTOtoProductCategory(productCategoryDto));

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
	public List<ProductDTO> searchByCategoryOrderPvpPriceAsc(ProductCategoryDTO productCategoryDto)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar productos por categoria ordenados ascendentemente por el precio");

		// Validar parametro
		ValidateParams.isNullObject(productCategoryDto.categoryId());

		// Buscar por categoria
		final List<Product> products = productRepository
				.findByProductCategoryOrderByPvpPriceAsc(DTOMapperI.MAPPER.mapDTOtoProductCategory(productCategoryDto));

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
	public List<ProductDTO> searchAllOrderPvpPriceDesc() throws InternalException {
		// Buscar productos
		final List<Product> products = productRepository.findAllByOrderByPvpPriceDesc();

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
	public List<ProductDTO> searchAllOrderPvpPriceAsc() throws InternalException {
		// Buscar por categoria
		final List<Product> products = productRepository.findAllByOrderByPvpPriceAsc();

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
	public List<ProductDTO> searchByNameAndProductCategoryOrderDesc(String productName,
			ProductCategoryDTO productCategoryDto) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar productos por categoria y nombre ordenados DESC por el precio");

		// Validar parametro
		ValidateParams.isNullObject(productCategoryDto.categoryId());
		ValidateParams.isNullObject(productName);

		// Buscar por categoria
		final List<Product> products = productRepository
				.findByProductNameIgnoreCaseContainingAndProductCategoryOrderByPvpPriceDesc(productName,
						DTOMapperI.MAPPER.mapDTOtoProductCategory(productCategoryDto));

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
	public List<ProductDTO> searchByNameAndProductCategoryOrderAsc(String productName,
			ProductCategoryDTO productCategoryDto) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar productos por categoria y nombre ordenados ASC por el precio");

		// Validar parametro
		ValidateParams.isNullObject(productCategoryDto.categoryId());
		ValidateParams.isNullObject(productName);

		// Buscar por categoria
		final List<Product> products = productRepository
				.findByProductNameIgnoreCaseContainingAndProductCategoryOrderByPvpPriceAsc(productName,
						DTOMapperI.MAPPER.mapDTOtoProductCategory(productCategoryDto));

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
	public List<ProductDTO> searchByNameAndProductCategory(String productName, ProductCategoryDTO productCategoryDto)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar productos por categoria y nombre");

		// Validar parametro
		ValidateParams.isNullObject(productCategoryDto.categoryId());
		ValidateParams.isNullObject(productName);

		// Buscar por categoria
		final List<Product> products = productRepository.findByProductNameIgnoreCaseContainingAndProductCategory(
				productName, DTOMapperI.MAPPER.mapDTOtoProductCategory(productCategoryDto));

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
	public long countByProductCategory(ProductCategoryDTO productCategoryDto) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Contar productos de una categoria");

		ValidateParams.isNullObject(productCategoryDto);

		return productRepository.countByProductCategory(DTOMapperI.MAPPER.mapDTOtoProductCategory(productCategoryDto));
	}
}
