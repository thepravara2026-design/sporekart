# Sprint 26 Part 11 — Shared Component Inventory

> Deliverable 4 of 12. Mock Mode only. Read-only validation artifact.

## 1. Design System Primitives (Shared, Not Duplicated)

Every LMS module imports UI primitives from the frozen global Design System at `src/design-system/**`. No module reimplements a primitive.

| Design System component | Path | Reused by (representative) |
| --- | --- | --- |
| `Icon` | `design-system/icons/Icon` | all modules |
| `Card` | `design-system/components/composite/Card` | dashboard, courses, builder, curriculum, resources |
| `Button` | `design-system/components/core/Button` | builder, curriculum |
| `Input` | `design-system/components/core/Input` | builder (SEO), enrollment (toolbar) |
| `Select` | `design-system/components/composite/Select` | builder (settings) |
| `Badge` | `design-system/components/display/Badge` | courses, curriculum, builder, resources, taxonomy widgets |
| `Dialog` | `design-system/components/feedback/Dialog` | communication (announcements, templates) |
| `Stack` / `Grid` / `Inline` | `design-system/components/layout/*` | builder, curriculum, taxonomy, resources |
| `LineChart` / `BarChart` / `PieChart` / `CircularKPI` | `design-system/components/charts/standard/*` | analytics |
| `ChartSkeleton` / `MetricTile` | `design-system/components/charts/*` | analytics (WidgetCard, KpiCard) |

**Result:** Cards, Buttons, Dialogs, Forms, Icons, Charts, Tables, Trees, Skeletons, Badges, Spacing, and theming all resolve to the single Design System. No UI-primitive duplication detected.

## 2. Shared Shell Components (Training Workspace Root)

| Component | Path | Purpose |
| --- | --- | --- |
| `TrainingWorkspaceLayout` | `training-workspace/TrainingWorkspaceLayout.tsx` | shell chrome + `<Outlet/>` |
| `TrainingWorkspaceRoute` | `training-workspace/TrainingWorkspaceRoute.tsx` | route host + WorkspaceProvider |
| `WorkspaceSidebar` | `components/workspace/WorkspaceSidebar.tsx` | nav from `data/navigation.ts` |
| `WorkspaceHeader` | `components/workspace/WorkspaceHeader.tsx` | header chrome |
| `WorkspaceBreadcrumb` | `components/workspace/WorkspaceBreadcrumb.tsx` | breadcrumb from nav |
| dashboard panels | `components/dashboard/*` | StatWidget, ActivityTimeline, AnnouncementPanel, QuickActionPanel, UpcomingTrainingPanel |
| `PlaceholderPage` | `pages/PlaceholderPage.tsx` | future-module placeholders |

## 3. Analytics Widgets Reused Cross-Module

The analytics module intentionally exposes reusable dashboard widgets, consumed by communication (single controlled cross-module reuse):

| Widget | Path | Consumed by |
| --- | --- | --- |
| `KpiCard` | `analytics/components/KpiCard.tsx` | analytics pages, communication overview & statistics |
| `WidgetGrid` | `analytics/components/WidgetGrid.tsx` | analytics pages, communication overview & statistics |
| `WidgetCard` | `analytics/components/WidgetCard.tsx` | analytics pages, communication overview & statistics |

## 4. Per-Module Component Families (Intentional, Not Duplicates)

Some component names repeat across modules because each is domain-scoped and bound to its own module Context. These are **intentional per-module**, not duplication:

| Name | Instances | Note |
| --- | --- | --- |
| `OverviewPanel` | builder, curriculum, taxonomy, enrollment, resources | 5 distinct, module-scoped |
| `LearningPathsPanel` | taxonomy, curriculum | 2 distinct, both legitimately own the concept |
| `*Sidebar` | Builder/Curriculum/Taxonomy/Enrollment/Resource/Workspace | distinctly named, no collision |
| `*DashboardWidgets` | Course/Taxonomy/Curriculum/Commerce/Resource | module-specific dashboards |

## 5. True Duplication (Documented for Part 12)

| Item | Locations | Recommendation |
| --- | --- | --- |
| `StatusBadge` | `communication/components/StatusBadge.tsx`, `course-enrollment/components/visualization/StatusBadge.tsx` | Consolidate into a generic DS-backed badge with injectable label/tone maps. |
| Pagination | `communication/components/CommPagination.tsx` vs shared `admin/components/navigation/Pagination.tsx` (used by enrollment) | Standardize communication onto the shared admin Pagination. |

These do not affect integration correctness (build + typecheck pass); they are non-blocking consolidation candidates.

## 6. Status

**Validated.** Zero Design System duplication. Cross-module reuse is deliberate and one-way. Two minor same-name duplicates documented as future consolidation.
