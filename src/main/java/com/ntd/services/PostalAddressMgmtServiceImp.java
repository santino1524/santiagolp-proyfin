package com.ntd.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ntd.dto.PostalAddressDTO;
import com.ntd.dto.mapper.DTOMapperI;
import com.ntd.exceptions.InternalException;
import com.ntd.persistence.PostalAddress;
import com.ntd.persistence.PostalAddressRepositoryI;
import com.ntd.persistence.User;
import com.ntd.utils.ValidateParams;

import lombok.extern.slf4j.Slf4j;

/**
 * Servicio de gestion de direcciones del usuario
 * 
 * @author SLP
 */
@Service
@Slf4j
public class PostalAddressMgmtServiceImp implements PostalAddressMgmtServiceI {

	/** Dependencia de PostalAddreessRepository */
	private final PostalAddressRepositoryI postalAddressRepository;

	/**
	 * Constructor
	 * 
	 * @param postalAddressRepository
	 */
	public PostalAddressMgmtServiceImp(final PostalAddressRepositoryI postalAddressRepository) {
		this.postalAddressRepository = postalAddressRepository;
	}

	@Override
	public PostalAddressDTO insertPostalAddress(PostalAddressDTO postalAddressDto) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Insertar direccion del usuario");

		// Validar parametro
		ValidateParams.isNullObject(postalAddressDto);

		// Mapear DTO y guardar
		final PostalAddress postalAddress = postalAddressRepository
				.save(DTOMapperI.MAPPER.mapDTOToPostalAddress(postalAddressDto));

		// Retornar DTO
		return DTOMapperI.MAPPER.mapPostalAddressToDTO(postalAddress);
	}

	@Override
	public void deletePostalAddress(Long addressId) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Eliminar relacion usuario direccion");

		// Validar campos del id
		ValidateParams.isNullObject(addressId);

		postalAddressRepository.deleteAddress(addressId);
	}

	@Override
	public PostalAddressDTO searchById(Long addressId) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar por direccion por id");

		// Validar campos del id
		ValidateParams.isNullObject(addressId);

		// Buscar id
		PostalAddress postalAddress = postalAddressRepository.findById(addressId).orElse(null);

		// Retornar DTO
		return DTOMapperI.MAPPER.mapPostalAddressToDTO(postalAddress);
	}

	@Override
	public List<PostalAddressDTO> searchByUser(User user) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar direccion por usuario");

		// Validar campos del id
		ValidateParams.isNullObject(user);

		// Buscar id
		List<PostalAddress> addresses = postalAddressRepository.findByUser(user);

		List<PostalAddressDTO> addressesDto = new ArrayList<>();

		for (PostalAddress postalAddress : addresses) {
			addressesDto.add(DTOMapperI.MAPPER.mapPostalAddressToDTO(postalAddress));
		}

		// Retornar DTO
		return addressesDto;
	}

	@Override
	public PostalAddressDTO findByCityDirectionLineProvinceUser(String city, String directionLine, String province,
			User user) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar por direccion por directionLine,city,province,user");

		// Validar campos del id
		ValidateParams.isNullObject(directionLine);
		ValidateParams.isNullObject(city);
		ValidateParams.isNullObject(province);

		// Buscar id
		PostalAddress postalAddress = postalAddressRepository
				.findByCityIgnoreCaseAndDirectionLineIgnoreCaseAndProvinceIgnoreCaseAndUser(city, directionLine,
						province, user);

		// Retornar DTO
		return DTOMapperI.MAPPER.mapPostalAddressToDTO(postalAddress);
	}

}
