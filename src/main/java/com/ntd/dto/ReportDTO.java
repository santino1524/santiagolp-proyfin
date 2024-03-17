package com.ntd.dto;

import com.ntd.utils.Constants;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO Reporte
 * 
 * @author SLP
 */
public record ReportDTO(Long reportId, ProductReviewDTO reviewDto, UserDTO reporterDto,
		// Validar razon de denuncia
		@NotBlank(message = Constants.MSG_REASON_NOT_VALID) String reason) {

}
