package com.ntd.dto;

import com.ntd.utils.Constants;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO Producto
 * 
 * @author SLP
 */
public record ProductCategoryDTO(Long categoryId,

		// Validar nombre de producto
		@NotBlank(message = Constants.MSG_CATEGORY_NOT_VALID) String categoryName) {
}
