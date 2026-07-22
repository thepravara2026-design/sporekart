package com.sporekart.gateway.filter;

import com.sporekart.gateway.error.GatewayException;
import com.sporekart.gateway.security.JwtValidator;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthenticationFilterTest {

    private final JwtValidator jwtValidator = new JwtValidator();

    @Test
    void shouldRejectMissingAuthHeader() {
        var filter = new AuthenticationFilter(jwtValidator);
        assertThat(filter.getOrder()).isEqualTo(Integer.MIN_VALUE + 2);
    }

    @Test
    void shouldValidateJwt() {
        var claims = jwtValidator.validate("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMTIzIiwicm9sZXMiOlsiQURNSU4iXX0.signature");
        assertThat(claims).isNotNull();
        assertThat(claims.subject()).isEqualTo("user123");
        assertThat(claims.roles()).contains("ADMIN");
    }

    @Test
    void shouldReturnNullForInvalidJwt() {
        var claims = jwtValidator.validate("invalid.token.here");
        assertThat(claims).isNull();
    }

    @Test
    void shouldReturnNullForBlankToken() {
        var claims = jwtValidator.validate("");
        assertThat(claims).isNull();
    }

    @Test
    void shouldReturnNullForNullToken() {
        var claims = jwtValidator.validate(null);
        assertThat(claims).isNull();
    }
}
