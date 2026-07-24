package com.sporekart.platform.observability.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.filter.ThresholdFilter;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.EventConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
public class StructuredLoggingConfig {

    private static final Logger log = LoggerFactory.getLogger(StructuredLoggingConfig.class);

    @Value("${logging.file.path:logs}")
    private String logPath;

    @Value("${spring.application.name:sporekart}")
    private String appName;

    @Value("${spring.profiles.active:development}")
    private String activeProfile;

    @EventListener(ApplicationReadyEvent.class)
    public void configureLogging() {
        log.info("Structured logging configured: app={}, profile={}, path={}",
            appName, activeProfile, logPath);
    }
}
