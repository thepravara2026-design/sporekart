package com.sporekart.ai.assistant.application;

import com.sporekart.ai.assistant.domain.IntentPriority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class IntentResolverImplTest {

    private IntentResolverImpl resolver;

    @BeforeEach
    void setUp() {
        resolver = new IntentResolverImpl();
    }

    @Test
    void shouldResolveProductSearchIntent() {
        var result = resolver.resolveIntent("search for product", null);
        assertEquals("product_search", result.intent());
        assertFalse(result.isFallback());
    }

    @Test
    void shouldResolveCustomerSupportIntent() {
        var result = resolver.resolveIntent("I need help with a refund", null);
        assertEquals("customer_support", result.intent());
        assertFalse(result.isFallback());
    }

    @Test
    void shouldResolveOrderStatusIntent() {
        var result = resolver.resolveIntent("track my order status", null);
        assertEquals("order_status", result.intent());
        assertFalse(result.isFallback());
    }

    @Test
    void shouldResolvePricingInfoIntent() {
        var result = resolver.resolveIntent("what is the price of this item", null);
        assertEquals("pricing_info", result.intent());
        assertFalse(result.isFallback());
    }

    @Test
    void shouldResolveTrainingRequestIntent() {
        var result = resolver.resolveIntent("I want to learn about farming", null);
        assertEquals("training_request", result.intent());
        assertFalse(result.isFallback());
    }

    @Test
    void shouldResolveAnalyticsReportIntent() {
        var result = resolver.resolveIntent("show me the sales analytics report", null);
        assertEquals("analytics_report", result.intent());
        assertFalse(result.isFallback());
    }

    @Test
    void shouldReturnUnknownForUnrelatedInput() {
        var result = resolver.resolveIntent("hello world", null);
        assertTrue(result.isFallback());
        assertEquals("unknown", result.intent());
    }

    @Test
    void shouldReturnFallbackForNullInput() {
        var result = resolver.resolveIntent(null, null);
        assertTrue(result.isFallback());
        assertEquals("unknown", result.intent());
    }

    @Test
    void shouldReturnFallbackForBlankInput() {
        var result = resolver.resolveIntent("   ", null);
        assertTrue(result.isFallback());
        assertEquals("unknown", result.intent());
    }

    @Test
    void shouldConsiderAvailableIntents() {
        var result = resolver.resolveIntent("search product", List.of("product_search"));
        assertEquals("product_search", result.intent());
        assertFalse(result.isFallback());
    }

    @Test
    void shouldThrowNoResultWhenIntentNotInAvailableList() {
        var result = resolver.resolveIntent("help with issue", List.of("product_search"));
        assertTrue(result.isFallback());
    }

    @Test
    void shouldResolveMultiIntentWithAnd() {
        var results = resolver.resolveMultiIntent("search product and track order", null);
        assertTrue(results.size() >= 2);
    }

    @Test
    void shouldResolveMultiIntentWithComma() {
        var results = resolver.resolveMultiIntent("search product, check price", null);
        assertTrue(results.size() >= 2);
    }

    @Test
    void shouldReturnFallbackForMultiIntentWithEmptyInput() {
        var results = resolver.resolveMultiIntent(null, null);
        assertEquals(1, results.size());
        assertTrue(results.getFirst().isFallback());
    }

    @Test
    void shouldReturnSingleResultForNonCompoundInput() {
        var results = resolver.resolveMultiIntent("search for product", null);
        assertEquals(1, results.size());
    }

    @Test
    void shouldAssignCorrectPriorityBasedOnConfidence() {
        var result = resolver.resolveIntent("order status delivery shipping", null);
        assertFalse(result.isFallback());

        var low = resolver.resolveIntent("hello", null);
        assertTrue(low.isFallback());
        assertEquals(IntentPriority.LOW, low.priority());
    }

    @Test
    void shouldContainMatchedKeywordsInMetadata() {
        var result = resolver.resolveIntent("track my order status", null);
        var metadata = result.metadata();
        assertNotNull(metadata.get("matched_keywords"));
    }

    @Test
    void shouldClassifyIntentWithResolvedStatus() {
        var sessionId = UUID.randomUUID();
        var intent = resolver.classifyIntent(sessionId, "search product");
        assertEquals("product_search", intent.resolvedIntent());
        assertEquals(sessionId, intent.sessionId());
        assertEquals("search product", intent.userInput());
    }

    @Test
    void shouldClassifyIntentWithUnknownStatus() {
        var sessionId = UUID.randomUUID();
        var intent = resolver.classifyIntent(sessionId, "random gibberish xyzzy");
        assertEquals("unknown", intent.resolvedIntent());
        assertEquals("UNKNOWN", intent.status().name());
    }
}
