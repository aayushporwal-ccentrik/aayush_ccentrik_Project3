package com.punit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.context.annotation.Configuration;

/**
 * HomeController - Handles static file serving and routing
 * Fixes 400 Bad Request errors on Cloud Foundry
 * 
 * Created: March 8, 2026
 * Author: PUNIT
 */
@Controller
public class HomeController {
    
    /**
     * Serve index.html on GET /
     */
    @GetMapping("/")
    public String index() {
        return "forward:/index.html";
    }
}

/**
 * Static Resource Configuration
 * Enables serving of static files (HTML, CSS, JS, images)
 */
@Configuration
class StaticResourceConfig implements WebMvcConfigurer {
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Only add if no mapping already exists
        if (!registry.hasMappingForPattern("/**")) {
            registry.addResourceHandler("/**")
                .addResourceLocations(
                    "classpath:/static/",           // /src/main/resources/static/
                    "classpath:/public/",           // /src/main/resources/public/
                    "classpath:/resources/",        // /src/main/resources/resources/
                    "classpath:/templates/",        // /src/main/resources/templates/
                    "classpath:/META-INF/resources/", // For Spring Boot resources
                    "classpath:/"                   // Root of classpath
                )
                .setCachePeriod(3600)               // Cache for 1 hour
                .resourceChain(false);              // Disable resource chain
        }
    }
}