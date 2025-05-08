package com.example.ToDo.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // This allows CORS for all API endpoints
                .allowedOrigins(
                        "[http://127.0.0.1](http://127.0.0.1):5500",
                        "http://localhost:3000",
                        "http://localhost:4000",
                        "http://localhost:4200"
                ) // Explicitly list all allowed origins
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Specify allowed methods
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true); // Allow credentials
    }
}