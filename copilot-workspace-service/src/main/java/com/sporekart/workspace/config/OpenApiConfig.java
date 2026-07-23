package com.sporekart.workspace.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI workspaceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SporeKart Enterprise Copilot Workspace API")
                        .description("Enterprise Unified Copilot Workspace & Multi-Copilot Collaboration Engine")
                        .version("1.0.0"))
                .servers(List.of(
                        new Server().url("http://localhost:8103").description("Local Development")));
    }
}
