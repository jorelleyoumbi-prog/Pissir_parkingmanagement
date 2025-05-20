package com.parkingsystem.frontend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.awt.Desktop;
import java.net.URI;

@SpringBootApplication
public class FrontendServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrontendServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner openBrowserAfterStartup() {
        return args -> {
            try {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI("http://localhost:8082/login"));
                } else {
                    System.err.println("❌ Desktop non supportato o browse non supportato.");
                }
            } catch (Exception e) {
                System.err.println("❌ Browser non aperto: " + e.getMessage());
            }
        };
    }
}

