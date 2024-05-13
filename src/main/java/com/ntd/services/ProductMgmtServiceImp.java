package com.ntd.services;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.ntd.dto.ProductCategoryDTO;
import com.ntd.dto.ProductDTO;
import com.ntd.dto.ProductSoldDTO;
import com.ntd.dto.mapper.DTOMapperI;
import com.ntd.exceptions.InternalException;
import com.ntd.persistence.Product;
import com.ntd.persistence.ProductRepositoryI;
import com.ntd.utils.QRCode;
import com.ntd.utils.ValidateParams;

import jakarta.transaction.Transactional;
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
	public ProductMgmtServiceImp(ProductRepositoryI productRepository) {
		this.productRepository = productRepository;
	}

	@Override
	public ProductDTO insertProduct(ProductDTO productDto) throws InternalException, SQLException {
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
	public List<ProductDTO> confirmOrder(List<ProductSoldDTO> productsDtoToBuy, boolean onlyCheck)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Confirmar disponibilidad de producto y actualizar stock");

		// Validar parametro
		ValidateParams.isNullObject(productsDtoToBuy);

		List<ProductDTO> productsDtoNotFound = new ArrayList<>();
		List<Product> productsConfirm = new ArrayList<>();

		for (ProductSoldDTO productSoldDto : productsDtoToBuy) {
			// Buscar producto en Stock
			final Product productInStock = productRepository.findById(productSoldDto.productId())
					.orElseThrow(InternalException::new);

			// Verificar producto a comprar con el stock
			if (productInStock.getProductQuantity() >= productSoldDto.quantity()) {
				if (!onlyCheck) {
					// Reducir cantidad de producto en stock
					productInStock.setProductQuantity(productInStock.getProductQuantity() - productSoldDto.quantity());

					// Agregar a lista de productos confirmados
					productsConfirm.add(productInStock);
				}
			} else {
				// Si algun producto no esta disponible agregarlo a la lista de no disponibles
				productsDtoNotFound.add(DTOMapperI.MAPPER.mapProductToDTO(productInStock));

			}
		}

		// Actualizar stock solo si todos los productos estan disponibles
		if (productsDtoNotFound.isEmpty() && !productsConfirm.isEmpty() && !onlyCheck) {
			// Actualizar stock
			updateStock(productsConfirm);
		}

		// Retornar lista
		return productsDtoNotFound;
	}

	/**
	 * Actualizar stock
	 * 
	 * @param productsConfirm
	 */
	@Transactional
	private void updateStock(List<Product> productsToUpdate) {
		if (log.isInfoEnabled())
			log.info("Actualizacion de stock por productos vendidos");

		// Guardar los productos por lotes
		if (!productsToUpdate.isEmpty()) {
			int batchSize = 50; // Tamanno del lote

			for (int i = 0; i < productsToUpdate.size(); i += batchSize) {
				int endIndex = Math.min(i + batchSize, productsToUpdate.size());
				List<Product> batch = productsToUpdate.subList(i, endIndex);

				productRepository.saveAll(batch);
			}
		}
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

	@Override
	public ProductDTO searchById(Long id) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar producto por id");

		// Validar parametro
		ValidateParams.isNullObject(id);

		// Buscar por id
		final Product product = productRepository.findById(id).orElse(null);

		// Retornar DTO
		return DTOMapperI.MAPPER.mapProductToDTO(product);
	}

	@Override
	public byte[] generateLabel(Long productId) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Generar etiqueta");

		// Validar parametro
		ValidateParams.isNullObject(productId);

		byte[] result = new byte[0];

		// Buscar pedido
		Product product = productRepository.findById(productId).orElse(new Product());

		if (product != null && product.getProductId() != null) {
			result = generateLabelPDF(product.getProductNumber());
		}

		// Generar etiqueta
		return result;
	}

	/**
	 * Generar etiqueta
	 * 
	 * @param productNumber
	 * @return byte[]
	 */
	private byte[] generateLabelPDF(String productNumber) {
		if (log.isInfoEnabled())
			log.info("Generar PDF");

		try {
			// Crear pagina para la etiqueta
			float widthInInches = 2.3f;
			float heightInInches = 3f;
			float widthInPoints = widthInInches * 72f;
			float heightInPoints = heightInInches * 72f;

			Rectangle labelPageSize = new Rectangle(widthInPoints, heightInPoints);

			// Crear un nuevo documento
			Document document = new Document(labelPageSize);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PdfWriter.getInstance(document, baos);

			document.open();

			// Numero de producto
			Paragraph productNumberText = new Paragraph(productNumber);
			productNumberText.setAlignment(Element.ALIGN_CENTER);
			document.add(productNumberText);

			// Generar el codigo QR
			int qrCodeWidth = 120;
			int qrCodeHeight = 120;
			Image qrCodeImage = QRCode.generateQRCodeImage(productNumber, qrCodeWidth, qrCodeHeight);
			qrCodeImage.setAlignment(Element.ALIGN_CENTER);
			document.add(qrCodeImage);

			document.close();
			return baos.toByteArray();

		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.getMessage());
			}
			return new byte[0];
		}
	}

}
