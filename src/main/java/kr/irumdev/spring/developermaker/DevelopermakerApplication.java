package kr.irumdev.spring.developermaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DevelopermakerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevelopermakerApplication.class, args);
	}

}
