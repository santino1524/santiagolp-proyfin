package com.ntd.dto;

import java.math.BigDecimal;
import java.util.List;

import com.ntd.utils.Constants;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * DTO Producto
 * 
 * @author SLP
 */
public record ProductDTO(Long productId,

		// Validar numero de producto
		@NotBlank(message = Constants.MSG_PRODUCT_NUMBER_NOT_VALID) String productNumber,

		// Validar nombre de producto
		@NotBlank(message = Constants.MSG_NAME_NOT_VALID) String productName,

		String productDescription,

		// Validar talla del producto
		@NotEmpty(message = Constants.MSG_PRODUCT_SIZES_NOT_VALID) String productSize,

		// Validar cantidad del producto
		@DecimalMin(value = "0", message = Constants.MSG_QUANTITY_NOT_VALID) int productQuantity,

		// Validar categoria del producto
		@NotNull(message = Constants.MSG_CATEGORY_NOT_VALID) ProductCategoryDTO productCategory,

		// Validar lista de urls de imagenes
		@NotEmpty(message = Constants.MSG_IMAGE_URL_NOT_VALID) List<String> imageUrls,

		// Validar iva
		@Digits(integer = 2, fraction = 2, message = Constants.MSG_IVA_NOT_VALID) BigDecimal iva,

		// Validar precio
		@DecimalMin(value = "0.01", message = Constants.MSG_PRICE_NOT_VALID) BigDecimal basePrice,

		// Validar precio PVP
		@DecimalMin(value = "0.01", message = Constants.MSG_PRICE_NOT_VALID) BigDecimal pvpPrice,

		List<ProductReviewDTO> reviewsDto) {

}
