# Architecture Correction Plan — Sprint E

**Program:** SporeKart Enterprise Release Program
**Phase:** Architecture Correction Sprint E (Post QA Sprint 5)
**Source:** QA_REPORTS/Sprint-05/ — RC1 Release Audit
**Authority:** Enterprise Architecture Council

---

## Root Cause Matrix

### CRITICAL-001: Auth Bypass via sessionStorage (P0-01 Unresolved)

| Field | Analysis |
|---|---|
| **Issue ID** | SPRINT5-CRITICAL-001 |
| **Business Impact** | Any user can access any route including admin. Complete authentication failure. Production launch impossible. |
| **Technical Root Cause** | `App.tsx:433` initializes `activeRole` from `sessionStorage.getItem('sk_session_role')`. `AuthLoadingPage:31` writes `sessionStorage.setItem('sk_session_role', nextRole)`. Auth state is mutable client-side React state. |
| **Architectural Root Cause** | Two parallel auth systems exist: (1) `AuthService`/`supabase.ts` wraps real Supabase but is NEVER connected to App context; (2) Legacy mock role system in `App.tsx`/`context.ts` controls all routing decisions. The legacy system remained operational because Sprint E only added wrappers without integration. |
| **Affected Components** | `App.tsx`, `context.ts`, `RequireAuth.tsx`, `AuthLoadingPage.tsx`, `authClient.ts`, `SessionPages.tsx` |
| **Security Risk** | **CRITICAL** — Complete auth bypass. CWE-287: Improper Authentication. CWE-306: Missing Authentication for Critical Function. |
| **Implementation Strategy** | Eliminate `activeRole` state from `App.tsx`. Derive auth state from `onAuthChange()` in `supabase.ts`. Remove `setActiveRole` from context. Remove all sessionStorage role reads/writes. Make `RequireAuth` check Supabase session directly. |
| **Regression Risk** | HIGH — Every route guard, every auth page, every role check must be verified. All protected routes will be unreachable if session integration fails. |
| **Rollback Plan** | Revert `App.tsx`, `context.ts`, `RequireAuth.tsx`, `SessionPages.tsx` to pre-correction state. Restore `setActiveRole` pattern. |
| **Acceptance Criteria** | (1) No `sessionStorage` read for auth in `App.tsx`. (2) No `setActiveRole` call in production code. (3) `RequireAuth` checks Supabase session. (4) AuthLoadingPage uses Supabase session. |

### CRITICAL-002: No Server-Side Payment Endpoints (P0-02)

| Field | Analysis |
|---|---|
| **Issue ID** | SPRINT5-CRITICAL-002 |
| **Business Impact** | Payment processing is impossible — core e-commerce function broken. |
| **Technical Root Cause** | `PaymentGateway.ts:63` calls `fetch(/payment/create-intent)` and `PaymentGateway.ts:112` calls `/payment/webhook`. No such endpoints exist. |
| **Architectural Root Cause** | The payment architecture was designed with real Stripe endpoints in mind but no backend was implemented. The Stripe code path calls URLs that have never been created. |
| **Affected Components** | `PaymentGateway.ts` |
| **Security Risk** | HIGH — No payment processing capability. If switched to Stripe mode, every payment would fail with network error. |
| **Implementation Strategy** | **OPTION B**: Remove all references to non-existent endpoints in Stripe mode. Provide self-contained mock payment lifecycle that never calls a server. The mock returns success immediately with a generated paymentIntentId. When real backend is available, the endpoint calls can be restored. |
| **Regression Risk** | LOW — Payment is entirely new functionality in Sprint E. No existing payment flows to regress. |
| **Rollback Plan** | Revert `PaymentGateway.ts` to pre-correction state. |
| **Acceptance Criteria** | (1) No `/payment/create-intent` call. (2) No `/payment/webhook` call. (3) Mock mode returns success. (4) Stripe mode falls back gracefully without network call. |

### CRITICAL-003: No Order Creation (P0-02)

