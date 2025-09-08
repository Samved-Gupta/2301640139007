package com.yourcompany.backendtestsubmission;

import org.example.LoggingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendTestSubmissionApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendTestSubmissionApplication.class, args);
	}

	@Bean
	public LoggingService loggingService(@Value("${evaluation.service.base-url}") String baseUrl) {
		return new LoggingService(baseUrl);
	}
}
