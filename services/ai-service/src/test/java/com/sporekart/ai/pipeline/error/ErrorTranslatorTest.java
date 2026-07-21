package com.sporekart.ai.pipeline.error;

import com.sporekart.ai.pipeline.PipelineException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorTranslatorTest {
    private ErrorTranslator translator;

    @BeforeEach
    void setUp() {
        translator = new ErrorTranslator();
    }

    @Test
    void shouldTranslateProviderAuthError() {
        var error = translator.translateProviderError("OPENAI", "Authentication failed: invalid API key");
        assertEquals("AUTH_ERROR", error.code());
        assertEquals(PipelineError.ErrorCategory.AUTHENTICATION, error.category());
    }

    @Test
    void shouldTranslateProviderRateLimit() {
        var error = translator.translateProviderError("OPENAI", "429 Too Many Requests");
        assertEquals("RATE_LIMIT", error.code());
    }

    @Test
    void shouldTranslateProviderQuotaExceeded() {
        var error = translator.translateProviderError("OPENAI", "quota exceeded for today");
        assertEquals("QUOTA_EXCEEDED", error.code());
    }

    @Test
    void shouldTranslateProviderTimeout() {
        var error = translator.translateProviderError("GEMINI", "504 Gateway Timeout");
        assertEquals("TIMEOUT", error.code());
    }

    @Test
    void shouldTranslateProviderOffline() {
        var error = translator.translateProviderError("CLAUDE", "503 Service Unavailable");
        assertEquals("PROVIDER_OFFLINE", error.code());
    }

    @Test
    void shouldTranslateInvalidModel() {
        var error = translator.translateProviderError("OPENAI", "model 'gpt-5' not found");
        assertEquals("INVALID_MODEL", error.code());
    }

    @Test
    void shouldTranslateNetworkError() {
        var error = translator.translateProviderError("OLLAMA", "Connection refused");
        assertEquals("NETWORK_ERROR", error.code());
    }

    @Test
    void shouldTranslateMalformedResponse() {
        var error = translator.translateProviderError("GROQ", "Malformed response from provider");
        assertEquals("MALFORMED_RESPONSE", error.code());
    }

    @Test
    void shouldTranslatePipelineException() {
        var pe = new PipelineException("req-1", PipelineException.ErrorCode.VALIDATION_FAILED, "Bad request");
        var error = translator.translate(pe);
        assertEquals("VALIDATION_FAILED", error.code());
    }

    @Test
    void shouldClassifyByMessage() {
        var error = translator.translate(new RuntimeException("Authentication failed"));
        assertEquals("AUTH_ERROR", error.code());
    }

    @Test
    void shouldReturnUnknownForNull() {
        var error = translator.translate((Throwable) null);
        assertEquals("UNKNOWN_ERROR", error.code());
    }
}
