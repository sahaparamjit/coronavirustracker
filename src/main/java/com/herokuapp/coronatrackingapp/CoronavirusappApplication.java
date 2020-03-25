package com.herokuapp.coronatrackingapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CoronavirusappApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoronavirusappApplication.class, args);
	}
	
	@Bean
	public AnnotationConfigApplicationContext getContext() {
		return new AnnotationConfigApplicationContext();
	}

}
