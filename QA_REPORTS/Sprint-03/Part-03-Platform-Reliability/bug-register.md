# Bug Register — QA Sprint 3 Part 3 (Platform Reliability)

## Active Defects (Carried Forward from Sprint 3)

| ID               | Severity | Priority | Module     | Description                                                                 | Status |
|------------------|----------|----------|------------|-----------------------------------------------------------------------------|--------|
| BUG-S3-CRIT-001  | Critical | P0       | Build      | Production build crash: React #62 + CSSStyleDeclaration TypeError on all routes using shared component library | Open |
| BUG-S3-HIGH-003  | High     | P1       | RBAC       | Role switcher component not present in codebase                             | Open |
| BUG-S3-P1-003    | Medium   | P2       | Account    | Account profile — Avatar section shows double-upload buttons                | Open |
| BUG-S3-P1-004    | Medium   | P2       | Account    | Notification preferences — Save button disabled after preferences loaded    | Open |
| BUG-S3-HIGH-005  | High     | P1       | Auth       | Auth context: `role` defaults to empty string causing infinite re-render loop | Open |
| BUG-S3-MED-006   | Medium   | P2       | Navigation | Mobile nav menu empty when toggled on small viewport                        | Open |
| BUG-S3-LOW-007   | Low      | P3       | UI         | Brand asset: `<title>` reads `LitVue App` instead of `SporeKart`            | Open |

## New Defects — Part 3 (Platform Reliability)

| ID               | Severity | Priority | Module        | Description                                                                 | Evidence        | Status |
|------------------|----------|----------|---------------|-----------------------------------------------------------------------------|-----------------|--------|
| BUG-S3-P3-001    | High     | P1       | Observability | No monitoring, logging, or observability infrastructure implemented — all three root directories are empty placeholders (`monitoring/`, `observability/`, `logging/`) | Code review of root directories | Open |
| BUG-S3-P3-002    | High     | P1       | Security      | No security headers (CSP, HSTS, X-Frame-Options, X-Content-Type-Options) emitted by the build server | Phase 8 security header check | Open |
| BUG-S3-P3-003    | Medium   | P2       | Offline       | No Service Worker implementation — offline mode has no caching, no background sync, no push notifications | Codebase search (no SW files) | Open |
| BUG-S3-P3-004    | Medium   | P2       | Auth          | Auth client is a stub with hardcoded OTP code `123456` — no real authentication | Code review of `authClient.ts` | Open |
| BUG-S3-P3-005    | Medium   | P2       | Notifications | Two competing toast implementations exist (`ToastQueue` vs `ToastProvider`) — risk of inconsistent UX | Code review of both implementations | Open |
| BUG-S3-P3-006    | Low      | P3       | Error Handling| No app-level 404 page — SPA falls back to `index.html` for unknown routes | Phase 2 error handling test | Open |
| BUG-S3-P3-007    | Low      | P3       | Database      | JPA entities lack audit timestamps (`created_at`/`updated_at`) and soft delete support | Code review of Identity Service entities | Open |
| BUG-S3-P3-008    | Low      | P3       | Performance   | No performance budgets, Lighthouse CI, or bundle analysis tooling configured | Infrastructure assessment | Open |

## Defect Summary

| Severity | Count (Unique) | Action Required                    |
|----------|---------------|-------------------------------------|
| Critical | 1             | Must fix before any further QA     |
| High     | 4             | Must fix before RC                 |
| Medium   | 5             | Should fix before RC               |
| Low      | 5             | Nice-to-have before RC             |
| **Total**| **15**        | -                                   |

**New Part 3 defects**: 8 (BUG-S3-P3-001 through BUG-S3-P3-008)
**Carried forward**: 7 (BUG-S3-CRIT-001 through BUG-S3-LOW-007)
