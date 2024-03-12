package com.ntd.persistence;

import java.io.Serializable;
import java.time.YearMonth;

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
 * Clase Tarjeta bancaria
 * 
 * @author SLP
 */
@Entity
@Setter
@Getter
@Table(name = "T_CARD")
public class Card implements Serializable {

	/** Serial Version */
	private static final long serialVersionUID = 1L;

	/** Identificador (PK) */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "C_CARD_ID")
	private Long cardId;

	/** Nombre en la tarjeta */
	@Column(name = "C_TITULAR", nullable = false)
	private String titular;

	/** Numero de tarjeta */
	@Column(name = "C_CARD_NUMBER", nullable = false)
	private String cardNumber;

	/** Fecha de vencimiento */
	@Column(name = "C_EXPIRATION", nullable = false)
	private YearMonth expiration;

	/** Tarjeta predeterminada */
	@Column(name = "C_FAVORITE")
	private boolean favorite;

	/** Usuario FK */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "C_USER_ID", referencedColumnName = "C_USER_ID")
	private User user;
}
