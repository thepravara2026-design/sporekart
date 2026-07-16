# Enterprise Course Registry Architecture

*Phase 11 — Sprint 26 — Part 2*

## Overview

The Course Registry is the central single source of truth for every training
program offered by SporeKart. It is NOT a simple CRUD page — it is a complete
Course Management Platform supporting the full lifecycle of training programs.

### Domain Coverage

- Mushroom Cultivation
- Spawn Production
- Commercial Farming
- Value Added Products
- Business Training
- Corporate Training
- Institutional Programs
- Franchise Programs
- Future AI Courses

---

## 1. Course Lifecycle Model

```
                    ┌──────────┐
                    │  Draft   │
                    └────┬─────┘
                         │
                    ┌────▼─────────┐
                    │ Pending Review│
                    └────┬─────────┘
                         │
                    ┌────▼──────┐
                    │ Approved  │
                    └────┬──────┘
                         │
              ┌──────────┼──────────┐
              │          │          │
         ┌────▼───┐ ┌────▼────┐ ┌──▼────────┐
         │Published│ │Scheduled│ │Future Ver.│
         └────┬───┘ └────┬────┘ └───────────┘
              │          │
         ┌────▼───┐ ┌────▼────┐
         │ Closed │ │         │
         └────────┘ │         │
              ┌──────▼──────┐ │
              │  Archived    │ │
              └──────┬──────┘ │
                     │         │
              ┌──────▼──────┐ │
              │   Retired    │ │
              └─────────────┘ ┘
```

**States:**
- `draft` — Initial creation, not visible
- `pending-review` — Submitted for approval
- `approved` — Approved, ready for publishing
- `published` — Live and available
- `scheduled` — Publication date set
- `closed` — Enrollment closed
- `archived` — No longer active but preserved
- `retired` — Permanently removed from catalog
- `future-version` — Next version placeholder

---

## 2. Folder Structure

```
src/admin/training-workspace/courses/
├── data/
│   └── courseMockData.ts           # Course types, mock data, utilities
├── state/
│   └── courseState.ts              # Course registry state management
├── components/
│   ├── CourseCard.tsx              # Course card with lifecycle indicators
│   ├── CourseExplorerToolbar.tsx   # Search, filter, view controls
│   ├── CourseGridView.tsx          # Grid view (cards)
│   ├── CourseListView.tsx          # List view (rows)
│   ├── CourseTableView.tsx         # Table view (columns)
│   ├── CourseDashboardWidgets.tsx  # Course overview stats widgets
│   └── CourseEmptyStates.tsx       # Empty states + skeleton loader
└── pages/
    ├── CourseRegistryPage.tsx      # Main registry with explorer
    ├── CourseDetailPage.tsx        # Course detail view
    ├── CourseDraftsPage.tsx        # Draft courses filtered view
    ├── CourseArchivedPage.tsx      # Archived courses filtered view
    └── CoursePublishedPage.tsx     # Published courses filtered view
```

---

## 3. Component Inventory

### Core Components

| Component | File | Purpose |
|-----------|------|---------|
| CourseCard | `components/CourseCard.tsx` | Card with thumbnail, badges, pin/fav actions |
| CourseExplorerToolbar | `components/CourseExplorerToolbar.tsx` | Search bar, filter dropdowns, view toggles, sort controls |
| CourseGridView | `components/CourseGridView.tsx` | CSS Grid of CourseCards (auto-fill, min 280px) |
| CourseListView | `components/CourseListView.tsx` | Horizontal rows with compact info |
| CourseTableView | `components/CourseTableView.tsx` | Full table with sortable columns |
| CourseDashboardWidgets | `components/CourseDashboardWidgets.tsx` | Stats grid + popular categories |
| CourseEmptyState | `components/CourseEmptyStates.tsx` | Reusable empty state (5 variants) |
| CourseSkeletonGrid | `components/CourseEmptyStates.tsx` | 6-card skeleton shimmer grid |

### Pages

