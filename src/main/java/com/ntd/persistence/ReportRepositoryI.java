package com.ntd.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio T_REPORT
 * 
 * @author SLP
 */
public interface ReportRepositoryI extends JpaRepository<Report, Long> {

	/**
	 * Contar los reportes sin procesar
	 * 
	 * @param status
	 * @return int
	 */
	public int countByProcessedEquals(boolean status);

	/**
	 * Contar los reportes de un usuario denunciante
	 * 
	 * @param user
	 * @return int
	 */
	public int countByReporter(User user);

	/**
	 * Buscar los reportes sin procesar
	 * 
	 * @param status
	 * @return List
	 */
	public List<Report> findByProcessedEquals(boolean status);

}
