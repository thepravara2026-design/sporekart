# Engineering Backlog — Bug Fix Sprint C (P2 Medium Priority)

## Master Backlog

Only P2 Medium-severity items eligible for Sprint C. Items are deduplicated, reclassified where appropriate, and ordered by Wave assignment.

| ID | Title | Module | Wave | Complexity | Effort | Owner |
|----|-------|--------|------|------------|--------|-------|
| BUG-C-001 | Account avatar shows duplicate upload buttons | Account Profile | Wave 1 | S | 4h eng + 2h QA | TBD |
| BUG-C-002 | Notification preferences Save button disabled after load | Account Notifications | Wave 1 | S | 3h eng + 2h QA | TBD |
| BUG-C-003 | No semantic ARIA landmarks on any page | Accessibility / Layout | Wave 1 | M | 8h eng + 3h QA | TBD |
| BUG-C-004 | Mobile nav menu renders empty on small viewport | Navigation / Responsive | Wave 1 | M | 6h eng + 3h QA | TBD |
| BUG-C-007 | Duplicate toast implementations (ToastQueue vs ToastProvider) | Notifications / Design System | Wave 1 | M | 8h eng + 3h QA | TBD |
| BUG-C-005 | No Service Worker — zero offline capability | Offline / PWA | Wave 2 | L | 20h eng + 6h QA | TBD |
| BUG-C-006 | Auth client is stub with hardcoded OTP code `123456` | Authentication | Wave 2 | L | 24h eng + 8h QA | TBD |
| BUG-C-008 | No app-level 404 page — SPA fallback to index.html | Error Handling / Routing | Wave 2 | S | 4h eng + 2h QA | TBD |
| BUG-C-009 | No performance budgets or Lighthouse CI tooling | Performance / CI | Wave 3 | M | 12h eng + 4h QA | TBD |

## P1/P0 Blocker Items (Not in Sprint C — must be resolved first or concurrently)

| ID | Severity | Priority | Title | Dependency |
|----|----------|----------|-------|------------|
| BUG-S3-CRIT-001 | Critical | P1 | Production build crash — React #62 + CSSStyleDeclaration | Blocks all frontend testing |
| BUG-S3-P1-004 | High | P1 | Admin routes accessible without authentication | Security gap |
| BUG-S3-HIGH-005 | High | P1 | Auth context infinite re-render loop | Blocks auth-dependent fixes |
| BUG-S3-P3-001 | High | P1 | No observability infrastructure | Operational risk |
| BUG-S3-P3-002 | High | P1 | No security headers (CSP, HSTS, XFO) | Security gap |
| BUG-S3-HIGH-003 | High | P1 | Role switcher absent from codebase | Blocks RBAC testing |

## Deferred Items (P3/P4 — future sprints)

| ID | Severity | Priority | Title | Rationale |
|----|----------|----------|-------|-----------|
| BUG-S3-P1-007 | Low | P4 | Notification badge unread count | Cosmetic; P4 |
| BUG-S3-P3-007 | Low | P3 | JPA entities lack audit timestamps | Tech debt; backend not wired |
| BUG-S3-LOW-007 | Low | P3 | `<title>` reads "LitVue App" instead of "SporeKart" | Cosmetic; P3 |
| BUG-S3-P1-005/MED-006 | Medium | P3 | Guest redirect behind ErrorBoundary | Resolved by CRIT-001 fix |

## Reclassification Notes

| Original ID | Original | Reclassified To | Rationale |
|-------------|----------|-----------------|-----------|
| BUG-S3-HIGH-004 | S2 High P2 | S3 Medium P2 | Missing landmarks affect accessibility but no data loss; reclassify from High to Medium |
| BUG-S3-P3-006 | S4 Low P3 | S3 Medium P2 | Missing 404 affects all users; reclassified from Low to Medium for UX completeness |
| BUG-S3-P3-008 | S4 Low P3 | S3 Medium P2 | Missing CI quality gates affect release confidence; reclassified from Low to Medium |

## Duplicates Removed

| Kept | Removed | Reason |
|------|---------|--------|
| BUG-S3-CRIT-001 | BUG-S3-CRIT-002, BUG-S3-P1-001, BUG-S3-MED-005, BUG-S3-P1-006, BUG-S3-MED-006, BUG-S3-P1-005, BUG-S3-LOW-007 | All secondary effects of the same build crash |
| BUG-S3-HIGH-003 | BUG-S3-P1-002 | Same root cause — missing role switcher |
