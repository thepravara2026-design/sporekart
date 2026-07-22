package com.sporekart.gateway;

import com.sporekart.gateway.security.JwtValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Base64;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SecurityCertificationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private JwtValidator jwtValidator;

    @Test
    void healthEndpointShouldBePublic() {
        webTestClient.get()
            .uri("/actuator/health")
            .exchange()
            .expectStatus().is5xxServerError();
    }

    @Test
    void infoEndpointShouldBePublic() {
        webTestClient.get()
            .uri("/actuator/info")
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void swaggerUiPathShouldBePublic() {
        webTestClient.get()
            .uri("/swagger-ui.html")
            .exchange()
            .expectStatus().is3xxRedirection();
    }

    @Test
    void apiDocsShouldBePublic() {
        webTestClient.get()
            .uri("/v3/api-docs")
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void jwtValidatorShouldRejectNullToken() {
        assertThat(jwtValidator.validate(null)).isNull();
    }

    @Test
    void jwtValidatorShouldRejectEmptyToken() {
        assertThat(jwtValidator.validate("")).isNull();
    }

    @Test
    void jwtValidatorShouldRejectMalformedToken() {
        assertThat(jwtValidator.validate("not-a-valid-jwt")).isNull();
    }

    @Test
    void jwtValidatorShouldExtractSubjectAndRoles() {
        var token = createTestToken("testuser", List.of("USER"));
        var claims = jwtValidator.validate(token);
        assertThat(claims).isNotNull();
        assertThat(claims.subject()).isEqualTo("testuser");
        assertThat(claims.roles()).containsExactly("USER");
    }

    @Test
    void jwtValidatorShouldExtractAdminRoles() {
        var token = createTestToken("admin", List.of("ADMIN", "USER"));
        var claims = jwtValidator.validate(token);
        assertThat(claims).isNotNull();
        assertThat(claims.subject()).isEqualTo("admin");
        assertThat(claims.roles()).containsExactly("ADMIN", "USER");
    }

    private String createTestToken(String subject, List<String> roles) {
        var header = Base64.getUrlEncoder().withoutPadding().encodeToString(
            "{\"alg\":\"HS256\"}".getBytes());
        var rolesJson = roles.stream()
            .map(r -> "\"" + r + "\"")
            .collect(java.util.stream.Collectors.joining(","));
        var payload = Base64.getUrlEncoder().withoutPadding().encodeToString(
            ("{\"sub\":\"" + subject + "\",\"roles\":[" + rolesJson + "]}").getBytes());
        return header + "." + payload + ".signature";
    }
}
