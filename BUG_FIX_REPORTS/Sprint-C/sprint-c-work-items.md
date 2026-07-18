# Sprint C Work Items — Executable Backlog

## Wave 1 — Quick Wins (Week 1)

### BUG-C-001: Avatar upload shows duplicate file inputs
| Field | Value |
|-------|-------|
| **File** | `AvatarUpload.tsx` |
| **Fix** | Remove duplicate `<input type="file">` — keep the styled wrapper |
| **Test** | Verify exactly 1 file input in avatar section |
| **Acceptance** | Single file picker button; no double-input visible |

### BUG-C-002: Notification prefs Save button stuck disabled
| Field | Value |
|-------|-------|
| **File** | `NotificationPreferences.tsx` |
| **Fix** | Reset `isLoading` to `false` after data fetch completes |
| **Test** | Submit button enabled after preferences load; disabled during save |
| **Acceptance** | Button enabled when page finishes loading; disabled during save; re-enabled after |

### BUG-C-003: Add semantic ARIA landmarks to all pages
| Field | Value |
|-------|-------|
| **Files** | `Header.tsx`, `Sidebar.tsx`, `AppLayout.tsx`, `ErrorBoundary.tsx` |
| **Fix** | Replace `<div>` with `<header>`, `<nav>`, `<main>`; add `role="alert"` to ErrorBoundary |
| **Test** | aXe scan 0 violations; `<main>`, `<nav>`, `<header>` count >= 3 |
| **Acceptance** | WCAG 2.1 SC 1.3.1 and SC 2.4.1 compliance; aXe passes |

### BUG-C-004: Mobile nav menu renders empty on small viewport
| Field | Value |
|-------|-------|
| **File** | `MobileNav.tsx` |
| **Fix** | Ensure nav items mount when toggle is activated; fix conditional render |
| **Test** | 375×667 viewport; toggle nav; verify `<nav>` contains `<li>` items |
| **Acceptance** | Mobile nav shows navigation links on toggle; all viewports functional |

### BUG-C-007: Consolidate duplicate toast implementations
| Field | Value |
|-------|-------|
| **Files** | `ToastProvider.tsx`, all consumers |
| **Fix** | Keep `ToastQueue`; migrate `ToastProvider` consumers; remove legacy provider |
| **Test** | All toast types render; no runtime errors; queue behavior works |
| **Acceptance** | Single toast system; success/error/warning/info/loading all work |

---

## Wave 2 — Infrastructure & Auth (Week 2-3)

### BUG-C-008: Add app-level 404 page
| Field | Value |
|-------|-------|
| **Files** | `App.tsx`, new `NotFoundPage.tsx` |
| **Fix** | Add `<Route path="*">` catch-all with user-friendly 404 page |
| **Test** | Random path → 404 page; "Back to home" link works |
| **Acceptance** | Unknown routes show branded 404 with navigation guidance |

### BUG-C-005: Implement Service Worker for offline capability
| Field | Value |
|-------|-------|
| **Files** | New `sw.js`, `main.tsx` registration |
| **Fix** | Workbox-based SW; cache-first for static assets; network-first for API; offline fallback |
| **Test** | Offline navigation; cached assets; SW update prompt |
| **Acceptance** | App loads offline for previously visited pages; SW lifecycle works |

### BUG-C-006: Replace stub auth client with real authentication
| Field | Value |
|-------|-------|
| **Files** | `authClient.ts`, `RequireAuth.tsx`, `context.ts`, session hooks |
| **Fix** | Integrate Supabase Auth or backend JWT; replace `sk_session_role` with real session |
| **Test** | Login/register/logout flows; token refresh; session persistence; protected routes |
| **Acceptance** | Real auth end-to-end; no hardcoded OTP; route protection enforced server-side |

---

## Wave 3 — CI Quality (Stretch — Week 3)

### BUG-C-009: Configure performance budgets and CI tooling
| Field | Value |
|-------|-------|
| **Files** | `.github/workflows/*.yml`, `vite.config.ts`, `lighthouserc.js` |
| **Fix** | Lighthouse CI integration; budget thresholds; bundle analysis |
| **Test** | PRs blocked if budgets exceeded; bundle report generated |
| **Acceptance** | CI blocks performance regressions; team can inspect bundle composition |
