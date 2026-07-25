# Alert SDK Design

## Purpose
Provides a fluent, type-safe SDK for consuming alert, risk, anomaly, and timeline engine results from external services.

## Core Interfaces

### AlertSDK (marker interface)
- Base interface for all runtime objects

### AlertClient
- `getAlertEngine()` — Returns AlertRuntime
- `getRiskEngine()` — Returns RiskRuntime
- `getTimelineEngine()` — Returns TimelineRuntime

### AlertRuntime
- Alert CRUD: `getById`, `getAll`, `getByStatus`, `getBySeverity`, `getByCategory`
- Cache operations: `getCacheStats`, `clearCache`

### RiskRuntime
- `getAllRisks()`, `getRiskById()`, `getRiskSummary()`

### TimelineRuntime
- `getTimeline()`, `getEventsByType()`

## AlertBuilder (fluent builder)
```java
Alert alert = AlertBuilder.create()
    .id(id)
    .title(title)
    .description(desc)
    .category(category)
    .severity(severity)
    .source(source)
    .timestamp(now)
    .status(OPEN)
    .metadata(Map.of("key", "value"))
    .build();
```

## Usage
```java
AlertClient client = new AlertClient(alertService);
AlertRuntime runtime = client.getAlertEngine();
List<Alert> criticalAlerts = runtime.getBySeverity(CRITICAL);
```
