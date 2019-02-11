package com.edu.ccsu.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages= {"com.edu.ccsu"})
@EnableAsync
public class PrivateRepoApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrivateRepoApplication.class, args);
	}
}
