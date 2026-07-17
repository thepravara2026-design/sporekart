# Remaining Defects Summary — Bug Fix Sprint B

**Branch:** `bugfix/sprint-b-high-priority` · **Date:** 2026-07-17

---

## 1. Status after Sprint B
- **P0 (Critical):** 0 remaining (resolved in Sprint A).
- **P1 (High) in scope:** 16/16 resolved.
- **P1 (High) deferred:** backend foundation & platform items below.

## 2. Deferred P1 defects (require dedicated sprints / platform work)
| ID | Title | Reason deferred |
|----|-------|----------------|
| BUG-API-013 | No JWT filter implementation | Backend IdP integration; out of P1 UI sprint |
| BUG-API-014 | No error boundary standardization (backend) | Platform-wide; not stabilizable here |
| BUG-API-015..018 | Cart/Content/Search/Support services empty placeholders | Phase 0 foundation; business implementation deferred |
| BUG-API-019 | GET /users/me returns placeholder string | Backend service build-out |
| BUG-API-020 | No pagination on list endpoints | Backend foundation |
| BUG-COMP/MOC-* | (responsive subset) | Addressed where frontend; remaining are native-mobile PWA gaps |
| SEC-006..008 | Security headers (CSP, X-Frame-Options, 8 headers) | Gateway/platform configuration |
| SEC-009 | IDOR on controllers (dup API-003) | Resolved in Sprint A for orders; other services deferred |
| SEC-010 | No session management (dup RT-008/009) | Resolved in Sprint B front-end |
| SEC-011 | Mock OTP accepts any code | ✅ RESOLVED in Sprint B |
| SEC-012 | No rate limiting | API gateway work |

## 3. Pre-existing build blockers (tracked)
- `ai-service`: deeper compile errors (missing imports, value-classes, unimplemented interface methods) beyond the Lombok/pom issue. Requires a dedicated foundation sprint; **not** a P1 stabilization task.
- `inventory-service`: ✅ RESOLVED in Sprint B (`getId()`).

## 4. Repository cleanliness note
An untracked `ai-service/.../config/SecurityConfig.java` artifact predates this sprint (observed during the gate's repository audit). It is unrelated to Sprint B and was left untouched; recommend a separate cleanup ticket.

---

*End of Remaining Defects Summary.*
