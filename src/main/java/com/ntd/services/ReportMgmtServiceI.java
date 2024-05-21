package com.ntd.services;

import java.util.List;

import com.ntd.dto.ReportDTO;
import com.ntd.dto.UserDTO;
import com.ntd.exceptions.InternalException;

/**
 * Servicio de gestion de Reportes
 * 
 * @author SLP
 */
public interface ReportMgmtServiceI {

	/**
	 * Insertar nuevo Reporte
	 * 
	 * @param reportDto
	 * @return ReportDTO
	 * @throws InternalException
	 */
	public ReportDTO insertReport(final ReportDTO reportDto) throws InternalException;

	/**
	 * Eliminar Reporte
	 * 
	 * @param id
	 * @throws InternalException
	 */
	public void deleteReport(final Long id) throws InternalException;

	/**
	 * Buscar todos los Reportes
	 * 
	 * @return List
	 */
	public List<ReportDTO> searchAll();

	/**
	 * Buscar el Reporte por id
	 * 
	 * @param id
	 * @return ReportDTO
	 * @throws InternalException
	 */
	public ReportDTO searchById(final Long id) throws InternalException;

	/**
	 * Contar los reportes sin procesar
	 * 
	 * @param status
	 * @return int
	 * @throws InternalException
	 */
	public int countByProcessedEquals(boolean status) throws InternalException;

	/**
	 * Buscar los reportes sin procesar
	 * 
	 * @param status
	 * @return List
	 * @throws InternalException
	 */
	public List<ReportDTO> findByProcessedEquals(boolean status) throws InternalException;

	/**
	 * Contar los reportes de un usuario denunciante
	 * 
	 * @param userDto
	 * @return int
	 * @throws InternalException
	 */
	public int countByReporter(UserDTO userDto) throws InternalException;

}
