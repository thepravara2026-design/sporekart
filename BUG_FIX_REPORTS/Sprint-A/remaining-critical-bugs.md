# Remaining Critical Bugs — Bug Fix Sprint A

**Result: 0 remaining critical bugs.** All 22 P0 (Critical, Release-Blocker = YES) defects from the QA Sprint 2 Master Bug Register are resolved in this sprint.

## Carry-over items for Bug Fix Sprint B (NOT critical / NOT P0)

The following are explicitly **out of P0 scope** and must be triaged in later sprints:

- **P1 (High) route/session bugs:** BUG-RT-007 (workspace redirect), RT-008 (history clear on logout), RT-009 (multi-tab sync), RT-010 (global error boundary), RT-011 (session expiry auto-redirect).
- **P2/P3 & non-critical:** URL param validation (RT-012/013), accessibility, performance, cross-browser, mobile native apps, security headers/CSP/rate-limiting (SEC-005..020), and full JWT/OAuth2 IdP replacement of the session model.

## Pre-existing build blockers (independent of Sprint A fixes)

- `inventory-service`: `InMemoryInventoryRepository` references missing `InventoryItem.getId()` (pre-existing compile error).
- `ai-service`: `lombok` dependency missing from `pom.xml` (pre-existing; breaks Lombok-annotated `Administration*ServiceImpl`).

Both are unrelated to the security fixes added this sprint and must be resolved before those two services can start.

---

*End of Remaining Critical Bugs.*
