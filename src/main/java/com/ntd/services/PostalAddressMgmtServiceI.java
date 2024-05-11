package com.ntd.services;

import java.util.List;

import com.ntd.dto.PostalAddressDTO;
import com.ntd.exceptions.InternalException;
import com.ntd.persistence.User;

import jakarta.transaction.Transactional;

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
	 * Eliminar direccion
	 * 
	 * @param addressId
	 * @throws InternalException
	 */
	@Transactional
	public void deletePostalAddress(final Long addressId) throws InternalException;

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
	public List<PostalAddressDTO> searchByUser(final User user) throws InternalException;

	/**
	 * Buscar por linea de direccion, ciudad y provincia
	 * 
	 * @param directionLine
	 * @param city
	 * @param province
	 * @param user
	 * @return PostalAddress
	 * @throws InternalException
	 */
	public PostalAddressDTO findByCityDirectionLineProvinceUser(String directionLine, String city, String province,
			User user) throws InternalException;

}
