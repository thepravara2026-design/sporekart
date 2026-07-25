package com.sporekart.alert.application.engine;

import com.sporekart.alert.domain.repository.AlertRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimelineEngineTest {

    @Mock private AlertRepositoryPort repository;
    private TimelineEngine engine;

    @BeforeEach
    void setUp() {
        when(repository.saveTimelineEvent(any())).thenAnswer(i -> i.getArgument(0));
        engine = new TimelineEngine(repository);
    }

    @Test
    void generateTimelineShouldReturnEvents() {
        var events = engine.generateTimeline();
        assertNotNull(events);
        assertFalse(events.isEmpty());
        assertTrue(events.size() >= 9);
    }
}
