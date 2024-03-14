package com.ntd.controllers.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ntd.dto.ReportDTO;
import com.ntd.exceptions.InternalException;
import com.ntd.services.ReportMgmtServiceI;
import com.ntd.utils.Constants;
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
@RestController
@RequestMapping("/rest/report")
public class ReportControllerRest {

	/** Dependencia del servicio de gestion de productos */
	private final ReportMgmtServiceI reportMgmtService;

	/**
	 * Constructor
	 * 
	 * @param reportMgmtService
	 */
	public ReportControllerRest(final ReportMgmtServiceI reportMgmtService) {
		this.reportMgmtService = reportMgmtService;
	}

	/**
	 * Registrar Reporte
	 * 
	 * @param report
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@PostMapping
	public ResponseEntity<String> saveReport(@RequestBody @Valid final ReportDTO reportDto) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Registrar Report");

		ResponseEntity<String> result = null;

		// Guardar Report
		if (reportMgmtService.insertReport(reportDto) != null) {
			result = ResponseEntity.status(HttpStatus.ACCEPTED).body(Constants.MSG_SUCCESSFUL_OPERATION);
		} else {
			result = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Constants.MSG_UNEXPECTED_ERROR);
		}

		return result;
	}

	/**
	 * Eliminar Reporte
	 * 
	 * @param reportDto
	 * @return ResponseEntity
	 * @throws InternalException
	 */
	@Transactional
	@DeleteMapping
	public ResponseEntity<String> deleteReport(@RequestBody @NotNull final ReportDTO reportDto)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Eliminar Report");

		// Validar id
		ValidateParams.isNullObject(reportDto.reportId());

		// Eliminar Report
		reportMgmtService.deleteReport(reportDto.reportId());

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(Constants.MSG_SUCCESSFUL_OPERATION);
	}

	/**
	 * Buscar todos los Reportes
	 * 
	 * @return List
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchAll")
	public List<ReportDTO> showReport() throws InternalException {
		if (log.isInfoEnabled())
			log.info("Mostrar todos los Report");

		// Retornar lista de Report
		return reportMgmtService.searchAll();
	}

	/**
	 * Buscar por id
	 * 
	 * @param id
	 * @return ReportDTO
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchById")
	public ReportDTO searchById(@RequestParam @NotNull final Long id) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar Report por id");

		// Retornar Report
		return reportMgmtService.searchById(id);
	}
}
