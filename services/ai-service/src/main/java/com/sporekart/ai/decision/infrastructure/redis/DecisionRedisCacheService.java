package com.sporekart.ai.decision.infrastructure.redis;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class DecisionRedisCacheService {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String RESULT_PREFIX = "decision:result:";
    private static final String METADATA_PREFIX = "decision:metadata:";
    private static final String REGISTRY_PREFIX = "decision:registry:";
    private static final String STATS_PREFIX = "decision:stats:";
    private static final String EXPLANATION_PREFIX = "decision:explanation:";
    private static final long RESULT_TTL = 300;
    private static final long METADATA_TTL = 300;
    private static final long REGISTRY_TTL = 300;
    private static final long STATS_TTL = 120;
    private static final long EXPLANATION_TTL = 300;

    public void cacheResult(String key, Object value) { redisTemplate.opsForValue().set(RESULT_PREFIX + key, value, RESULT_TTL, TimeUnit.SECONDS); }
    public Object getResult(String key) { return redisTemplate.opsForValue().get(RESULT_PREFIX + key); }
    public void evictResult(String key) { redisTemplate.delete(RESULT_PREFIX + key); }
    public void cacheMetadata(String key, Object value) { redisTemplate.opsForValue().set(METADATA_PREFIX + key, value, METADATA_TTL, TimeUnit.SECONDS); }
    public Object getMetadata(String key) { return redisTemplate.opsForValue().get(METADATA_PREFIX + key); }
    public void cacheRegistry(String key, Object value) { redisTemplate.opsForValue().set(REGISTRY_PREFIX + key, value, REGISTRY_TTL, TimeUnit.SECONDS); }
    public Object getRegistry(String key) { return redisTemplate.opsForValue().get(REGISTRY_PREFIX + key); }
    public void cacheStatistics(String key, Object value) { redisTemplate.opsForValue().set(STATS_PREFIX + key, value, STATS_TTL, TimeUnit.SECONDS); }
    public Object getStatistics(String key) { return redisTemplate.opsForValue().get(STATS_PREFIX + key); }
    public void cacheExplanation(String key, Object value) { redisTemplate.opsForValue().set(EXPLANATION_PREFIX + key, value, EXPLANATION_TTL, TimeUnit.SECONDS); }
    public Object getExplanation(String key) { return redisTemplate.opsForValue().get(EXPLANATION_PREFIX + key); }
    public void invalidateAll() { log.info("Invalidating all decision caches"); redisTemplate.delete(redisTemplate.keys("decision:*")); }
}
