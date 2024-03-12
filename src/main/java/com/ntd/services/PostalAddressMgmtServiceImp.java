package com.ntd.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ntd.dto.PostalAddressDTO;
import com.ntd.dto.mapper.DTOMapperI;
import com.ntd.exceptions.InternalException;
import com.ntd.persistence.AddressId;
import com.ntd.persistence.PostalAddress;
import com.ntd.persistence.PostalAddressRepositoryI;
import com.ntd.persistence.User;
import com.ntd.persistence.UserRepositoryI;
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

	/** Dependencia de UserRepository */
	private final UserRepositoryI userRepository;

	/**
	 * Constructor
	 * 
	 * @param postalAddressRepository
	 * @param userRepository
	 */
	public PostalAddressMgmtServiceImp(final PostalAddressRepositoryI postalAddressRepository,
			UserRepositoryI userRepository) {
		this.postalAddressRepository = postalAddressRepository;
		this.userRepository = userRepository;
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
	public void deletePostalAddress(PostalAddressDTO postalAddressDto) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Eliminar direccion del usuario");

		// Validar campos del id
		ValidateParams.isNullObject(postalAddressDto.city());
		ValidateParams.isNullObject(postalAddressDto.directionLine());
		ValidateParams.isNullObject(postalAddressDto.province());

		// Construir id
		AddressId id = new AddressId(postalAddressDto.directionLine(), postalAddressDto.city(),
				postalAddressDto.province());

		// Eliminar direccion del usuario
		PostalAddress postalAddress = postalAddressRepository.findById(id).orElseThrow(InternalException::new);
		if (!postalAddress.getUsers().isEmpty()) {
			for (User user : postalAddress.getUsers()) {
				user.getAddresses().remove(postalAddress);
				userRepository.save(user);
			}
		}

		// Eliminar por Id
		postalAddressRepository.deleteById(id);

	}

	@Override
	public List<PostalAddressDTO> searchAll() {
		if (log.isInfoEnabled())
			log.info("Buscar todos las direcciones del usuario");

		// Buscar todas las direcciones
		final List<PostalAddress> addresses = postalAddressRepository.findAll();

		// Mapear DTO
		final List<PostalAddressDTO> addressesDto = new ArrayList<>();

		if (!addresses.isEmpty()) {
			for (PostalAddress postalAddress : addresses) {
				addressesDto.add(DTOMapperI.MAPPER.mapPostalAddressToDTO(postalAddress));
			}
		}

		// Retornar lista DTO
		return addressesDto;
	}

	@Override
	public PostalAddressDTO searchById(PostalAddressDTO postalAddressDto) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar por direccion por id");

		// Validar campos del id
		ValidateParams.isNullObject(postalAddressDto.city());
		ValidateParams.isNullObject(postalAddressDto.directionLine());
		ValidateParams.isNullObject(postalAddressDto.province());

		// Construir id
		AddressId id = new AddressId(postalAddressDto.directionLine(), postalAddressDto.city(),
				postalAddressDto.province());

		// Buscar id
		PostalAddress postalAddress = postalAddressRepository.findById(id).orElse(null);

		// Retornar DTO
		return DTOMapperI.MAPPER.mapPostalAddressToDTO(postalAddress);
	}

}
