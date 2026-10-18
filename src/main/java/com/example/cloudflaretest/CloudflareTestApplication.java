package com.example.cloudflaretest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CloudflareTestApplication {

    private static final Logger logger = LoggerFactory.getLogger(CloudflareTestApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CloudflareTestApplication.class, args);
        logger.info("Java 17 and Spring Boot 3.2.5 test application has started successfully.");
        System.out.println("Java 17 and Spring Boot 3.2.5 test application has started successfully.");
    }
}
