package com.ntd.persistence;

import java.io.Serializable;
import java.util.List;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.ntd.security.UserRole;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase Usuario
 * 
 * @author SLP
 */
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
	@Enumerated(EnumType.STRING)
	@Column(name = "C_ROLE", nullable = false)
	private UserRole role;

	/** Indica si el usuario esta bloqueado */
	@Column(name = "C_BLOCKED", nullable = false)
	private boolean blocked;

	/** Direcciones */
	@ManyToMany
	@JoinTable(name = "T_USER_ADDRESS", joinColumns = @JoinColumn(name = "C_USER_ID"), inverseJoinColumns = {
			@JoinColumn(name = "C_ADDRESS_ID") })
	private List<PostalAddress> addresses;

	/** Pedidos */
	@OneToMany(mappedBy = "user")
	@Cascade(CascadeType.ALL)
	private List<Order> orders;

	/** Denuncias del usuario */
	@OneToMany(mappedBy = "reporter")
	@Cascade(CascadeType.ALL)
	private List<Report> reportedReviews;

	/** Listado de preguntas para resetear contrasena */
	@ElementCollection
	@Cascade(value = { CascadeType.ALL })
	@CollectionTable(name = "T_PASSWORD_RESET_QUESTION", joinColumns = @JoinColumn(name = "C_QUESTION_ID"))
	@Column(name = "C_QUESTION", nullable = false)
	private List<String> questions;

	/** Listado de preguntas para resetear contrasena */
	@ElementCollection
	@Cascade(value = { CascadeType.ALL })
	@CollectionTable(name = "T_PASSWORD_RESET_ANSWER", joinColumns = @JoinColumn(name = "C_USER_ID"))
	@Column(name = "C_ANSWER", nullable = false)
	private List<String> answers;
}