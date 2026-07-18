# SporeKart QA Sprint 2 — Security Report (Checkout)

**Date:** 2026-07-17  
**Scope:** Authentication guards, protected routes, error pages  

---

## Test Results

| Test | Chromium | WebKit | Mobile Chrome | Mobile Safari | Status |
|------|----------|--------|---------------|---------------|--------|
| Unauth /admin/orders redirects | FAIL | FAIL | FAIL | FAIL | DEFECT |
| Unauth /dashboard/addresses redirects | FAIL | FAIL | FAIL | FAIL | DEFECT |
| Unauth /dashboard/orders redirects | FAIL | FAIL | FAIL | FAIL | DEFECT |
| Session-expired page renders | PASS | PASS | PASS | PASS | PASS |
| Access-denied page renders | PASS | PASS | PASS | PASS | PASS |

**Total: 20 executions, 8 pass, 12 fail**

---

## Detailed Findings

### DEFECT: Dashboard Routes Accessible Without Authentication

| Route | Issue | Impact |
|-------|-------|--------|
| `/dashboard/orders` | Full orders dashboard renders without login | Exposes order data, prices, customer names, payment methods |
| `/dashboard/addresses` | Full addresses page renders without login | Exposes address management UI |
| `/admin/orders` | No auth redirect observed | Exposes admin order management |

**Root Cause:** Mock mode default role is "Administrator" which has full access. No authentication guard exists on these routes for unauthenticated users.

**Evidence:**
- Screenshots show "Logout" button present without login
- Error context snapshots show complete order data with customer "Jane Doe"
- Consistent across all 4 browsers (10+ retries all failed)

### PASS: Error Pages

| Page | Status | Notes |
|------|--------|-------|
| `/session-expired` | PASS | Renders with content (body length > 10) |
| `/access-denied` | PASS | Renders with content (body length > 10) |

---

## Bug Reference

See `bug-register.md` for BUG-CHK-001 (Dashboard routes accessible without authentication).

## Recommendations

1. **CRITICAL:** Add authentication guards to all /dashboard/* and /admin/* routes
2. Implement proper redirection to /login for unauthenticated users
3. Set default mock role to "Guest" instead of "Administrator"
4. Test auth enforcement with role selector set to "Guest" before navigating to protected routes
