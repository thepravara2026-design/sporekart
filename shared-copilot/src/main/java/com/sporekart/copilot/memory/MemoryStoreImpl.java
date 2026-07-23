package com.sporekart.copilot.memory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class MemoryStoreImpl implements MemoryStore {

    private static final Logger log = LoggerFactory.getLogger(MemoryStoreImpl.class);

    private final ConcurrentMap<String, MemoryEntry> entries = new ConcurrentHashMap<>();
    private final ConcurrentMap<MemoryType, List<String>> typeIndex = new ConcurrentHashMap<>();

    public MemoryStoreImpl() {
        for (MemoryType type : MemoryType.values()) {
            typeIndex.put(type, new CopyOnWriteArrayList<>());
        }
        log.debug("MemoryStoreImpl initialized");
    }

    @Override
    public void store(MemoryEntry entry) {
        purgeExpired();
        entries.put(entry.id(), entry);
        typeIndex.get(entry.type()).add(entry.id());
        log.debug("Stored memory entry: id={}, type={}, key={}", entry.id(), entry.type(), entry.key());
    }

    @Override
    public MemoryEntry retrieve(String id) {
        MemoryEntry entry = entries.get(id);
        if (entry != null && entry.isExpired()) {
            delete(id);
            return null;
        }
        return entry;
    }

    @Override
    public List<MemoryEntry> findByType(MemoryType type) {
        purgeExpired();
        List<String> ids = typeIndex.getOrDefault(type, new CopyOnWriteArrayList<>());
        return ids.stream()
            .map(entries::get)
            .filter(e -> e != null && !e.isExpired())
            .collect(Collectors.toList());
    }

    @Override
    public List<MemoryEntry> search(String query) {
        purgeExpired();
        String lowerQuery = query.toLowerCase();
        return entries.values().stream()
            .filter(e -> !e.isExpired())
            .filter(e -> {
                String key = e.key().toLowerCase();
                String strValue = String.valueOf(e.value()).toLowerCase();
                return key.contains(lowerQuery) || strValue.contains(lowerQuery);
            })
            .sorted(Comparator.comparingDouble(MemoryEntry::relevance).reversed())
            .collect(Collectors.toList());
    }

    @Override
    public void delete(String id) {
        MemoryEntry entry = entries.remove(id);
        if (entry != null) {
            typeIndex.get(entry.type()).remove(id);
            log.debug("Deleted memory entry: id={}", id);
        }
    }

    @Override
    public void clear() {
        entries.clear();
        for (MemoryType type : MemoryType.values()) {
            typeIndex.get(type).clear();
        }
        log.debug("Cleared all memory entries");
    }

    private void purgeExpired() {
        List<String> expiredIds = new ArrayList<>();
        for (MemoryEntry entry : entries.values()) {
            if (entry.isExpired()) {
                expiredIds.add(entry.id());
            }
        }
        for (String id : expiredIds) {
            delete(id);
        }
        if (!expiredIds.isEmpty()) {
            log.debug("Purged {} expired memory entries", expiredIds.size());
        }
    }
}
