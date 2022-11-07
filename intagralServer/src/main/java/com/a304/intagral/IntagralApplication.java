package com.a304.intagral;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class IntagralApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntagralApplication.class, args);
	}

}
