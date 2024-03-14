package com.ntd.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;

/**
 * Repositorio T_POSTAL_ADDRESS
 * 
 * @author SLP
 */
public interface PostalAddressRepositoryI extends JpaRepository<PostalAddress, AddressId> {

	/**
	 * Eliminar direccion asociada a usuario
	 * 
	 * @param userId
	 * @param city
	 * @param directionLine
	 * @param province
	 */
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM T_USER_ADDRESS WHERE C_USER_ID = :userId AND C_CITY = :city AND C_DIRECTION_LINE = :directionLine AND C_PROVINCE = :province", nativeQuery = true)
	public void deleteRelationPostalAddress(Long userId, String city, String directionLine, String province);

}
