# Bugs and Defects — Part 7: Training Platform

## New Bugs

### BUG-TRN-001 (P2) — Course detail page missing section content
- **Status:** Confirmed
- **Affects:** ALL browsers
- **Evidence:** Course detail page loads but body text lacks hero/banner, curriculum, objectives, prerequisites, trainer info, pricing, CTA, FAQ, related courses, SEO metadata keywords.
- **Root Cause:** Course detail page renders as a content placeholder — no detailed course information sections are implemented.
- **Impact:** Users cannot view comprehensive course details before enrolling.

### BUG-TRN-002 (P2) — Video classroom page lacks video player
- **Status:** Confirmed
- **Affects:** ALL browsers
- **Evidence:** `/dashboard/training/classroom/crs-001` loads but body text contains no video, lesson, or lecture content.
- **Root Cause:** Video classroom is a placeholder page without a player component.
- **Impact:** Learners cannot access course content through the classroom.

### BUG-TRN-003 (P2) — Catalog lacks view toggles (grid/list/table)
- **Status:** Confirmed
- **Affects:** ALL browsers
- **Evidence:** Catalog page renders course cards but offers no grid/list/table view switching controls.
- **Root Cause:** The training catalog uses a single view mode without toggle support.
- **Impact:** Users cannot switch between different catalog view layouts.

### BUG-TRN-004 (P2) — Catalog page missing ARIA landmark (no `<main>` element)
- **Status:** Confirmed
- **Affects:** ALL browsers
- **Evidence:** `/training/courses` has no `<main>` element or `role="main"` landmark.
- **Root Cause:** Catalog page uses div-based layout without semantic HTML landmarks.
- **Impact:** Reduced accessibility for screen reader users.

### BUG-TRN-005 (P3) — Mobile Safari catalog rendering issues
- **Status:** Confirmed
- **Affects:** Mobile Safari only
- **Evidence:** Catalog page renders with only 15 characters of body text at mobile viewport. Filter controls, category highlights, sort controls, and course cards missing.
- **Root Cause:** Responsive CSS issues on Safari mobile viewport.
- **Impact:** Mobile Safari users see broken/empty catalog page.

### BUG-TRN-006 (P3) — Admin course builder uses non-standard CSS class names
- **Status:** Confirmed
- **Affects:** ALL browsers
- **Evidence:** Elements on `/admin/training/courses/builder` don't match `panel`, `Panel`, or `section` selectors.
- **Root Cause:** Course builder uses custom CSS naming conventions.
- **Impact:** Visual structural tests fail; potential maintainability concern.

## Existing Bugs (Carried Forward)

### BUG-001 (P0) — Firefox: Complete mock API interception failure (ACCEPTED)
- **Status:** Accepted limitation per Governance Override
- **Affects:** Firefox only — all tests timeout at 30s
- **Mitigation:** Firefox excluded from Sprint 2 execution

### BUG-ADM-001 (P0) — No auth guard on /admin routes (RE-CONFIRMED)
- **Status:** Re-confirmed for training admin workspace
- **Evidence:** `/admin/training/*` routes accessible without authentication on all browsers
- **Impact:** Unauthorized users can access admin training management pages

### BUG-PAY-001 (P2) — Horizontal scroll on mobile payment page
### BUG-PAY-002 (P2) — Payment card input field validation

## Implementation Gap Corrections
The following 3 gap tests FAIL because the feature IS present (test logic is inverted):

| Test | Expected | Actual | Note |
|------|----------|--------|------|
| No admin certificates page | keyword absent | keyword found | Admin certificates page exists |
| No attendance tracking | keyword absent | keyword found | Attendance page exists |
| No assessment management | keyword absent | keyword found | Assessment page exists |

These are not defects — they indicate the admin training workspace is more complete than initially assumed. The gap tests should be updated to validate functional depth rather than page existence.
