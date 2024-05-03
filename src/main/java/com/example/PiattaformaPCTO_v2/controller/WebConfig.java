package com.example.PiattaformaPCTO_v2.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200") // Specifica gli origini consentiti
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Specifica i metodi HTTP consentiti
                .allowedHeaders("*"); // Specifica gli header consentiti
    }
}
