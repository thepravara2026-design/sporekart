package com.sporekart.prompt.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TokenEstimatorTest {

    private TokenEstimator estimator;

    @BeforeEach
    void setUp() {
        estimator = new TokenEstimator();
    }

    @Test
    void shouldEstimatePromptTokens() {
        var tokens = estimator.estimatePromptTokens("Hello world, this is a test prompt");
        assertThat(tokens).isPositive();
    }

    @Test
    void shouldReturnZeroForNullPrompt() {
        assertThat(estimator.estimatePromptTokens(null)).isZero();
    }

    @Test
    void shouldReturnZeroForBlankPrompt() {
        assertThat(estimator.estimatePromptTokens("   ")).isZero();
    }

    @Test
    void shouldEstimateSystemTokens() {
        var tokens = estimator.estimateSystemTokens("You are a helpful assistant");
        assertThat(tokens).isPositive();
    }

    @Test
    void shouldReturnZeroForNullSystem() {
        assertThat(estimator.estimateSystemTokens(null)).isZero();
    }

    @Test
    void shouldEstimateTotalTokens() {
        var total = estimator.estimateTotalTokens("Hello {{name}}", "System prompt", null);
        assertThat(total).isPositive();
    }

    @Test
    void shouldEstimateCompletionTokens() {
        var tokens = estimator.estimateCompletionTokens("This is a completion response");
        assertThat(tokens).isPositive();
    }

    @Test
    void shouldReturnZeroForNullCompletion() {
        assertThat(estimator.estimateCompletionTokens(null)).isZero();
    }
}
