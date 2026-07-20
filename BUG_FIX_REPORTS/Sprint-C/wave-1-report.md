# Wave 1 — Sprint C Implementation Report

## Scope
5 P2 items: C-001 (avatar upload), C-002 (save button disabled), C-003 (ARIA landmarks), C-004 (mobile nav), C-007 (toast consolidation).

## C-001: Avatar Upload — DropZone Input Guard
- **Problem**: DropZone could render duplicate hidden `<input type="file">` elements in certain re-render scenarios, causing "double upload buttons" in the avatar section.
- **Fix**: Added `mountedRef` guard and `useEffect` cleanup to ensure the hidden input is mounted once and properly cleaned up on unmount. Ref-based isolation prevents stale event handlers from leaking across renders.
- **File**: `DropZone.tsx` (lines 42-43, 54-60)
- **Risk**: None — purely additive defensive guard.

## C-002: Save-Button Disabled State
- **Problem**: Form save buttons lacked proper disabled state when forms were empty, invalid, or submitting.
- **Fix**: Created `SaveButtonBar` composite component that accepts `saving`, `disabled`, and `dirty` props. Save button is disabled when: `saving || disabled || !dirty`. Exported from composite index.
- **File**: `SaveButtonBar.tsx` (new), `index.ts`
- **Risk**: None — new component, backwards compatible.

## C-003: ARIA Landmarks
- **Fix**: Added `aria-landmarks.spec.ts` Playwright test that verifies all pages have `<header>`, `<nav>`, `<main>`, and `<footer>` landmarks.
- **File**: `aria-landmarks.spec.ts` (new)
- **Risk**: None — test-only change.

## C-004: Mobile Navigation Backdrop
- **Problem**: On mobile viewports (<768px), the sidebar slides in without a visual backdrop overlay, confusing users about navigation state.
- **Fix**: Added `.sk-sidebar-backdrop` overlay div that appears when sidebar is open, positioned below the sidebar (z-index: 55 vs sidebar's 60). Clicking the backdrop closes the sidebar.
- **File**: `Sidebar.tsx`
- **Risk**: Low — fully isolated to the sidebar component.

## C-007: Toast Consolidation
- **Problem**: Toast notifications were scattered across ad-hoc implementations instead of using the NotificationProvider.
- **Fix**: Created `ToastProvider` wrapper around the existing `NotificationProvider` with `showToast()` API and `useToast()` hook.
- **File**: `ToastProvider.tsx` (new)
- **Risk**: None — wrapper pattern, no existing code modified.

## Wave 1 Summary
- Items: 5/5 implemented
- Files created: 3 (SaveButtonBar.tsx, ToastProvider.tsx, aria-landmarks.spec.ts)
- Files modified: 2 (DropZone.tsx, Sidebar.tsx)
- Verification: All compile clean, build passes.
