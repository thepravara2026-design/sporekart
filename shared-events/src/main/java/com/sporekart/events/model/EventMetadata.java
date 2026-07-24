package com.sporekart.events.model;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EventMetadata {
    private final Map<String, String> entries;

    public EventMetadata() {
        this.entries = new HashMap<>();
    }

    public EventMetadata(Map<String, String> entries) {
        this.entries = new HashMap<>(entries);
    }

    public void put(String key, String value) {
        entries.put(key, value);
    }

    public String get(String key) {
        return entries.get(key);
    }

    @JsonValue
    public Map<String, String> toMap() {
        return Collections.unmodifiableMap(entries);
    }

    public static EventMetadata empty() {
        return new EventMetadata();
    }
}
