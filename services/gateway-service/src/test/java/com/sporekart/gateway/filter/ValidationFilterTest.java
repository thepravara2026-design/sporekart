package com.sporekart.gateway.filter;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ValidationFilterTest {

    private final ValidationFilter filter = new ValidationFilter();

    @Test
    void shouldHaveCorrectOrder() {
        assertThat(filter.getOrder()).isEqualTo(Integer.MIN_VALUE + 4);
    }
}
