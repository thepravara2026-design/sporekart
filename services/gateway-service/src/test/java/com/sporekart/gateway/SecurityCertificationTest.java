package com.sporekart.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SecurityCertificationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ReactiveJwtDecoder jwtDecoder;

    @Test
    void healthEndpointShouldBePublic() {
        webTestClient.get()
            .uri("/actuator/health")
            .exchange()
            .expectStatus().isOk();
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
    void jwtDecoderShouldAcceptValidSignedToken() {
        var token = com.sporekart.gateway.security.JwtTestHelper.createSignedToken("testuser", List.of("USER"));
        jwtDecoder.decode(token)
            .map(jwt -> {
                assertThat(jwt.getSubject()).isEqualTo("testuser");
                assertThat(jwt.getClaimAsStringList("roles")).containsExactly("USER");
                return jwt;
            })
            .block();
    }

    @Test
    void jwtDecoderShouldRejectUnsignedToken() {
        var token = com.sporekart.gateway.security.JwtTestHelper.createUnsignedToken("attacker", List.of("ADMIN"));
        var result = jwtDecoder.decode(token)
            .map(jwt -> true)
            .onErrorReturn(false)
            .block();
        assertThat(result).isFalse();
    }

    @Test
    void jwtDecoderShouldRejectNullToken() {
        var result = jwtDecoder.decode("")
            .map(jwt -> true)
            .onErrorReturn(false)
            .block();
        assertThat(result).isFalse();
    }
}
