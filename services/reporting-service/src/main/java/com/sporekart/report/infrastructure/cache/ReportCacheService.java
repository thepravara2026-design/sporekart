package com.sporekart.report.infrastructure.cache;

import com.sporekart.report.domain.model.ReportCache;
import com.sporekart.report.domain.repository.ReportRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReportCacheService {
    private final ReportRepositoryPort repository;
    private boolean enabled = true;

    public ReportCacheService(ReportRepositoryPort repository) {
        this.repository = repository;
    }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public Optional<Object> get(String cacheType, String cacheKey) {
        if (!enabled) return Optional.empty();
        return repository.findCacheByKey(cacheKey)
            .filter(entry -> !entry.isExpired())
            .map(entry -> {
                repository.saveCache(entry.withHit());
                return entry.cachedData();
            });
    }

    public void put(String cacheType, String cacheKey, Object data, int ttlSeconds) {
        if (!enabled) return;
        repository.saveCache(ReportCache.create(cacheType, cacheKey, data, ttlSeconds));
    }

    public void invalidate(String cacheKey) {
        repository.deleteCache(cacheKey);
    }

    public void clear() {
        repository.clearCache();
    }

    public List<ReportCache> getAllEntries() {
        return repository.findAllCacheEntries();
    }

    public long getActiveEntryCount() {
        return repository.findAllCacheEntries().stream()
            .filter(e -> !e.isExpired())
            .count();
    }

    public int getTotalEntryCount() {
        return repository.findAllCacheEntries().size();
    }
}
