package com.rmit.sept.septbackend.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.rmit.sept.septbackend")
@EnableJpaRepositories(basePackages = "com.rmit.sept.septbackend.repository")
@EntityScan(basePackages = "com.rmit.sept.septbackend.entity")
public class SeptBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeptBackendApplication.class, args);
    }
}
