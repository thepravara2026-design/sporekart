package com.sporekart.marketplace.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI marketplaceOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("SporeKart Copilot Marketplace API")
                .description("Enterprise Copilot Marketplace and Plugin SDK Platform")
                .version("1.0.0"));
    }
}