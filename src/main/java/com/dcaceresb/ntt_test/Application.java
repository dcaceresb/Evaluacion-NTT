package com.dcaceresb.ntt_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableJpaRepositories(basePackages = "com.dcaceresb.ntt_test")
@EntityScan(basePackages = "com.dcaceresb.ntt_test")
@ComponentScan({"com.dcaceresb.ntt_test"})
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

	}

}
