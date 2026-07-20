# QA Sprint 2 — Coverage & Traceability Report

This report maps the test specifications to components and features within SporeKart's Authentication and Session scopes.

## 1. Traceability Matrix

| Part | Feature / Component | Test Spec File | Verified Scenarios | Coverage Status |
|---|---|---|---|---|
| **Part 1** | Authentication Views | `auth-validation.spec.ts` | Happy login, terms gate, error validation, OTP entry, registration submission, reset recovery instructions, social stubs | **PASS** |
| **Part 2** | Session Screens | `session-management.spec.ts` | Loading page spinner, expired redirect action, logout verification, 403 access denied layout | **PASS** |
| **Part 3** | Role-Based Access Control | `rbac-authorization.spec.ts` | Role switcher validation, per-role sidebar visibility (9 roles), protected route access, navigation security, URL access patterns, admin permission provider, storage/state validation, multi-tab consistency, responsive sidebar | **PASS** |
| **Part 4** | Route Protection | `protected-routes.spec.ts` | Unauthorized direct URL entry redirecting, deep link guard actions, guest restrictions | **PASS** |
| **Part 5** | Authentication APIs | `auth-apis.spec.ts` | Response schema validation, 900ms simulated latency check, endpoint config variables | **PASS** |
| **Part 6** | Browser Compatibility | `cross-browser.spec.ts` | Layout comparison on Chromium, Firefox, WebKit | **PASS** |
| **Part 7** | Responsive Layouts | `mobile-responsive.spec.ts` | Desktop, Tablet, and Mobile viewports layouts, touch target visibility, sidebar responsiveness | **PASS** |
| **Part 8** | Web Accessibility (a11y) | `accessibility.spec.ts` | WCAG 2.1 AA rules scan, keyboard tab indexing, skip links presence, focus styling, label association | **PASS** |
| **Part 9** | Performance Baselines | `performance.spec.ts` | Base load time limits, network resource weights, cache headers verification | **PASS** |
| **Part 10**| Security Validations | `security-validation.spec.ts` | Authentication bypass preventions, secure session token simulated storage | **PASS** |
| **Part 11**| Regression Checks | `regression.spec.ts` | Verification of sprint-01 baseline checks (npm build/run dev validations, mock-mode verification) | **PASS** |

---

## 2. Test Cases Specification Details

### 2.1 Part 1 — Authentication Validation
- **TC-AUTH-001 (Happy Login):** Input valid identifier (phone/email), check terms checkbox, click Submit. Expected: Redirects to `/verify-otp`. **[PASS]**
- **TC-AUTH-002 (Terms Gate):** Input valid identifier, leave terms checkbox unchecked, click Submit. Expected: Shows form validation alert; submit blocked. **[PASS]**
- **TC-AUTH-003 (Invalid Inputs):** Input invalid email or phone number formats. Expected: Returns inline error text. **[PASS]**
- **TC-AUTH-004 (OTP Cooldown):** Navigate to `/verify-otp`, check countdown timer counts down from 30s for resend, and verify resend button is disabled during countdown. **[PASS]**
- **TC-AUTH-005 (Verify OTP Success):** Input any 6-digit code except `000000`. Expected: Displays success screen, then redirects. **[PASS]**
- **TC-AUTH-006 (Verify OTP Fail):** Input code `000000`. Expected: Shows inline error "Incorrect code. Please try again." **[PASS]**
- **TC-AUTH-007 (Register Page):** Fill name, phone, optional email, agree to privacy policy, click Register. Expected: Triggers OTP verification flow. **[PASS]**
- **TC-AUTH-008 (Password Recovery):** Request password recovery link for email/phone. Expected: Shows confirmation message "Recovery instructions sent". **[PASS]**
- **TC-AUTH-009 (Social Authentication):** Click Google/Apple login. Expected: Displays "Social login is not enabled yet." **[PASS]**

### 2.2 Part 2 — Session Management
- **TC-SESS-001 (Session Loading):** Request `/auth/loading`. Expected: Spinner displays with text "Establishing your session". Redirects after timeout. **[PASS]**
- **TC-SESS-002 (Session Expired):** Request `/session-expired`. Expected: Warning status screen with primary action "Sign in again" linking to `/login`. **[PASS]**
- **TC-SESS-003 (Access Denied):** Request `/access-denied`. Expected: Danger status screen indicating no permissions, linking back to `/`. **[PASS]**

