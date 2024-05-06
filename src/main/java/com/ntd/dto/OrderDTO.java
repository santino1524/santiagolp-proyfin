package com.ntd.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.ntd.dto.validators.ValidOrderStatus;
import com.ntd.utils.Constants;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

/**
 * DTO Pedido
 * 
 * @author SLP
 */
public record OrderDTO(Long orderId,

		// Validar pedido
		@NotBlank(message = Constants.MSG_ORDER_NUMBER_NOT_VALID) String orderNumber,

		// Validar fecha de pedido
		@Past(message = Constants.MSG_ORDER_DATE_NOT_VALID) LocalDateTime orderDate,

		// Validar total del pedido
		@DecimalMin(value = "0.01", inclusive = true, message = Constants.MSG_TOTAL_NOT_VALID) BigDecimal total,

		// Validar estado del pedido
		@ValidOrderStatus String status,

		// Validar usuario
		@NotNull(message = Constants.MSG_USER_NOT_VALID) Long userId,

		// Validar productos vendidos
		@NotNull(message = Constants.MSG_PRODUCT_LIST_NOT_VALID) List<ProductSoldDTO> soldProductsDto,

		// Validar direccion
		@NotNull(message = Constants.MSG_ADDRESS_LIST_NOT_VALID) Long addressId) {

}
