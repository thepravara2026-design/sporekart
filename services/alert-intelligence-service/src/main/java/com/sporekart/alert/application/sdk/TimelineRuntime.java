package com.sporekart.alert.application.sdk;

import com.sporekart.alert.application.engine.TimelineEngine;
import com.sporekart.alert.domain.model.*;
import com.sporekart.alert.domain.repository.AlertRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TimelineRuntime {

    private final TimelineEngine timelineEngine;
    private final AlertRepositoryPort repository;

    public TimelineRuntime(TimelineEngine timelineEngine, AlertRepositoryPort repository) {
        this.timelineEngine = timelineEngine; this.repository = repository;
    }

    public List<TimelineEvent> generateTimeline() {
        List<TimelineEvent> events = timelineEngine.generateTimeline();
        events.forEach(repository::saveTimelineEvent);
        return events;
    }

    public List<TimelineEvent> getAllEvents() { return repository.findAllTimelineEvents(); }

    public Optional<TimelineEvent> findById(String id) { return repository.findTimelineEventById(id); }
}
