package com.sporekart.gateway.security;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JwtValidatorTest {

    private final JwtValidator validator = new JwtValidator();

    @Test
    void shouldValidateWellFormedToken() {
        var token = createToken("user123", List.of("ADMIN", "USER"));
        var claims = validator.validate(token);
        assertThat(claims).isNotNull();
        assertThat(claims.subject()).isEqualTo("user123");
        assertThat(claims.roles()).containsExactly("ADMIN", "USER");
    }

    @Test
    void shouldExtractAdminFromToken() {
        var claims = validator.validate("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbIkFETUlOIl19.signature");
        assertThat(claims).isNotNull();
        assertThat(claims.subject()).isEqualTo("admin");
        assertThat(claims.roles()).containsExactly("ADMIN");
    }

    @Test
    void shouldHandleEmptyRoles() {
        var token = createToken("nobody", List.of());
        var claims = validator.validate(token);
        assertThat(claims).isNotNull();
        assertThat(claims.roles()).isEmpty();
    }

    @Test
    void shouldHandleMalformedBase64() {
        var claims = validator.validate("header.!!!invalid!!!base64.signature");
        assertThat(claims).isNull();
    }

    @Test
    void shouldHandleNotEnoughParts() {
        var claims = validator.validate("only.two");
        assertThat(claims).isNull();
    }

    @Test
    void shouldHandleEmptyString() {
        assertThat(validator.validate("")).isNull();
    }

    @Test
    void shouldHandleNullInput() {
        assertThat(validator.validate(null)).isNull();
    }

    @Test
    void shouldHandleSingleRole() {
        var token = createToken("analyst", List.of("ANALYST"));
        var claims = validator.validate(token);
        assertThat(claims).isNotNull();
        assertThat(claims.roles()).containsExactly("ANALYST");
    }

    @Test
    void shouldHandleMultipleRoles() {
        var token = createToken("superuser", List.of("ADMIN", "ANALYST", "USER", "SYSTEM"));
        var claims = validator.validate(token);
        assertThat(claims).isNotNull();
        assertThat(claims.roles()).containsExactly("ADMIN", "ANALYST", "USER", "SYSTEM");
    }

    private String createToken(String subject, List<String> roles) {
        var header = java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(
            "{\"alg\":\"HS256\"}".getBytes());
        var rolesJson = roles.stream()
            .map(r -> "\"" + r + "\"")
            .collect(java.util.stream.Collectors.joining(","));
        var payload = java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(
            ("{\"sub\":\"" + subject + "\",\"roles\":[" + rolesJson + "]}").getBytes());
        return header + "." + payload + ".fakesignature";
    }
}
