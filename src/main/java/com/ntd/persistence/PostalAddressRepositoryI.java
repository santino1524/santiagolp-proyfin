package com.ntd.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;

/**
 * Repositorio T_POSTAL_ADDRESS
 * 
 * @author SLP
 */
public interface PostalAddressRepositoryI extends JpaRepository<PostalAddress, Long> {

	/**
	 * Eliminar direccion asociada a usuario
	 * 
	 * @param userId
	 * @param addressId
	 */
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM T_USER_ADDRESS WHERE C_USER_ID = :userId AND c_address_id = :addressId", nativeQuery = true)
	public void deleteRelationPostalAddress(Long userId, Long addressId);

	/**
	 * Buscar direcciones por usuario
	 * 
	 * @param productId
	 * @return List
	 */
	@Query(value = "SELECT pa.* " + "FROM t_user_address ua"
			+ "JOIN t_postal_address pa ON ua.c_address_id = pa.c_address_id "
			+ "WHERE ua.c_user_id = :userId;", nativeQuery = true)
	public List<PostalAddress> findAddressByUser(Long userId);

	/**
	 * Comprobar existencia por usuario
	 * 
	 * @param userId
	 * @param addressId
	 * @return boolean
	 */
	@Query(value = "SELECT EXISTS (SELECT FROM public.t_user_address "
			+ "    WHERE c_user_id = :userId AND c_address_id = :addressId)", nativeQuery = true)
	public boolean existsByUser(Long userId, Long addressId);

	/**
	 * Insertar relacion entre usuario y direccion
	 * 
	 * @param userId
	 * @param addressId
	 * @return PostalAddress
	 */
	@Query(value = "INSERT INTO public.t_user_address (c_user_id, c_address_id) "
			+ "VALUES (:userId, :addressId);", nativeQuery = true)
	public void insertRelation(Long userId, Long addressId);

	/**
	 * Buscar por linea de direccion, ciudad y provincia
	 * 
	 * @param directionLine
	 * @param city
	 * @param province
	 * @return PostalAddress
	 */
	public PostalAddress findByCityIgnoreCaseDirectionLineIgnoreCaseProvinceIgnoreCase(String directionLine,
			String city, String province);

}
