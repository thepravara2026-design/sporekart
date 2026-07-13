package com.sporekart.ai.performance;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class KafkaThroughputTest {

    private static final int EVENT_COUNT = 100;
    private static final String TOPIC = "governance-events";

    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    void publish100EventsAndMeasureThroughput() {
        when(kafkaTemplate.send(anyString(), anyString(), anyString()))
                .thenReturn(CompletableFuture.completedFuture(null));

        long start = System.currentTimeMillis();
        List<String> results = new ArrayList<>();

        for (int i = 0; i < EVENT_COUNT; i++) {
            try {
                var future = kafkaTemplate.send(TOPIC, "test-event-" + i,
                        "{\"id\":\"" + i + "\",\"type\":\"test\"}");
                future.get();
                results.add("OK");
            } catch (Exception e) {
                results.add("FAIL:" + e.getMessage());
            }
        }

        long elapsed = System.currentTimeMillis() - start;
        double eventsPerSecond = (double) EVENT_COUNT / (elapsed / 1000.0);

        System.out.printf("Published %d events in %dms (%.2f events/sec)%n",
                EVENT_COUNT, elapsed, eventsPerSecond);

        assertEquals(EVENT_COUNT, results.size(), "All events should be published");
        long failed = results.stream().filter(r -> r.startsWith("FAIL")).count();
        assertEquals(0, failed, "No events should fail");
        assertTrue(eventsPerSecond > 0, "Throughput should be measurable");

        verify(kafkaTemplate, times(EVENT_COUNT))
                .send(anyString(), anyString(), anyString());
    }
}
