# Report Registry

## Purpose
Central registry for all report artifacts: reports, templates, schedules, exports, and BI reports.

## Registry Operations
| Entity | Operations | Filters |
|---|---|---|
| Report | list, getById, getByType, getByCategory, getByStatus, getByOwner | type, category, status, owner |
| Template | list, getById, getByCategory, getByType, getActive | category, type, active |
| Schedule | list, getById, getByFrequency, getActive | frequency, active |
| Export | list, getById, getByReportId, getByFormat | reportId, format |
| BI Report | list, getById, getByCategory | category |

## Repository Interface
```java
public interface ReportRepositoryPort {
    // Reports
    List<Report> findAllReports();
    Optional<Report> findReportById(String id);
    List<Report> findReportsByType(ReportType type);
    List<Report> findReportsByCategory(ReportCategory category);
    List<Report> findReportsByStatus(ReportStatus status);
    List<Report> findReportsByOwner(String owner);
    Report saveReport(Report report);
    void deleteReport(String id);

    // Templates, Schedules, Exports, BI Reports, Cache
    // ... (40+ methods total)
}
```

## Implementation
- **InMemoryReportRepository** — ConcurrentHashMap-based, thread-safe
- All methods use `synchronized` on mutating operations
- Returns `List.copyOf()` for immutability
