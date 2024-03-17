package com.ntd.dto;

import com.ntd.utils.Constants;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO Reporte
 * 
 * @author SLP
 */
public record PasswordResetQuestionDTO(Long questionId,
		// Validar pregunta
		@NotBlank(message = Constants.MSG_QUESTION_NOT_VALID) String question,

		// Validar respuesta
		@NotBlank(message = Constants.MSG_ANSWER_NOT_VALID) String answer,

		UserDTO userDto) {

}
