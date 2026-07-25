# Report SDK

## Overview
The Report SDK provides a fluent, type-safe Java API for consuming report, template, schedule, export, and BI services from external modules.

## Core Interfaces

### ReportSDK (30+ methods)
```java
public interface ReportSDK {
    // Reports
    List<Report> getAllReports();
    Optional<Report> getReportById(String id);
    List<Report> getReportsByType(ReportType type);
    List<Report> getReportsByCategory(ReportCategory category);
    List<Report> getReportsByStatus(ReportStatus status);
    List<Report> generateAllReports();
    List<Report> generateReportsForCategory(ReportCategory category);
    Report generateReport(String title, String description, ReportType type,
        ReportCategory category, String owner, ...);

    // Templates, Schedules, Exports, BI Reports, Cache, Telemetry, Health
    // ...
}
```

### ReportClient
Delegates all methods to `ReportingService`. Designed for injection into other service modules.

### ReportBuilder (fluent builder)
```java
Report report = ReportBuilder.builder()
    .withTitle("Revenue Summary")
    .withType(ReportType.WEEKLY)
    .withCategory(ReportCategory.REVENUE)
    .withOwner("Finance")
    .withSummary("Revenue up 12%")
    .addRecommendation("Expand wholesale")
    .addRisk("Concentration risk")
    .addKpi("revenue", 712000.0)
    .build();
```

### Runtime Components
| Runtime | Responsibility |
|---|---|
| ReportRuntime | Report generation + queries |
| TemplateRuntime | Template CRUD + lifecycle |
| ExportRuntime | Export operations + status |
| SchedulerRuntime | Schedule CRUD + execute/pause/resume |
