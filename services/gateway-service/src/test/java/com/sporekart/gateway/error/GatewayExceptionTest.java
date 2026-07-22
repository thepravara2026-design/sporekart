package com.sporekart.gateway.error;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class GatewayExceptionTest {

    @Test
    void shouldCreateException() {
        var ex = new GatewayException(HttpStatus.BAD_GATEWAY, "UPSTREAM_ERROR", "Upstream service failed");
        assertThat(ex.getHttpStatus()).isEqualTo(HttpStatus.BAD_GATEWAY);
        assertThat(ex.getErrorCode()).isEqualTo("UPSTREAM_ERROR");
        assertThat(ex.getMessage()).isEqualTo("Upstream service failed");
    }

    @Test
    void shouldCreateUnauthorizedException() {
        var ex = new GatewayException(HttpStatus.UNAUTHORIZED, "INVALID_TOKEN", "Token expired");
        assertThat(ex.getHttpStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(ex.getErrorCode()).isEqualTo("INVALID_TOKEN");
    }

    @Test
    void shouldCreateForbiddenException() {
        var ex = new GatewayException(HttpStatus.FORBIDDEN, "FORBIDDEN", "Insufficient permissions");
        assertThat(ex.getHttpStatus()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(ex.getMessage()).isEqualTo("Insufficient permissions");
    }
}
