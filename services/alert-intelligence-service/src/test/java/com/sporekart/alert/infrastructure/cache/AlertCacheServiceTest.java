package com.sporekart.alert.infrastructure.cache;

import com.sporekart.alert.domain.model.AlertCache;
import com.sporekart.alert.domain.repository.AlertRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertCacheServiceTest {

    @Mock private AlertRepositoryPort repository;
    private AlertCacheService cacheService;

    @BeforeEach
    void setUp() { cacheService = new AlertCacheService(repository); }

    @Test
    void getShouldReturnEmptyWhenNotCached() {
        when(repository.findCacheByKey("nonexistent")).thenReturn(Optional.empty());
        assertTrue(cacheService.get("nonexistent", "ALERT").isEmpty());
    }

    @Test
    void putShouldStoreAndGetShouldRetrieve() {
        when(repository.findCacheByKey("alert:001")).thenReturn(Optional.empty());
        when(repository.saveCache(any())).thenAnswer(i -> i.getArgument(0));
        cacheService.put("alert:001", "ALERT", "{}", 60);
        var cached = AlertCache.create("alert:001", "ALERT", "{}", 60);
        when(repository.findCacheByKey("alert:001")).thenReturn(Optional.of(cached));
        var result = cacheService.get("alert:001", "ALERT");
        assertTrue(result.isPresent());
        assertEquals("{}", result.get());
    }

    @Test
    void invalidateShouldRemoveEntry() {
        cacheService.invalidate("alert:001");
        verify(repository).deleteCache("alert:001");
    }

    @Test
    void clearShouldRemoveAllEntries() {
        cacheService.clear();
        verify(repository).clearCache();
    }

    @Test
    void isEnabledShouldReturnTrueByDefault() {
        assertTrue(cacheService.isEnabled());
    }

    @Test
    void getActiveEntryCountShouldReturnNonExpiredCount() {
        var cache = AlertCache.create("a:1", "ALERT", "1", 3600);
        when(repository.findAllCacheEntries()).thenReturn(List.of(cache));
        assertEquals(1, cacheService.getActiveEntryCount());
    }

    @Test
    void getAllEntriesShouldReturnAllCachedEntries() {
        var c1 = AlertCache.create("a:1", "ALERT", "1", 3600);
        var c2 = AlertCache.create("a:2", "RISK", "2", 3600);
        when(repository.findAllCacheEntries()).thenReturn(List.of(c1, c2));
        assertEquals(2, cacheService.getAllEntries().size());
    }
}
