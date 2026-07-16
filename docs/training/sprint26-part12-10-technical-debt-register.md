# Sprint 26 Part 12 — Deliverable 10: Technical Debt Register

> Certification gate. Audit-only. Mock Mode. Recommendations only — NOT auto-fixed (per Final Certification Rule).

## 1. Critical Issues

**None.** Zero Critical, zero High severity. No issue blocks certification.

## 2. Debt Register

Each item follows: Issue ID · Severity · Impact · Root Cause · Affected Modules · Recommended Solution · Estimated Risk · Estimated Effort.

---

### DEBT-01 · Duplicate number/currency formatters
- **Severity:** Medium
- **Impact:** Inconsistent locale/format output; maintenance drift.
- **Root Cause:** `formatNumber`/`formatCurrency` reimplemented independently instead of using DS `LocalizationProvider`.
- **Affected:** analytics, course-enrollment (plus unrelated inventory/warehouse).
- **Recommendation:** Consolidate onto DS `LocalizationProvider` formatters or a shared `utils/formatters`.
- **Risk:** Low · **Effort:** S (0.5–1 day)

### DEBT-02 · Multiple pagination implementations
- **Severity:** Medium
- **Impact:** UX/behaviour drift; duplicated maintenance.
- **Root Cause:** `CommPagination`, `CatalogPagination`, shared `admin/.../Pagination`, and an inline pager in `CourseRegistryPage`.
- **Affected:** communication, course-discovery, courses, course-enrollment.
- **Recommendation:** Standardize on one shared `Pagination` primitive; migrate the others.
- **Risk:** Low · **Effort:** M (1–2 days)

### DEBT-03 · Duplicated search/filter/sort comparators
- **Severity:** Medium
- **Impact:** Repeated logic across state hooks; drift risk.
- **Root Cause:** Each module hand-rolls text-filter + sort.
- **Affected:** communication, course-enrollment, course-discovery (+ others).
- **Recommendation:** Shared `useFilteredList` / `filterByText` / `sortByKey` helpers.
- **Risk:** Low · **Effort:** M (1–2 days)

### DEBT-04 · Hardcoded colors / magic values (~21)
- **Severity:** Medium
- **Impact:** Theming inconsistency; dark/light gaps on those surfaces.
- **Root Cause:** Inline hex/rgb instead of tokens (esp. `LivePreview`, `CourseDashboardWidgets`, `CategoryExplorer`, several `#fff`).
- **Affected:** course-builder, courses, course-taxonomy, learning-resources, communication (2×`#fff`).
- **Recommendation:** Replace with `var(--color-*)` tokens; treat `LivePreview` iframe as a documented special case or tokenize its theme pair.
- **Risk:** Low · **Effort:** M (1–2 days)

### DEBT-05 · Over-responsible / large components
- **Severity:** Medium
- **Impact:** Harder testing/maintenance.
- **Root Cause:** `CourseExplorerToolbar.tsx` (400) mixes search/filter/view/sort/bulk/export; `CourseDetailPage.tsx` (346).
- **Affected:** courses.
- **Recommendation:** Decompose toolbar into `FilterBar`/`ViewToggle`/`SortMenu`/`BulkActionBar`; split detail page tabs.
- **Risk:** Low · **Effort:** M (1–2 days)

### DEBT-06 · Duplicate StatusBadge & CourseCard
- **Severity:** Low
- **Impact:** Minor duplication.
- **Root Cause:** Domain-specific copies (communication vs enrollment badge; admin vs public card).
- **Affected:** communication, course-enrollment, courses, course-discovery.
- **Recommendation:** Generic DS-backed badge with injectable maps; shared presentational card.
- **Risk:** Low · **Effort:** S–M

### DEBT-07 · Dead code
- **Severity:** Low
- **Impact:** Confusion; unused surface.
- **Root Cause:** Unused `CoursesPage`/`ResourcesPage` exports in `allPlaceholders.tsx`; empty `training-workspace/course-discovery/` stub dir.
- **Affected:** shell pages.
- **Recommendation:** Remove unused exports and empty stub dir.
- **Risk:** Very Low · **Effort:** S (<0.5 day)

### DEBT-08 · State-file naming inconsistency
- **Severity:** Low
- **Impact:** Onboarding friction.
- **Root Cause:** `courses/state/courseState.ts` (no Context), communication has no `CommunicationContext`, root `workspaceState.ts` vs `useXState`.
- **Affected:** courses, communication, workspace shell.
- **Recommendation:** Adopt one convention (`XContext.tsx` + `useXState.ts`) where a Context is warranted; document intentional exceptions.
- **Risk:** Very Low · **Effort:** S

### DEBT-09 · Uneven empty/loading-state coverage
- **Severity:** Low/Info
- **Impact:** Placeholder modules lack skeleton/empty states.
- **Root Cause:** Builder/Taxonomy/Curriculum/Resources still placeholder-phase.
- **Affected:** course-builder, course-taxonomy, course-curriculum, learning-resources, course-enrollment (partial).
- **Recommendation:** Shared `EmptyState`/`Skeleton` primitive; apply as modules mature.
- **Risk:** Low · **Effort:** M (grows with real UI)

### DEBT-10 · No ESLint config / lint script
- **Severity:** Low
- **Impact:** No automated style/quality enforcement.
- **Root Cause:** Not configured in `web-app`.
- **Affected:** whole app.
- **Recommendation:** Add ESLint + `lint` script; wire into CI.
- **Risk:** Low · **Effort:** S

## 3. Summary

| Severity | Count |
| --- | --- |
| Critical | 0 |
| High | 0 |
| Medium | 5 (DEBT-01..05) |
| Low | 5 (DEBT-06..10) |

**Total debt effort estimate:** ~8–12 engineer-days, all deferrable to a later hardening sprint. **No debt blocks Phase 11 certification.**
