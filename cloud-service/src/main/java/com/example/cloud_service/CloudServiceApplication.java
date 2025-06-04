package com.example.cloud_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example")
public class CloudServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudServiceApplication.class, args);
	}

}