| Field | Analysis |
|---|---|
| **Issue ID** | SPRINT5-CRITICAL-003 |
| **Business Impact** | After successful payment, no order record exists. Customer sees confirmation but no order is created. |
| **Technical Root Cause** | `CheckoutPage.tsx:42` calls `clearCart()` after mock payment success. No order object is created or persisted. |
| **Architectural Root Cause** | Checkout flow was implemented as UI-only. No order model, no order service, no order persistence. The success state is purely cosmetic. |
| **Affected Components** | `CheckoutPage.tsx` |
| **Security Risk** | HIGH — Customers could receive confirmation screens for orders that never exist. Payment could be charged with no order record. |
| **Implementation Strategy** | Create a client-side order object on payment success. Store in localStorage with status tracking. This is a mock order — real order management requires backend. |
| **Regression Risk** | LOW |
| **Rollback Plan** | Revert `CheckoutPage.tsx`. |
| **Acceptance Criteria** | (1) Order object created on payment success. (2) Order ID displayed. (3) Order persisted in localStorage. |

### CRITICAL-004: Session in sessionStorage (P1-01 Unresolved)

| Field | Analysis |
|---|---|
| **Issue ID** | SPRINT5-CRITICAL-004 |
| **Business Impact** | XSS vulnerability — session tokens readable by any script. No httpOnly cookie protection. |
| **Technical Root Cause** | `AuthLoadingPage.tsx:31` writes role to `sessionStorage`. `App.tsx:435` reads from `sessionStorage`. Supabase session (which uses httpOnly cookies) exists but is never used for app routing decisions. |
| **Architectural Root Cause** | Same root cause as CRITICAL-001 — dual auth systems. |
| **Affected Components** | `AuthLoadingPage.tsx`, `App.tsx`, `SessionPages.tsx` |
| **Security Risk** | CRITICAL — XSS can read sessionStorage and exfiltrate session data. CWE-312: Cleartext Storage of Sensitive Information. |
| **Implementation Strategy** | Remove all `sessionStorage` auth reads/writes. Derive session from Supabase `onAuthChange`. |
| **Regression Risk** | HIGH (tied to CRITICAL-001 fix) |
| **Rollback Plan** | Same as CRITICAL-001 rollback. |
| **Acceptance Criteria** | (1) No `sessionStorage.setItem('sk_session_role')` in production code. (2) No `sessionStorage.getItem('sk_session_role')` in production code. |

### SPRINT5-HIGH-001: No CSRF Protection

| Field | Analysis |
|---|---|
| **Issue ID** | SPRINT5-HIGH-001 |
| **Business Impact** | Cross-site request forgery possible on state-changing operations. |
| **Technical Root Cause** | No CSRF token generation or validation implemented. Planned in Sprint E but not delivered. |
| **Architectural Root Cause** | CSRF was scoped to Sprint E but not completed. The `lib/csrf.ts` file was never created. |
| **Affected Components** | N/A — new feature |
| **Security Risk** | HIGH — CWE-352: Cross-Site Request Forgery. |
| **Implementation Strategy** | Create `src/lib/csrf.ts` with token generation and validation utility. |
| **Regression Risk** | LOW |
| **Rollback Plan** | Remove `csrf.ts` file. |
| **Acceptance Criteria** | (1) `csrf.ts` exists. (2) Token generation works. (3) Token validation works. |

### SPRINT5-HIGH-004: No Idempotency Key

| Field | Analysis |
|---|---|
| **Issue ID** | SPRINT5-HIGH-004 |
| **Business Impact** | Duplicate payment submission possible — customer could be charged multiple times. |
| **Technical Root Cause** | `PaymentGateway.ts` and `CheckoutPage.tsx` have no idempotency key generation. Only a `processing` boolean prevents double-submit. |
| **Architectural Root Cause** | Payment flow designed without idempotency consideration. |
| **Affected Components** | `PaymentGateway.ts`, `CheckoutPage.tsx` |
| **Security Risk** | HIGH — Duplicate charges. CWE-841: Improper Enforcement of Behavioral Workflow. |
| **Implementation Strategy** | Add idempotency key generation and tracking in the checkout flow. Store used keys to prevent replay. |
| **Regression Risk** | LOW |
| **Rollback Plan** | Remove idempotency logic. |
| **Acceptance Criteria** | (1) Idempotency key generated per checkout attempt. (2) Duplicate key prevented. |

