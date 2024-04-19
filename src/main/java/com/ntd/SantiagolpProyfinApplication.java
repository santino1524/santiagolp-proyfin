package com.ntd;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ntd.persistence.User;
import com.ntd.persistence.UserRepositoryI;
import com.ntd.security.UserRole;
import com.ntd.utils.Constants;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class SantiagolpProyfinApplication {

	/** Encriptar password */
	PasswordEncoder passwordEncoder;

	/** Dependencia de UserRepository */
	UserRepositoryI userRepository;

	/**
	 * Constructor
	 * 
	 * @param passwordEncoder
	 * @param userRepository
	 */
	public SantiagolpProyfinApplication(PasswordEncoder passwordEncoder, UserRepositoryI userRepository) {
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
	}

	/**
	 * Metodo principal
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(SantiagolpProyfinApplication.class, args);
	}

	/**
	 * Ejecutar al inicio de la app
	 * 
	 * @return args
	 */
	@Bean
	protected CommandLineRunner init() {
		if (log.isInfoEnabled())
			log.info("Ejecucion de comandos al inicio");

		// Crear usuario de inicio
		return args -> {
			if (userRepository.count() == 0) {
				User userInit = User.builder().email(Constants.DEFAULT_USER)
						.passwd(passwordEncoder.encode(Constants.DEFAULT_PASSWD)).name("Goku").surname("Goku")
						.secondSurname("Goku").dni("11111111P").phoneNumber("123456789")
						.role(UserRole.valueOf(UserRole.SELLER.name())).questions(Arrays.asList("1", "2", "3"))
						.answers(Arrays.asList("1", "2", "3")).build();

				userRepository.save(userInit);
			}
		};

	}

}
