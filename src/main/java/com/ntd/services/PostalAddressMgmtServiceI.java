package com.ntd.services;

import java.util.List;

import com.ntd.dto.PostalAddressDTO;
import com.ntd.exceptions.InternalException;

/**
 * Servicio de gestion de direcciones del usuario
 * 
 * @author SLP
 */
public interface PostalAddressMgmtServiceI {

	/**
	 * Insertar nueva direccion del usuario
	 * 
	 * @param postalAddressDto
	 * @return PostalAddressDTO
	 * @throws InternalException
	 */
	public PostalAddressDTO insertPostalAddress(final PostalAddressDTO postalAddressDto) throws InternalException;

	/**
	 * Eliminar asociacion con direccion del usuario
	 * 
	 * @param userId
	 * @param addressId
	 * @throws InternalException
	 */
	public void deleteRelationPostalAddress(final Long userId, final Long addressId) throws InternalException;

	/**
	 * Buscar por direccion por id
	 * 
	 * @param addressId
	 * @return
	 * @throws InternalException
	 */
	public PostalAddressDTO searchById(final Long addressId) throws InternalException;

	/**
	 * Buscar direccion por usuario
	 * 
	 * @param userId
	 * @return List
	 * @throws InternalException
	 */
	public List<PostalAddressDTO> searchByUser(final Long userId) throws InternalException;

	/**
	 * Comprobar existencia por usuario
	 * 
	 * @param userId
	 * @param addressId
	 * @return boolean
	 * @throws InternalException
	 */
	public boolean existsByUser(Long userId, Long addressId) throws InternalException;

	/**
	 * Buscar por linea de direccion, ciudad y provincia
	 * 
	 * @param directionLine
	 * @param city
	 * @param province
	 * @return PostalAddress
	 * @throws InternalException
	 */
	public PostalAddressDTO findByCityDirectionLineProvince(String directionLine, String city, String province)
			throws InternalException;

	/**
	 * Insertar relacion entre usuario y direccion
	 * 
	 * @param userId
	 * @param addressId
	 * @throws InternalException
	 */
	public void insertRelation(Long userId, Long addressId) throws InternalException;

}
