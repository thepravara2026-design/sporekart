# Blog & Knowledge Hub — Architecture

The Blog & Knowledge Hub is a fully client-rendered, token-driven experience
built on the Sprint 21 public-website foundation (PublicLayout, Seo,
PublicContentContainer, Reveal, MediaPlaceholder, Design System v1.0.0).

## Layered structure

```
config.ts                  route classification (isPublicWebsiteRoute) + nav
  │
App.tsx                    lazy route + preview wiring
  │
pages/                     route components (Blog / Article / Category / Tag / Search)
  │
blog/
  ├─ data.ts               categories, tags, articles, query helpers (single source)
  ├─ BlogStyles.tsx        scoped, token-only CSS (no hardcoded color/spacing)
  └─ components/
       ArticleCard / FeaturedArticle / CategoryCard / TagChip
       SearchBar / TableOfContents / ArticleBody / ShareButtons / EmptyState
  │
preview/BlogPreview.tsx    /preview/blog, /preview/blog/article, /preview/blog/search
```

## Data layer (`blog/data.ts`)
- `CATEGORIES` (12), `TAGS` (16), `ARTICLES` (14 placeholder articles).
- Each `Article` carries `slug`, `title`, `excerpt`, `categorySlug`, `tagSlugs[]`,
  `author`, dates, `readingTime`, `imageLabel`, flags (`featured`, `trending`,
  `placeholder`), and a structured `body: ArticleBlock[]`.
- `ArticleBlock` union: `heading | paragraph | list | callout | quote | image | video`.
- Query helpers are pure functions; pages never query raw arrays directly.
- Every article is flagged `placeholder: true`; the article hero surfaces a
  "Placeholder content — pending CMS" badge.

## Styling contract
- All visuals use design tokens (`--color-*`, `--space-*`, `--radius-*`,
  `--shadow-*`, `--text-*`, `--container-*`, `--font-family-*`). No hardcoded
  colors/spacing/typography.
- `BlogStyles.tsx` injects one `<style>` per page (idempotent) for hover, sticky
  TOC, prose, callouts, quotes, share, search, and `prefers-reduced-motion` /
  `@media print` rules.

## SEO contract
- `Seo` (from `public-website/Seo.tsx`) is the single meta writer.
- Pages pass `title`, `description`, `canonical`, `type`, and `structuredData`.
- Search pages pass `noindex: true`.

## Routing contract
- `isPublicWebsiteRoute()` returns true for `/blog/*` and `/search`, so blog pages
  render inside the public chrome (PublicHeader/Footer), not the enterprise shell.
- Article pages use `:slug`; unknown slug → `NotFound`.
