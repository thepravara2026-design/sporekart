package com.sporekart.alert.domain.model;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class TimelineEvent {
    private final String id;
    private final TimelineEventType eventType;
    private final String category;
    private final String domain;
    private final String title;
    private final String description;
    private final AlertSeverity severity;
    private final String source;
    private final Map<String, Object> metadata;
    private final Instant timestamp;

    public TimelineEvent(String id, TimelineEventType eventType, String category, String domain,
                         String title, String description, AlertSeverity severity, String source,
                         Map<String, Object> metadata, Instant timestamp) {
        this.id = id; this.eventType = eventType; this.category = category; this.domain = domain;
        this.title = title; this.description = description; this.severity = severity;
        this.source = source; this.metadata = metadata; this.timestamp = timestamp;
    }

    public static TimelineEvent create(TimelineEventType eventType, String category, String domain,
                                        String title, String description, AlertSeverity severity,
                                        String source, Map<String, Object> metadata) {
        return new TimelineEvent(UUID.randomUUID().toString(), eventType, category, domain,
                title, description, severity, source, metadata, Instant.now());
    }

    public String id() { return id; }
    public TimelineEventType eventType() { return eventType; }
    public String category() { return category; }
    public String domain() { return domain; }
    public String title() { return title; }
    public String description() { return description; }
    public AlertSeverity severity() { return severity; }
    public String source() { return source; }
    public Map<String, Object> metadata() { return metadata; }
    public Instant timestamp() { return timestamp; }
}
