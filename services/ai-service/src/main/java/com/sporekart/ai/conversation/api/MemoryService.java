package com.sporekart.ai.conversation.api;
import com.sporekart.ai.conversation.domain.*;
import java.time.Duration;
import java.util.*;

public interface MemoryService {
    void store(MemoryLayer layer, String key, String value, Map<String,String> metadata);
    void store(MemoryLayer layer, String key, String value, Map<String,String> metadata, Duration ttl);
    Optional<MemoryEntry> retrieve(String key, MemoryLayer layer);
    List<MemoryEntry> retrieveByLayer(MemoryLayer layer);
    List<MemoryEntry> retrieveByLayerAndWorkspace(MemoryLayer layer, WorkspaceId workspaceId);
    List<MemoryEntry> query(String query, MemoryLayer layer);
    void clear(MemoryLayer layer);
    void clearWorkspace(WorkspaceId workspaceId);
    void promote(MemoryEntry entry, MemoryLayer targetLayer);
}
