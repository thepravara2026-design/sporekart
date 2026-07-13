# Sprint 21 Part 1 — Implementation Plan

## 1. Architecture Plan
Public website is rendered **outside** the enterprise shell. `App.tsx` branches on
`isNonEnterpriseRoute(pathname)`:
- Public routes (`config.ts` `PUBLIC_WEBSITE_ROUTE_SET`) and `/preview/public-*` take the
  non-enterprise branch (no `Header`/`Sidebar`/`BreadcrumbBar`/enterprise footer).
- Everything else uses the unchanged enterprise shell.

## 2. Routing Diagram
```
/path
  ├── isNonEnterpriseRoute?
  │     ├── yes → <PublicRoutePage /> (13 routes) | public preview (5 routes) | NotFound
  │     └── no  → enterprise shell → workspace pages / demos / design-system / NotFound
```

## 3. Layout Diagram
```
PublicLayout
├── Seo
├── AnnouncementRegion (optional)
├── PublicHeader
│   ├── brand → /
│   ├── PublicNav (horizontal NavLinks)
│   ├── search (placeholder)
│   └── Sign In → /auth  + mobile drawer
├── BreadcrumbFoundation (optional)
├── <main>
│   └── PublicContentContainer + section foundations
└── PublicFooter (Explore / Company / Policies)
```

## 4. Navigation Map
- Header primary: Products, Training, Blog, About, Contact.
- Footer: grouped PublicFooterNav (Explore / Company / Policies).
- No admin routes exposed.

## 5. SEO Plan
`Seo` component injects title, description, robots, OG, Twitter, canonical, JSON-LD.
Each placeholder passes `title`, `description`, `canonical`. Preview at `/preview/public-seo`.

## 6. Responsive Strategy
Token-driven grids (`auto-fit minmax`), sticky header collapses to mobile drawer,
`ResponsivePreview` provides 4 viewports for the 5 preview routes.

## 7. Testing Checklist
See `docs/public-website/testing-checklist.md`. Summary: types 0, build ok, routing ok,
header/footer/SEO/responsive verified.

## 8. Risk Analysis
| Risk | Likelihood | Mitigation |
| --- | --- | --- |
| Public route conflicts with legacy `public` workspace in navigation.ts | Medium | Non-enterprise branch isolates rendering; consolidation deferred |
| Mobile drawer focus management | Low | Non-modal by design; focus trap deferred to content part |
| Token coverage for new components | Low | All colours/spacing from v1.0.0 tokens; fallback literals in `var()` |
| SEO SPA crawlability | Medium | Head injection works client-side; prerender/sitemap deferred |
