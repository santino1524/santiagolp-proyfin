package com.ntd.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio T_POSTAL_ADDRESS
 * 
 * @author SLP
 */
public interface PostalAddressRepositoryI extends JpaRepository<PostalAddress, AddressId> {

}
