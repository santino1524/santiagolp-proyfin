package com.ntd.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ntd.dto.PostalAddressDTO;
import com.ntd.dto.mapper.DTOMapperI;
import com.ntd.exceptions.InternalException;
import com.ntd.persistence.PostalAddress;
import com.ntd.persistence.PostalAddressRepositoryI;
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
	 * @param userRepository
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
	public void deleteRelationPostalAddress(Long userId, Long addressId) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Eliminar relacion usuario direccion");

		// Validar campos del id
		ValidateParams.isNullObject(userId);
		ValidateParams.isNullObject(addressId);

		postalAddressRepository.deleteRelationPostalAddress(userId, addressId);
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
	public List<PostalAddressDTO> searchByUser(Long userId) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar direccion por usuario");

		// Validar campos del id
		ValidateParams.isNullObject(userId);

		// Buscar id
		List<PostalAddress> addresses = postalAddressRepository.findAddressByUser(userId);

		List<PostalAddressDTO> addressesDto = new ArrayList<>();

		for (PostalAddress postalAddress : addresses) {
			addressesDto.add(DTOMapperI.MAPPER.mapPostalAddressToDTO(postalAddress));
		}

		// Retornar DTO
		return addressesDto;
	}

	@Override
	public boolean existsByUser(Long userId, Long addressId) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Comprobar existencia por usuario");

		// Validar campos del id
		ValidateParams.isNullObject(userId);
		ValidateParams.isNullObject(addressId);

		return postalAddressRepository.existsByUser(userId, addressId);
	}

	@Override
	public PostalAddressDTO findByCityDirectionLineProvince(String directionLine, String city, String province)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar por direccion por directionLine,city,province");

		// Validar campos del id
		ValidateParams.isNullObject(directionLine);
		ValidateParams.isNullObject(city);
		ValidateParams.isNullObject(province);

		// Buscar id
		PostalAddress postalAddress = postalAddressRepository
				.findByCityIgnoreCaseDirectionLineIgnoreCaseProvinceIgnoreCase(directionLine, city, province);

		// Retornar DTO
		return DTOMapperI.MAPPER.mapPostalAddressToDTO(postalAddress);
	}

	@Override
	public void insertRelation(Long userId, Long addressId) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Insertar relacion usuario direccion");

		// Validar campos del id
		ValidateParams.isNullObject(userId);
		ValidateParams.isNullObject(addressId);

		postalAddressRepository.insertRelation(userId, addressId);
	}
}
