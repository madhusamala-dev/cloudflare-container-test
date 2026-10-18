package com.example.cloudflaretest;

import org.springframework.boot.SpringBootVersion;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class HelloController {

    private static final String DEFAULT_SPRING_BOOT_VERSION = "3.2.5";

    @GetMapping("/hello")
    public Map<String, Object> hello() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Hello World! Spring Boot is running successfully on Java 17.");
        response.put("application", "cloudflare-java17-test");
        response.put("javaVersion", System.getProperty("java.version"));
        response.put("springBootVersion", resolveSpringBootVersion());
        response.put("status", "RUNNING");
        return response;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Collections.singletonMap("status", "UP");
    }

    private String resolveSpringBootVersion() {
        Package bootPackage = SpringBootVersion.class.getPackage();
        String version = (bootPackage != null) ? bootPackage.getImplementationVersion() : null;
        if (version == null || version.isBlank()) {
            version = SpringBootVersion.getVersion();
        }
        if (version == null || version.isBlank()) {
            version = DEFAULT_SPRING_BOOT_VERSION;
        }
        return version;
    }
}
