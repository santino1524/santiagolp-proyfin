package com.ntd.dto;

import java.util.List;

import com.ntd.utils.Constants;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * Respuestas para resetear contrasenna
 * 
 * @author slopezpi
 */
public record AnswersDTO(
		// Validar Id
		@NotNull(message = Constants.MSG_USER_NOT_VALID) Long userId,

		// Validar respuestas
		@NotEmpty(message = Constants.MSG_ANSWER_NOT_VALID) List<String> answers,

		String passwd) {
}
