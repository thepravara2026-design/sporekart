# Phase 6 — Sprint 21 Part 4: Public Website Inner Pages

**Status:** COMPLETE (awaiting approval)
**Branch:** `sporetest`
**Date:** 2026-07-13
**Depends on:** Sprint 21 Part 3 (Homepage) — APPROVED, committed

## Summary
Replaced the public route placeholders (except `/auth`) with real, content-rich
inner pages, completing the public website’s primary page set. All work reuses
Design System v1.0.0, the Part 1 foundation, and the Part 3 motion helpers.
No backend/API changes.

## Deliverables
- `src/public-website/pages/PageShell.tsx` — shared `PageHeader`, `SectionHeading`, `Prose`, `CtaBanner`.
- `src/public-website/pages/AboutPage.tsx`
- `src/public-website/pages/ProductsPage.tsx`
- `src/public-website/pages/TrainingPage.tsx`
- `src/public-website/pages/BlogPage.tsx`
- `src/public-website/pages/ContactPage.tsx` (UI-only form)
- `src/public-website/pages/FaqPage.tsx` (reuses `FAQ_ITEMS`)
- `src/public-website/pages/CertificationsPage.tsx`
- `src/public-website/pages/LegalPage.tsx` (data-driven for 4 policies)
- `src/public-website/preview/InnerPagesPreview.tsx` (`/preview/inner-pages`)
- `src/App.tsx` — routes wired to real pages; `/auth` remains placeholder.

## Verification
- `npx tsc --noEmit`: 0 errors.
- `npm run build`: success (per-page chunks code-split).
- Review: http://localhost:5173/<route> and `/preview/inner-pages`.

## Notes
- Placeholders clearly marked; no fake statistics or fabricated legal claims.
- Forms are UI-only (no submission).
- `/auth` intentionally left as placeholder (auth requires backend).
- **Pending:** real catalog/blog/course/team data, legal copy review, auth flow.
