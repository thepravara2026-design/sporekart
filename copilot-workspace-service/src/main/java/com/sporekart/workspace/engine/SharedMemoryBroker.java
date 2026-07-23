package com.sporekart.workspace.engine;

import com.sporekart.workspace.domain.SharedMemoryEntry;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class SharedMemoryBroker {

    private static final Logger log = LoggerFactory.getLogger(SharedMemoryBroker.class);

    private final ConcurrentHashMap<String, SharedMemoryEntry> store = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @PostConstruct
    public void init() {
        scheduler.scheduleAtFixedRate(this::expireEntries, 1, 1, TimeUnit.MINUTES);
    }

    public SharedMemoryEntry store(String namespace, String key, String value, String type, List<String> tags, int ttlMinutes) {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime expiresAt = ttlMinutes > 0 ? now.plusMinutes(ttlMinutes) : null;
        SharedMemoryEntry entry = new SharedMemoryEntry(
            UUID.randomUUID().toString(),
            namespace,
            key,
            value,
            type,
            tags != null ? tags : List.of(),
            ttlMinutes,
            now,
            expiresAt
        );
        store.put(entry.entryId(), entry);
        return entry;
    }

    public Optional<SharedMemoryEntry> get(String namespace, String key) {
        return store.values().stream()
            .filter(e -> e.namespace().equals(namespace) && e.key().equals(key))
            .filter(e -> !isExpired(e))
            .findFirst();
    }

    public List<SharedMemoryEntry> getByNamespace(String namespace) {
        return store.values().stream()
            .filter(e -> e.namespace().equals(namespace))
            .filter(e -> !isExpired(e))
            .collect(Collectors.toList());
    }

    public List<SharedMemoryEntry> getByTag(String tag) {
        return store.values().stream()
            .filter(e -> e.tags().contains(tag))
            .filter(e -> !isExpired(e))
            .collect(Collectors.toList());
    }

    public List<SharedMemoryEntry> search(String query, String namespace) {
        String lowerQuery = query.toLowerCase();
        return store.values().stream()
            .filter(e -> namespace == null || e.namespace().equals(namespace))
            .filter(e -> !isExpired(e))
            .filter(e -> e.key().toLowerCase().contains(lowerQuery)
                || e.value().toLowerCase().contains(lowerQuery)
                || e.tags().stream().anyMatch(t -> t.toLowerCase().contains(lowerQuery)))
            .collect(Collectors.toList());
    }

    public boolean delete(String namespace, String key) {
        return store.values().removeIf(e -> e.namespace().equals(namespace) && e.key().equals(key));
    }

    public int deleteByNamespace(String namespace) {
        int before = store.size();
        store.values().removeIf(e -> e.namespace().equals(namespace));
        return before - store.size();
    }

    public void expireEntries() {
        int before = store.size();
        store.values().removeIf(this::isExpired);
        int removed = before - store.size();
        if (removed > 0) {
            log.info("Expired {} shared memory entries", removed);
        }
    }

    public Map<String, Object> getMemorySummary() {
        Map<String, Long> namespaceCounts = store.values().stream()
            .filter(e -> !isExpired(e))
            .collect(Collectors.groupingBy(SharedMemoryEntry::namespace, Collectors.counting()));
        Map<String, Object> summary = new HashMap<>(namespaceCounts);
        summary.put("total", store.values().stream().filter(e -> !isExpired(e)).count());
        return summary;
    }

    private boolean isExpired(SharedMemoryEntry entry) {
        return entry.expiresAt() != null && OffsetDateTime.now().isAfter(entry.expiresAt());
    }
}