package com.example.cloudflaretest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("1. Application context starts successfully")
    void contextLoads() {
        // Verifies the Spring Boot application context starts successfully
    }

    @Test
    @DisplayName("2-4. GET /hello returns HTTP 200 with expected message, application name, and running status")
    void helloReturnsExpectedMessageAndApplication() throws Exception {
        mockMvc.perform(get("/hello").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Hello World! Spring Boot is running successfully on Java 17.")))
                .andExpect(jsonPath("$.application", is("cloudflare-java17-test")))
                .andExpect(jsonPath("$.status", is("RUNNING")));
    }

    @Test
    @DisplayName("5. GET /hello detects Java version dynamically")
    void helloDetectsJavaVersionDynamically() throws Exception {
        String currentJavaVersion = System.getProperty("java.version");

        mockMvc.perform(get("/hello").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.javaVersion", is(currentJavaVersion)))
                .andExpect(jsonPath("$.springBootVersion", notNullValue()));
    }

    @Test
    @DisplayName("6. GET /health returns HTTP 200 with status UP")
    void healthReturnsStatusUp() throws Exception {
        mockMvc.perform(get("/health").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UP")));
    }

    @Test
    @DisplayName("GET /actuator/health returns standard Spring Boot Actuator health response")
    void actuatorHealthReturnsStatusUp() throws Exception {
        mockMvc.perform(get("/actuator/health").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UP")));
    }
}
