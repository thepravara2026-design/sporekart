# Sprint 26 Final Closure — Deliverable 6: Folder Structure Report

> Phase 11 closure. Audit-only. Mock Mode. This structure is FROZEN as the Phase 11 baseline.

## 1. Canonical Structure

```
frontend/web-app/src/
├── admin/training-workspace/
│   ├── TrainingWorkspaceRoute.tsx
│   ├── TrainingWorkspaceLayout.tsx
│   ├── state/            WorkspaceContext.tsx, workspaceState.ts
│   ├── data/             mockData.ts, navigation.ts
│   ├── components/       dashboard/*, workspace/*
│   ├── pages/            TrainingDashboardPage, allPlaceholders, PlaceholderPage, index
│   ├── courses/          components/ data/ pages/ state/
│   ├── course-builder/   components/{panels,preview,shared}/ data/ state/
│   ├── course-taxonomy/  components/{panels,shared,visualization,widgets}/ data/ state/
│   ├── course-curriculum/components/{panels,shared,visualization,widgets}/ data/ state/
│   ├── learning-resources/components/{panels,shared,visualization,widgets}/ data/ state/
│   ├── course-enrollment/components/{panels,shared,visualization,widgets}/ data/ state/
│   ├── analytics/        components/ data/ pages/ state/
│   └── communication/    components/ data/ pages/ state/
└── public-website/course-discovery/  components/ data/ pages/ state/
```

## 2. Per-Module Template (Frozen Contract)

Each module MUST contain: `components/`, `state/`, `data/`, and either `pages/` (routed multi-page) or `panels/` (single-layout with sections). Future modules follow this template.

## 3. Documented Structural Debt (Non-Blocking)

- `pages/allPlaceholders.tsx` — 2 unused exports (`CoursesPage`, `ResourcesPage`) → remove (DEBT-07).
- `training-workspace/course-discovery/` — empty stub dir → remove (DEBT-07).
- `panels/` vs flat `components/` inconsistency (analytics/communication/courses flat) — documented, acceptable (DEBT/F-7.2).

## 4. Verdict

**Folder structure certified and frozen.** Deviations documented; the template governs all future modules.
