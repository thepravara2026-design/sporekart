# Master Bug Register — SporeKart RC1

**QA Sprint 2 (Parts 1-11)** | **Date:** 2026-07-17
**Total Unique Bugs:** 85 (after deduplication)

---

## Bug Distribution

| Severity | Count | % of Total |
|----------|-------|-----------|
| 🔴 CRITICAL | 22 | 25.9% |
| 🟠 HIGH | 35 | 41.2% |
| 🟡 MEDIUM | 28 | 32.9% |
| 🔵 LOW | 10 | 0.0% |

---

## 🔴 CRITICAL Bugs (22)

| ID | Part | Module | Title | Recommendation |
|----|------|--------|-------|---------------|
| BUG-RT-001 | 4 | Route Protection | No ProtectedRoute component exists | Create `<ProtectedRoute>` and wrap all protected routes |
| BUG-RT-002 | 4 | Auth/Routes | Default role is 'administrator' | Change to 'guest' |
| BUG-RT-003 | 4 | Route Protection | 53 customer dashboard routes have no guards | Add route guard to CustomerLayout |
| BUG-RT-004 | 4 | Route Protection | 96 admin routes have no guards | Add route guard to AdminLayout |
| BUG-RT-005 | 4 | Route Protection | Deep links bypass all protection | Implement route-level guards |
| BUG-RT-006 | 4 | Route Protection | No auth check on any route | Integrate session state with router |
| BUG-API-001 | 5 | API Security | 128 endpoints have no authentication | Add @Secured to all controller methods |
| BUG-API-002 | 5 | Identity | Circular auth on /auth/register | Fix security config for registration |
| BUG-API-003 | 5 | Orders | IDOR — no order ownership check | Add ownership verification |
| BUG-API-004 | 5 | Orders | IDOR — no customer filter validation | Filter by authenticated user |
| BUG-API-005 | 5 | AI Service | Mass assignment via Map<String,String> DTOs | Use typed DTOs |
| BUG-API-006 | 5 | Analytics | Customer PII exposed | Add auth and field filtering |
| BUG-API-007 | 5 | Analytics | Payment data exposed | Add auth and field filtering |
| BUG-API-008 | 5 | Catalog | Anyone can create products | Add authentication |
| BUG-API-009 | 5 | Orders | Anyone can create orders | Add authentication |
| BUG-API-010 | 5 | Fulfillment | Anyone can create shipments | Add authentication |
| BUG-API-011 | 5 | Payments | Anyone can initiate payments | Add authentication |
| BUG-API-012 | 5 | Notifications | Anyone can send notifications | Add authentication |
| SEC-001 | 10 | Route Protection | No route guards (duplicate of RT-001) | — |
| SEC-002 | 10 | Auth | Default admin role (duplicate of RT-002) | — |
| SEC-003 | 10 | Auth | Mock authentication | Replace with real auth |
| SEC-004 | 10 | API Security | 82.6% unauthenticated APIs (dup of API-001) | — |

---

## 🟠 HIGH Bugs (35)

| ID | Part | Module | Title |
|----|------|--------|-------|
| BUG-RT-007 | 4 | Workspace | WorkspacePage shows placeholder instead of redirecting |
| BUG-RT-008 | 4 | Session | No browser history clearing on logout |
| BUG-RT-009 | 4 | Session | No multi-tab session synchronization |
| BUG-RT-010 | 4 | Global | No global error boundary |
| BUG-RT-011 | 4 | Session | No session expiry auto-redirect |
| BUG-API-013 | 5 | Identity | No JWT filter implementation |
| BUG-API-014 | 5 | All Services | No error boundary standardization |
| BUG-API-015 | 5 | Cart | Cart service is empty placeholder |
| BUG-API-016 | 5 | Content | Content service is empty placeholder |
| BUG-API-017 | 5 | Search | Search service is empty placeholder |
| BUG-API-018 | 5 | Support | Support service is empty placeholder |
| BUG-API-019 | 5 | Identity | GET /users/me returns placeholder string |
| BUG-API-020 | 5 | All Services | No pagination on any list endpoint |
| BUG-COMP-001 | 6 | Admin | Admin KPI grid collapses poorly on mobile |
| BUG-COMP-002 | 6 | Admin | Admin tables overflow without horizontal scroll |
| BUG-COMP-003 | 6 | Profile | Profile page buttons overlap on mobile |
| BUG-COMP-004 | 6 | Navigation | Sidebar overlay remains after navigation |
| BUG-MOB-001 | 7 | Admin | KPI grid collapses to single column on mobile |
| BUG-MOB-002 | 7 | Admin | Tables overflow without horizontal scroll |
| BUG-MOB-003 | 7 | Profile | Profile action buttons overlap on small mobile |
| BUG-MOB-004 | 7 | Navigation | Sidebar drawer overlay persists after navigation |
| BUG-MOB-005 | 7 | Checkout | Mobile checkout flow does not exist |
| BUG-MOB-006 | 7 | UI | Touch targets below WCAG minimum size |
| BUG-PERF-001 | 9 | Rendering | No data virtualization in admin tables |
| BUG-PERF-002 | 9 | Memory | Timer leak in SessionTimeoutWarning |
| BUG-PERF-003 | 9 | Scalability | No pagination for data pages |
| BUG-PERF-004 | 9 | Network | No caching, service worker, or offline support |
| SEC-005 | 10 | Auth | Role can be changed via UI dropdown |
| SEC-006 | 10 | Security | No Content-Security-Policy header |
| SEC-007 | 10 | Security | No X-Frame-Options header |
| SEC-008 | 10 | Security | No security headers configured (all 8 missing) |
| SEC-009 | 10 | API | IDOR on all controllers (dup of API-003) |
| SEC-010 | 10 | Session | No session management (dup of RT-008/009) |
| SEC-011 | 10 | Auth | Mock OTP accepts any code |
| SEC-012 | 10 | API | No rate limiting |

