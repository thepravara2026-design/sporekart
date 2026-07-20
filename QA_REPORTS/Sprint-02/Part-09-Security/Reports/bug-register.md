# Bug Register — Security

## New Security Bugs

### BUG-SEC-001 (P0) — Admin routes accessible without authentication
- **Module:** Authorization
- **Browser:** All
- **Expected:** `/admin` routes require authentication
- **Actual:** `/admin`, `/admin/dashboard`, `/admin/users`, `/admin/settings` are all accessible without any auth check
- **Root Cause:** No `ProtectedRoute` or `RequireAuth` component. WorkspacePage guard only covers enterprise routes.
- **Impact:** Unauthorized access to admin pages

### BUG-SEC-002 (P2) — Role does not persist across page navigation
- **Module:** RBAC
- **Browser:** All
- **Expected:** Role selection persists across SPA navigation
- **Actual:** Role stored only in React state, resets to 'administrator' on full page reload
- **Root Cause:** `useState<Role>('administrator')` — no persistence mechanism
- **Impact:** Role-based gating is not reliable for security enforcement

### BUG-SEC-003 (P3) — Route `/account` shows "Access restricted" for admin role
- **Module:** Authorization
- **Browser:** All
- **Expected:** Admin can access `/account` route
- **Actual:** `/account` shows "Access restricted" heading
- **Root Cause:** Route `/account` may not be configured for admin role in navigation.ts
- **Impact:** Admin cannot access customer account page

### BUG-SEC-004 (P2) — Social login buttons show "not enabled" with no alternative
- **Module:** Authentication
- **Browser:** All
- **Expected:** Social login buttons are functional or hidden
- **Actual:** Buttons visible, clicking shows "Social login is not enabled yet."
- **Root Cause:** `socialLogin()` in authClient.ts always returns error
- **Impact:** Confusing UX for users expecting social login

## Re-Confirmed Bugs

### BUG-001 (P0) — Firefox: Complete mock API interception failure (ACCEPTED)
- **Status:** Accepted per Governance Override

### BUG-ADM-001 (P0) — No auth guard on /admin routes (RE-CONFIRMED)
- **Re-confirmed in:** Part 9 Security Validation
