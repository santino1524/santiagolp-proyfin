package com.ntd.dto;

/**
 * DTO Producto vendido
 * 
 * @author SLP
 */
public record ProductSoldDTO(Long productSoldId, Long orderId, Long productId, Integer quantity) {

}
