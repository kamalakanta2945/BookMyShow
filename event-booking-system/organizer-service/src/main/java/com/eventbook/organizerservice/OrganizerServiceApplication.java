package com.eventbook.organizerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class OrganizerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrganizerServiceApplication.class, args);
	}

}