package com.sporekart.platform.async;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PerformanceContext {

    private static final Logger log = LoggerFactory.getLogger(PerformanceContext.class);

    private final Map<String, AtomicLong> counters = new ConcurrentHashMap<>();
    private final Map<String, LatencyTracker> trackers = new ConcurrentHashMap<>();

    public void incrementCounter(String name) {
        counters.computeIfAbsent(name, k -> new AtomicLong()).incrementAndGet();
    }

    public long getCounter(String name) {
        return counters.getOrDefault(name, new AtomicLong()).get();
    }

    public Map<String, Long> getAllCounters() {
        Map<String, Long> snapshot = new ConcurrentHashMap<>();
        counters.forEach((k, v) -> snapshot.put(k, v.get()));
        return snapshot;
    }

    public LatencyTracker getLatencyTracker(String name) {
        return trackers.computeIfAbsent(name, k -> new LatencyTracker(name));
    }

    public AutoCloseable trackLatency(String name) {
        LatencyTracker tracker = getLatencyTracker(name);
        long start = System.nanoTime();
        return () -> {
            long duration = System.nanoTime() - start;
            tracker.record(duration);
            if (duration > 50_000_000) {
                log.warn("Slow operation [{}]: {}ms", name, duration / 1_000_000);
            }
        };
    }

    public void resetAll() {
        counters.clear();
        trackers.clear();
    }

    public static class LatencyTracker {
        private final String name;
        private final AtomicLong count = new AtomicLong();
        private final AtomicLong totalNanos = new AtomicLong();
        private final AtomicLong maxNanos = new AtomicLong();
        private final AtomicLong minNanos = new AtomicLong(Long.MAX_VALUE);

        LatencyTracker(String name) {
            this.name = name;
        }

        public void record(long nanos) {
            count.incrementAndGet();
            totalNanos.addAndGet(nanos);
            maxNanos.updateAndGet(m -> Math.max(m, nanos));
            minNanos.updateAndGet(m -> Math.min(m, nanos));
        }

        public long getCount() { return count.get(); }
        public double getAvgMs() { return count.get() > 0 ? totalNanos.get() / 1_000_000.0 / count.get() : 0; }
        public double getMaxMs() { return maxNanos.get() / 1_000_000.0; }
        public double getMinMs() { return minNanos.get() < Long.MAX_VALUE ? minNanos.get() / 1_000_000.0 : 0; }
        public double getTotalMs() { return totalNanos.get() / 1_000_000.0; }
    }
}
