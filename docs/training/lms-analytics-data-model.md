# LMS Analytics — Data Model & Mock Layer (Sprint 26 · Part 9)

## Single Source of Truth
All course facts originate from
`admin/training-workspace/courses/data/courseMockData.ts`
(`MOCK_COURSES`, `Course`, `CATEGORY_LABELS`, `LEVEL_LABELS`, `DELIVERY_LABELS`).
No course data is duplicated in the analytics layer.

## Derived Types
| Type              | Shape |
|-------------------|-------|
| `ExecutiveKpis`   | totals for courses, enrollments, revenue (mock), capacity, certificates, hours |
| `TimePoint`       | `{ label, enrollments, completions, revenue }` |
| `DistributionSlice`| `{ label, value, color? }` |
| `DashboardMetric` | `{ id, title, value, trend?, format?, drillRoute? }` |
| `ResourceUsage`   | `{ name, type, used, unused }` |
| `CurriculumStat`  | `{ label, value }` |
| `SavedDashboard`  | `{ id, title, kind, route, updatedAt }` |

## Generators
| Function                | Output |
|-------------------------|--------|
| `computeExecutiveKpis`  | Aggregated KPIs from a course list |
| `buildEnrollmentTrend`  | 12-month enrollment/completion/revenue series |
| `buildDailyEnrollments` | 30-day daily series |
| `categoryDistribution`  | Course count by category |
| `difficultyDistribution`| Course count by level |
| `languageDistribution`  | Course count by language |
| `deliveryDistribution`  | Course count by delivery mode |
| `coursePopularity`      | Courses ranked by enrollment |
| `resourceUsage`         | Resource view counts + engagement flag |
| `curriculumStats`       | Modules/lessons/activities/assignments/assessments |
| `enrollmentFunnel`      | Visit → Started → Applied → Approved → Completed |
| `capacityUtilization`   | Per-course utilization % |

## Determinism
Randomized fields use a seeded linear congruential generator (`seeded(seed)`),
guaranteeing identical output across renders and sessions — critical for a
mock environment and stable snapshots.

## Formatting Helpers
- `formatNumber` — compact `K`/`M` formatting with locale grouping.
- `formatCurrency` — `₹` prefixed compact currency (mock).

## Placeholders
- `revenuePlaceholder`, `certificatesPlaceholder`, `learningHoursPlaceholder`,
  `activeTrainersPlaceholder`, `activeStudentsPlaceholder` are explicitly named
  as mock/placeholder values pending real backend integration.
- `SAVED_DASHBOARDS` and `EXPORT_FORMATS` back placeholder UX only.
