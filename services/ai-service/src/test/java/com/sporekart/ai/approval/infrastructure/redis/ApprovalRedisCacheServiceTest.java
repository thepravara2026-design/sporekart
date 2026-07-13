package com.sporekart.ai.approval.infrastructure.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.approval.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApprovalRedisCacheServiceTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    private ObjectMapper objectMapper;
    private ApprovalRedisCacheService cacheService;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        cacheService = new ApprovalRedisCacheService(redisTemplate, objectMapper);
    }

    @Test
    void shouldCachePending() throws Exception {
        var request = new ApprovalRequest(UUID.randomUUID(), "content", "publish",
            Map.of(), Map.of(), "user1", List.of("admin"),
            "reason", "normal", null, Map.of(), null, ApprovalStatus.PENDING, OffsetDateTime.now());

        cacheService.cachePending("key1", request);

        var expectedJson = objectMapper.writeValueAsString(request);
        verify(valueOperations).set(eq("approval:pending:key1"), eq(expectedJson), eq(60L), eq(TimeUnit.SECONDS));
    }

    @Test
    void shouldGetPending() throws Exception {
        var request = new ApprovalRequest(UUID.randomUUID(), "content", "publish",
            Map.of(), Map.of(), "user1", List.of("admin"),
            "reason", "normal", null, Map.of(), null, ApprovalStatus.PENDING, OffsetDateTime.now());

        var json = objectMapper.writeValueAsString(request);
        when(valueOperations.get("approval:pending:key1")).thenReturn(json);

        var result = cacheService.getPending("key1");

        assertNotNull(result);
        assertEquals(request.id(), result.id());
    }

    @Test
    void shouldInvalidateAll() {
        when(redisTemplate.keys("approval:*")).thenReturn(Set.of("approval:pending:key1", "approval:config:key2"));

        cacheService.invalidateAll();

        verify(redisTemplate).delete(Set.of("approval:pending:key1", "approval:config:key2"));
    }
}
