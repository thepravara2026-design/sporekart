package com.sporekart.ai.policy.infrastructure.redis;
import com.sporekart.ai.policy.infrastructure.redis.PolicyRedisCacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PolicyRedisCacheServiceTest {
    @Mock private RedisTemplate<String, Object> redisTemplate;
    @Mock private ValueOperations<String, Object> valueOps;
    private PolicyRedisCacheService cacheService;

    @BeforeEach void setUp() { cacheService = new PolicyRedisCacheService(redisTemplate); }

    @Test void testCacheRegistry() { when(redisTemplate.opsForValue()).thenReturn(valueOps); assertDoesNotThrow(() -> cacheService.cacheRegistry("reg1", "data")); }
    @Test void testGetRegistry() { when(redisTemplate.opsForValue()).thenReturn(valueOps); when(valueOps.get("policy:registry:reg1")).thenReturn("data"); assertEquals("data", cacheService.getRegistry("reg1")); }
    @Test void testCacheCompiled() { when(redisTemplate.opsForValue()).thenReturn(valueOps); assertDoesNotThrow(() -> cacheService.cacheCompiled("c1", "code")); }
    @Test void testCacheMetadata() { when(redisTemplate.opsForValue()).thenReturn(valueOps); assertDoesNotThrow(() -> cacheService.cacheMetadata("m1", "meta")); }
    @Test void testCacheEvaluation() { when(redisTemplate.opsForValue()).thenReturn(valueOps); assertDoesNotThrow(() -> cacheService.cacheEvaluation("e1", "result")); }
    @Test void testCacheHealth() { when(redisTemplate.opsForValue()).thenReturn(valueOps); assertDoesNotThrow(() -> cacheService.cacheHealth("h1", "ok")); }
    @Test void testEvictRegistry() { when(redisTemplate.opsForValue()).thenReturn(valueOps); cacheService.evictRegistry("reg1"); verify(redisTemplate).delete("policy:registry:reg1"); }
}
