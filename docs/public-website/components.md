# Public Website — Components

All components are exported from `src/public-website/index.ts` and styled exclusively
with CSS custom property tokens. No comments are added in source (per repo rules).

## Core
- **`PublicLayout`** — props: `children`, `seo?`, `breadcrumbs?`, `announcement?`.
  Composes Seo + AnnouncementRegion + PublicHeader + BreadcrumbFoundation + main + PublicFooter.
- **`PublicHeader`** — sticky header. Brand link, `PublicNav` (horizontal), search trigger
  (placeholder), sign-in `RouterLink` to `/auth`, and a responsive mobile drawer (Esc / outside-click to close).
- **`PublicFooter`** — link columns (Explore / Company / Policies), social links, copyright + secure row.
- **`PublicNav`** — props: `orientation: 'horizontal' | 'vertical'`. Renders `NavLink`s with active state.
- **`PublicContentContainer`** — props: `children`, `maxWidth?: 'sm'|'md'|'lg'|'xl'`, `padding?: boolean`.
- **`AnnouncementRegion`** — props: `message?`, `ctaLabel?`, `ctaHref?`, `dismissible?`. Dismissible banner.
- **`BreadcrumbFoundation`** — props: `items: { label; href? }[]`. `aria-current` on last item.
- **`Seo`** — props: `title?`, `description?`, `canonical?`, `type?`, `image?`, `noindex?`, `structuredData?`.
  Injects/updates `<title>`, meta description, robots, Open Graph, Twitter, canonical `<link>`,
  and a JSON-LD `<script>` (cleaned up on unmount).

## Section Foundations
- **`TrustSection`** — 4 trust badges (shipping, quality, support, loved) using icons.
- **`FutureCta`** — props: `title?`, `description?`, `primaryLabel?`, `primaryHref?`, `secondaryLabel?`, `secondaryHref?`.
- **`FutureTestimonial`** — props: `quote?`, `author?`, `role?`.
- **`FutureStatistics`** — props: `stats?: { value; label }[]`.
- **`FutureBlogSection`** — props: `posts?: { title; href; excerpt; tag }[]`.

These are deliberately generic building blocks; full content is deferred to later parts.

## Route & Preview Pages
- **`PublicRoutePlaceholder`** — renders `PublicLayout` + a "coming soon" placeholder body.
- **`PublicRoutePage`** (default export) — resolves the route def from `useLocation()` and renders the placeholder.
- **`PublicPreviews`** — exports `PublicLayoutPreview`, `PublicHeaderPreview`, `PublicFooterPreview`,
  `PublicNavigationPreview`, `PublicSeoPreview`, each wrapping content in `ResponsivePreview`.

## Dependencies (all from Design System v1.0.0)
- `Icon` from `design-system/icons/Icon` (registry keys: `menu`, `search`, `log-in`, `x`,
  `chevron-right`, `mail`, `phone`, `map-pin`, `shield`, `truck`, `headphone`, `star`, `arrow-right`).
- `ResponsivePreview` from `design-system/playground/components/ResponsivePreview`.
- Routing from `react-router-dom` (`Link`, `NavLink`, `useLocation`).
