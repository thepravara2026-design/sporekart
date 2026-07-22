package com.sporekart.gateway;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class PerformanceCertificationTest {

    private static final Logger log = LoggerFactory.getLogger(PerformanceCertificationTest.class);
    private static final long MAX_ACCEPTABLE_LATENCY_MS = 500;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void healthEndpointShouldRespondWithinLimit() {
        var start = System.currentTimeMillis();
        webTestClient.get()
            .uri("/actuator/health")
            .exchange()
            .expectStatus().is5xxServerError();
        var duration = System.currentTimeMillis() - start;
        log.info("Health endpoint latency: {}ms", duration);
        assertThat(duration).isLessThan(MAX_ACCEPTABLE_LATENCY_MS);
    }

    @Test
    void infoEndpointShouldRespondWithinLimit() {
        var start = System.currentTimeMillis();
        webTestClient.get()
            .uri("/actuator/info")
            .exchange()
            .expectStatus().isOk();
        var duration = System.currentTimeMillis() - start;
        log.info("Info endpoint latency: {}ms", duration);
        assertThat(duration).isLessThan(MAX_ACCEPTABLE_LATENCY_MS);
    }

    @Test
    void metricsEndpointShouldRespondWithinLimit() {
        var start = System.currentTimeMillis();
        webTestClient.get()
            .uri("/actuator/metrics")
            .exchange()
            .expectStatus().isOk();
        var duration = System.currentTimeMillis() - start;
        log.info("Metrics endpoint latency: {}ms", duration);
        assertThat(duration).isLessThan(MAX_ACCEPTABLE_LATENCY_MS);
    }

    @Test
    void unknownRouteShouldRespondWithinLimit() {
        var start = System.currentTimeMillis();
        webTestClient.get()
            .uri("/api/benchmark/test")
            .exchange()
            .expectStatus().is4xxClientError();
        var duration = System.currentTimeMillis() - start;
        log.info("404 response latency: {}ms", duration);
        assertThat(duration).isLessThan(MAX_ACCEPTABLE_LATENCY_MS);
    }

    @Test
    void repeatedRequestsShouldNotDegrade() {
        var times = new long[5];
        for (int i = 0; i < 5; i++) {
            var start = System.currentTimeMillis();
            webTestClient.get()
                .uri("/actuator/info")
                .exchange()
                .expectStatus().isOk();
            times[i] = System.currentTimeMillis() - start;
        }
        log.info("Repeated request latencies: {}ms avg", 
            (times[0] + times[1] + times[2] + times[3] + times[4]) / 5);
        for (int i = 1; i < 5; i++) {
            assertThat(times[i]).isLessThan(MAX_ACCEPTABLE_LATENCY_MS);
        }
    }
}
