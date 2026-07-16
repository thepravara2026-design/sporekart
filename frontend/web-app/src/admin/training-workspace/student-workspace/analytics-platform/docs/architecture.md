# Analytics & Learning Intelligence Platform — Architecture

## Overview

The Analytics & Learning Intelligence Platform (Sprint 27 Part 9) extends the Student Workspace with comprehensive analytics dashboards, learning intelligence insights, academic intelligence, and executive KPI tracking. Operates entirely in Mock Mode.

## Directory Structure

```
analytics-platform/
  types.ts                                    — Domain model (StudentAnalytics, CourseMetrics, TrainerMetrics, BatchMetrics, ExecutiveDashboard, KPIData, AcademicInsights, LearningIntelligence, chart types)
  data/mockData.ts                            — 10 deterministic generators covering all entities
  state/AnalyticsContext.tsx                   — Context with search/filter/pagination
  components/
    BarChart.tsx                              — SVG-based bar chart
    LineChart.tsx                             — SVG-based line chart with optional area
    PieChart.tsx                              — SVG pie chart with legend
    ProgressRing.tsx                          — Circular progress indicator
    MetricCard.tsx                            — Single metric display card
    KPITile.tsx                               — KPI tile with variant colors and change indicator
    TrendIndicator.tsx                        — Trend direction indicator
    ScoreCard.tsx                             — Category score with progress bar
    RadarChart.tsx                            — SVG radar/spider chart
    AreaChart.tsx                             — SVG area chart
    ComparisonChart.tsx                       — Side-by-side current vs previous bars
    DataTable.tsx                             — Sortable data table with column renderers
    DashboardWidget.tsx                       — Reusable widget wrapper
    EmptyStates.tsx                           — 6 typed empty states
    Skeletons.tsx                             — Dashboard and table skeletons
    SharedFilters.tsx                         — Search bar + course/batch/performance filters
  pages/
    AnalyticsIndexWrapper.tsx                 — Wrapper with sub-navigation tabs + provider
    ExecutiveDashboard.tsx                    — Executive KPI overview with trends & activity
    StudentAnalytics.tsx                      — Per-student performance table and progress rings
    CourseAnalytics.tsx                       — Course-level metrics and comparison
    TrainerAnalytics.tsx                      — Trainer effectiveness metrics
    BatchAnalytics.tsx                        — Batch comparison and at-risk tracking
    AcademicIntelligence.tsx                  — Top/low performers, risks, learning gaps
    LearningIntelligence.tsx                  — Learning health, competency, forecasts, engagement
  docs/                                       — 15 documentation files
```
