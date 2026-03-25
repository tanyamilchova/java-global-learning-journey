package com.example.serviceRest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(proxyBeanMethods = false)
@ComponentScan(basePackages = {"com.example"})
@EnableJpaRepositories(basePackages = "com.example.cloudServiceImpl")
@EntityScan(basePackages = {"com.domain"})
public class RestApiModulesApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestApiModulesApplication.class, args);
    }

}