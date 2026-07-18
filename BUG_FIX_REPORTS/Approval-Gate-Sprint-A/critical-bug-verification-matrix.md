# Critical Bug Verification Matrix — Approval Gate Sprint A

**Legend:** ✅ = verified (root cause identified + fixed + permanent solution, no workaround); ⚠️ = fixed but conditional (env/build dependent); ➖ = duplicate of another fix.

| ID | Root Cause Identified | Root Cause Fixed | Repro Pre-Fix (per QA) | Repro Post-Fix | Evidence | Workaround? | Verdict |
|----|----------------------|-----------------|----------------------|-----------------|----------|-------------|---------|
| BUG-RT-001 | Yes — no `ProtectedRoute` | Yes | Yes (QA RT report) | No (guard added) | `RequireAuth.tsx` + `App.tsx:499` | No | ✅ |
| BUG-RT-002 | Yes — default `administrator` | Yes | Yes | No (`guest` default) | `App.tsx:427` | No | ✅ |
| BUG-RT-003 | Yes — `/dashboard` unguarded | Yes | Yes | No (wrapped) | `App.tsx:499` | No | ✅ |
| BUG-RT-004 | Yes — `/admin` unguarded | Yes | Yes | No (wrapped + roles) | `App.tsx:545` | No | ✅ |
| BUG-RT-005 | Yes — deep-link bypass | Yes | Yes | No (route guard) | `RequireAuth.tsx` | No | ✅ |
| BUG-RT-006 | Yes — no auth on routes | Yes | Yes | No (context-integrated) | `RequireAuth.tsx` | No | ✅ |
| SEC-001 | dup RT-001 | Yes | — | — | — | — | ➖ |
| SEC-002 | dup RT-002 | Yes | — | — | — | — | ➖ |
| SEC-003 | Yes — session never established | Yes | Yes | No (session set) | `SessionPages.tsx:38-52` | No | ✅ |
| BUG-API-001 | Yes — no `SecurityConfig` | Yes | Yes | No (15 configs) | `services/*/config/SecurityConfig.java` | No | ✅ |
| BUG-API-002 | Yes — `/auth/**` blocked | Yes | Yes | No (permitAll) | `identity/.../SecurityConfig.java:30-31` | No | ✅ |
| BUG-API-003 | Yes — no order ownership | Yes | Yes | No (principal==customerId) | `OrderController.java:27-40` | No | ✅ |
| BUG-API-004 | Yes — no customer filter | Yes | Yes | No (rejected mismatch) | `OrderController.java:52-58` | No | ✅ |
| BUG-API-005 | Yes — `X-User-Id` header | Yes | Yes | No (principal from context) | `SemanticController.java:52-75` | No | ✅ |
| BUG-API-006 | Yes — PII open | Yes | Yes | No (ADMIN-gated) | `AnalyticsController.java` | No | ✅ |
| BUG-API-007 | Yes — payment open | Yes | Yes | No (ADMIN-gated) | `AnalyticsController.java` | No | ✅ |
| BUG-API-008 | Yes — anon create product | Yes | Yes | No (ADMIN) | `ProductController.java` | No | ✅ |
| BUG-API-009 | Yes — anon create order | Yes | Yes | No (auth+ownership) | `OrderController.java:42-50` | No | ✅ |
| BUG-API-010 | Yes — anon create shipment | Yes | Yes | No (ADMIN) | `ShipmentController.java` | No | ✅ |
| BUG-API-011 | Yes — anon payment | Yes | Yes | No (ADMIN) | `PaymentController.java` | No | ✅ |
| BUG-API-012 | Yes — anon notify | Yes | Yes | No (ADMIN) | `NotificationController.java` | No | ✅ |
| SEC-004 | dup API-001 | Yes | — | — | — | — | ➖ |

**Tally:** 22/22 Critical — 20 uniquely fixed (✅), 2 duplicates (➖). No workarounds. No temporary fixes.

---

*End of Critical Bug Verification Matrix.*
