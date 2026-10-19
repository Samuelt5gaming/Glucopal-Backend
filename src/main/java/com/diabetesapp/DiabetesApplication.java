package com.diabetesapp;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DiabetesApplication {
    public static void main(String[] args) {
        // Load .env file and set variables as system properties
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));

        SpringApplication.run(DiabetesApplication.class, args);
    }
}
