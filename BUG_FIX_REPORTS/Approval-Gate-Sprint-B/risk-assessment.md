# Risk Assessment — Approval Gate Sprint B

**Branch:** `bugfix/sprint-b-high-priority` · **Date:** 2026-07-17

## Risk Register

| # | Risk | Likelihood | Impact | Severity | Mitigation |
|---|------|:--:|:--:|:--:|-----------|
| R1 | Playwright/cross-browser E2E not executed | High (env constraint) | Medium | High | Run full browser suite in CI before RC2 (gate condition 2) |
| R2 | Repository not clean (uncommitted Sprint A + artifacts) | High | Medium | High | Commit/stash Sprint A; remove untracked artifacts before Sprint C |
| R3 | ai-service cannot start (deeper compile errors) | Certain (known) | High | High | Dedicated foundation sprint; not in P1 scope |
| R4 | Mock OTP demo PIN `123456` is predictable | Medium | Low | Medium | Acceptable for UX stub; real OTP via IdP later |
| R5 | Role switcher hidden but `activeRole` still client-storeable | Low | Medium | Low | Session role is not a security control; backend enforces RBAC (Sprint A) |
| R6 | Responsive CSS relies on class/structure assumptions | Low | Low | Low | Verified against KPIGrid + admin.css; E2E will confirm |

## Residual Risk Posture
- **Sprint B P1 work:** Low residual risk — root-cause fixes, no workarounds.
- **RC2 readiness:** Medium residual risk driven by R1/R2/R3 (all continuation/infra, not B-quality failures).

---

*End of Risk Assessment.*
