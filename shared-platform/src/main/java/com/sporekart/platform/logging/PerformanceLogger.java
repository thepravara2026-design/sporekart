package com.sporekart.platform.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerformanceLogger {
    private final Logger log;
    private final String component;
    private long startTime;

    private PerformanceLogger(String component) {
        this.log = LoggerFactory.getLogger("performance." + component);
        this.component = component;
    }

    public static PerformanceLogger start(String component) {
        PerformanceLogger pl = new PerformanceLogger(component);
        pl.startTime = System.nanoTime();
        return pl;
    }

    public void log(String operation) {
        long elapsed = (System.nanoTime() - startTime) / 1_000_000;
        if (elapsed > 1000) {
            log.warn("[PERF] {}::{} took {}ms (SLOW)", component, operation, elapsed);
        } else if (elapsed > 100) {
            log.info("[PERF] {}::{} took {}ms", component, operation, elapsed);
        } else {
            log.debug("[PERF] {}::{} took {}ms", component, operation, elapsed);
        }
    }

    public long elapsedMs() {
        return (System.nanoTime() - startTime) / 1_000_000;
    }
}
