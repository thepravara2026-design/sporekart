package com.sporekart.events.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sporekart.events.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventSerializer {
    private static final Logger log = LoggerFactory.getLogger(EventSerializer.class);
    private static final ObjectMapper mapper = createMapper();

    private static ObjectMapper createMapper() {
        ObjectMapper m = new ObjectMapper();
        m.registerModule(new JavaTimeModule());
        m.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        m.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        m.enable(SerializationFeature.INDENT_OUTPUT);
        return m;
    }

    public String serialize(Event event) {
        try {
            return mapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize event {}", event.getEventId(), e);
            throw new RuntimeException("Event serialization failed", e);
        }
    }

    public <T extends Event> T deserialize(String json, Class<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize event", e);
            throw new RuntimeException("Event deserialization failed", e);
        }
    }

    public String toJson(Event event) {
        return serialize(event);
    }

    public ObjectMapper getMapper() {
        return mapper;
    }
}
