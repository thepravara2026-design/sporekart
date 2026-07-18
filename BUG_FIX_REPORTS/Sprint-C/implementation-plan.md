# Implementation Plan — Sprint C

## Execution Strategy

1. **Fix the build crash first** (BUG-S3-CRIT-001) as a pre-Sprint-C or parallel P1 task. Without it, 4 of 9 items cannot be visually verified.
2. Execute Sprint C in 3 waves over 3 weeks.
3. Each wave ends with a QA validation gate.

---

## Wave 1: Quick Wins (Week 1)
**Focus**: Low risk, high value, independent items  
**Total**: 39h engineering + 13h QA = 52h

### Day 1-2: BUG-C-002 (Save button disabled — 3h eng)
| | Detail |
|---|--------|
| **Fix** | Locate `NotificationPreferences.tsx`. Find the loading state boolean that controls the Save button `disabled` attribute. Ensure `isLoading` resets to `false` after data fetch completes (success or error). |
| **Files** | `frontend/web-app/src/features/customer/engagement/NotificationPreferences.tsx` |
| **Test** | Submit button becomes enabled after preferences load. Verify disabled state during load and enabled after. |
| **QA** | 2h — Playwright test: load page, verify button enabled, verify button disabled during save |

### Day 2-3: BUG-C-001 (Avatar double upload — 4h eng)
| | Detail |
|---|--------|
| **Fix** | Locate the avatar upload component. Identify which `<input type="file">` is the wrapper and which is the native. Remove the duplicate. Keep the styled wrapper's click handler pointing to the hidden native input. |
| **Files** | `frontend/web-app/src/features/customer/profile/AvatarUpload.tsx` |
| **Test** | Count `<input type="file">` elements in the avatar section. Verify exactly 1. |
| **QA** | 2h — Playwright test: navigate to profile, assert single file input |

### Day 3-5: BUG-C-004 (Mobile nav — 6h eng)
| | Detail |
|---|--------|
| **Fix** | Locate the mobile navigation toggle component. Identify why nav items are not mounting when the toggle opens. Likely a conditional render issue or state initialization problem. Ensure nav items render on toggle. |
| **Files** | `frontend/web-app/src/layout/nav/MobileNav.tsx` |
| **Test** | Set viewport to 375×667. Toggle nav. Verify `<nav>` contains list items. |
| **QA** | 3h — Playwright responsive test: mobile viewport, toggle nav, assert items present |

### Day 5-7: BUG-C-003 (ARIA landmarks — 8h eng)
| | Detail |
|---|--------|
| **Fix** | Update `AppLayout.tsx`, `Header.tsx`, `Sidebar.tsx` to use semantic HTML: `<header>` instead of `<div id="header">`, `<nav>` instead of `<div class="sidebar">`, `<main>` instead of `<div id="content">`. Add `role="banner"`, `role="navigation"`, `role="main"` as appropriate. |
| **Files** | Layout components in `frontend/web-app/src/layout/` |
| **Test** | aXe scan on every route. Verify `<main>`, `<nav>`, `<header>` count >= 3. |
| **QA** | 3h — aXe scans on 5+ routes; keyboard navigation test |

### Day 5-7 (parallel): BUG-C-007 (Duplicate toasts — 8h eng)
| | Detail |
|---|--------|
| **Fix** | Audition all consumers of `ToastProvider` and `useToast` vs `ToastQueue` and `useToastQueue`. Choose `ToastQueue` as the canonical implementation (more features, accessibility-aware). Migrate all consumers. Remove `ToastProvider`. |
| **Files** | `frontend/web-app/src/design-system/providers/ToastProvider.tsx`, all files importing it |
| **Test** | All toast types render. No runtime errors. |
| **QA** | 3h — Verify success, error, warning, info, loading toasts; verify queue behavior; verify no console errors |

---

## Wave 2: Infrastructure & Auth (Week 2-3)
**Focus**: Higher effort, operational improvements  
**Total**: 48h engineering + 16h QA = 64h

### Week 2: BUG-C-008 (404 page — 4h eng)
| | Detail |
|---|--------|
| **Fix** | Add a catch-all `<Route path="*">` to the router in `App.tsx`. Create a `NotFoundPage` component with "Page not found" guidance and a "Back to home" link. |
| **Files** | `frontend/web-app/src/App.tsx`, new `NotFoundPage.tsx` |
| **Test** | Navigate to random path. Verify 404 page renders. |
| **QA** | 2h — Playwright test: random path, verify 404 content |

### Week 2-3: BUG-C-005 (Service Worker — 20h eng)
| | Detail |
|---|--------|
| **Fix** | Implement a Service Worker using Workbox (or manual). Register in `index.html` or `main.tsx`. Cache strategy: static assets (JS/CSS/images) cache-first; API calls network-first. Add offline fallback page. |
| **Files** | New `frontend/web-app/public/sw.js`, registration in `main.tsx` |
| **Test** | Offline navigation works. Cached assets serve without network. Updates prompt correctly. |
| **QA** | 6h — Full offline test suite: go offline, navigate, verify cached content; SW update flow |

### Week 2-3: BUG-C-006 (Auth client — 24h eng)
| | Detail |
|---|--------|
| **Fix** | Replace the stub auth client in `authClient.ts` with real Supabase Auth or backend JWT authentication. Wire up: login, register, forgot-password, OTP verification, token refresh. Update session management to use real JWT tokens instead of `sk_session_role` in sessionStorage. |
| **Files** | `frontend/web-app/src/features/auth/authClient.ts`, `RequireAuth.tsx`, `context.ts`, session hooks |
| **Test** | Complete login flow. Token refresh. Expired token → re-login. Logout → clear session. Protected routes enforce auth. |
| **QA** | 8h — E2E auth flows; session timeout; multi-tab sync; protected route access |

---

## Wave 3: CI Quality (Stretch — Week 3)
**Focus**: Developer tooling, quality gates  
**Total**: 12h engineering + 4h QA = 16h

### Week 3: BUG-C-009 (Performance budgets — 12h eng)
| | Detail |
|---|--------|
| **Fix** | Configure Lighthouse CI in the GitHub Actions pipeline. Set performance budgets: bundle < 300KB gzip, DCL < 2s, FCP < 1.5s. Add `vite-bundle-analyzer` for bundle inspection. |
| **Files** | `.github/workflows/*.yml`, `vite.config.ts`, new `lighthouserc.js` |
| **Test** | CI pipeline runs Lighthouse on PR. Budgets enforced. Bundle report generated. |
| **QA** | 4h — Verify CI gates block regressions; verify bundle report accuracy |

---

## Rollback Plan

| Item | Rollback Strategy |
|------|-------------------|
| BUG-C-001 through C-004, C-007, C-008 | Revert specific file changes; Git revert. All are isolated component changes. |
| BUG-C-005 | Unregister SW: `navigator.serviceWorker.getRegistrations().then(r => r.forEach(r => r.unregister()))` |
| BUG-C-006 | Revert to stub auth client; restore sessionStorage role management |
| BUG-C-009 | Revert CI config changes; remove Lighthouse step |

## Verification Gates

| Gate | Criteria | Owner |
|------|----------|-------|
| **G1** — Build Fix | Production build runs without errors; login page renders | Front-end lead |
| **G2** — Wave 1 Complete | All 5 items merged; QA validated; no regressions | SDET |
| **G3** — Wave 2 Complete | Auth, SW, 404 shipped; E2E tests pass | QA lead |
| **G4** — Sprint C Complete | All 9 items verified; CI gates green; no P1 regressions | Release manager |