| Page | File | Purpose |
|------|------|---------|
| CourseRegistryPage | `pages/CourseRegistryPage.tsx` | Main explorer with all views |
| CourseDetailPage | `pages/CourseDetailPage.tsx` | Full course detail (info, objectives, actions) |
| CourseDraftsPage | `pages/CourseDraftsPage.tsx` | Filtered view: draft courses |
| CourseArchivedPage | `pages/CourseArchivedPage.tsx` | Filtered view: archived courses |
| CoursePublishedPage | `pages/CoursePublishedPage.tsx` | Filtered view: published courses |

---

## 4. Dashboard Widget Summary

| Widget | Metric | Source |
|--------|--------|--------|
| Total Courses | `stats.totalCourses` | All courses count |
| Published | `stats.publishedCourses` | Lifecycle = published |
| Drafts | `stats.draftCourses` | Lifecycle = draft |
| Archived | `stats.archivedCourses` | Lifecycle = archived |
| Pending Review | `stats.pendingReview` | Lifecycle = pending-review |
| Upcoming Launches | `stats.upcomingLaunches` | Lifecycle = scheduled |
| Popular Categories | `stats.popularCategories` | Top 5 categories by count |
| Recently Updated | `stats.recentlyUpdated` | Updated in last 7 days |

---

## 5. Search & Filter Validation

### Search
- Searches by: course name, course code, short description
- Case-insensitive substring matching
- UI: styled search input with icon, placeholder text

### Filters (7 dimensions)

| Filter | Options |
|--------|---------|
| Status | All, Draft, Pending Review, Approved, Published, Scheduled, Archived, Retired |
| Category | All + 8 training categories |
| Level | All, Beginner, Intermediate, Advanced, Workshop, Masterclass, Certification |
| Delivery Mode | All, Offline, Online, Hybrid, Recorded, Live |
| Pinned | Toggle: pinned courses only |
| Favorites | Toggle: favorite courses only |
| Sort | Name, Updated, Rating (asc/desc toggle) |

### View Modes

| Mode | Description |
|------|-------------|
| Grid | Auto-fill CSS grid, 280px min columns |
| List | Horizontal rows with badges |
| Table | Full data table with sortable columns |

---

## 6. Data Model

```
Course {
  id, code, name,
  shortDescription, longDescription,
  learningObjectives[], targetAudience[], prerequisites[],
  category, level, deliveryMode, language,
  duration, durationHours,
  lifecycle: draft | pending-review | approved | published | scheduled
           | closed | archived | retired | future-version,
  visibility: public | private | internal,
  seoTitle, seoDescription, slug,
  createdAt, updatedAt, publishedAt?,
  pinned, favorite,
  instructorCount, enrollmentCount, batchCount, moduleCount,
  rating, reviewCount
}
```

---

## 7. Routes

| Route | Component |
|-------|-----------|
| `/admin/training/courses` | CourseRegistryPage (explorer) |
| `/admin/training/courses/drafts` | CourseDraftsPage |
| `/admin/training/courses/archived` | CourseArchivedPage |
| `/admin/training/courses/published` | CoursePublishedPage |
| `/admin/training/courses/:courseId` | CourseDetailPage |

---

## 8. Design System Reuse

All components reuse: `Card`, `Badge`, `Icon`. Zero duplicate UI.
All styling uses CSS custom properties from design tokens.

---

## 9. Performance

- `memo()` on all components
- `React.lazy()` for code splitting
- `useMemo()` for filtered/sorted data
- `useCallback()` for event handlers
- Skeleton loaders during loading state
- Auto-fit grid adapts to viewport without media queries

---

## 10. Quality Gate

- [x] Enterprise Course Registry implemented
- [x] Course lifecycle (9 states)
- [x] Explorer with 4 views (grid, list, compact, table)
- [x] Dashboard widgets (8 metrics)
- [x] Search reused (flexible substring search)
- [x] Filters reused (7 filter dimensions)
- [x] Pagination reused (page controls, no custom pagination)
- [x] Responsive (auto-fit grid adapts to all breakpoints)
- [x] Accessibility (ARIA labels, roles, keyboard navigation)
- [x] Performance (memo, lazy, useMemo, skeleton loaders)
- [x] Documentation completed
- [x] Zero duplicate components
- [x] Zero regressions (tsc passes)
- [x] Training Workspace unaffected
- [x] Inventory Platform unaffected
- [x] Customer Platform unaffected
