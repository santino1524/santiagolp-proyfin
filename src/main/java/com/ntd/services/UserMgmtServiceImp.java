package com.ntd.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ntd.dto.UserDTO;
import com.ntd.dto.mapper.DTOMapperI;
import com.ntd.exceptions.InternalException;
import com.ntd.persistence.User;
import com.ntd.persistence.UserRepositoryI;
import com.ntd.utils.ValidateParams;

import lombok.extern.slf4j.Slf4j;

/**
 * Servicio de gestion de usuarios
 * 
 * @author SLP
 */
@Service
@Slf4j
public class UserMgmtServiceImp implements UserMgmtServiceI {

	/** Dependencia de UserRepository */
	private final UserRepositoryI userRepository;

	/**
	 * Constructor
	 * 
	 * @param userRepository
	 */
	public UserMgmtServiceImp(final UserRepositoryI userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDTO insertUser(UserDTO userDto) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Insertar usuario");

		// Validar parametro
		ValidateParams.isNullObject(userDto);

		// Mapear DTO y guardar
		final User user = userRepository.save(DTOMapperI.MAPPER.mapDTOToUser(userDto));

		// Retornar DTO
		return DTOMapperI.MAPPER.mapUserToDTO(user);
	}

	@Override
	public UserDTO updateUser(UserDTO userDto) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Actualizar usuario");

		// Validar parametro
		ValidateParams.isNullObject(userDto);

		// Mapear DTO y actualizar
		final User user = userRepository.save(DTOMapperI.MAPPER.mapDTOToUser(userDto));

		// Retornar DTO
		return DTOMapperI.MAPPER.mapUserToDTO(user);
	}

	@Override
	public void deleteUser(Long id) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Eliminar usuario");

		// Validar parametro
		ValidateParams.isNullObject(id);

		// Eliminar por Id
		userRepository.deleteById(id);
	}

	@Override
	public List<UserDTO> searchAll() {
		if (log.isInfoEnabled())
			log.info("Buscar todos los usuarios");

		// Buscar todos los usuarios
		final List<User> users = userRepository.findAll();

		// Mapear DTO
		final List<UserDTO> usersDto = new ArrayList<>();

		if (!users.isEmpty()) {
			for (User user : users) {
				usersDto.add(DTOMapperI.MAPPER.mapUserToDTO(user));
			}
		}

		// Retornar lista DTO
		return usersDto;

	}

	@Override
	public boolean existsDni(String dni) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Verificar si existe el DNI");

		// Validar parametro
		ValidateParams.isNullObject(dni);

		// Retornar si existe el usuario en BBDD
		return userRepository.existsByDni(dni);
	}

	@Override
	public boolean existsPhoneNumber(String phoneNumber) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Verificar si existe el numero telefonico");

		// Validar parametro
		ValidateParams.isNullObject(phoneNumber);

		// Retornar si existe el numero telefonico
		return userRepository.existsByPhoneNumber(phoneNumber);
	}

	@Override
	public boolean existsEmail(String email) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Verificar si existe el email");

		// Validar parametro
		ValidateParams.isNullObject(email);

		// Retornar si existe el email
		return userRepository.existsByEmail(email);
	}

	@Override
	public UserDTO searchByDni(String dni) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar usuario por DNI");

		// Validar parametro
		ValidateParams.isNullObject(dni);

		// Buscar por DNI
		final User user = userRepository.findByDniIgnoreCase(dni);

		// Retornar DTO
		return DTOMapperI.MAPPER.mapUserToDTO(user);
	}

	@Override
	public UserDTO searchByEmail(String email) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar usuario por email");

		// Validar parametro
		ValidateParams.isNullObject(email);

		// Buscar por email
		final User user = userRepository.findByEmailIgnoreCase(email);

		// Retornar DTO
		return DTOMapperI.MAPPER.mapUserToDTO(user);
	}

	@Override
	public UserDTO searchByPhoneNumber(String phoneNumber) throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar usuario por numero telefonico");

		// Validar parametro
		ValidateParams.isNullObject(phoneNumber);

		// Buscar por numero telefonico
		final User user = userRepository.findByPhoneNumber(phoneNumber);

		// Retornar DTO
		return DTOMapperI.MAPPER.mapUserToDTO(user);
	}

	@Override
	public List<UserDTO> searchByNameOrSurnameOrSecondSurname(String name, String surname, String secondSurname)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar usuario por nombre y apellidos");

		// Validar parametros
		ValidateParams.isNullObject(name);
		ValidateParams.isNullObject(surname);
		ValidateParams.isNullObject(secondSurname);

		// Buscar por nombre y apellidos
		final List<User> users = userRepository
				.findByNameIgnoreCaseContainingOrSurnameIgnoreCaseContainingOrSecondSurnameIgnoreCaseContaining(name,
						surname, secondSurname);

		// Mapear DTO
		final List<UserDTO> usersDto = new ArrayList<>();

		if (!users.isEmpty()) {
			for (User user : users) {
				usersDto.add(DTOMapperI.MAPPER.mapUserToDTO(user));
			}
		}

		// Retornar lista DTO
		return usersDto;

	}

	@Override
	public List<UserDTO> searchByDniOrEmailOrPhoneNumber(String dni, String email, String phoneNumber)
			throws InternalException {
		if (log.isInfoEnabled())
			log.info("Buscar usuario por dni o email o telefono");

		// Validar parametros
		ValidateParams.isNullObject(dni);
		ValidateParams.isNullObject(email);
		ValidateParams.isNullObject(phoneNumber);

		// Buscar por dni o email o telefono
		final List<User> users = userRepository.findByDniIgnoreCaseOrEmailIgnoreCaseOrPhoneNumber(dni, email,
				phoneNumber);

		// Mapear DTO
		final List<UserDTO> usersDto = new ArrayList<>();

		if (!users.isEmpty()) {
			for (User user : users) {
				usersDto.add(DTOMapperI.MAPPER.mapUserToDTO(user));
			}
		}

		// Retornar lista DTO
		return usersDto;

	}

}
