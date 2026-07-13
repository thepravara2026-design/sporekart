# Public Website — Testing Checklist (Part 1)

## Build & Types
- [x] `npx tsc --noEmit` → 0 errors
- [x] `npx vite build` → success (243+ modules, ~2.7s)

## Routing
- [x] Each of the 13 public routes renders the placeholder shell (no enterprise chrome).
- [x] Preview routes `/preview/public-*` render without enterprise chrome.
- [x] Unknown public path falls back to `NotFound` in the non-enterprise branch.
- [x] Enterprise routes (`/orders`, design system, demos) still render with enterprise shell.
- [x] Public routes are lazy-loaded (separate chunks).

## Header
- [x] Sticky on scroll.
- [x] Primary nav active state highlights current route.
- [x] Sign-in links to `/auth`.
- [x] At mobile width, hamburger opens drawer; `Escape` and outside-click close it.

## Footer
- [x] Link columns present (Explore / Company / Policies).
- [x] No admin/enterprise links exposed.
- [x] Copyright year is dynamic.

## SEO
- [x] Title, description, canonical, OG, Twitter injected.
- [x] JSON-LD script present and removed on unmount.
- [x] `/preview/public-seo` demonstrates injection.

## Responsive
- [x] Layout reflows at 1280 / 1024 / 768 / 375.
- [x] No horizontal overflow at 375px.
- [x] Preview routes allow per-viewport review.

## Tokens
- [x] No hardcoded hex outside `var()` fallbacks.
- [x] Works under light / dark / high-contrast themes via tokens.

## Known Limitations (deferred)
- Real page content (hero, about, blog, contact form, auth UI) not yet built.
- Mobile drawer is non-modal (no focus trap yet).
- Visible focus-visible ring tokens not yet applied to public links.
- Legacy `public` workspace in `navigation.ts` not yet consolidated.
