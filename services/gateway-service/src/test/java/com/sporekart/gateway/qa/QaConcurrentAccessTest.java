package com.sporekart.gateway.qa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import java.util.stream.IntStream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class QaConcurrentAccessTest {

    @Autowired
    private WebTestClient webClient;

    @Test
    @DisplayName("QA-08-001: 50 concurrent health requests all succeed")
    void fiftyConcurrentHealthRequests_allSucceed() {
        var results = IntStream.range(0, 50)
            .parallel()
            .mapToObj(i -> {
                try {
                    webClient.get().uri("/actuator/health").exchange().expectStatus().isOk();
                    return true;
                } catch (Exception e) {
                    return false;
                }
            })
            .toList();
        var failures = results.stream().filter(s -> !s).count();
        assert failures == 0 : "P3 DEFECT: " + failures + " concurrent requests failed (out of 50)";
    }

    @Test
    @DisplayName("QA-08-002: 50 concurrent correlation IDs are unique")
    void fiftyConcurrentCorrelationIds_allUnique() {
        var ids = IntStream.range(0, 50)
            .parallel()
            .mapToObj(i -> {
                try {
                    return webClient.get()
                        .uri("/actuator/health")
                        .exchange()
                        .expectStatus().isOk()
                        .returnResult(Void.class)
                        .getResponseHeaders()
                        .getFirst("X-Correlation-Id");
                } catch (Exception e) {
                    return null;
                }
            })
            .filter(id -> id != null)
            .toList();
        var uniqueCount = ids.stream().distinct().count();
        assert uniqueCount == ids.size() : "P3 DEFECT: Duplicate correlation IDs generated (" + uniqueCount + " unique out of " + ids.size() + ")";
    }

    @Test
    @DisplayName("QA-08-003: Mixed HTTP methods on same path in parallel")
    void mixedMethodsOnSamePath_parallel() {
        var results = IntStream.range(0, 20)
            .parallel()
            .mapToObj(i -> {
                try {
                    if (i % 2 == 0) {
                        webClient.get().uri("/actuator/health").exchange().expectStatus().isOk();
                    } else {
                        webClient.post().uri("/actuator/health").exchange().expectStatus().is4xxClientError();
                    }
                    return true;
                } catch (Exception e) {
                    return false;
                }
            })
            .toList();
        var nullCount = results.stream().filter(s -> !s).count();
        assert nullCount == 0 : "P3 DEFECT: " + nullCount + " parallel mixed requests resulted in exceptions";
    }
}
