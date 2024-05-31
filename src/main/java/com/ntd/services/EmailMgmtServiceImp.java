package com.ntd.services;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio de correo
 * 
 * @author slopezpi
 */
@Service
@Slf4j
public class EmailMgmtServiceImp implements EmailMgmtServiceI {

	@Value("${mail.smtp.host}")
	private String host;

	@Value("${mail.smtp.port}")
	private int port;

	@Value("${mail.smtp.ssl.enable}")
	private boolean sslEnable;

	@Value("${mail.smtp.auth}")
	private boolean auth;

	@Value("${mail.smtp.user}")
	private String username;

	@Value("${mail.smtp.password}")
	private String password;

	/**
	 * Crear sesion para envio de email
	 * 
	 * @return Session
	 */
	private Session createSession() {
		if (log.isInfoEnabled())
			log.info("Crear sesion para envio de email");

		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.ssl.enable", sslEnable);
		props.put("mail.smtp.auth", auth);

		Authenticator authenticator = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		};

		return Session.getInstance(props, authenticator);
	}

	/**
	 * Enviar email
	 * 
	 * @param to
	 * @param subject
	 * @param text
	 * @throws MessagingException
	 */
	public void sendEmail(String to, String subject, String text) throws MessagingException {
		if (log.isInfoEnabled())
			log.info("Enviar email");

		try {
			Session session = createSession();

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(subject);
			message.setText(text);

			Transport.send(message);
		} catch (MessagingException e) {
			if (log.isErrorEnabled())
				log.error("Enviar email");

			throw new MessagingException(e.getMessage());
		}
	}

}
