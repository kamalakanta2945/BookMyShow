package com.eventbook.settingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SettingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SettingServiceApplication.class, args);
	}

}