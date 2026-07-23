package com.sporekart.copilot.memory;

import java.util.List;

public interface MemoryStore {

    void store(MemoryEntry entry);

    MemoryEntry retrieve(String id);

    List<MemoryEntry> findByType(MemoryType type);

    List<MemoryEntry> search(String query);

    void delete(String id);

    void clear();
}
