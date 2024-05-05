package com.ntd.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio T_POSTAL_ADDRESS
 * 
 * @author SLP
 */
public interface PostalAddressRepositoryI extends JpaRepository<PostalAddress, Long> {

	/**
	 * Buscar direcciones por usuario
	 * 
	 * @param user
	 * @return List
	 */
	public List<PostalAddress> findByUser(User user);

	/**
	 * Buscar por linea de direccion, ciudad y provincia
	 * 
	 * @param directionLine
	 * @param city
	 * @param province
	 * @param user
	 * @return PostalAddress
	 */
	public PostalAddress findByCityIgnoreCaseAndDirectionLineIgnoreCaseAndProvinceIgnoreCaseAndUser(String city,
			String directionLine, String province, User user);

}
