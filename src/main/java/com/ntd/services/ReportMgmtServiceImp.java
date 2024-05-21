package com.ntd.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ntd.dto.ReportDTO;
import com.ntd.dto.UserDTO;
import com.ntd.dto.mapper.DTOMapperI;
import com.ntd.exceptions.InternalException;
import com.ntd.persistence.Report;
import com.ntd.persistence.ReportRepositoryI;
import com.ntd.utils.ValidateParams;

import lombok.extern.slf4j.Slf4j;

/**
 * Servicio de gestion de Reportes
 * 
 * @author SLP
 */
@Service
@Slf4j
public class ReportMgmtServiceImp implements ReportMgmtServiceI {

	/** Dependencia de ReportRepository */
	private final ReportRepositoryI reportRepository;

	/**
	 * Constructor
	 * 
	 * @param reportRepository
	 */
	public ReportMgmtServiceImp(final ReportRepositoryI reportRepository) {
		this.reportRepository = reportRepository;
	}

	@Override
	public ReportDTO insertReport(ReportDTO reportDto) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Insertar reporte");

		// Validar parametro
		ValidateParams.isNullObject(reportDto);

		// Mapear DTO y guardar
		final Report report = reportRepository.save(DTOMapperI.MAPPER.mapDTOToReport(reportDto));

		// Retornar DTO
		return DTOMapperI.MAPPER.mapReportToDTO(report);
	}

	@Override
	public void deleteReport(Long id) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Eliminar reporte");

		// Validar parametro
		ValidateParams.isNullObject(id);

		// Eliminar por Id
		reportRepository.deleteById(id);

	}

	@Override
	public List<ReportDTO> searchAll() {
		if (log.isInfoEnabled())
			log.info("Buscar todos los reportes");

		// Buscar todos los reportes
		final List<Report> reports = reportRepository.findAll();

		// Mapear DTO
		final List<ReportDTO> reportsDto = new ArrayList<>();

		if (!reports.isEmpty()) {
			for (Report report : reports) {
				reportsDto.add(DTOMapperI.MAPPER.mapReportToDTO(report));
			}
		}

		// Retornar lista DTO
		return reportsDto;
	}

	@Override
	public ReportDTO searchById(Long id) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar reporte por id");

		// Validar parametro
		ValidateParams.isNullObject(id);

		// Buscar por id
		final Report report = reportRepository.findById(id).orElseThrow();

		// Retornar DTO
		return DTOMapperI.MAPPER.mapReportToDTO(report);
	}

	@Override
	public int countByProcessedEquals(boolean status) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Contar cantidad de reportes sin procesar");

		// Validar parametro
		ValidateParams.isNullObject(status);

		return reportRepository.countByProcessedEquals(status);
	}

	@Override
	public List<ReportDTO> findByProcessedEquals(boolean status) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar los reportes sin procesar");

		// Validar parametro
		ValidateParams.isNullObject(status);

		return DTOMapperI.MAPPER.listReportToDTO(reportRepository.findByProcessedEquals(status));
	}

	@Override
	public int countByReporter(UserDTO userDto) throws InternalException {
		if (log.isInfoEnabled())
			log.info("COntar cantidad de reportes por usuario");

		// Validar parametro
		ValidateParams.isNullObject(userDto);

		return reportRepository.countByReporter(DTOMapperI.MAPPER.mapDTOToUser(userDto));
	}

}
