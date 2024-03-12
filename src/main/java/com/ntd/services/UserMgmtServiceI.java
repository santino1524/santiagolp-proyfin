package com.ntd.services;

import java.util.List;

import com.ntd.dto.UserDTO;
import com.ntd.exceptions.InternalException;

/**
 * Servicio de gestion de usuarios
 * 
 * @author SLP
 */
public interface UserMgmtServiceI {

	/**
	 * Insertar nuevo usuario
	 * 
	 * @param userDto
	 * @return UserDTO
	 * @throws InternalException
	 */
	public UserDTO insertUser(final UserDTO userDto) throws InternalException;

	/**
	 * Actualizar nuevo usuario
	 * 
	 * @param userDto
	 * @return UserDTO
	 * @throws InternalException
	 */
	public UserDTO updateUser(final UserDTO userDto) throws InternalException;

	/**
	 * Eliminar usuario
	 * 
	 * @param id
	 * @throws InternalException
	 */
	public void deleteUser(final Long id) throws InternalException;

	/**
	 * Buscar todos los usuarios
	 * 
	 * @return List
	 */
	public List<UserDTO> searchAll();

	/**
	 * Comprobar existencia de usuario por DNI
	 * 
	 * @param dni
	 * @return boolean
	 * @throws InternalException
	 */
	public boolean existsDni(final String dni) throws InternalException;

	/**
	 * Comprobar existencia del numero telefonico
	 * 
	 * @param phoneNumber
	 * @return boolean
	 * @throws InternalException
	 */
	public boolean existsPhoneNumber(final String phoneNumber) throws InternalException;

	/**
	 * Comprobar existencia del email
	 * 
	 * @param email
	 * @return boolean
	 * @throws InternalException
	 */
	public boolean existsEmail(final String email) throws InternalException;

	/**
	 * Buscar por DNI
	 * 
	 * @param dni
	 * @return UserDTO
	 * @throws InternalException
	 */
	public UserDTO searchByDni(final String dni) throws InternalException;

	/**
	 * Buscar por email
	 * 
	 * @param email
	 * @return UserDTO
	 * @throws InternalException
	 */
	public UserDTO searchByEmail(final String email) throws InternalException;

	/**
	 * Buscar por numero de telefono
	 * 
	 * @param phoneNumber
	 * @return UserDTO
	 * @throws InternalException
	 */
	public UserDTO searchByPhoneNumber(final String phoneNumber) throws InternalException;

	/**
	 * Buscar por nombre y apellidos
	 * 
	 * @param name
	 * @param surname
	 * @param secondSurname
	 * @return List
	 * @throws InternalException
	 */
	public List<UserDTO> searchByNameOrSurnameOrSecondSurname(final String name, final String surname,
			final String secondSurname) throws InternalException;

	/**
	 * Buscar usuarios por dni o email o telefono
	 * 
	 * @param dni
	 * @param email
	 * @param phoneNumber
	 * @return List
	 * @throws InternalException
	 */
	public List<UserDTO> searchByDniOrEmailOrPhoneNumber(final String dni, final String email, final String phoneNumber)
			throws InternalException;

}
