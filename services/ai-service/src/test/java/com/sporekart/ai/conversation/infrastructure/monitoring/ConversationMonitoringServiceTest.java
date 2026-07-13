package com.sporekart.ai.conversation.infrastructure.monitoring;

import com.sporekart.ai.conversation.api.MessageService;
import com.sporekart.ai.conversation.api.SessionManager;
import com.sporekart.ai.conversation.infrastructure.redis.ConversationRedisCacheService;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConversationMonitoringServiceTest {

    @Mock
    private SessionManager sessionManager;

    @Mock
    private MessageService messageService;

    @Mock
    private ConversationRedisCacheService cacheService;

    private SimpleMeterRegistry meterRegistry;
    private ConversationMonitoringService monitoringService;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        monitoringService = new ConversationMonitoringService(
                meterRegistry, sessionManager, messageService, cacheService);
    }

    @Test
    void shouldRecordSessionLatency() {
        var result = monitoringService.recordSessionLatency(() -> "test");
        assertEquals("test", result);
    }

    @Test
    void shouldRecordMessageLatency() {
        var result = monitoringService.recordMessageLatency(() -> 42);
        assertEquals(42, result);
    }

    @Test
    void shouldRecordMessageSent() {
        monitoringService.recordMessageSent("USER");
        var count = meterRegistry.counter("conversation.message.sent", "role", "USER").count();
        assertEquals(1.0, count);
    }

    @Test
    void shouldRecordSessionCreated() {
        monitoringService.recordSessionCreated();
        var count = meterRegistry.counter("conversation.session.created").count();
        assertEquals(1.0, count);
    }

    @Test
    void shouldRecordSessionClosed() {
        monitoringService.recordSessionClosed();
        var count = meterRegistry.counter("conversation.session.closed").count();
        assertEquals(1.0, count);
    }

    @Test
    void shouldRecordCacheHit() {
        monitoringService.recordCacheHit("sessions");
        var count = meterRegistry.counter("conversation.cache.hit", "cache", "sessions").count();
        assertEquals(1.0, count);
    }

    @Test
    void shouldRecordCacheMiss() {
        monitoringService.recordCacheMiss("sessions");
        var count = meterRegistry.counter("conversation.cache.miss", "cache", "sessions").count();
        assertEquals(1.0, count);
    }

    @Test
    void shouldReturnZeroCacheHitRatioWhenNoData() {
        var ratio = monitoringService.getCacheHitRatio("sessions");
        assertEquals(0.0, ratio);
    }

    @Test
    void shouldReturnCacheHitRatio() {
        monitoringService.recordCacheHit("sessions");
        monitoringService.recordCacheMiss("sessions");
        monitoringService.recordCacheMiss("sessions");
        var ratio = monitoringService.getCacheHitRatio("sessions");
        assertEquals(1.0 / 3.0, ratio, 0.001);
    }

    @Test
    void shouldReturnUpHealth() {
        when(sessionManager.getUserSessions("health-check")).thenReturn(List.of());
        var health = monitoringService.checkHealth();
        assertEquals("UP", health.status());
    }

    @Test
    void shouldReturnDownHealthOnError() {
        when(sessionManager.getUserSessions("health-check")).thenThrow(new RuntimeException("DB down"));
        var health = monitoringService.checkHealth();
        assertEquals("DOWN", health.status());
    }
}
