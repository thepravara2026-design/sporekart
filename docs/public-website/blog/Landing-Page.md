# Blog Landing Page (`/blog`)

The landing page establishes SporeKart as a trusted knowledge platform and is
one of the site's primary SEO entry points.

## Sections (top → bottom)
1. **Hero** — gradient band, "Knowledge Hub" eyebrow, H1 "Grow with confidence",
   supporting copy, and a `SearchBar` that navigates to `/search?q=…`.
2. **Featured article** — `FeaturedArticle` (first `featured` article), two-column
   hero image + content, "Featured" chip, author/date meta.
3. **Explore by topic** — `CategoryGrid` of all 12 categories with counts.
4. **Latest articles** — `getLatestArticles(6)` in a responsive card grid.
5. **Trending + Popular tags** — two-column: trending cards (left) and an aside
   with `TagCloud` + "Popular searches" quick links (right). Collapses under 920px.
6. **Training promotion** — `CtaBanner` → `/training`.
7. **Product promotion** — `CtaBanner` → `/products`.
8. **Newsletter** — reused `NewsletterCta`.

## Data
- `getFeaturedArticles()`, `getLatestArticles(6)`, `getTrendingArticles(3)`,
  `POPULAR_SEARCHES`, `CATEGORIES`, `TAGS`.

## SEO
- Title + description + canonical `/blog`.
- JSON-LD: `Organization` + `BreadcrumbList`.

## Notes
- All article copy is placeholder (badged on the article page, not on cards).
- Category counts are illustrative placeholders.
