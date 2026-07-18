# Security Bug Report — QA Sprint 2 Part 10

**Release:** v1.0.0-rc1 | **Date:** 2026-07-17 | **Total Vulnerabilities:** 22

---

## Vulnerability Summary

| Severity | CVSS Range | Count | IDs |
|----------|-----------|-------|-----|
| 🔴 CRITICAL | 9.0-10.0 | 4 | SEC-001 through SEC-004 |
| 🟠 HIGH | 7.0-8.9 | 8 | SEC-005 through SEC-012 |
| 🟡 MEDIUM | 4.0-6.9 | 7 | SEC-013 through SEC-019 |
| 🔵 LOW | 0.1-3.9 | 3 | SEC-020 through SEC-022 |

---

## 🔴 CRITICAL Vulnerabilities

### SEC-001: No Route Guards on Any Protected Route — CVSS 9.5

| Field | Value |
|-------|-------|
| **Security Category** | Broken Access Control (OWASP A01) |
| **Affected Module** | All 256 protected routes in App.tsx |
| **Environment** | Frontend web-app |
| **Preconditions** | User knows URL of protected route |

**Steps:**
1. Without logging in, navigate to any protected URL (e.g., `/admin/dashboard`, `/dashboard/profile`)
2. Observe that the page loads without any authentication check

**Expected:** Route guard component redirects to login. Unauthenticated users cannot access protected routes.

**Actual:** All routes are accessible without authentication. No route guard components exist anywhere.

**Metrics:** 337 total routes, 256 protected by role config, 0 with actual guards

**Potential Impact:** Complete bypass of access control. Any user can access any page.

**Recommendation:** Implement route guard component that checks authentication and authorization before rendering protected routes. Wrap all protected routes in `<ProtectedRoute>` component.

---

### SEC-002: Default Role is Administrator — CVSS 9.0

| Field | Value |
|-------|-------|
| **Security Category** | Authentication Failure (OWASP A07) |
| **Affected Module** | App.tsx — `useState<Role>('administrator')` |
| **Environment** | Frontend web-app |

**Steps:**
1. Open application without any authentication
2. Observe header shows administrator role

**Expected:** Default role should be `guest` or `null` requiring explicit authentication.

**Actual:** Default role is `administrator`. New users automatically have full admin access.

**Metrics:** Single line: `const [activeRole, setActiveRole] = useState<Role>('administrator');`

**Potential Impact:** Unauthenticated users can perform admin actions.

**Recommendation:** Change default role to `guest`. Require authentication to set a higher-privilege role.

---

### SEC-003: Authentication is Entirely Mock-Based — CVSS 9.5

| Field | Value |
|-------|-------|
| **Security Category** | Authentication Failure (OWASP A07) / Insecure Design (OWASP A04) |
| **Affected Module** | authClient.ts, all auth pages |
| **Environment** | Frontend web-app |

**Steps:**
1. Navigate to login page
2. Enter any phone number
3. Enter OTP code of any 6 digits (except 000000)
4. Observe successful "authentication"

**Expected:** Real authentication with credential validation, MFA, session tokens.

**Actual:** Auth is a UI stub. Any phone number is accepted. Any OTP except 000000 succeeds. No backend integration.

**Metrics:** Mock latency: 900ms, Mock OTP: any 6-digit code except 000000, Mock registration: any non-empty name

**Potential Impact:** No real security. Application will fail security review.

**Recommendation:** Replace mock authClient with real Supabase Auth or custom auth service with proper password hashing, MFA, and session management.

---

### SEC-004: 82.6% of API Endpoints Have No Authentication — CVSS 9.0

| Field | Value |
|-------|-------|
| **Security Category** | Broken Access Control (OWASP A01) |
| **Affected Module** | 128 of 155 backend endpoints across 10 microservices |
| **Environment** | Backend microservices |

**Steps:**
1. Call any unprotected endpoint (e.g., GET /api/products)
2. Observe that no authentication token is required

**Expected:** All endpoints require valid authentication (JWT, API key, or similar).

**Actual:** 128 of 155 endpoints have no authentication requirement in their annotations.

**Metrics:** Auth coverage: 17.4% (27/155 endpoints)

**Potential Impact:** Complete data exposure. Anyone can read/write any data.

**Recommendation:** Add `@Secured` or `@PreAuthorize` annotations to all controller methods. Implement JWT validation filter.

---

## 🟠 HIGH Vulnerabilities

### SEC-005: Role Can Be Changed Via UI Dropdown — CVSS 7.5

| Field | Value |
|-------|-------|
| **Security Category** | Authorization |
| **Affected Module** | Header.tsx — role switch dropdown |
| **Environment** | Frontend web-app |

**Steps:**
1. Navigate to any page
2. Click the role dropdown in header
3. Select any role (administrator, customer, grower, etc.)

