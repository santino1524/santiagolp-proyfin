package com.ntd.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * Servicio de correo
 * 
 * @author slopezpi
 */
@Service
@Slf4j
public class EmailMgmtServiceImp implements EmailMgmtServiceI {

	/** Dependecia JavaMailSender */
	private JavaMailSender mailSender;

	/**
	 * Constructor
	 * 
	 * @param mailSender
	 */
	public EmailMgmtServiceImp(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Override
	public void sendEmail(String to, String subject, String text) {
		if (log.isInfoEnabled())
			log.info("Enviar mensaje de confirmacion");

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		mailSender.send(message);
	}

}
