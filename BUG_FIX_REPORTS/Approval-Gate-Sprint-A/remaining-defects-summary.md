# Remaining Defects Summary — Approval Gate Sprint A

**Critical (P0) remaining: 0** — all 22 resolved.

## Classification of post-Sprint-A defects

| Class | Items | Detail | Disposition |
|-------|-------|-------|-------------|
| **P0 (Critical)** | 0 | — | None remaining |
| **P1 (High)** | 5 | BUG-RT-007 (workspace redirect), RT-008 (history clear on logout), RT-009 (multi-tab sync), RT-010 (global error boundary), RT-011 (session expiry auto-redirect) | Deferred → Sprint B |
| **P2/P3** | many | URL param validation (RT-012/013), a11y, performance, cross-browser, mobile native, security headers/CSP/X-Frame (SEC-005..008), CSRF (SEC-013), rate limiting (SEC-012/022), server-side validation (SEC-017) | Deferred → later sprints |
| **Implementation Gap** | 6 | Cart, checkout, content, search, support, risk services are placeholder (pre-existing) | Out of P0 scope |
| **Pre-Existing Build Blocker** | 2 | `inventory-service` `InventoryItem.getId()` missing; `ai-service` Lombok absent from pom | **Must fix before those services start** — not introduced by Sprint A |
| **Accepted Risk** | 1 | Full JWT/OAuth2 IdP deferred; current session model uses `activeRole` + `sessionStorage` | Accepted for RC1 stabilization |
| **Blocked** | 0 | — | — |

---

*End of Remaining Defects Summary.*
