package com.starly.main.SosysicBot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.starly.main.model"})
public class SosysicBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(SosysicBotApplication.class, args);
	}

}
