# Wave Planning — Bug Fix Sprint D

**Date:** 2026-07-18

---

## Wave Strategy

Issues are grouped into 4 waves based on dependency chains, risk profile, and value-to-effort ratio within the expanded Sprint D scope.

---

## Wave 1: Quick Wins (Days 1–3, Parallelizable)

Small, isolated fixes with no external dependencies. Can be done in parallel by multiple engineers.

| Issue | Effort | Assignee Profile | Risk |
|-------|--------|------------------|------|
| BUG-AUTH-001 — Firefox CSS `:has()` fix | 0.5–1 day | Frontend | Low |
| BUG-QA4-HIGH-003 — OTP navigation state | 0.5–1 day | Frontend | Low |
| BUG-QA4-HIGH-004 — Product mock data | 1 day | Frontend + Content | Low |
| BUG-QA4-MED-004 — ARIA labels on social icons | 0.25 day | Frontend | Very Low |
| BUG-QA4-LOW-001 — Footer contrast | 0.25 day | Frontend | Very Low |
| BUG-QA4-LOW-002 — Training page title | 0.1 day | Frontend | Very Low |
| BUG-QA4-LOW-003 — Deprecated console warning | 0.1 day | Frontend | Very Low |
| **Total Wave 1** | **3–4 days** | | |

**Note:** These are marked High+Vy Low because they are technically simple but were incorrectly classified. All are safe to parallelize.

---

## Wave 2: Dependency Foundation (Days 2–7)

Foundational work that other waves depend on. Must be completed first.

| Issue | Effort | Dependents | Risk |
|-------|--------|------------|------|
| BUG-S3-CRIT-001 — Route guards (AuthGuard) | 5–7 days | CRIT-003, HIGH-003, MED-001 | Medium |
| BUG-S3-CRIT-002 — Cart scaffolding | 15–20 days | (future payment work) | High |
| **Total Wave 2** | **20–27 days** | | |

**Strategy:** Route guards and cart can be done in parallel by two engineers. Route guards are prerequisite for admin console work.

---

## Wave 3: Dependent Builds (Days 5–12)

Work that depends on Wave 2 completion.

| Issue | Effort | Depends On | Risk |
|-------|--------|------------|------|
| BUG-S3-CRIT-003 — Admin console functional | 10–15 days | CRIT-001 (route guards) | High |
| BUG-S3-HIGH-003 — Role switcher | 2–3 days | CRIT-001 (route guards) | Low |
| BUG-QA4-MED-001 — Dashboard redirect | 0.5 day | CRIT-001 (route guards) | Low |
| BUG-QA4-MED-002 — OTP validation messages | 0.5 day | HIGH-003 (OTP fix) | Low |
| **Total Wave 3** | **13–19 days** | | |

---

## Wave 4: Polish & Debt (Days 12–20)

Independent polish work; can overlap with Wave 3.

| Issue | Effort | Risk |
|-------|--------|------|
| BUG-QA4-MED-003 — Case-insensitive search | 0.5 day | Very Low |
| BUG-QA4-MED-005 — Flaky test stabilization | 2 days | Low |
| **Total Wave 4** | **2.5 days** | |

---

## Parallelization Strategy

```
Week 1:
  Engineer A: Wave 2 — Route guards (5–7 days)
  Engineer B: Wave 1 — Quick wins (3–4 days) → Wave 4 — Polish (2.5 days)
  Engineer C: Wave 2 — Cart scaffolding (15–20 days, spans all 4 weeks)

Week 2:
  Engineer A: Wave 3 — Admin console (10–15 days)
  Engineer B: Wave 3 — Role switcher + Dashboard + OTP validation (3–4 days)

Week 3:
  Engineer A: Continue admin console
  Engineer B: Continue cart

Week 4:
  All: Integration testing, regression, QA Sprint 5
```

---

## Cumulative Progress

| End of Week | Issues Closed | Est. % Complete |
|-------------|---------------|-----------------|
| Week 1 | 7 | 35% (all Wave 1 + route guards + cart started) |
| Week 2 | 4 | 65% (admin started + role switcher + dashboard) |
| Week 3 | 3 | 85% (admin + OTP validation + cart) |
| Week 4 | 2 | 100% (polish + flaky tests + QA) |
