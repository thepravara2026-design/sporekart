# Risk Assessment — QA Sprint 3

**Date:** 2026-07-18  
**Assessed by:** Enterprise Principal QA Engineer

---

## Risk Matrix

| Risk ID | Description | Probability | Impact | RPN | Mitigation |
|---------|-------------|-------------|--------|-----|------------|
| R1 | Production build collapse — CSS-in-JS crash | **Certain (5)** | **Critical (5)** | **25** | Fix library incompatibility immediately |
| R2 | Login page unreachable in production | **Certain (5)** | **Critical (5)** | **25** | Blocking dependency on R1 |
| R3 | RBAC untestable — no role switcher | **High (4)** | **High (4)** | **16** | Implement or restore component |
| R4 | Auth guard invisible behind error boundary | **High (4)** | **High (4)** | **16** | Fix build crash first (R1) |
| R5 | Vacuous test assertions mask real defects | **High (4)** | **Medium (3)** | **12** | Audit all test specs |
| R6 | No ARIA landmarks prevents screen reader use | **Certain (5)** | **Medium (3)** | **15** | Add landmark elements to layout |
| R7 | Console errors pollute error monitoring | **Certain (5)** | **Low (2)** | **10** | Resolve CSS-in-JS errors |
| R8 | Missing auth guard on admin routes | **Certain (5)** | **High (4)** | **20** | Wrap admin routes in RequireAuth |
| R9 | RC phase cannot start without stable build | **Certain (5)** | **Critical (5)** | **25** | Pivot Sprint C to build fix |
| R10 | Existing RBAC tests (30) permanently broken | **Certain (5)** | **Medium (3)** | **15** | Fix role switcher, re-run tests |

**RPN Legend:** Probability × Impact (1-25). Score >12 requires mitigation before RC.

---

## Risk Details

### R1: Production Build Collapse (RPN: 25)
**What:** The `vite build` output crashes at runtime on every page except the homepage. Two error types: React error #62 (style value type) and CSSStyleDeclaration TypeError (indexed property setter).
**Why it matters:** End users cannot access any feature. Zero functional pages beyond landing page.
**Blocker for:** Everything. RC, UAT, deployment.

### R2: Login Page Unreachable (RPN: 25)
**What:** Consequence of R1. The login page uses the shared component library (Input, Checkbox, Button) which triggers the CSS-in-JS crash.
**Why it matters:** No user can authenticate. No session, no personalization, no orders.
**Blocker for:** All authenticated flows.

### R3: RBAC Untestable (RPN: 16)
**What:** 30 existing test cases reference `select[aria-label="Switch review role"]` which does not exist in the source code.
**Why it matters:** QA cannot validate role-based access control. Risk of privilege escalation bugs reaching production.

### R4: Auth Guard Invisible (RPN: 16)
**What:** When a guest accesses a protected route, they see the ErrorBoundary instead of being redirected to `/login`.
**Why it matters:** Users don't know they need to authenticate. Poor UX; appears broken.

### R8: Missing Admin Auth Guard (RPN: 20)
**What:** Admin routes (`/admin/*`) have no `<RequireAuth>` wrapper. Any user can access admin pages without authentication.
**Why it matters:** Unauthenticated users could view admin dashboards, product catalogs, order lists, user management.
**Status:** Known gap from Sprint B (BUG-ADM-001), deferred.

---

## Dependency Graph

```
R1 (Build Crash)
 ├── R2 (Login Unreachable)
 │    ├── R4 (Auth Guard Invisible)
 │    └── Session flow blocked
 ├── Dashboard unreachable
 ├── Admin unreachable
 └── Training unreachable
     
R3 (No Role Switcher) [independent]
R6 (No ARIA Landmarks) [independent]
R8 (No Admin Auth Guard) [independent]
R5 (Vacuous Assertions) [independent]
```

R1 is the single-point-of-failure. All other risks are either direct consequences or independent but manageable.

---

## Recommended Response

| Priority | Action | Owner | Timeline |
|----------|--------|-------|----------|
| **P0** | Debug and fix production build crash | Dev Lead | Day 1-2 |
| **P0** | Re-validate all pages in preview mode | QA | Day 3 |
| **P1** | Implement/restore role switcher | Dev | Day 3 |
| **P1** | Add auth guard to admin routes | Dev | Day 3 |
| **P2** | Add ARIA landmarks to layout | Dev | Day 4 |
| **P2** | Fix vacuous assertions in test specs | QA | Day 4 |
| **P3** | Full regression re-run | QA | Day 5 |

---

*End of Risk Assessment — QA Sprint 3*
