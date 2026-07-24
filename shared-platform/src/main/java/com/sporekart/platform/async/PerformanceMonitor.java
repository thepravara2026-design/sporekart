package com.sporekart.platform.async;

import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PerformanceMonitor {

    private static final Logger log = LoggerFactory.getLogger(PerformanceMonitor.class);
    private final PerformanceContext context;

    public PerformanceMonitor(PerformanceContext context) {
        this.context = context;
    }

    @Scheduled(fixedRateString = "${sporekart.perfmon.report-interval-ms:60000}")
    public void reportPerformanceMetrics() {
        Map<String, Long> counters = context.getAllCounters();
        if (counters.isEmpty()) {
            return;
        }

        log.info("=== Performance Metrics Report ===");
        counters.forEach((name, count) ->
            log.info("Counter [{}]: {}", name, count)
        );

        context.getAllTrackers().forEach((name, tracker) -> {
            if (tracker.getCount() > 0) {
                log.info("Latency [{}]: avg={}ms, min={}ms, max={}ms, count={}",
                    name,
                    String.format("%.2f", tracker.getAvgMs()),
                    String.format("%.2f", tracker.getMinMs()),
                    String.format("%.2f", tracker.getMaxMs()),
                    tracker.getCount()
                );
            }
        });
    }

    @Scheduled(cron = "${sporekart.perfmon.reset-cron:0 0 * * * ?}")
    public void resetMetrics() {
        context.resetAll();
        log.info("Performance metrics reset");
    }
}
