# LMS Analytics — Widget Catalog (Sprint 26 · Part 9)

A reference of every widget rendered per dashboard. All charts are wrapped in
`ResponsiveChart` and framed by `WidgetCard` inside a `WidgetGrid`.

## Executive Dashboard
| Widget                       | Type          | Data source                     |
|------------------------------|---------------|---------------------------------|
| Total Courses                | KPI tile      | `kpis.totalCourses`             |
| Active Enrollments           | KPI tile      | `kpis.approvedEnrollments`      |
| Revenue (mock)               | KPI tile      | `kpis.revenuePlaceholder`       |
| Training Capacity            | KPI tile      | `kpis.trainingCapacity`         |
| Certificates (mock)          | KPI tile      | `kpis.certificatesPlaceholder`  |
| Learning Hours (mock)        | KPI tile      | `kpis.learningHoursPlaceholder` |
| Enrollment vs Completion     | Line (area)   | `buildEnrollmentTrend`          |
| Revenue Trend (mock)         | Line (area)   | `buildEnrollmentTrend`          |
| Courses by Category          | Donut         | `categoryDistribution`          |
| Delivery Modes               | Pie           | `deliveryDistribution`          |
| Top Courses                  | Bar (horiz.)  | `coursePopularity`              |
| Enrollment Funnel            | Bar (vert.)   | `enrollmentFunnel`              |
| Completion Rate              | Circular KPI  | static mock                     |
| Capacity Utilization         | Circular KPI  | static mock                     |

## Course Analytics
| Widget                    | Type         | Data source              |
|---------------------------|--------------|--------------------------|
| Courses / Published / Avg Rating / Modules | KPI tiles | `filteredCourses`, `kpis` |
| Courses by Category       | Donut        | `categoryDistribution`   |
| Difficulty Levels         | Pie          | `difficultyDistribution` |
| Languages                 | Bar (vert.)  | `languageDistribution`   |
| Enrollment Trend          | Line (area)  | `buildEnrollmentTrend`   |
| Course Popularity Ranking | Table        | `coursePopularity`       |

## Enrollment Analytics
| Widget               | Type         | Data source             |
|----------------------|--------------|-------------------------|
| Requests / Approved / Pending / Completed | KPI tiles | `kpis` |
| Daily Enrollments    | Line         | `buildDailyEnrollments` |
| Monthly Trend        | Line (area)  | `buildEnrollmentTrend`  |
| Enrollment Funnel    | Bar (vert.)  | `enrollmentFunnel`      |
| Capacity Utilization | Bar (horiz.) | `capacityUtilization`   |
| Approval Rate        | Circular KPI | static mock             |
| Completion Rate      | Circular KPI | static mock             |

## Curriculum Analytics
| Widget                    | Type         | Data source           |
|---------------------------|--------------|-----------------------|
| Modules/Lessons/Activities/Assignments/Assessments | KPI tiles | `curriculumStats` |
| Curriculum Volume         | Bar (horiz.) | `curriculumStats`     |
| Coverage by Category      | Donut        | `categoryDistribution`|
| Coverage by Difficulty    | Pie          | `difficultyDistribution`|
| Curriculum Coverage Detail| Table        | `filteredCourses`     |

## Resource Analytics
| Widget                   | Type         | Data source     |
|--------------------------|--------------|-----------------|
| Resources / Views / Unused / Avg | KPI tiles | `resourceUsage` |
| Usage by Type            | Donut        | `resourceUsage` |
| Top Resources            | Bar (horiz.) | `resourceUsage` |
| Resource Engagement Detail| Table       | `resourceUsage` |

## Saved Dashboards
| Section          | Content                          |
|------------------|----------------------------------|
| Pinned           | `SAVED_DASHBOARDS` (kind=pinned) |
| Favorites        | `SAVED_DASHBOARDS` (kind=favorite)|
| Recently Viewed  | `SAVED_DASHBOARDS` (kind=recent) |
