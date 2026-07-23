package com.sporekart.workspace.infrastructure.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.sporekart.workspace.dto.ChatRequest;
import com.sporekart.workspace.dto.ChatResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Component
public class CopilotServiceClient {

    private static final Logger log = LoggerFactory.getLogger(CopilotServiceClient.class);

    private static final Map<String, String> COPILOT_BASE_URLS = new HashMap<>();

    static {
        COPILOT_BASE_URLS.put("CUSTOMER", "http://localhost:8099/api/v1/copilot/customer");
        COPILOT_BASE_URLS.put("ADMIN", "http://localhost:8100/api/v1/copilot/admin");
        COPILOT_BASE_URLS.put("TRAINER", "http://localhost:8101/api/v1/copilot/trainer");
        COPILOT_BASE_URLS.put("GROWER", "http://localhost:8102/api/v1/copilot/grower");
    }

    private final WebClient webClient;

    public CopilotServiceClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .codecs(config -> config.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .build();
    }

    public Mono<JsonNode> sendToCopilot(String copilotType, String endpoint, Object requestBody) {
        String baseUrl = getCopilotBaseUrl(copilotType);
        return webClient.post()
                .uri(baseUrl + endpoint)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .doOnError(error -> log.error("Error sending to copilot {}: {}", copilotType, error.getMessage()));
    }

    public ChatResponse sendChat(String copilotType, ChatRequest request) {
        String baseUrl = getCopilotBaseUrl(copilotType);
        try {
            return webClient.post()
                    .uri(baseUrl + "/chat")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(ChatResponse.class)
                    .block(Duration.ofSeconds(30));
        } catch (Exception e) {
            log.error("Chat request failed for copilot {}: {}", copilotType, e.getMessage());
            return new ChatResponse(
                    request.sessionId(),
                    "Error communicating with copilot service: " + e.getMessage(),
                    copilotType,
                    false,
                    null,
                    null,
                    null,
                    null,
                    false
            );
        }
    }

    public Flux<String> streamChat(String copilotType, ChatRequest request) {
        String baseUrl = getCopilotBaseUrl(copilotType);
        return webClient.post()
                .uri(baseUrl + "/stream")
                .bodyValue(request)
                .retrieve()
                .bodyToFlux(String.class)
                .doOnError(error -> log.error("Stream error for copilot {}: {}", copilotType, error.getMessage()))
                .onErrorResume(error -> Flux.just("Error: " + error.getMessage()));
    }

    public Map<String, Object> healthCheck(String copilotType) {
        String baseUrl = getCopilotBaseUrl(copilotType);
        try {
            Map<String, Object> result = webClient.get()
                    .uri(baseUrl + "/health")
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block(Duration.ofSeconds(5));
            return result != null ? result : Map.of("status", "UP");
        } catch (Exception e) {
            return Map.of("status", "DOWN", "error", e.getMessage());
        }
    }

    public String getCopilotBaseUrl(String copilotType) {
        String url = COPILOT_BASE_URLS.get(copilotType.toUpperCase());
        if (url == null) {
            throw new IllegalArgumentException("Unknown copilot type: " + copilotType);
        }
        return url;
    }
}
