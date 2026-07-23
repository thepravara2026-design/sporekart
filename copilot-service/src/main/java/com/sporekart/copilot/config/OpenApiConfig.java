package com.sporekart.copilot.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI copilotOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("SporeKart Enterprise Copilot Service")
                .description("REST API for the SporeKart Enterprise Copilot Framework")
                .version("0.2.0")
                .contact(new Contact()
                    .name("SporeKart Team")
                    .email("dev@sporekart.com"))
                .license(new License()
                    .name("Proprietary")
                    .url("https://sporekart.com/license")));
    }
}
