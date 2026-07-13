package com.sporekart.ai.admin.infrastructure.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.admin.domain.AdminConfiguration;
import com.sporekart.ai.admin.domain.ConfigurationStatus;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminRedisCacheServiceTest {

    @Mock private StringRedisTemplate redisTemplate;
    @Mock private ValueOperations<String, String> valueOperations;

    private AdminRedisCacheService cacheService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MeterRegistry meterRegistry = new SimpleMeterRegistry();

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        cacheService = new AdminRedisCacheService(redisTemplate, objectMapper, meterRegistry);
    }

    @Test
    void testCacheConfiguration() {
        var config = new AdminConfiguration(UUID.randomUUID(), "key", "value", "mod", "env",
                "desc", ConfigurationStatus.ACTIVE, 1, UUID.randomUUID(), Instant.now(), Instant.now());

        cacheService.cacheConfiguration("cache-key", config);
        verify(valueOperations).set(eq("admin:configuration:cache-key"), anyString(), eq(300L), eq(TimeUnit.SECONDS));
    }

    @Test
    void testGetConfiguration_Hit() {
        var config = new AdminConfiguration(UUID.randomUUID(), "key", "value", "mod", "env",
                "desc", ConfigurationStatus.ACTIVE, 1, UUID.randomUUID(), Instant.now(), Instant.now());
        when(valueOperations.get("admin:configuration:cache-key")).thenReturn(objectMapper.writeValueAsString(config));

        var result = cacheService.getConfiguration("cache-key");
        assertTrue(result.isPresent());
        assertEquals("key", result.get().key());
    }

    @Test
    void testGetConfiguration_Miss() {
        when(valueOperations.get("admin:configuration:cache-key")).thenReturn(null);
        var result = cacheService.getConfiguration("cache-key");
        assertFalse(result.isPresent());
    }

    @Test
    void testEvictConfiguration() {
        cacheService.evictConfiguration("cache-key");
        verify(redisTemplate).delete("admin:configuration:cache-key");
    }

    @Test
    void testCacheFeatureFlag() {
        var featureFlag = new com.sporekart.ai.admin.domain.FeatureFlag(
                UUID.randomUUID(), "ff-key", "FF", "desc", true, "prod", "mod",
                java.util.Map.of(), Instant.now(), Instant.now());

        cacheService.cacheFeatureFlag("ff-key", featureFlag);
        verify(valueOperations).set(eq("admin:featureFlag:ff-key"), anyString(), eq(300L), eq(TimeUnit.SECONDS));
    }

    @Test
    void testGetFeatureFlag_Hit() {
        var flag = new com.sporekart.ai.admin.domain.FeatureFlag(
                UUID.randomUUID(), "ff-key", "FF", "desc", true, "prod", "mod",
                java.util.Map.of(), Instant.now(), Instant.now());
        when(valueOperations.get("admin:featureFlag:ff-key")).thenReturn(objectMapper.writeValueAsString(flag));

        var result = cacheService.getFeatureFlag("ff-key");
        assertTrue(result.isPresent());
        assertTrue(result.get().enabled());
    }

    @Test
    void testGetFeatureFlag_Miss() {
        when(valueOperations.get("admin:featureFlag:ff-key")).thenReturn(null);
        var result = cacheService.getFeatureFlag("ff-key");
        assertFalse(result.isPresent());
    }

    @Test
    void testCacheAndGetEnvironment() {
        var profile = new com.sporekart.ai.admin.domain.EnvironmentProfile(
                UUID.randomUUID(), "prod", com.sporekart.ai.admin.domain.EnvironmentType.PRODUCTION,
                "Production env", true, "db", Instant.now(), Instant.now());

        cacheService.cacheEnvironment("prod", profile);
        verify(valueOperations).set(eq("admin:environment:prod"), anyString(), eq(600L), eq(TimeUnit.SECONDS));

        when(valueOperations.get("admin:environment:prod")).thenReturn(objectMapper.writeValueAsString(profile));
        var result = cacheService.getEnvironment("prod");
        assertTrue(result.isPresent());
        assertEquals("prod", result.get().name());
    }

    @Test
    void testCacheAndGetSnapshot() {
        var snapshot = new com.sporekart.ai.admin.domain.ConfigurationSnapshot(
                UUID.randomUUID(), "snap1", java.util.Map.of("k", "v"), "prod", "desc",
                Instant.now(), UUID.randomUUID());

        cacheService.cacheSnapshot("snap1", snapshot);
        verify(valueOperations).set(eq("admin:snapshot:snap1"), anyString(), eq(600L), eq(TimeUnit.SECONDS));

        when(valueOperations.get("admin:snapshot:snap1")).thenReturn(objectMapper.writeValueAsString(snapshot));
        var result = cacheService.getSnapshot("snap1");
        assertTrue(result.isPresent());
        assertEquals("snap1", result.get().name());
    }

    @Test
    void testCacheAndGetMetadata() {
        cacheService.cacheMetadata("meta-key", java.util.Map.of("k", "v"));
        verify(valueOperations).set(eq("admin:metadata:meta-key"), anyString(), eq(300L), eq(TimeUnit.SECONDS));
    }

    @Test
    void testEvictFeatureFlag() {
        cacheService.evictFeatureFlag("ff-key");
        verify(redisTemplate).delete("admin:featureFlag:ff-key");
    }

    @Test
    void testEvictEnvironment() {
        cacheService.evictEnvironment("prod");
        verify(redisTemplate).delete("admin:environment:prod");
    }

    @Test
    void testEvictSnapshot() {
        cacheService.evictSnapshot("snap1");
        verify(redisTemplate).delete("admin:snapshot:snap1");
    }

    @Test
    void testEvictMetadata() {
        cacheService.evictMetadata("meta-key");
        verify(redisTemplate).delete("admin:metadata:meta-key");
    }

    @Test
    void testInvalidateAll_WithKeys() {
        var keys = Set.of("admin:configuration:cfg1", "admin:featureFlag:ff1");
        when(redisTemplate.keys(anyString())).thenReturn(keys);

        cacheService.invalidateAll();
        verify(redisTemplate, atLeastOnce()).delete(anySet());
    }

    @Test
    void testInvalidateAll_NoKeys() {
        when(redisTemplate.keys(anyString())).thenReturn(Set.of());
        cacheService.invalidateAll();
        verify(redisTemplate, never()).delete(anySet());
    }
}
