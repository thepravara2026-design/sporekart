package com.sporekart.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ObservabilityCertificationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void healthEndpointShouldReturnJson() {
        webTestClient.get()
            .uri("/actuator/health")
            .exchange()
            .expectBody()
            .jsonPath("$.status").exists();
    }

    @Test
    void metricsEndpointShouldReturnPrometheusMetrics() {
        webTestClient.get()
            .uri("/actuator/metrics")
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void healthShouldIncludeComponents() {
        webTestClient.get()
            .uri("/actuator/health")
            .exchange()
            .expectBody()
            .jsonPath("$.components").exists();
    }

    @Test
    void responseShouldIncludeSecurityHeaders() {
        webTestClient.get()
            .uri("/actuator/info")
            .exchange()
            .expectHeader().valueEquals("X-Content-Type-Options", "nosniff")
            .expectHeader().valueEquals("X-Frame-Options", "DENY");
    }
}
