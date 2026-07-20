# Dependency Graph — Bug Fix Sprint D

**Date:** 2026-07-18

---

## Dependency Map

```
WAVE 1 (Quick Wins — no deps)
├── BUG-AUTH-001     Firefox CSS fix
├── BUG-QA4-HIGH-003 OTP navigation state
├── BUG-QA4-HIGH-004 Product mock data
├── BUG-QA4-MED-004  ARIA labels
├── BUG-QA4-LOW-001  Footer contrast
├── BUG-QA4-LOW-002  Training page title
├── BUG-QA4-LOW-003  Deprecated console warning
├── BUG-QA4-MED-003  Case-insensitive search
└── BUG-QA4-MED-005  Flaky test stabilization

WAVE 2 (Foundation)
└── BUG-S3-CRIT-001  Route guards (AuthGuard) ──────┐
└── BUG-S3-CRIT-002  Cart scaffolding ───────────────┤
                                                     │
WAVE 3 (Depends on Wave 2)                           │
├── BUG-S3-CRIT-003  Admin console ◄─── depends on ──┤ (route guards)
├── BUG-S3-HIGH-003  Role switcher ◄─── depends on ──┤ (route guards)
├── BUG-QA4-MED-001  Dashboard redirect ◄── depends ─┤ (route guards)
└── BUG-QA4-MED-002  OTP validation ◄── depends on ──┘ (OTP fix, Wave 1)

ALL PHASES → QA Sprint 5 (regression validation)
```

---

## Critical Path

The longest chain of dependent work:

```
BUG-S3-CRIT-001 (Route guards, 5–7d)
  → BUG-S3-CRIT-003 (Admin console, 10–15d)
  → QA Sprint 5 validation (2d)

Total: 17–24 days on critical path
```

The **cart scaffolding** (BUG-S3-CRIT-002, 15–20d) has no dependents and is not on the critical path — it can run in parallel.

---

## Dependency Details

| Dependent Issue | Depends On | Nature of Dependency | Risk if Dep Not Done |
|-----------------|------------|---------------------|---------------------|
| BUG-S3-CRIT-003 (Admin console) | BUG-S3-CRIT-001 (Route guards) | Admin routes cannot be secured without AuthGuard | Admin console would remain accessible without auth |
| BUG-S3-HIGH-003 (Role switcher) | BUG-S3-CRIT-001 (Route guards) | Role-based rendering requires auth context | Role switcher would work but routes would still be unguarded |
| BUG-QA4-MED-001 (Dashboard redirect) | BUG-S3-CRIT-001 (Route guards) | Redirect logic is part of AuthGuard | Dashboard would show content instead of redirecting |
| BUG-QA4-MED-002 (OTP validation) | BUG-QA4-HIGH-003 (OTP nav fix) | Validation messages cannot be tested until OTP flow works | Validation would exist but OTP would still be broken |

---

## Circular Dependency Check

No circular dependencies detected. The graph is a directed acyclic graph with clear layering.
