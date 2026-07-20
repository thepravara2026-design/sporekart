# Risk Assessment — Approval Gate Sprint A

**Release Candidate:** v1.0.0-rc1 | **Date:** 2026-07-17

## Residual Risks (post-Sprint-A)

| # | Risk | Likelihood | Impact | Severity | Mitigation / Condition |
|---|------|-----------|--------|-----------|----------------------|
| R-1 | `inventory-service` & `ai-service` do not compile (pre-existing) | Certain | High (2 services cannot start) | **HIGH** | **Condition:** fix before RC2; unrelated to P0 fixes |
| R-2 | Full Playwright/regression suite not executed (no browser runtime here) | Certain | Medium (latent UI regressions unseen) | **MEDIUM** | **Condition:** run in browser-enabled CI before sign-off |
| R-3 | Session model is `activeRole` + `sessionStorage`, not a federated IdP/JWT | Likely | Medium (no SSO/token propagation across services) | **MEDIUM** | Accepted for RC1; full IdP deferred to later sprint |
| R-4 | Backend auth uses Spring auto-configured in-memory user (no shared IdP) | Likely | Medium (services independently authenticated, no SSO) | **MEDIUM** | Addressed by R-3 roadmap |
| R-5 | Security headers / CSP / CSRF / rate-limiting still absent | Certain | Medium (defense-in-depth gaps) | **MEDIUM** | Deferred to Sprint B (P1+) |
| R-6 | Out-of-scope pre-existing diff `SessionTimeoutWarning.tsx` (focus-trap, 39 lines) present in working tree | Certain | Low (a11y improvement, not P0) | **LOW** | Recommend separate commit/PR review for scope clarity |

## Accepted Risks
- R-3 (session model) — explicit program scope exclusion (stabilization, not redesign).
- R-5 (headers/CSRF/rate-limit) — explicitly P1+ per charter.

## Risk Posture
**Residual risk is MODERATE and bounded.** No P0 (Critical) risk remains. All conditions are actionable and do not block the *decision* to approve Sprint A (they gate *Sprint B readiness*).

---

*End of Risk Assessment.*
