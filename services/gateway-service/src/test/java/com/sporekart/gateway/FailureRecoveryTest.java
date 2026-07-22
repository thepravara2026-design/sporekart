package com.sporekart.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class FailureRecoveryTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void unknownRouteShouldReturnProblemJson() {
        webTestClient.get()
            .uri("/api/nonexistent/resource")
            .exchange()
            .expectStatus().is4xxClientError()
            .expectHeader().contentTypeCompatibleWith("application/problem+json");
    }

    @Test
    void actuatorHealthShouldReturnValidStatus() {
        webTestClient.get()
            .uri("/actuator/health")
            .exchange()
            .expectBody()
            .jsonPath("$.status").isNotEmpty();
    }

    @Test
    void invalidMethodShouldFailGracefully() {
        webTestClient.options()
            .uri("/actuator/health")
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void metricsShouldAlwaysBeAvailable() {
        webTestClient.get()
            .uri("/actuator/metrics")
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void infoShouldAlwaysBeAvailable() {
        webTestClient.get()
            .uri("/actuator/info")
            .exchange()
            .expectStatus().isOk();
    }
}