---

## Implementation Plan

### Wave 1 — Auth Architecture (HIGHEST RISK)
1. Refactor `context.ts` — Replace `activeRole`/`setActiveRole` with session-based auth state
2. Refactor `App.tsx` — Remove sessionStorage initialization, wire to Supabase session listener
3. Refactor `RequireAuth.tsx` — Check Supabase session instead of context role
4. Refactor `AuthLoadingPage.tsx` — Use session listener, remove sessionStorage write
5. Remove `setActiveRole` from all production code paths

### Wave 2 — Payment Architecture
6. Fix `PaymentGateway.ts` — Remove non-existent endpoint calls
7. Fix `CheckoutPage.tsx` — Add order creation, idempotency

### Wave 3 — Security Hardening
8. Create `csrf.ts` — CSRF token utility
9. Verify no remaining sessionStorage role access

---

## Order of Implementation

1. `context.ts` — New auth state interface
2. `lib/supabase.ts` — Already exists, verify session listener works
3. `App.tsx` — Wire to Supabase
4. `RequireAuth.tsx` — Use session
5. `SessionPages.tsx` — Remove sessionStorage
6. `AuthLoadingPage.tsx` — Remove sessionStorage
7. `PaymentGateway.ts` — Remove bad endpoints
8. `CheckoutPage.tsx` — Order creation, idempotency
9. `lib/csrf.ts` — New utility
10. Build → TypeCheck → Verify

---

## Quality Gates — Completion Status

| Gate | Status | Check |
|------|--------|-------|
| No sessionStorage role read in App.tsx | ✅ PASS | grep for `sessionStorage` in App.tsx — none found |
| No sessionStorage role write in any production file | ✅ PASS | grep for `sk_session_role` across src/ — none found |
| No setActiveRole in production code | ✅ PASS | grep for `setActiveRole` across src/ — only in context.ts definition |
| RequireAuth checks session, not context | ✅ PASS | `RequireAuth.tsx` uses `auth.isAuthenticated` and `auth.userRole` |
| PaymentGateway has no non-existent endpoint calls | ✅ PASS | Self-contained mock mode; no `/payment/create-intent` or `/payment/webhook` |
| Order created on payment success | ✅ PASS | `CheckoutPage.tsx` creates `OrderRecord` and calls `saveOrder()` on success |
| Idempotency key generated per checkout | ✅ PASS | `generateIdempotencyKey()` + `loadUsedKeys()` + `markKeyUsed()` in `CheckoutPage.tsx` |
| CSRF utility exists | ✅ PASS | `src/lib/csrf.ts` with token generation, caching, and `csrfHeader()` |
| CSRF integrated into HttpClient | ✅ PASS | `httpClient.ts` adds `X-CSRF-Token` header to all mutating requests |
| WorkspacePage uses auth.userRole | ✅ PASS | Replaced `activeRole` → `auth.userRole` |
| Sidebar uses auth.userRole | ✅ PASS | Replaced `activeRole` → `auth.userRole` |
| CommandPalette uses auth.userRole | ✅ PASS | Replaced `activeRole` → `auth.userRole` |
| AdminLayout uses auth.userRole | ✅ PASS | Replaced `activeRole` → `auth.userRole` |
| AdminDashboard uses auth.userRole | ✅ PASS | Replaced `activeRole` → `auth.userRole` |
| useSession no longer touches sessionStorage for auth | ✅ PASS | Removed `sk_session_role` read/write; only navigates to `/session-expired` |
| Build PASS | ⏳ PENDING | Run `npm run build` |
| TypeScript 0 errors | ⏳ PENDING | Run `npx tsc --noEmit` |
