# LMS Analytics — Testing & Verification Guide (Sprint 26 · Part 9)

## Automated
| Check                | Command                          | Result |
|----------------------|----------------------------------|--------|
| Type check (project) | `npx tsc -b --noEmit`            | Pass (0 errors) |

> Note: `vite build` currently fails on **pre-existing, out-of-scope** Phase 7
> files (`ProfileDashboard.tsx` → missing `./profile.css`, `DashboardPage.tsx` →
> missing `../../auth.css`). These are unrelated to the Analytics Foundation and
> are explicitly out of scope for this part.

## Manual Smoke Test
1. Navigate to `/admin/training/lms-analytics` → redirects to `/executive`.
2. Confirm 6 KPI tiles + 8 widgets render on the Executive Dashboard.
3. Click **Total Courses** KPI → navigates to Course Analytics.
4. Click **Active Enrollments** KPI → navigates to Enrollment Analytics.
5. Visit each section tab: Course, Enrollment, Curriculum, Resource, Saved.
6. Confirm Student / Trainer / Financial tabs are visible but disabled.

## Filter & Search
1. Type in the search box → KPIs and charts update to matching courses.
2. Change Category / Level / Delivery / Language → distributions recompute.
3. Confirm the **Clear (n)** button appears and resets all filters.

## Accessibility Spot Checks
- Tab through the toolbar: every control is reachable and labelled.
- Open the Export menu with the keyboard; items are focusable.
- Verify each widget panel exposes an `aria-label` (widget title).
- Verify tables expose a caption and column headers.

## Responsive Spot Checks
- Resize from desktop to mobile: grids reflow to a single column.
- Charts scale to container width without horizontal overflow.
- Tables scroll horizontally within their card on narrow screens.

## Regression Guard
- Confirm `/admin/analytics` (Enterprise Dashboard) still resolves unchanged.
- Confirm existing `/admin/training/*` routes are unaffected.
