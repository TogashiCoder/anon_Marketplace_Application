package com.marketplace.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //phone
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:4200", "http://192.168.1.107:4200", "http://192.168.1.112:4200")  // Add your IP address here
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);

        registry.addMapping("/register/**")
                .allowedOrigins("http://localhost:4200", "http://192.168.1.107:4200", "http://192.168.1.112:4200")  // Add your IP address here
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);

        registry.addMapping("/login/**")
                .allowedOrigins("http://localhost:4200", "http://192.168.1.107:4200", "http://192.168.1.112:4200")  // Add your IP address here
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
