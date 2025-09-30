package com.bookmyshow.settingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SettingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SettingServiceApplication.class, args);
    }

}