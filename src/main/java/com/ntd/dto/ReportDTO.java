package com.ntd.dto;

/**
 * DTO Reporte
 * 
 * @author SLP
 */
public record ReportDTO(Long reportId, ProductReviewDTO reviewDto, UserDTO reporterDto, boolean processed) {

}
