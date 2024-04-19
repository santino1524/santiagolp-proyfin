package com.ntd.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio T_USER
 * 
 * @author SLP
 */
public interface UserRepositoryI extends JpaRepository<User, Long> {

	/**
	 * Comprobar existencia de usuario por DNI
	 * 
	 * @param dni
	 * @return boolean
	 */
	public boolean existsByDni(String dni);

	/**
	 * Comprobar existencia del numero telefonico
	 * 
	 * @param phoneNumber
	 * @return boolean
	 */
	public boolean existsByPhoneNumber(String phoneNumber);

	/**
	 * Comprobar existencia del email
	 * 
	 * @param email
	 * @return boolean
	 */
	public boolean existsByEmail(String email);

	/**
	 * Buscar por DNI
	 * 
	 * @param dni
	 * @return User
	 */
	public User findByDniIgnoreCase(String dni);

	/**
	 * Buscar por email
	 * 
	 * @param email
	 * @return User
	 */
	public User findByEmailIgnoreCase(String email);

	/**
	 * Buscar por numero de telefono
	 * 
	 * @param phoneNumber
	 * @return User
	 */
	public User findByPhoneNumber(String phoneNumber);

	/**
	 * Buscar por nombre y apellidos
	 * 
	 * @param name
	 * @param surname
	 * @param secondSurname
	 * @return List
	 */
	public List<User> findByNameIgnoreCaseContainingOrSurnameIgnoreCaseContainingOrSecondSurnameIgnoreCaseContaining(
			String name, String surname, String secondSurname);

	/**
	 * Buscar por rol
	 * 
	 * @param role
	 * @return List
	 */
	public List<User> findByRole(Integer role);

	/**
	 * Buscar usuarios por dni o email o telefono
	 * 
	 * @param dni
	 * @param email
	 * @param phoneNumber
	 * @return List
	 */
	public List<User> findByDniIgnoreCaseOrEmailIgnoreCaseOrPhoneNumber(String dni, String email, String phoneNumber);

}
