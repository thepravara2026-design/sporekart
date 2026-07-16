# LMS Analytics — Routing & Navigation (Sprint 26 · Part 9)

## Route Registration
Routes are registered in `frontend/web-app/src/App.tsx` as **lazy** chunks,
nested under the existing `TrainingWorkspaceRoute` (`/admin/training/*`).

```tsx
<Route path="lms-analytics" element={<Navigate to="/admin/training/lms-analytics/executive" replace />} />
<Route path="lms-analytics/*" element={<AnalyticsWorkspaceRoute />}>
  <Route index element={<Navigate to="/admin/training/lms-analytics/executive" replace />} />
  <Route path="executive"  element={<AnalyticsExecutivePage />} />
  <Route path="course"     element={<AnalyticsCoursePage />} />
  <Route path="enrollment" element={<AnalyticsEnrollmentPage />} />
  <Route path="curriculum" element={<AnalyticsCurriculumPage />} />
  <Route path="resource"   element={<AnalyticsResourcePage />} />
  <Route path="saved"      element={<AnalyticsSavedPage />} />
</Route>
```

## Why not `/admin/analytics`?
`/admin/analytics` is already owned by the **Enterprise Dashboard**
(`AdminAnalyticsPage`) and is a protected module. To respect that boundary, the
LMS Analytics Foundation lives under the LMS domain at
`/admin/training/lms-analytics/*`.

## In-Feature Navigation
`AnalyticsWorkspaceRoute` renders a self-contained section tab bar
(`<nav aria-label="Analytics sections">`) using `NavLink`, providing:
- Active state via `aria-current` + token-based highlight.
- Disabled "coming soon" tabs for Student / Trainer / Financial analytics.

**No shared Navigation module was modified.** The global admin/training rails
are untouched; discovery of the new section is via direct route + in-feature tabs.

## Drill-Down Links
- Executive KPI tiles deep-link to the Course and Enrollment sections via
  `useNavigate`.
- Saved Dashboard cards link directly to their target section route.

## Future Placeholders
| Scope     | Route (reserved)                              | Status |
|-----------|-----------------------------------------------|--------|
| Student   | `/admin/training/lms-analytics/student`       | tab disabled |
| Trainer   | `/admin/training/lms-analytics/trainer`       | tab disabled |
| Financial | `/admin/training/lms-analytics/financial`     | tab disabled |
