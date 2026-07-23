package com.sporekart.customer.copilot;

import com.sporekart.copilot.CopilotEngine;
import com.sporekart.copilot.CopilotSDK;
import com.sporekart.customer.copilot.service.CustomerCopilotRegistrationService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties
public class CustomerCopilotApplication {

    private static final Logger log = LoggerFactory.getLogger(CustomerCopilotApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CustomerCopilotApplication.class, args);
    }

    @Bean
    public CopilotEngine copilotEngine() {
        return CopilotSDK.createDefaultEngine();
    }

    @PostConstruct
    public void onStartup() {
        log.info("Customer Copilot Service starting...");
        log.info("Registering CUSTOMER copilot capabilities and tools");
        log.info("Customer Copilot Service started successfully");
    }
}
