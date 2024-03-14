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
	 * Eliminar direccion del usuario
	 * 
	 * @param postalAddressDto
	 * @throws InternalException
	 */
	public void deletePostalAddress(final PostalAddressDTO postalAddressDto) throws InternalException;

	/**
	 * Eliminar asociacion con direccion del usuario
	 * 
	 * @param userId
	 * @param city
	 * @param directionLine
	 * @param province
	 * @throws InternalException
	 */
	public void deleteRelationPostalAddress(final Long userId, final String city, final String directionLine,
			final String province) throws InternalException;

	/**
	 * Buscar todas las direcciones del usuario
	 * 
	 * @return List
	 */
	public List<PostalAddressDTO> searchAll();

	/**
	 * Buscar por direccion por id
	 * 
	 * @param postalAddressDto
	 * @return PostalAddressDTO
	 * @throws InternalException
	 */
	public PostalAddressDTO searchById(final PostalAddressDTO postalAddressDto) throws InternalException;

}
