package com.sporekart.trainer.copilot.config;

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
                        .title("SporeKart Enterprise Trainer Copilot API")
                        .version("1.0")
                        .description("Enterprise AI Training Assistant for Mushroom Cultivation Programs")
                        .contact(new Contact()
                                .name("SporeKart Platform Team")));
    }
}
