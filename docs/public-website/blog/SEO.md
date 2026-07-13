# Blog SEO Summary

The Blog & Knowledge Hub is intended to be one of SporeKart's primary organic
acquisition assets. SEO is centralized in `public-website/Seo.tsx`.

## On-page
- **Titles** — page/scoped, suffixed with `| SporeKart` by `Seo`.
- **Meta description** — unique per page, ≤ ~155 chars.
- **Canonical** — absolute canonical per route (includes `?q=` on search).
- **Open Graph** — `og:title`, `og:description`, `og:type`, `og:url`,
  `og:image` (when available).
- **Twitter Cards** — `summary_large_image` when an image is present.
- **Semantic headings** — one H1 per page; H2/H3 outline the content; article
  body headings carry `id`s that back the table of contents and in-page anchors.

## Structured data (JSON-LD via `structuredData`)
- **Landing** — `Organization` + `BreadcrumbList`.
- **Article** — `Article` (headline, author `Person`, publisher `Organization`,
  `datePublished`/`dateModified`, `articleSection`, `keywords`) + `BreadcrumbList`.
- **Category / Tag** — `BreadcrumbList`.
- **Search** — `noindex, nofollow` (excluded from indexing).

## Internal linking
- Hero category chip → category page; tags → tag pages; "Related articles" →
  article pages; featured/latest/trending cards → article pages; footer/nav →
  hub landing. SEO-friendly, human-readable slugs throughout.

## Future (Part 6 foundations)
- FAQ schema foundation on article/guide pages.
- `Article` `mainEntity` QAPage/FAQPage expansion.
- Sitemap generation including blog routes; RSS feed.
- `og:image` real assets via CDN/image pipeline.

## Verification checklist
- Unique `<title>` + meta description per route.
- Canonical present and absolute.
- OG + Twitter tags present.
- JSON-LD valid (Organization/BreadcrumbList/Article).
- One H1; logical heading order; no broken internal links.
