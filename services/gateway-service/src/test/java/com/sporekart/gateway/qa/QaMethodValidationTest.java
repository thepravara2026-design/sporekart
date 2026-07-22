package com.sporekart.gateway.qa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class QaMethodValidationTest {

    @Autowired
    private WebTestClient webClient;

    @Test
    @DisplayName("QA-05-001: GET on health endpoint returns 200")
    void getOnHealth_returns200() {
        webClient.get().uri("/actuator/health").exchange().expectStatus().isOk();
    }

    @Test
    @DisplayName("QA-05-002: POST on health endpoint returns 405 or 404")
    void postOnHealth_returns405() {
        webClient.post().uri("/actuator/health").exchange().expectStatus().is4xxClientError();
    }

    @Test
    @DisplayName("QA-05-003: PUT on health endpoint returns 405 or 404")
    void putOnHealth_returns405() {
        webClient.put().uri("/actuator/health").exchange().expectStatus().is4xxClientError();
    }

    @Test
    @DisplayName("QA-05-004: DELETE on health endpoint returns 405 or 404")
    void deleteOnHealth_returns405() {
        webClient.delete().uri("/actuator/health").exchange().expectStatus().is4xxClientError();
    }

    @Test
    @DisplayName("QA-05-005: PATCH on health endpoint returns 405 or 404")
    void patchOnHealth_returns405() {
        webClient.patch().uri("/actuator/health").exchange().expectStatus().is4xxClientError();
    }

    @Test
    @DisplayName("QA-05-006: OPTIONS returns CORS headers")
    void options_returnsCorsHeaders() {
        webClient.options().uri("/actuator/health")
            .header("Origin", "http://localhost:3000")
            .header("Access-Control-Request-Method", "GET")
            .exchange()
            .expectStatus().isOk()
            .expectHeader().exists("Access-Control-Allow-Origin");
    }

    @Test
    @DisplayName("QA-05-007: TRACE method should be disabled")
    void traceMethod_shouldBeDisabled() {
        webClient.method(org.springframework.http.HttpMethod.TRACE)
            .uri("/actuator/health")
            .exchange()
            .expectStatus().is4xxClientError();
    }

    @Test
    @DisplayName("QA-05-008: Custom CONNECT-like method should be rejected")
    void connectMethod_shouldBeDisabled() {
        webClient.method(org.springframework.http.HttpMethod.valueOf("CONNECT"))
            .uri("/actuator/health")
            .exchange()
            .expectStatus().is4xxClientError();
    }

    @Test
    @DisplayName("QA-05-009: Unknown route returns 404 with Problem JSON")
    void unknownRoute_returns404problemJson() {
        webClient.get().uri("/api/nonexistent/resource")
            .exchange()
            .expectStatus().isNotFound()
            .expectHeader().contentType("application/problem+json");
    }

    @Test
    @DisplayName("QA-05-010: Fallback endpoint returns 503 with Problem JSON")
    void fallbackEndpoint_returns503problemJson() {
        webClient.get().uri("/fallback/test-service")
            .exchange()
            .expectStatus().is5xxServerError()
            .expectHeader().contentType("application/problem+json");
    }
}
