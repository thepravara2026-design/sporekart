# Article Page (`/blog/:slug`)

The reading experience: comfortable width, sticky table of contents, reading
progress, rich body blocks, and related/navigation surfaces.

## Anatomy
- **Reading progress** — reused `ScrollProgress` (`role="progressbar"`), fixed top.
- **Hero banner** — gradient band; category chip (links to category), H1 title,
  excerpt, author block (initials avatar + name + role), published/updated dates,
  reading time, and a "Placeholder content — pending CMS" badge when applicable.
- **Body + TOC** — `.sk-blog-layout` two-column grid:
  - `<article>` renders `ArticleBody` (headings, paragraphs, lists, callouts,
    quotes, image/video placeholders).
  - `<aside>` renders `TableOfContents` (sticky; scroll-spy highlights the active
    H2 via IntersectionObserver; `aria-current`).
- **Share + tags** — `ShareButtons` (X / LinkedIn / WhatsApp / copy-link) and tag chips.
- **Prev / Next** — derived from `getLatestArticles()` order.
- **Related articles** — `getRelatedArticles(article, 3)` (same category first,
  then shared tags).
- **Product + Training promos** — two `CtaBanner`s.
- **Newsletter** — reused `NewsletterCta`.

## Body blocks
`ArticleBody` switches on `ArticleBlock.type`:
- `heading` → `<h2 id>`/`<h3 id>` (ids back the TOC; `scroll-margin-top` prevents
  header overlap).
- `paragraph`, `list` (ul/ol), `callout` (info/success/warning/tip, token-colored),
  `quote`, `image`/`video` (MediaPlaceholder).

## SEO
- `type: 'article'`; JSON-LD `Article` (headline, author Person, publisher
  Organization, datePublished/Modified, section, keywords) + `BreadcrumbList`.
- Canonical = `https://sporekart.example.com/blog/<slug>`.

## Accessibility
- Breadcrumb navigation, single H1, logical H2/H3.
- TOC links carry `aria-current`; progress bar `role="progressbar"` with
  `aria-valuenow`.
- Share buttons have `aria-label`s; placeholder badge is an `aria` note.

## Responsive
- Two-column collapses to single column under 920px; TOC becomes static (non-sticky).
- Author/meta row wraps gracefully.
- Print stylesheet hides chrome, TOC, share, related, promos, and progress bar.
