package com.ntd.dto;

/**
 * DTO Critica de Producto
 * 
 * @author SLP
 */
public record ProductReviewDTO(Long productReviewId, ProductDTO productDto, UserDTO userDto, int rating, String comment,
		boolean reported) {

}
