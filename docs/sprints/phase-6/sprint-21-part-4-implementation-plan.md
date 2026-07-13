# Sprint 21 — Part 4: Public Website Inner Pages — Implementation Plan

**Branch:** `sporetest`
**Depends on:** Sprint 21 Part 3 (Homepage) — APPROVED, committed
**Reuses:** Design System v1.0.0, Part 1 foundation (`PublicLayout`, `Seo`, `PublicContentContainer`), Part 3 motion helpers (`Reveal`, `MediaPlaceholder`)
**Constraint:** No backend/API changes. Pages are content/markup only; data-bound lists are placeholders.

## Scope
Replace the 12 public route placeholders (except `/` and `/auth`) with real, content-rich
inner pages. `/auth` stays a placeholder (auth requires backend — out of scope).

| Route | Page | Notes |
|-------|------|-------|
| `/about` | AboutPage | Mission, story, values, team (placeholders) |
| `/products` | ProductsPage | Category grid (reuse homepage categories) + CTA |
| `/training` | TrainingPage | Course/program list + benefits |
| `/blog` | BlogPage | Article cards (placeholders) |
| `/contact` | ContactPage | UI-only contact form + info + map placeholder |
| `/faq` | FaqPage | Full FAQ accordion (reuse FAQ_ITEMS + more) |
| `/certifications` | CertificationsPage | Certification/compliance cards |
| `/privacy-policy` | LegalPage | Static policy content |
| `/terms-and-conditions` | LegalPage | Static policy content |
| `/refund-policy` | LegalPage | Static policy content |
| `/shipping-policy` | LegalPage | Static policy content |
| `/auth` | (keep placeholder) | Auth = backend scope |

## Shared building blocks
- `src/public-website/pages/PageShell.tsx`: `PageHeader` (eyebrow + H1 + intro + breadcrumb), `SectionHeading`, `Prose` wrappers.
- Motion: wrap major blocks in `Reveal` (reduced-motion safe).
- Media: `MediaPlaceholder` for hero/team/blog imagery.

## Per-page content
- Real, production-ready copy (tone from `homepage-content-strategy`).
- Clear CTAs linking internally (`/products`, `/training`, `/contact`, `/faq`).
- SEO per page: title, description, canonical, Open Graph via `Seo`.
- Breadcrumbs: Home → Page.

## Preview
- Add `/preview/inner-pages` route rendering an index of inner pages (links) for review,
  plus keep individual routes directly reviewable.

## Verification
- `npx tsc --noEmit` → 0 errors.
- `npm run build` → success.
- Review at http://localhost:5173/<route>.

## Out of Scope
- Real product/blog/course data (backend).
- Auth flow (`/auth`).
- Forms submission (UI-only).
