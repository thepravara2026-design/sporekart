package com.sporekart.alert.infrastructure.cache;

import com.sporekart.alert.domain.model.AlertCache;
import com.sporekart.alert.domain.repository.AlertRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlertCacheService {

    private final AlertRepositoryPort repository;
    private final boolean enabled;

    public AlertCacheService(AlertRepositoryPort repository) {
        this.repository = repository;
        this.enabled = true;
    }

    public boolean isEnabled() { return enabled; }

    public Optional<Object> get(String cacheKey, String cacheType) {
        if (!enabled) return Optional.empty();
        return repository.findCacheByKey(cacheKey)
                .filter(c -> !c.isExpired())
                .map(c -> { repository.saveCache(c.withHit()); return c.cachedData(); });
    }

    public void put(String cacheKey, String cacheType, Object data, int ttlSeconds) {
        if (!enabled) return;
        repository.saveCache(AlertCache.create(cacheKey, cacheType, data, ttlSeconds));
    }

    public void invalidate(String cacheKey) { repository.deleteCache(cacheKey); }
    public void clear() { repository.clearCache(); }
    public List<AlertCache> getAllEntries() { return repository.findAllCacheEntries(); }
    public long getActiveEntryCount() {
        return repository.findAllCacheEntries().stream().filter(c -> !c.isExpired()).count();
    }
}
