package com.sporekart.ai.providers.registry;

import com.sporekart.ai.providers.registry.audit.AuditEntry;

import java.time.Instant;
import java.util.List;

public interface ProviderAuditManager {
    void recordEvent(AuditEntry entry);
    List<AuditEntry> getEvents(String providerId);
    List<AuditEntry> getEventsByType(String eventType);
    List<AuditEntry> getEventsByTimeRange(Instant from, Instant to);
    List<AuditEntry> getRecentEvents(int limit);
    void clearEvents(String providerId);
}
