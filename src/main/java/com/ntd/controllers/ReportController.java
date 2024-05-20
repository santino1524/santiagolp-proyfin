package com.ntd.controllers;

import java.util.Collections;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	 * Eliminar Reporte
	 * 
	 * @param reportDto
	 * @param model
	 * @return String
	 * @throws InternalException
	 */
	@Transactional
	@DeleteMapping
	public String deleteReport(@RequestBody @NotNull final ReportDTO reportDto, final Model model)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Eliminar Report");

		// Validar id
		ValidateParams.isNullObject(reportDto.reportId());

		// Eliminar Report
		reportMgmtService.deleteReport(reportDto.reportId());

		model.addAttribute(Constants.MESSAGE_GROWL, Constants.MSG_SUCCESSFUL_OPERATION);

		return "VISTA MOSTRAR RESPUESTA DE  Report ELIMINADO";
	}

	/**
	 * Buscar todos los Reportes
	 * 
	 * @param model
	 * @return String
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchAll")
	public String showReport(final Model model) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Mostrar todos los Report");

		// Retornar lista de Report
		model.addAttribute("reviewsDto", reportMgmtService.searchAll());

		return "VISTA BUSCAR TODOS LOS Report";
	}

	/**
	 * Buscar por id
	 * 
	 * @param model
	 * @param id
	 * @return String
	 * @throws InternalException
	 */
	@GetMapping(path = "/searchById")
	public String searchById(@RequestParam @NotNull final Long id, final Model model) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar Report por id");

		// Retornar Report
		model.addAttribute("reportDto", reportMgmtService.searchById(id));

		return "VISTA BUSCAR Report POR id";
	}
}
