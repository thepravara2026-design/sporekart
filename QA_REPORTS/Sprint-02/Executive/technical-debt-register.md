# Technical Debt Register — SporeKart RC1

**QA Sprint 2 — Part 12** | **Date:** 2026-07-17

---

## Classification

| Category | Complexity | Risk | Priority |
|----------|-----------|------|----------|
| 🔴 CRITICAL | High complexity, high impact | Blocks production deployment | P0 |
| 🟠 HIGH | Medium complexity, high impact | Significant architectural concern | P1 |
| 🟡 MEDIUM | Medium complexity, medium impact | Should be addressed soon | P2 |
| 🔵 LOW | Low complexity, low impact | Nice to have | P3 |

---

## 🔴 CRITICAL Technical Debt

| # | Item | Complexity | Risk | Effort | Priority |
|---|------|-----------|------|--------|----------|
| TD-001 | No authentication boundary — mock auth with any phone/OTP accepted | High | Security: CRITICAL | 5 days | P0 |
| TD-002 | No route guard infrastructure — 256 protected routes are unprotected | Medium | Security: CRITICAL | 3 days | P0 |
| TD-003 | 128/155 API endpoints use `permitAll()` — no security config | High | Security: CRITICAL | 5 days | P0 |
| TD-004 | Default role is `administrator` in `useState` | Low | Security: CRITICAL | 1 hour | P0 |
| TD-005 | No session management — no JWT, no tokens, no expiry | High | Security: CRITICAL | 3 days | P0 |
| TD-006 | Role is client-side state switchable via UI dropdown | Low | Security: CRITICAL | 1 day | P0 |
| TD-007 | No security headers — CSP, HSTS, XFO, etc. all missing | Medium | Security: HIGH | 1 day | P0 |
| TD-008 | IDOR on all controllers — no ownership verification | High | Security: CRITICAL | 5 days | P0 |
| TD-009 | 6 of 16 microservices have zero controller implementation | High | Functional: BLOCKER | 20 days | P0 |
| TD-010 | Cart service is empty — core e-commerce flow absent | High | Functional: BLOCKER | 15 days | P0 |
| TD-011 | Mass assignment via `Map<String,String>` DTOs in ai-service | Medium | Security: CRITICAL | 3 days | P0 |

---

## 🟠 HIGH Technical Debt

| # | Item | Complexity | Risk | Effort | Priority |
|---|------|-----------|------|--------|----------|
| TD-012 | No error boundary — React crash = white screen | Low | Reliability: HIGH | 1 day | P1 |
| TD-013 | No pagination on any data page — unbounded DOM | Medium | Performance: HIGH | 3 days | P1 |
| TD-014 | No data virtualization in admin tables | Medium | Performance: HIGH | 3 days | P1 |
| TD-015 | Timer leak in SessionTimeoutWarning — no cleanup | Low | Memory: HIGH | 1 hour | P1 |
| TD-016 | No service worker or offline support | Medium | Performance: HIGH | 3 days | P1 |
| TD-017 | Toast timer leaks across 15+ components | Medium | Memory: HIGH | 2 days | P1 |
| TD-018 | No JWT filter implementation despite JWT secret configured | Medium | Security: HIGH | 2 days | P1 |
| TD-019 | WorkspacePage shows placeholder instead of redirecting | Low | UX: HIGH | 1 day | P1 |
| TD-020 | Admin tables overflow on mobile — no horizontal scroll | Low | UX: HIGH | 1 day | P1 |
| TD-021 | Sidebar overlay persists after mobile navigation | Medium | UX: HIGH | 1 day | P1 |
| TD-022 | Mobile touch targets below WCAG 2.5.8 minimum (36px vs 44px) | Low | A11y: HIGH | 1 day | P1 |
| TD-023 | Checkout flow does not exist on any platform | High | Functional: HIGH | 15 days | P1 |
| TD-024 | No CORS configuration on any service | Low | Security: HIGH | 1 day | P1 |
| TD-025 | No rate limiting on any endpoint | Medium | Security: HIGH | 2 days | P1 |
| TD-026 | No CSRF protection | Medium | Security: HIGH | 2 days | P1 |

---

## 🟡 MEDIUM Technical Debt

| # | Item | Complexity | Risk | Effort | Priority |
|---|------|-----------|------|--------|----------|
| TD-027 | AppContext combines UI + Auth state — unnecessary re-renders | Medium | Performance: MED | 2 days | P2 |
| TD-028 | 200+ inline style objects recreated per render | Medium | Performance: MED | 3 days | P2 |
| TD-029 | Breakpoint system mismatch — CSS 480px vs React 479px | Low | UX: MED | 1 day | P2 |
| TD-030 | No KeyboardAvoidingView in mobile apps | Low | UX: MED | 1 day | P2 |
| TD-031 | No orientation handling in mobile or web | Low | UX: MED | 2 days | P2 |
| TD-032 | No preload/prefetch hints for critical routes | Low | Performance: MED | 1 day | P2 |
| TD-033 | Layout thrashing in ResizableDrawer | Medium | Performance: MED | 2 days | P2 |
| TD-034 | No bundle analysis or performance budget | Low | Monitoring: MED | 1 day | P2 |
| TD-035 | AuthClient has hardcoded 900ms latency | Low | Performance: MED | 1 hour | P2 |
| TD-036 | No post-login redirect to original URL | Low | UX: MED | 1 day | P2 |
| TD-037 | No URL parameter validation on dynamic routes | Low | Security: MED | 1 day | P2 |

---

## 🔵 LOW Technical Debt

| # | Item | Complexity | Risk | Effort | Priority |
|---|------|-----------|------|--------|----------|
| TD-038 | Missing @JsonInclude(NON_NULL) on response DTOs | Low | Quality: LOW | 1 day | P3 |
| TD-039 | No React.memo on frequently re-rendered list items | Low | Performance: LOW | 1 day | P3 |
| TD-040 | No useDeferredValue for search/filter inputs | Low | Performance: LOW | 1 day | P3 |
| TD-041 | Non-passive touch event listeners in ResizableDrawer | Low | Performance: LOW | 1 hour | P3 |
| TD-042 | No `aria-live` region for route errors | Low | A11y: LOW | 1 hour | P3 |
| TD-043 | console.log statements in production code | Low | Quality: LOW | 1 hour | P3 |
| TD-044 | Vite dev server exposed with host:true | Low | Security: LOW | 1 hour | P3 |

---

## Summary

| Category | Count | Est. Total Effort |
|----------|-------|-------------------|
| 🔴 CRITICAL | 11 items | 61+ days |
| 🟠 HIGH | 15 items | 35+ days |
| 🟡 MEDIUM | 11 items | 16+ days |
| 🔵 LOW | 7 items | 4+ days |
| **TOTAL** | **44 items** | **~116+ days** |

---

*End of Technical Debt Register*
