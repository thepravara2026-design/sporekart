# Analytics Platform — Folder Structure

```
analytics-platform/
├── types.ts                                    # Domain model types, labels & constants
├── data/
│   └── mockData.ts                             # 10 deterministic generators
├── state/
│   └── AnalyticsContext.tsx                    # Context + provider + hook
├── components/
│   ├── BarChart.tsx                            # SVG bar chart
│   ├── LineChart.tsx                           # SVG line chart
│   ├── PieChart.tsx                            # SVG pie chart
│   ├── ProgressRing.tsx                        # Circular progress
│   ├── MetricCard.tsx                          # Metric card
│   ├── KPITile.tsx                             # KPI tile (5 variants)
│   ├── TrendIndicator.tsx                      # Trend direction
│   ├── ScoreCard.tsx                           # Score bar
│   ├── RadarChart.tsx                          # SVG radar chart
│   ├── AreaChart.tsx                           # SVG area chart
│   ├── ComparisonChart.tsx                     # Current vs previous
│   ├── DataTable.tsx                           # Sortable table
│   ├── DashboardWidget.tsx                     # Widget wrapper
│   ├── EmptyStates.tsx                         # 6 empty states
│   ├── Skeletons.tsx                           # 2 skeleton variants
│   └── SharedFilters.tsx                       # Global filter controls
├── pages/
│   ├── AnalyticsIndexWrapper.tsx               # Nav wrapper with AnalyticsProvider
│   ├── ExecutiveDashboard.tsx                   # Executive KPI overview
│   ├── StudentAnalytics.tsx                    # Student performance
│   ├── CourseAnalytics.tsx                     # Course metrics
│   ├── TrainerAnalytics.tsx                    # Trainer effectiveness
│   ├── BatchAnalytics.tsx                      # Batch comparison
│   ├── AcademicIntelligence.tsx                # Insights & risks
│   └── LearningIntelligence.tsx                # Deep learning analysis
└── docs/
    ├── architecture.md
    ├── component-inventory.md
    ├── data-model.md
    ├── executive-dashboard.md
    ├── student-analytics.md
    ├── course-analytics.md
    ├── trainer-analytics.md
    ├── batch-analytics.md
    ├── academic-intelligence.md
    ├── learning-intelligence.md
    ├── state-management.md
    ├── responsive.md
    ├── accessibility.md
    ├── future-integration-readiness.md
    └── sprint27-part9-completion.md
```
