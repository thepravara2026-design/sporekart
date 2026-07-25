# Alert SDK Guide

## Overview
The Alert SDK provides a fluent, type-safe Java API for integrating with the Alert Intelligence Engine from external services.

## Dependencies
```xml
<dependency>
    <groupId>com.sporekart.enterprise</groupId>
    <artifactId>alert-intelligence-service</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Client Initialization
```java
AlertSDK sdk = new AlertClient(alertService);
```

## AlertRuntime API
```java
AlertRuntime alerts = sdk.getAlertEngine();

// Query
Alert a = alerts.getById(id);
List<Alert> all = alerts.getAll();
List<Alert> open = alerts.getByStatus(AlertStatus.OPEN);
List<Alert> critical = alerts.getBySeverity(AlertSeverity.CRITICAL);
List<Alert> business = alerts.getByCategory(AlertCategory.BUSINESS);

// Actions
alerts.acknowledge(id);
alerts.resolve(id);
```

## RiskRuntime API
```java
RiskRuntime risks = sdk.getRiskEngine();
List<BusinessRisk> all = risks.getAllRisks();
BusinessRisk r = risks.getRiskById(id);
RiskSummary summary = risks.getRiskSummary();
```

## TimelineRuntime API
```java
TimelineRuntime timeline = sdk.getTimelineEngine();
List<TimelineEvent> events = timeline.getTimeline();
List<TimelineEvent> filtered = timeline.getEventsByType(TimelineEventType.BUSINESS);
```

## AlertBuilder Example
```java
Alert alert = AlertBuilder.create()
    .id(UUID.randomUUID().toString())
    .title("Revenue Drop Detected")
    .description("Revenue 15% below forecast")
    .category(AlertCategory.BUSINESS)
    .severity(AlertSeverity.HIGH)
    .source("analytics-engine")
    .timestamp(Instant.now())
    .metadata(Map.of("expected", "1000000", "actual", "850000"))
    .build();
```
