package com.sporekart.gateway.error;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class ProblemDetailsTest {

    @Test
    void shouldCreateProblemDetails() {
        var problem = ProblemDetails.from(HttpStatus.NOT_FOUND, "NOT_FOUND", "Resource not found", "/api/test");

        assertThat(problem.status()).isEqualTo(404);
        assertThat(problem.title()).isEqualTo("Not Found");
        assertThat(problem.detail()).isEqualTo("Resource not found");
        assertThat(problem.errorCode()).isEqualTo("NOT_FOUND");
        assertThat(problem.instance()).isEqualTo("/api/test");
        assertThat(problem.timestamp()).isNotNull();
    }

    @Test
    void shouldCreateUnauthorizedProblem() {
        var problem = ProblemDetails.from(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "Invalid token", "/api/auth");

        assertThat(problem.status()).isEqualTo(401);
        assertThat(problem.errorCode()).isEqualTo("UNAUTHORIZED");
    }

    @Test
    void shouldCreateServerErrorProblem() {
        var problem = ProblemDetails.from(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "Unexpected error", "/api/data");

        assertThat(problem.status()).isEqualTo(500);
        assertThat(problem.title()).isEqualTo("Internal Server Error");
    }
}
