package com.sporekart.gateway.filter;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorizationFilterTest {

    private final AuthorizationFilter filter = new AuthorizationFilter();

    @Test
    void shouldHaveCorrectOrder() {
        assertThat(filter.getOrder()).isEqualTo(Integer.MIN_VALUE + 3);
    }
}
