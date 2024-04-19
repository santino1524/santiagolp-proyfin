package com.ntd.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ntd.utils.Constants;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Controlador para manejar errores del servidor
 */
@Controller
public class ErrController implements ErrorController {

	/**
	 * Maneja las solicitudes de error y devuelve la vista de error correspondiente.
	 *
	 * @param model Objeto de modelo para pasar datos a la vista.
	 * @return Vista de error personalizada.
	 */
	@GetMapping(Constants.URL_ERROR_VIEW)
	public String handleError(final Model model, final HttpServletRequest request) {
		// Obtiene el codigo de estado HTTP del atributo de solicitud
		Object status = request.getAttribute("jakarta.servlet.error.status_code");

		if (status != null) {
			int statusCode = Integer.parseInt(status.toString());

			if (statusCode == HttpStatus.NOT_FOUND.value()) {
				// Manejo de errores 404 (Recurso no encontrado)
				model.addAttribute("message", "La página que buscas no existe.");
				return Constants.URL_ERROR_VIEW;
			}

		}
		return null;

	}

}
