package com.ntd.dto;

/**
 * DTO Producto vendido
 * 
 * @author SLP
 */
public record ProductSoldDTO(Long productSoldId, OrderDTO orderDto, ProductDTO productDto, Integer quantitySold) {

}
