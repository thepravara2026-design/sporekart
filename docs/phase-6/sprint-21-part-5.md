# Phase 6 — Sprint 21 Part 5: Blog & Knowledge Hub

**Status:** APPROVED (frozen — 2026-07-13)
**Branch:** `sporetest`
**Date:** 2026-07-13
**Depends on:** Sprint 21 Part 4 (Inner Pages) — COMPLETE, Sprint 21 Part 3 (Homepage) — APPROVED

## Mission
Implement the complete public Blog & Knowledge Hub — SporeKart's educational
platform and a primary SEO asset. Establishes SporeKart as India's trusted
mushroom knowledge platform, a research-driven agri-tech brand, and a
cultivation-education destination.

## Routes implemented
| Route | Page | File |
| --- | --- | --- |
| `/blog` | Blog landing | `pages/BlogPage.tsx` |
| `/blog/:slug` | Article page | `pages/ArticlePage.tsx` |
| `/blog/category/:slug` | Category page | `pages/BlogCategoryPage.tsx` |
| `/blog/tag/:slug` | Tag page | `pages/BlogTagPage.tsx` |
| `/search` | Search experience | `pages/SearchPage.tsx` |
| `/preview/blog` | Preview: landing | `preview/BlogPreview.tsx` |
| `/preview/blog/article` | Preview: article | `preview/BlogPreview.tsx` |
| `/preview/blog/search` | Preview: search | `preview/BlogPreview.tsx` |

## Deliverables
- `src/public-website/blog/data.ts` — categories (12), tags (16), 14 placeholder
  articles, query helpers (`getArticleBySlug`, `getArticlesByCategory`,
  `getArticlesByTag`, `getFeaturedArticles`, `getLatestArticles`,
  `getTrendingArticles`, `getRelatedArticles`, `searchArticles`, `formatDate`).
  All copy flagged `placeholder: true`.
- `src/public-website/blog/BlogStyles.tsx` — scoped, token-only CSS (card hover,
  category hover, tag chips, sticky TOC, prose typography, callouts, quotes,
  share buttons, search field, reduced-motion + print rules).
- `src/public-website/blog/components/`
  - `ArticleCard.tsx` (`ArticleCard`, `ArticleMeta`)
  - `FeaturedArticle.tsx`
  - `CategoryCard.tsx` (`CategoryCard`, `CategoryGrid`)
  - `TagChip.tsx` (`TagChip`, `TagCloud`)
  - `SearchBar.tsx`
  - `TableOfContents.tsx` (scroll-spy via IntersectionObserver)
  - `ArticleBody.tsx` (renders heading/paragraph/list/callout/quote/image/video)
  - `ShareButtons.tsx` (X / LinkedIn / WhatsApp / copy-link)
  - `EmptyState.tsx`
- `src/public-website/pages/BlogPage.tsx` — hero + search, featured, categories,
  latest, trending + popular tags, training & product promos, newsletter.
- `src/public-website/pages/ArticlePage.tsx` — reading progress, hero banner,
  sticky TOC, body, share/tags, prev/next, related articles, product/training
  promos, newsletter.
- `src/public-website/pages/BlogCategoryPage.tsx`, `BlogTagPage.tsx`,
  `SearchPage.tsx`.
- `src/public-website/preview/BlogPreview.tsx` — 3 preview views with
  viewport switcher + a11y/responsive/section-checklist notes + approval status.
- `src/public-website/config.ts` — `isPublicWebsiteRoute` now matches
  `/blog/*` and `/search`; blog routes added to `PUBLIC_WEBSITE_ROUTES`.
- `src/App.tsx` — lazy routes + preview routes wired; `ScrollProgress` reused for
  reading progress.

## SEO
- `Seo` drives `<title>`, meta description, canonical, Open Graph, Twitter Cards.
- JSON-LD: `Organization` + `BreadcrumbList` (landing), `Article` + `BreadcrumbList`
  (article), `BreadcrumbList` (category/tag). Search pages set `noindex`.
- Semantic H1/H2/H3, internal linking, SEO-friendly slugs.

## Verification
- `npx tsc --noEmit`: 0 errors.
- `npm run build`: success (per-page code-split chunks).
- Review: `http://localhost:5173/blog`, `/blog/<slug>`, `/blog/category/<slug>`,
  `/blog/tag/<slug>`, `/search`, `/preview/blog*`.

## Notes / Risks
- All article content is **placeholder** (clearly badged) — CMS integration pending.
- Search is client-side over placeholder data; "recent searches" and "live
  suggestions" are intentionally placeholder-ready for backend.
- No backend/API changes; no fabricated statistics.
- **Pending Part 6:** real CMS data, author pages, pagination, RSS, FAQ schema
  foundation expansion, image/CDN pipeline, dark-mode toggle wiring.
