package com.sporekart.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class AdminCopilotApplication {

    private static final Logger log = LoggerFactory.getLogger(AdminCopilotApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AdminCopilotApplication.class, args);
        log.info("Admin Copilot initializing");
    }
}
