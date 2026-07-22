package com.sporekart.ai.conversation.api;
import com.sporekart.ai.conversation.domain.*;
import java.util.*;

public interface MemoryRepository {
    List<MemoryEntry> findByLayer(MemoryLayer layer);
    List<MemoryEntry> findByKey(String key);
    List<MemoryEntry> findByLayerAndWorkspace(MemoryLayer layer, WorkspaceId workspaceId);
    List<MemoryEntry> findByLayerAndUser(MemoryLayer layer, String userId);
    List<MemoryEntry> findExpired();
    MemoryEntry save(MemoryEntry entry);
    List<MemoryEntry> saveAll(List<MemoryEntry> entries);
    void delete(String id);
    void clearByLayer(MemoryLayer layer);
    void clearByWorkspace(WorkspaceId workspaceId);
    boolean exists(String key, MemoryLayer layer);
}
