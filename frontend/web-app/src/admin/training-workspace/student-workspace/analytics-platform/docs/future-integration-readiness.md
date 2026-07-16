# Analytics Platform — Future Integration Readiness

## Backend Integration Points

| Slice | Backend Source | Integration Method |
|---|---|---|
| StudentAnalytics | Student + Enrollment + Assessment DB | Replace mockData generators with API calls |
| CourseMetrics | Course + Enrollment + Assessment DB | API aggregation endpoint |
| TrainerMetrics | Trainer + Batch + Assessment DB | API aggregation endpoint |
| ExecutiveDashboard | All sources aggregated | Dedicated executive summary endpoint |
| KPIData | All sources aggregated | KPI calculation service |
| AcademicInsights | Risk detection engine | ML-based risk scoring service |
| LearningIntelligence | Learning analytics engine | Real-time intelligence pipeline |

## Natural Language Integration

- Context's `searchTerm` designed to accept NL queries
- `getFilteredStudents` can be extended with intent parsing
- Analytics pages ready for AI-powered insight generation

## Real-time Updates

- All metrics can be updated via WebSocket/SSE without component changes
- Context state is designed to be merged with streaming data
- Chart components use `transition` CSS for smooth data updates

## Export & Reporting

- DataTable supports all columns needed for CSV/Excel export
- Chart SVG components are printable and exportable as PNG
- MetricCard values ready for PDF report generation
