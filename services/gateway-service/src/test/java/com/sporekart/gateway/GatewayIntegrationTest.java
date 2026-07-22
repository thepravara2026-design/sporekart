package com.sporekart.gateway;

import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class GatewayIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private MeterRegistry meterRegistry;

    @Test
    void healthEndpointShouldReturnStatus() {
        webTestClient.get()
            .uri("/actuator/health")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.status").isEqualTo("UP");
    }

    @Test
    void infoEndpointShouldBeAccessible() {
        webTestClient.get()
            .uri("/actuator/info")
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void metricsEndpointShouldBeAccessible() {
        webTestClient.get()
            .uri("/actuator/metrics")
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void unknownRouteShouldReturn404() {
        webTestClient.get()
            .uri("/api/nonexistent/resource")
            .exchange()
            .expectStatus().is4xxClientError();
    }

    @Test
    void meterRegistryShouldBeAvailable() {
        assertThat(meterRegistry).isNotNull();
    }

    @Test
    void metricsRecorderShouldRecordMetrics() {
        var counter = meterRegistry.counter("gateway.requests.total", "method", "GET", "path", "/actuator/health");
        counter.increment();
        assertThat(counter.count()).isEqualTo(1);
    }
}
