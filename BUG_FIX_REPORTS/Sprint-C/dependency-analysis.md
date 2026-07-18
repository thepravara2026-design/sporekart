# Dependency Analysis — Sprint C

## Bug Dependency Graph

```
BUG-C-006 (Auth stub)
  └── BLOCKED BY: BUG-S3-CRIT-001 (build crash — login page renders ErrorBoundary)
  └── BLOCKED BY: BUG-S3-HIGH-005 (auth context infinite loop)

BUG-C-005 (Service Worker)
  └── NO BLOCKERS — independent of build crash

BUG-C-003 (ARIA landmarks)
  └── PARTIAL BLOCKER: Build crash (BUG-S3-CRIT-001) prevents verifying landmarks on complex pages
  └── Can implement on layout shell without build fix

BUG-C-004 (Mobile nav)
  └── PARTIAL BLOCKER: Build crash prevents nav component rendering
  └── Can review code and fix in source

BUG-C-001 (Avatar upload)
  └── BLOCKED BY: BUG-S3-CRIT-001 (account profile page crashes)

BUG-C-002 (Save button)
  └── BLOCKED BY: BUG-S3-CRIT-001 (notification prefs page crashes)

BUG-C-007 (Duplicate toasts)
  └── NO BLOCKERS — design system code is independent

BUG-C-008 (404 page)
  └── NO BLOCKERS — router-level change, no shared component dependency

BUG-C-009 (Perf budgets)
  └── NO BLOCKERS — CI config only
```

## Shared Component Dependencies

| Component | Used By | Crash Impact |
|-----------|---------|-------------|
| Shared component library (CSS-in-JS) | All routes | All routes crash (BUG-S3-CRIT-001) |
| AccountLayout | Profile, Orders, Wishlist, Addresses | Blocks BUG-C-001, BUG-C-002 |
| Navigation / Sidebar | All protected routes | Blocks BUG-C-004 |
| Design System Toasts | All interactive pages | BUG-C-007 is code-level, not render-level |
| Router | All routes | BUG-C-008 is router-level, no render dep |

## Authentication Dependencies

| Item | Auth Required | Notes |
|------|-------------|-------|
| BUG-C-001 | Yes | Profile page requires authenticated session |
| BUG-C-002 | Yes | Notification prefs require authenticated session |
| BUG-C-004 | No | Nav is part of app shell |
| BUG-C-003 | No | Layout-level change |
| BUG-C-005 | No | Service Worker registration in index.html |
| BUG-C-006 | Yes | This IS the auth fix |
| BUG-C-007 | No | Design system component |
| BUG-C-008 | No | Router-level |
| BUG-C-009 | No | CI config |

## Database Dependencies

| Item | DB Required | Notes |
|------|------------|-------|
| BUG-C-001 | No | Frontend-only (duplicate DOM elements) |
| BUG-C-002 | No | Frontend-only (disabled button state) |
| BUG-C-003 | No | Layout component change |
| BUG-C-004 | No | Navigation component change |
| BUG-C-005 | No | Service Worker registration |
| BUG-C-006 | Partial | Auth client stub → real auth needs backend Identity Service |
| BUG-C-007 | No | Design system |
| BUG-C-008 | No | Router config |
| BUG-C-009 | No | CI config |

## Testing Dependencies

| Item | Can Test Standalone | Requires Build Fix | Requires Auth |
|------|-------------------|-------------------|---------------|
| BUG-C-001 | No (profile crashes) | Yes | Yes |
| BUG-C-002 | No (notifications crash) | Yes | Yes |
| BUG-C-003 | Partial (homepage works) | Yes for full coverage | No |
| BUG-C-004 | No (nav crashes) | Yes | No |
| BUG-C-005 | Yes (SW registration) | No | No |
| BUG-C-006 | Yes (code review + mock) | No | N/A |
| BUG-C-007 | Yes (design system) | No | No |
| BUG-C-008 | Yes (router test) | No | No |
| BUG-C-009 | Yes (CI config) | No | No |

## Key Finding

**BUG-S3-CRIT-001 (build crash) is the critical dependency.** 4 of 9 Sprint C items (BUG-C-001, BUG-C-002, BUG-C-003 partially, BUG-C-004) cannot be visually verified until the build crash is fixed. The remaining 5 items can be implemented and tested independently.
