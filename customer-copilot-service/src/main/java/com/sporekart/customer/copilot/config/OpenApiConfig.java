package com.sporekart.customer.copilot.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customerCopilotOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("SporeKart Enterprise Customer Copilot Service")
                .description("Enterprise AI Shopping & Support Assistant API for the SporeKart platform")
                .version("0.2.0")
                .contact(new Contact()
                    .name("SporeKart Engineering")
                    .email("engineering@sporekart.com")
                    .url("https://sporekart.com"))
                .license(new License()
                    .name("Proprietary")
                    .url("https://sporekart.com/license")));
    }
}
