package com.ntd.persistence;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Clase Usuario
 * 
 * @author SLP
 */
@Entity
@Setter
@Getter
@Table(name = "T_USER")
public class User implements Serializable {

	/** Serial Version */
	private static final long serialVersionUID = -5240072470881647002L;

	/** Identificador (PK) */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "C_USER_ID")
	private Long userId;

	/** Nombre */
	@Column(name = "C_NAME", nullable = false)
	private String name;

	/** Primer apellido */
	@Column(name = "C_SURNAME", nullable = false)
	private String surname;

	/** Segundo apellido */
	@Column(name = "C_SECOND_SURNAME", nullable = false)
	private String secondSurname;

	/** DNI */
	@Column(name = "C_DNI", length = 9, nullable = false, unique = true)
	private String dni;

	/** Correo electronico */
	@Column(name = "C_EMAIL", nullable = false, unique = true)
	private String email;

	/** Contrasena */
	@Column(name = "C_PASSWD", nullable = false)
	private String passwd;

	/** Numero telefonico */
	@Column(name = "C_PHONE_NUMBER", nullable = false, unique = true)
	private String phoneNumber;

	/** Rol */
	@Column(name = "C_ROLE", nullable = false)
	private String role;

	/** Indica si el usuario esta bloqueado */
	@Column(name = "C_BLOCKED", nullable = false)
	private boolean blocked;

	/** Direcciones */
	@ManyToMany
	@JoinTable(name = "T_USER_ADDRESS", joinColumns = @JoinColumn(name = "C_USER_ID"), inverseJoinColumns = {
			@JoinColumn(name = "C_DIRECTION_LINE"), @JoinColumn(name = "C_PROVINCE"), @JoinColumn(name = "C_CITY") })
	private List<PostalAddress> addresses;

	/** Pedidos */
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Order> orders;

	/** Denuncias del usuario */
	@OneToMany(mappedBy = "reporter")
	private List<Report> reportedReviews;
}