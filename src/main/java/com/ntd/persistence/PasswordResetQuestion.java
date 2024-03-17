package com.ntd.persistence;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Clase Preguntas para resetear contraseña
 * 
 * @author SLP
 */
@Entity
@Getter
@Setter
@Table(name = "T_PASSWORD_RESET_QUESTION")
public class PasswordResetQuestion implements Serializable {

	/** Serial Version */
	private static final long serialVersionUID = -5335235112246467256L;

	/** Identificador (PK) */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "C_QUESTION_ID")
	private Long questionId;

	/* Usuario */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "C_USER_ID", nullable = false)
	private User user;

	/* Pregunta */
	@Column(name = "C_QUESTION", nullable = false)
	private String question;

	/* Respuesta */
	@Column(name = "C_ANSWER", nullable = false)
	private String answer;

}
