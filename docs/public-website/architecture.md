# Public Website — Architecture

## Shell Decision
The public website must **not** inherit the enterprise application shell
(`Header`, `Sidebar`, `BreadcrumbBar`, enterprise `footer`). Public pages use a
dedicated `PublicLayout` that renders its own `PublicHeader` and `PublicFooter`.

`src/App.tsx` branches on the current path:

```
isNonEnterpriseRoute(pathname) =
    isPublicWebsiteRoute(pathname)             // config.ts
 || pathname.startsWith('/preview/public-')    // preview routes
```

- **Enterprise branch** → unchanged enterprise shell + `<Routes>` (workspace pages, demos, design system).
- **Non-enterprise branch** → `PublicRoutePage` (placeholders) + public preview routes, no enterprise chrome.

This keeps the public website visually and behaviourally distinct while reusing the
same router, context provider, and lazy-loading patterns.

## Component Hierarchy
```
PublicLayout
├── Seo                      (document head injection)
├── AnnouncementRegion       (optional, dismissible)
├── PublicHeader             (sticky, responsive)
│   ├── brand (RouterLink /)
│   ├── PublicNav (horizontal, NavLink)
│   ├── search trigger (placeholder)
│   └── sign-in (RouterLink /auth) + mobile drawer
├── BreadcrumbFoundation     (optional, route-aware)
├── <main> children
│   └── PublicContentContainer
│   └── section foundations (TrustSection, FutureCta, …)
└── PublicFooter             (link columns + legal row)
```

## File Map
| Concern | File |
| --- | --- |
| Config (routes, nav) | `src/public-website/config.ts` |
| Shell | `src/public-website/PublicLayout.tsx` |
| Header | `src/public-website/PublicHeader.tsx` |
| Footer | `src/public-website/PublicFooter.tsx` |
| Primary nav | `src/public-website/PublicNav.tsx` |
| Content container | `src/public-website/PublicContentContainer.tsx` |
| Announcement | `src/public-website/AnnouncementRegion.tsx` |
| Breadcrumb | `src/public-website/BreadcrumbFoundation.tsx` |
| SEO | `src/public-website/Seo.tsx` |
| Section foundations | `src/public-website/sections/*.tsx` |
| Route placeholders | `src/public-website/routes/PublicRoutePlaceholder.tsx`, `PublicRoutePage.tsx` |
| Preview pages | `src/public-website/preview/PublicPreviews.tsx` |
| Barrel | `src/public-website/index.ts` |
| Wiring | `src/App.tsx` |

## Design Tokens Used
`--color-bg-surface-*`, `--color-text-*`, `--color-border-*`, `--color-bg-accent-*`,
`--space-*`, `--radius-*`, `--shadow-*`, `--font-family-sans`, `--text-*`,
`--z-header`, `--container-*`, `--header-height`.
