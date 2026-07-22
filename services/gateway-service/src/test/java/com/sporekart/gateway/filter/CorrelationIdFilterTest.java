package com.sporekart.gateway.filter;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CorrelationIdFilterTest {

    private final CorrelationIdFilter filter = new CorrelationIdFilter();

    @Test
    void shouldHaveCorrectOrder() {
        assertThat(filter.getOrder()).isEqualTo(Integer.MIN_VALUE);
    }

    @Test
    void shouldDefineCorrelationIdHeader() {
        assertThat(CorrelationIdFilter.CORRELATION_ID_HEADER).isEqualTo("X-Correlation-Id");
        assertThat(CorrelationIdFilter.REQUEST_ID_HEADER).isEqualTo("X-Request-Id");
    }
}
