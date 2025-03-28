package com.lowkey.userlistservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableWebMvc
public class UserListServiceApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(UserListServiceApplication.class, args);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve static resources (if required, for a front-end interface or API documentation)
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }

    @Bean
    public StartupInitializer startupInitializer() {
        // Optional: Use for preloading data or performing startup tasks
        return new StartupInitializer();
    }
}