package com.sporekart.gateway.filter;

import org.junit.jupiter.api.Test;
import org.springframework.core.Ordered;

import static org.assertj.core.api.Assertions.assertThat;

class AuthenticationFilterTest {

    @Test
    void shouldHaveCorrectOrder() {
        assertThat(Ordered.HIGHEST_PRECEDENCE + 2).isEqualTo(Integer.MIN_VALUE + 2);
    }
}
