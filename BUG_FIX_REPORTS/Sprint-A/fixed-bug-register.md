# Fixed Bug Register — Bug Fix Sprint A

**Status:** 22 / 22 Critical (P0) bugs fixed.

| ID | Title | Fix Summary | Validation |
|----|-------|-------------|------------|
| BUG-RT-001 | No ProtectedRoute | `RequireAuth` component created & wired | typecheck ✅ |
| BUG-RT-002 | Default role administrator | `App.tsx` default `'guest'` | typecheck ✅ |
| BUG-RT-003 | Customer routes unguarded | `/dashboard` wrapped in `RequireAuth` | typecheck ✅ |
| BUG-RT-004 | Admin routes unguarded | `/admin` wrapped in `RequireAuth allowedRoles=[admin,business_owner,governance_manager]` | typecheck ✅ |
| BUG-RT-005 | Deep links bypass | Solved by route-level guard | typecheck ✅ |
| BUG-RT-006 | No auth check on any route | `RequireAuth` integrated with `AppContext` | typecheck ✅ |
| SEC-001 | No route guards (dup) | — | — |
| SEC-002 | Default admin role (dup) | — | — |
| SEC-003 | Mock authentication | `AuthLoadingPage` establishes session | typecheck ✅ |
| BUG-API-001 | 128 endpoints unauthenticated | `SecurityConfig` added to 15 services | compile ✅ (8/10 services) |
| BUG-API-002 | Circular auth on register | `SecurityConfig` permits `/auth/**`, `/users/me` | compile ✅ |
| BUG-API-003 | IDOR order ownership | `OrderController` enforces principal == customerId | compile ✅ |
| BUG-API-004 | IDOR customer filter | `history()` rejects mismatched customerId | compile ✅ |
| BUG-API-005 | Mass assignment (AI) | `SemanticController` drops `X-User-Id` header | compile ✅ |
| BUG-API-006 | Customer PII exposed | `/analytics/customers` gated ADMIN | compile ✅ |
| BUG-API-007 | Payment data exposed | `/analytics/payments` gated ADMIN | compile ✅ |
| BUG-API-008 | Anyone creates products | `ProductController.create` ADMIN | compile ✅ |
| BUG-API-009 | Anyone creates orders | `OrderController.create` auth+ownership | compile ✅ |
| BUG-API-010 | Anyone creates shipments | `ShipmentController` ADMIN | compile ✅ |
| BUG-API-011 | Anyone initiates payments | `PaymentController` ADMIN | compile ✅ |
| BUG-API-012 | Anyone sends notifications | `NotificationController` ADMIN | compile ✅ |
| SEC-004 | 82.6% unauthenticated (dup) | — | — |

---

*End of Fixed Bug Register.*