**Expected:** Role is determined by authentication token, not user-selectable.

**Actual:** User can freely switch between all roles via a dropdown. Role is stored in React state only.

**Potential Impact:** Privilege escalation to any role without authentication.

**Recommendation:** Remove the role switch dropdown from production. Role must be issued by backend via JWT claims.

---

### SEC-006: No Content Security Policy Header — CVSS 7.0

| Field | Value |
|-------|-------|
| **Security Category** | Security Misconfiguration (OWASP A05) |
| **Affected Module** | index.html, server configuration |
| **Environment** | All |

**Steps:**
1. Load application
2. Check response headers for Content-Security-Policy

**Expected:** CSP header with strict policy preventing XSS and data injection.

**Actual:** No CSP header configured. Any script can execute.

**Potential Impact:** XSS attacks can execute arbitrary scripts.

**Recommendation:** Add CSP header via Vite plugin or server config.

---

### SEC-007: No X-Frame-Options Header — CVSS 7.0

| Field | Value |
|-------|-------|
| **Security Category** | Security Misconfiguration (OWASP A05) |
| **Affected Module** | Server configuration |
| **Environment** | All |

**Steps:**
1. Load application in an `<iframe>` on another domain
2. Observe application renders inside iframe

**Expected:** X-Frame-Options header prevents clickjacking.

**Actual:** No X-Frame-Options header. Application can be embedded in iframes.

**Potential Impact:** Clickjacking attacks.

**Recommendation:** Add `X-Frame-Options: DENY` header.

---

### SEC-008: No Security Headers Configured — CVSS 7.0

| Field | Value |
|-------|-------|
| **Security Category** | Security Misconfiguration |
| **Affected Module** | Server configuration |
| **Environment** | All |

Missing headers: `X-Content-Type-Options`, `Strict-Transport-Security`, `Referrer-Policy`, `Permissions-Policy`, `Cache-Control`

**Recommendation:** Configure all security headers in server or Vite config.

---

### SEC-009: IDOR on All Controllers — CVSS 7.5

| Field | Value |
|-------|-------|
| **Security Category** | Broken Access Control |
| **Affected Module** | All backend controllers |
| **Environment** | Backend |

**Details:** No ownership verification on any resource endpoint. Any authenticated user can access any other user's data by manipulating resource IDs in URLs.

**Recommendation:** Implement ownership checks on all resource endpoints.

---

### SEC-010: No Session Management — CVSS 7.0

| Field | Value |
|-------|-------|
| **Security Category** | Authentication |
| **Affected Module** | All |
| **Environment** | All |

**Details:** No session tokens, no token expiry, no session rotation, no concurrent session handling.

**Recommendation:** Implement JWT-based session management with short-lived access tokens and refresh tokens.

---

### SEC-011: Mock OTP Accepts Any Code — CVSS 7.5

| Field | Value |
|-------|-------|
| **Security Category** | Authentication |
| **Affected Module** | authClient.ts — verifyOtp |
| **Environment** | Frontend |

**Details:** `verifyOtp` accepts any 6-digit code except `000000`. No real OTP verification.

**Recommendation:** Replace with real OTP service integration.

---

### SEC-012: No Rate Limiting — CVSS 7.0

| Field | Value |
|-------|-------|
| **Security Category** | Insecure Design |
| **Affected Module** | All auth endpoints |
| **Environment** | Backend |

**Details:** No rate limiting on login, OTP, registration, or any API endpoint. Brute force attacks are trivial.

**Recommendation:** Implement rate limiting on all public endpoints.

---

## 🟡 MEDIUM Vulnerabilities

| ID | Title | CVSS | Recommendation |
|----|-------|------|---------------|
| SEC-013 | No CSRF protection | 5.5 | Add CSRF tokens for state-changing operations |
| SEC-014 | Error details may leak component structure | 4.5 | Use generic error messages in production |
| SEC-015 | No security audit trail | 5.0 | Implement security event logging |
| SEC-016 | No HTTP method restrictions on API | 5.0 | Restrict HTTP methods per endpoint |
| SEC-017 | No server-side input validation | 5.5 | Add @Valid annotations to all controllers |
| SEC-018 | console.log in production code | 4.0 | Remove before production build |
| SEC-019 | No Subresource Integrity | 4.0 | Add SRI hashes to script tags |

## 🔵 LOW Vulnerabilities

| ID | Title | CVSS | Recommendation |
|----|-------|------|---------------|
| SEC-020 | Vite dev server exposed to network | 3.0 | Remove host:true for production |
| SEC-021 | No Subresource Integrity | 2.5 | Add SRI hashes |
| SEC-022 | No API authorization headers | 2.5 | Add to API design |

---

*End of Security Bug Report — 22 vulnerabilities (4 critical, 8 high, 7 medium, 3 low)*
