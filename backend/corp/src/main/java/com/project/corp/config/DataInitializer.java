package com.project.corp.config;

import com.project.corp.model.User;
import com.project.corp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserService userService;

    public DataInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Inicializando dados de exemplo...");
        
        try {
            User admin = userService.register("Admin", "admin", "admin@corporativa.local", "admin123");
            log.info("Admin user created: {}", admin);
        } catch (IllegalArgumentException e) {
            log.info("Admin user already exists");
        }

        try {
            User user = userService.register("User", "user", "user@corporativa.local", "user123");
            log.info("Regular user created: {}", user);
        } catch (IllegalArgumentException e) {
            log.info("User already exists");
        }
    }
}