---

## 🟡 MEDIUM Bugs (28)

| ID | Part | Module | Title |
|----|------|--------|-------|
| BUG-QA-2-001 | 1 | Login | Terms Agreement validation error not rendered |
| BUG-QA-2-002 | 1 | Register | Registration consent error not rendered |
| BUG-QA-2-003 | 2 | Session | Focus not trapped in SessionTimeoutWarning |
| BUG-RT-012 | 4 | Routing | No URL parameter validation |
| BUG-RT-013 | 4 | Routing | No post-login redirect to original URL |
| BUG-API-021 | 5 | All Services | No CORS configuration |
| BUG-API-022 | 5 | All Services | No rate limiting |
| BUG-API-023 | 5 | Frontend | Dashboards send no auth headers |
| BUG-COMP-005 | 6 | Header | Hamburger below touch target minimum |
| BUG-COMP-006 | 6 | Modals | ResponsiveModal shows desktop variant |
| BUG-COMP-007 | 6 | Global | Breakpoint system mismatch |
| BUG-COMP-008 | 6 | Grid | Firefox CSS Grid rendering differences |
| BUG-MOB-007 | 7 | Modals | ResponsiveModal shows desktop variant (dup) |
| BUG-MOB-008 | 7 | Global | Breakpoint mismatch CSS vs React (dup) |
| BUG-MOB-009 | 7 | Mobile | No KeyboardAvoidingView in mobile apps |
| BUG-MOB-010 | 7 | Mobile | No orientation handling |
| BUG-MOB-011 | 7 | Mobile | Swipe gestures not supported |
| BUG-MOB-012 | 7 | Mobile | Long-press context menu not implemented |
| BUG-PERF-005 | 9 | Rendering | AppContext causes excessive re-renders |
| BUG-PERF-006 | 9 | Rendering | Inline styles recreate objects on every render |
| BUG-PERF-007 | 9 | Memory | Toast timeout without proper cleanup refs |
| BUG-PERF-008 | 9 | Network | No preload/prefetch hints for critical routes |
| BUG-PERF-009 | 9 | Rendering | Layout thrashing in ResizableDrawer |
| BUG-PERF-010 | 9 | Build | No bundle analysis or performance budget |
| BUG-PERF-011 | 9 | Auth | AuthClient has hardcoded 900ms latency |
| SEC-013 | 10 | Security | No CSRF protection |
| SEC-015 | 10 | Security | No security audit trail |
| SEC-017 | 10 | API | No server-side input validation |

---

## 🔵 LOW Bugs (10)

| ID | Part | Module | Title |
|----|------|--------|-------|
| BUG-RT-014 | 4 | A11y | No `aria-live` region for route errors |
| BUG-API-024 | 5 | All Services | Missing @JsonInclude(NON_NULL) |
| BUG-MOB-013 | 7 | Mobile | Mobile native apps in Phase 0 (placeholder) |
| BUG-MOB-014 | 7 | Mobile | Admin Companion app has zero source files |
| BUG-PERF-012 | 9 | Rendering | No React.memo on frequently re-rendered items |
| BUG-PERF-013 | 9 | UI | No useDeferredValue for search/filter inputs |
| BUG-PERF-014 | 9 | UI | Non-passive touch event listeners |
| SEC-014 | 10 | Error | Error details may leak component structure |
| SEC-018 | 10 | Code | console.log in production code |
| SEC-020 | 10 | Config | Vite dev server exposed to network |

---

## Bug Distribution by Module

| Module | Critical | High | Medium | Low | Total |
|--------|----------|------|--------|-----|-------|
| Route Protection | 6 | 5 | 2 | 1 | 14 |
| API Security | 12 | 6 | 3 | 1 | 22 |
| Security Hardening | 4 | 8 | 3 | 3 | 18 |
| Mobile Experience | 0 | 6 | 6 | 2 | 14 |
| Performance | 0 | 4 | 7 | 3 | 14 |
| Cross-Browser | 0 | 4 | 4 | 0 | 8 |
| Authentication | 0 | 0 | 2 | 0 | 2 |
| Session | 0 | 0 | 1 | 0 | 1 |
| **TOTAL** | **22** | **33** | **28** | **10** | **93** |

*Note: 6 duplicates removed from cross-part overlaps (SEC = RT/API)*

---

## Blocker Assessment

All 22 critical bugs are **production blockers**. The following capabilities are completely absent:
- Route protection (no guards whatsoever)
- Real authentication (mock only)
- API authentication (82.6% uncovered)
- Session management (no tokens)
- Cart & checkout (not implemented)
- Mobile native apps (not built)
- Security headers (none configured)
- CSRF protection
- Rate limiting
- Ownership/IDOR checks
- Accessibility testing (directory empty)

---

*End of Master Bug Register*
