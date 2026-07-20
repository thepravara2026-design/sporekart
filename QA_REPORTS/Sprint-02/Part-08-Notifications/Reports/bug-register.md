# Bug Register — Notifications

## New Bugs

### BUG-NOT-001 (P2) — Notification dropdown doesn't open on bell click
- **Module:** In-App Notifications
- **Browser:** All
- **Expected:** Clicking notification bell opens dropdown with notification list
- **Actual:** Bell is visible but dropdown doesn't appear
- **Root Cause:** NotificationCenter component initializes with empty notification array; no seed data available
- **Impact:** Users cannot view notifications from bell icon

### BUG-NOT-002 (P3) — Console error on communication notifications page
- **Module:** Admin Communication
- **Browser:** All
- **Expected:** Zero console errors
- **Actual:** 1 console error on `/admin/training/communication/notifications`
- **Root Cause:** Missing asset or component error
- **Impact:** Potential rendering issue or broken dependency

### BUG-NOT-003 (P3) — Horizontal scroll on communication pages (mobile)
- **Module:** Admin Communication
- **Browser:** Mobile Chrome, Mobile Safari
- **Expected:** No horizontal scroll
- **Actual:** 214-239px horizontal scroll on notification and delivery queue pages
- **Root Cause:** Responsive layout overflow on mobile viewports
- **Impact:** Poor mobile user experience

## Re-Confirmed Bugs

### BUG-001 (P0) — Firefox: Complete mock API interception failure (ACCEPTED)
- **Status:** Accepted per Governance Override

### BUG-ADM-001 (P0) — No auth guard on /admin routes (RE-CONFIRMED)
- **Re-confirmed for:** `/admin/training/communication/*` routes
- **Evidence:** All 9 communication pages accessible without authentication

## False Positive Tests
The following tests fail due to test logic issues rather than actual bugs:
- **PII "token" in page source:** Expected SPA behavior (auth token in script tags)
- **Retry mechanism absent:** Delivery queue page DOES mention "retry" — gap test is inverted
