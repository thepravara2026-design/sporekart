# Wave 2 — Sprint C Implementation Report

## Scope
3 P2 items: C-005 (Service Worker), C-006 (Real auth), C-008 (404 page).

## C-005: Service Worker
- **Fix**: Created `sw.js` (ES module service worker) with cache-first strategy for static assets, stale-while-revalidate for navigation requests. Created `serviceWorkerRegistration.ts` for registration with update notifications.
- **Files**: `public/sw.js` (new), `src/serviceWorkerRegistration.ts` (new)
- **Strategy**: install → precache → activate (clean old caches) → fetch (cache with network fallback)
- **Risk**: Low — no existing code modified; registration is opt-in via explicit `register()` call.

## C-006: Real Auth (Session Management)
- **Problem**: Auth client used pure stubs without token management. Sessions were not persisted across page refreshes.
- **Fix**: Created `AuthStore` class with in-memory + sessionStorage persistence for tokens. Updated `authClient.login()` to create real tokens and store sessions. Added `logout()` method.
- **Files**: `AuthStore.ts` (new), `authClient.ts` (modified)
- **Security**: Tokens stored in sessionStorage (cleared on tab close) rather than localStorage, reducing XSS exposure.
- **Risk**: Medium — touches auth flow. All existing interfaces preserved. No redesign.

## C-008: 404 Page
- **Problem**: Minimal 404 page with only a PlaceholderPanel and no navigation options.
- **Fix**: Redesigned with large "404" hero text, clear heading, helpful description, and navigation links to Home, Dashboard, and Help Centre.
- **File**: `NotFound.tsx`
- **Risk**: None — standalone component, no routing changes.

## Wave 2 Summary
- Items: 3/3 implemented
- Files created: 2 (sw.js, serviceWorkerRegistration.ts, AuthStore.ts)
- Files modified: 2 (authClient.ts, NotFound.tsx)
- Verification: All compile clean, build passes.
