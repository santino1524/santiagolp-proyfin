package com.ntd.controllers;

import java.util.Collections;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ntd.dto.ReportDTO;
import com.ntd.dto.UserDTO;
import com.ntd.exceptions.InternalException;
import com.ntd.services.ReportMgmtServiceI;
import com.ntd.utils.ValidateParams;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador de Reportes
 * 
 * @author SLP
 */
@Slf4j
@Controller
@RequestMapping("/report")
public class ReportController {

	/** Dependencia del servicio de gestion de productos */
	private final ReportMgmtServiceI reportMgmtService;

	/**
	 * Constructor
	 * 
	 * @param reportMgmtService
	 */
	public ReportController(final ReportMgmtServiceI reportMgmtService) {
		this.reportMgmtService = reportMgmtService;
	}

	/**
	 * Registrar Reporte
	 * 
	 * @param reportDto
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@PostMapping(path = "/save")
	public ResponseEntity<Object> saveReport(@RequestBody @Valid final ReportDTO reportDto) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Registrar Report");

		return ResponseEntity.ok().body(Collections.singletonMap("report", reportMgmtService.insertReport(reportDto)));
	}

	/**
	 * Contar cantidad de reportes sin procesar
	 * 
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@GetMapping(path = "/countByStatus")
	public ResponseEntity<Integer> countByStatusCreado() throws InternalException {
		log.info("Retornar cantidad de reportes sin procesar");

		return ResponseEntity.ok(reportMgmtService.countByProcessedEquals(false));
	}

	/**
	 * Contar cantidad de reportes por usuario
	 * 
	 * @param userDto
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@PostMapping(path = "/countByReporter")
	public ResponseEntity<Integer> countByReporter(@RequestBody @Valid final UserDTO userDto) throws InternalException {
		log.info("Contar cantidad de reportes por usuario");

		return ResponseEntity.ok(reportMgmtService.countByReporter(userDto));
	}

	/**
	 * Obtener reportes sin procesar
	 * 
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchByWithoutProcessing")
	public ResponseEntity<Object> searchByWithoutProcessing() throws InternalException {
		log.info("Buscar los reportes sin procesar");

		return ResponseEntity.ok()
				.body(Collections.singletonMap("reports", reportMgmtService.findByProcessedEquals(false)));

	}

	/**
	 * Eliminar Reporte
	 * 
	 * @param reportDto
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@Transactional
	@DeleteMapping(path = "/delete")
	public ResponseEntity<Void> deleteReport(@RequestBody @NotNull final ReportDTO reportDto) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Eliminar Report");

		// Validar id
		ValidateParams.isNullObject(reportDto);

		// Eliminar Report
		reportMgmtService.deleteReport(reportDto.reportId());

		return ResponseEntity.ok().build();
	}
}
