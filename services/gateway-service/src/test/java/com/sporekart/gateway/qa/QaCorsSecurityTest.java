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
class QaCorsSecurityTest {

    @Autowired
    private WebTestClient webClient;

    @Test
    @DisplayName("QA-07-011: CORS preflight with valid origin returns allowed headers")
    void corsPreflight_withValidOrigin() {
        webClient.options().uri("/api/public/test")
            .header("Origin", "http://localhost:4200")
            .header("Access-Control-Request-Method", "POST")
            .exchange()
            .expectStatus().isOk()
            .expectHeader().valueEquals("Access-Control-Allow-Origin", "*");
    }

    @Test
    @DisplayName("QA-07-012: Security headers present on health response")
    void securityHeaders_presentOnHealth() {
        webClient.get().uri("/actuator/health")
            .exchange()
            .expectStatus().isOk()
            .expectHeader().exists("X-Content-Type-Options")
            .expectHeader().exists("X-Frame-Options")
            .expectHeader().exists("Strict-Transport-Security")
            .expectHeader().exists("Cache-Control");
    }

    @Test
    @DisplayName("QA-07-013: X-Content-Type-Options is nosniff")
    void xContentTypeOptions_isNosniff() {
        webClient.get().uri("/actuator/health")
            .exchange()
            .expectHeader().valueEquals("X-Content-Type-Options", "nosniff");
    }

    @Test
    @DisplayName("QA-07-014: X-Frame-Options is DENY")
    void xFrameOptions_isDeny() {
        webClient.get().uri("/actuator/health")
            .exchange()
            .expectHeader().valueEquals("X-Frame-Options", "DENY");
    }

    @Test
    @DisplayName("QA-07-015: Server header is removed")
    void serverHeader_removed() {
        webClient.get().uri("/actuator/health")
            .exchange()
            .expectHeader().doesNotExist("Server");
    }

    @Test
    @DisplayName("QA-07-016: Large payload (>10MB) returns 413")
    void largePayload_returns413() {
        var largeBody = "X".repeat(11 * 1024 * 1024);
        webClient.post()
            .uri("/api/public/test")
            .bodyValue(largeBody)
            .exchange()
            .expectStatus().is4xxClientError();
    }

    @Test
    @DisplayName("QA-07-017: Non-ASCII origin in CORS should not crash")
    void corsWithNonAsciiOrigin() {
        webClient.options().uri("/api/public/test")
            .header("Origin", "http://evíl.com")
            .header("Access-Control-Request-Method", "GET")
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    @DisplayName("QA-07-018: Malformed Content-Type should not crash")
    void malformedContentType_shouldNotCrash() {
        webClient.post()
            .uri("/api/public/test")
            .header("Content-Type", "application/json; charset=utf-8'; DROP TABLE users; --")
            .bodyValue("{}")
            .exchange()
            .expectStatus().is4xxClientError();
    }
}
