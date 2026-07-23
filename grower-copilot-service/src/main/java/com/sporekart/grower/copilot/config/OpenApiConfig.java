package com.sporekart.grower.copilot.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SporeKart Enterprise Grower Copilot API")
                        .version("1.0")
                        .description("Enterprise AI Agricultural Assistant for Mushroom Growers")
                        .contact(new Contact()
                                .name("SporeKart Platform Team")));
    }
}
