package com.sporekart.ai.providers.registry.audit;

import java.time.Instant;
import java.util.List;

public interface AuditLog {
    void write(AuditEntry entry);
    List<AuditEntry> readByProvider(String providerId);
    List<AuditEntry> readByType(String eventType);
    List<AuditEntry> readByTimeRange(Instant from, Instant to);
    List<AuditEntry> readRecent(int limit);
    void clear(String providerId);
}
