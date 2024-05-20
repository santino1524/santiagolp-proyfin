package com.ntd.dto;

/**
 * DTO Critica de Producto
 * 
 * @author SLP
 */
public record ProductReviewDTO(Long productReviewId, Long productId, UserDTO user, int rating, String comment,
		boolean reported) {
}
