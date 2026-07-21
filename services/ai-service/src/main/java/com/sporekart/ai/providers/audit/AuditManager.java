package com.sporekart.ai.providers.audit;

import java.time.Instant;
import java.util.List;

public interface AuditManager {
    void recordEvent(AuditEvent event);
    List<AuditEvent> getEvents(String providerId);
    List<AuditEvent> getEventsByType(String eventType);
    List<AuditEvent> getEventsByTimeRange(Instant from, Instant to);
    List<AuditEvent> getRecentEvents(int limit);
    void clearEvents(String providerId);
}