### 2.3 Part 3 — Authorization & RBAC (28 tests)
- **TC-RBAC-001 (Role Switcher):** Role switcher `<select>` exists with all 9 role options. **[PASS]**
- **TC-RBAC-002 (Guest Workspace):** Guest sees only Public workspace; hidden: Customer, Orders, Products, Administration, Governance, Analytics, Support, Settings, AI Workspace, CMS. **[PASS]**
- **TC-RBAC-003 (Customer Workspace):** Customer sees Public, Customer, Training, Support, Settings, AI Workspace; hidden: Orders, Products, Administration, Governance, Analytics, CMS. **[PASS]**
- **TC-RBAC-004 (Grower Workspace):** Grower sees Public, Customer, Products, Training, Support, Settings, Analytics, AI Workspace; hidden: Orders, Administration, Governance, CMS. **[PASS]**
- **TC-RBAC-005 (Trainer Workspace):** Trainer sees Public, Training, Support, Settings, CMS, AI Workspace; hidden: Orders, Products, Administration, Governance, Analytics. **[PASS]**
- **TC-RBAC-006 (Distributor Workspace):** Distributor sees Public, Orders, Products, Training; hidden: Customer, Support, Settings, AI Workspace, Administration, Governance, Analytics, CMS. **[PASS]**
- **TC-RBAC-007 (Support Workspace):** Support sees Public, Orders, Training, Support, Settings, AI Workspace; hidden: Products, Administration, Governance, Analytics, CMS, Customer. **[PASS]**
- **TC-RBAC-008 (Administrator Workspace):** Administrator sees all workspaces except Customer; hidden: Customer. **[PASS]**
- **TC-RBAC-009 (Business Owner Workspace):** Business Owner sees Public, Training, Settings, AI Workspace, Analytics; hidden: Customer, Orders, Products, Support, Administration, Governance, CMS. **[PASS]**
- **TC-RBAC-010 (Governance Manager Workspace):** Governance Manager sees Public, Training, Settings, AI Workspace, Governance; hidden: Customer, Orders, Products, Support, Administration, Analytics, CMS. **[PASS]**
- **TC-RBAC-011 (Role Switcher):** Role switcher `<select>` exists with all 9 role options. **[PASS]**
- **TC-RBAC-012 (Guest Admin Access):** Guest cannot access `/admin/dashboard` directly. **[PASS]**
- **TC-RBAC-013 (Customer Order Queue):** Customer cannot access order management queue directly. **[PASS]**
- **TC-RBAC-014 (SPA Role Persistence):** Role persists after SPA navigation (pushState). **[PASS]**
- **TC-RBAC-015 (Admin Route Navigation):** Administrator can navigate to all protected routes without error. **[PASS]**
- **TC-RBAC-016 (Role Reset on Refresh):** Role resets to `administrator` on full browser refresh. **[PASS]**
- **TC-RBAC-017 (Workspace Groups):** Sidebar groups correctly labelled: Discover, Operate, Intelligence, Platform. **[PASS]**
- **TC-RBAC-018 (Role Switch Changes Sidebar):** Sidebar workspaces update when role is switched. **[PASS]**
- **TC-RBAC-019 (Direct URL /settings):** Direct navigation to `/settings` renders settings workspace. **[PASS]**
- **TC-RBAC-020 (Direct URL /admin/dashboard):** Direct navigation to `/admin/dashboard` renders admin dashboard. **[PASS]**
- **TC-RBAC-021 (Direct URL /account):** Direct navigation to `/account` renders customer workspace. **[PASS]**
- **TC-RBAC-022 (Direct URL /demo):** Navigation to `/demo` shows Demo workspace in sidebar. **[PASS]**
- **TC-RBAC-023 (Admin Dashboard):** Admin dashboard route accessible with administrator role. **[PASS]**
- **TC-RBAC-024 (Admin Users):** Admin users management route renders without error. **[PASS]**
- **TC-RBAC-025 (Admin Settings):** Admin settings route renders without error. **[PASS]**
- **TC-RBAC-026 (No localStorage Role Data):** No role/permission data stored in localStorage. **[PASS]**
- **TC-RBAC-027 (No sessionStorage Role Data):** No role/permission data stored in sessionStorage. **[PASS]**
- **TC-RBAC-028 (Multi-tab Consistency):** Multiple tabs show same default role. **[PASS]**
- **TC-RBAC-029 (Mobile Responsive):** Sidebar renders correctly on mobile viewport (375x667). **[PASS]**

### 2.4 Part 4 — Protected Routes
- **TC-PROT-001 (Admin Guard):** Navigate directly to `/admin` as guest/customer. Expected: Redirects to `/access-denied` or `/login`. **[PASS]**
- **TC-PROT-002 (Customer Guard):** Navigate directly to `/dashboard` as guest. Expected: Redirects to `/login`. **[PASS]**

### 2.5 Part 8 — Accessibility
- **TC-A11Y-001 (Axe Audit):** Run Axe scanner on `/login`, `/register`, `/verify-otp`. Expected: 0 critical violations. **[PASS]**
- **TC-A11Y-002 (Keyboard Flow):** Navigate pages using Tab key. Expected: Visual focus indicator clearly visible. **[PASS]**

### 2.6 Part 10 — Security
- **TC-SEC-001 (Bypass Prevention):** Direct access to post-auth screens without token/state. Expected: Graceful fallback and redirect to `/login`. **[PASS]**
