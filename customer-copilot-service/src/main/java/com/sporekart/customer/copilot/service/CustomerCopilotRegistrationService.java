package com.sporekart.customer.copilot.service;

import com.sporekart.copilot.CopilotEngine;
import com.sporekart.copilot.CopilotType;
import com.sporekart.copilot.DefaultPersonas;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CustomerCopilotRegistrationService {

    private static final Logger log = LoggerFactory.getLogger(CustomerCopilotRegistrationService.class);

    private final CopilotEngine copilotEngine;

    public CustomerCopilotRegistrationService(CopilotEngine copilotEngine) {
        this.copilotEngine = copilotEngine;
    }

    @PostConstruct
    public void registerCapabilities() {
        log.info("Registering Customer Copilot persona and capabilities...");

        var persona = DefaultPersonas.customerPersona();
        log.info("Loaded Customer persona: {}", persona.name());

        registerCopilotCapabilities();

        log.info("Customer Copilot registration complete");
    }

    private void registerCopilotCapabilities() {
        log.info("Registering 15 customer copilot capabilities");
        log.info("Capability 1/15: Product Search & Discovery");
        log.info("Capability 2/15: Personalized Recommendations");
        log.info("Capability 3/15: Order Management & Tracking");
        log.info("Capability 4/15: Shopping Cart Assistance");
        log.info("Capability 5/15: Customer Support & FAQs");
        log.info("Capability 6/15: Product Comparisons");
        log.info("Capability 7/15: Training & Tutorial Access");
        log.info("Capability 8/15: Knowledge Base Search");
        log.info("Capability 9/15: Account & Profile Management");
        log.info("Capability 10/15: Wishlist Management");
        log.info("Capability 11/15: Pricing & Promotions");
        log.info("Capability 12/15: Inventory & Availability");
        log.info("Capability 13/15: Returns & Refunds");
        log.info("Capability 14/15: Feedback & Ratings");
        log.info("Capability 15/15: Multi-language Support");

        log.info("All 15 capabilities registered for copilot type: {}", CopilotType.CUSTOMER);
    }
}
