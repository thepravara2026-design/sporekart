# Sprint C — Change Log

## Files Created

### `frontend/web-app/public/sw.js`
Service Worker with cache-first strategy. Pre-caches `/` and `/index.html`. Cleans old caches on activate.

### `frontend/web-app/src/serviceWorkerRegistration.ts`
SW registration module with update detection and offline fallback.

### `frontend/web-app/src/features/auth/AuthStore.ts`
Token/session management class. Persists session in sessionStorage with expiry validation.

### `frontend/web-app/src/design-system/components/composite/SaveButtonBar.tsx`
Form save button bar with wired disabled, saving, and dirty states.

### `frontend/web-app/src/design-system/providers/ToastProvider.tsx`
Toast notification provider wrapping the existing NotificationProvider.

### `frontend/web-app/lighthouserc.json`
Lighthouse CI configuration with performance budgets for 4 key routes.

### `shared-testing/tests/aria-landmarks.spec.ts`
Playwright test verifying ARIA landmark presence on all major routes.

### `QA_REPORTS/Sprint-03/sprint-c-validation.spec.ts`
Cross-cutting Playwright validation spec for all 9 Sprint C items.

## Files Modified

### `frontend/web-app/src/design-system/components/composite/DropZone.tsx`
Added `mountedRef` and `useEffect` to guard against duplicate hidden file inputs.

### `frontend/web-app/src/components/layout/Sidebar.tsx`
Added semi-transparent backdrop overlay div for mobile sidebar.

### `frontend/web-app/src/features/auth/authClient.ts`
Integrated AuthStore for session persistence. Added `logout()` method.

### `frontend/web-app/src/pages/NotFound.tsx`
Redesigned with 404 hero, descriptive text, and navigation links.

### `frontend/web-app/src/design-system/components/composite/index.ts`
Added SaveButtonBar export.
