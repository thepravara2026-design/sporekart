# Sprint 21 — Part 1: Public Website Foundation Architecture

**Phase:** 6 — Public Website
**Part:** 1
**Status:** ✅ Complete (2026-07-13)
**Dependencies:** Design System v1.0.0 (frozen, Phase 5 complete)

## Goal
Deliver the reusable public website foundation: shell, header, footer, SEO, content
container, responsive grid, breadcrumb foundation, and placeholder routes + preview
environment. No page content.

## What Was Built
- `src/public-website/` — config, layout, header, footer, nav, content container,
  announcement, breadcrumb, SEO, section foundations, route placeholders, previews, barrel.
- `src/App.tsx` — non-enterprise route branch (`isNonEnterpriseRoute`) so public + preview
  routes render without the enterprise shell; 13 public + 5 preview lazy routes.
- `docs/public-website/` — 8 documentation files.

## Verification
- `tsc --noEmit`: 0 errors.
- `vite build`: success (~2.7s).

## Out of Scope (later parts)
- Hero / landing content, About copy, Blog listing + posts, Contact form, Auth UI.
- Sitemap / robots.txt, i18n, analytics wiring.
- Consolidation of the legacy `public` workspace in `navigation.ts`.

## Sign-off
- Architecture lead: approved
- Accessibility: foundational semantics in place; focus-visible ring deferred
- Next: Part 2 — Home & Landing page content on top of this foundation.
