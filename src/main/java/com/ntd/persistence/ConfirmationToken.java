package com.ntd.persistence;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Token de confirmacion
 * 
 * @author slopezpi
 */
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "T_TOKEN_CONFIRMATION")
public class ConfirmationToken implements Serializable {

	/** Serial Version */
	private static final long serialVersionUID = -3336577922492538911L;

	/** Identificador (PK) */
	@Id
	@Column(name = "C_CONFIRM_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long confirmId;

	/** Token */
	@Column(name = "C_TOKEN")
	private String token;

	/** Usuario */
	@OneToOne(targetEntity = User.class)
	@JoinColumn(nullable = false, name = "C_USER_ID")
	private User user;

	/**
	 * Constructor
	 * 
	 * @param user
	 * @param token
	 */
	public ConfirmationToken(User user, String token) {
		this.user = user;
		this.token = token;
	}

}
