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
class QaInjectionSecurityTest {

    @Autowired
    private WebTestClient webClient;

    @Test
    @DisplayName("QA-07-001: SQL injection in path should return 404 not 500")
    void sqlInjectionInPath_shouldNotCrash() {
        webClient.get()
            .uri("/api/auth/login' OR '1'='1")
            .exchange()
            .expectStatus().is4xxClientError();
    }

    @Test
    @DisplayName("QA-07-002: SQL injection in query parameter should not cause internal error")
    void sqlInjectionInQuery_shouldNotCrash() {
        webClient.get()
            .uri("/api/public/search?q=1' UNION SELECT * FROM users--")
            .exchange()
            .expectStatus().is4xxClientError();
    }

    @Test
    @DisplayName("QA-07-003: XSS in path should be rejected gracefully")
    void xssInPath_shouldNotCrash() {
        webClient.get()
            .uri("/api/public/<script>alert('xss')</script>")
            .exchange()
            .expectStatus().is4xxClientError();
    }

    @Test
    @DisplayName("QA-07-004: XSS in header should not reflect unsanitized")
    void xssInHeader_shouldNotReflect() {
        webClient.get()
            .uri("/actuator/health")
            .header("X-Custom-Header", "<script>alert(1)</script>")
            .exchange()
            .expectStatus().isOk()
            .expectBody().consumeWith(response -> {
                var body = new String(response.getResponseBody() != null ? response.getResponseBody() : new byte[0]);
                assert !body.contains("<script>") || !body.contains("alert(1)") :
                    "P3 WARNING: XSS payload reflected in response body";
            });
    }

    @Test
    @DisplayName("QA-07-005: Path traversal should be rejected")
    void pathTraversal_shouldNotExposeFiles() {
        webClient.get()
            .uri("/../../../etc/passwd")
            .exchange()
            .expectStatus().is4xxClientError();
    }

    @Test
    @DisplayName("QA-07-006: Null byte injection should not crash")
    void nullByteInjection_shouldNotCrash() {
        webClient.get()
            .uri("/api/public/%00")
            .exchange()
            .expectStatus().is4xxClientError();
    }

    @Test
    @DisplayName("QA-07-007: CRLF injection in header should not split response")
    void crlfInjection_shouldNotSplitResponse() {
        webClient.get()
            .uri("/actuator/health")
            .header("X-Forwarded-For", "127.0.0.1%0d%0aX-Custom:%20injected")
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    @DisplayName("QA-07-008: Very long URL (5000 chars) should not crash")
    void veryLongUrl_shouldNotCrash() {
        var longPath = "/api/public/" + "a".repeat(5000);
        webClient.get()
            .uri(longPath)
            .exchange()
            .expectStatus().is4xxClientError();
    }

    @Test
    @DisplayName("QA-07-009: Unicode/control characters in path should not crash")
    void unicodeControlChars_shouldNotCrash() {
        webClient.get()
            .uri("/api/public/%00%01%02%1F%7F")
            .exchange()
            .expectStatus().is4xxClientError();
    }

    @Test
    @DisplayName("QA-07-010: Deeply nested JSON in public endpoint should not crash")
    void deeplyNestedJson_shouldNotCrash() {
        var nested = "{\"a\":" + "{\"b\":" .repeat(100) + "\"val\"" + "}".repeat(100) + "}";
        webClient.post()
            .uri("/api/public/test")
            .bodyValue(nested)
            .exchange()
            .expectStatus().is4xxClientError();
    }
}
