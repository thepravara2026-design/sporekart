# Blog Accessibility Report

Target: **WCAG 2.2 AA**. Status: implemented; pending manual audit sign-off.

## Landmarks & structure
- Each page renders within `PublicLayout` (header / main / footer landmarks).
- One `<h1>` per page; H2/H3 used logically (no skipped levels in article body).
- Breadcrumb navigation present on article, category, tag, and search pages.

## Keyboard & focus
- All cards, chips, CTAs, and TOC links are native `<a>`/`<button>` — fully
  keyboard operable with visible focus styles (`focus-visible` token outlines).
- Search field is a labelled `<input>` inside a `role="search"` form; submit via
  Enter or the Search button.
- Skip-to-content link provided by `App` shell.

## Screen readers
- `ArticleMeta` and cards expose descriptive `aria-label`s (e.g., article title).
- `TableOfContents` uses `aria-current="true"` on the active link; nav has
  `aria-label="Table of contents"`.
- Reading progress bar: `role="progressbar"` with `aria-valuemin/max/now`.
- `ShareButtons` have `aria-label`s; copy action announces "Link copied".
- `EmptyState` and placeholder badge use `role="note"`.
- Icons are `aria-hidden` unless they convey standalone meaning (then `aria-label`).

## Motion & contrast
- All entrance/hover motion respects `prefers-reduced-motion` (BlogStyles +
  `Reveal`/`ScrollProgress`). Reduced-motion disables card transforms.
- Colors come from design tokens validated for AA contrast in the style guide.

## Known gaps (Part 6)
- Full keyboard trap testing of the search autocomplete (when added).
- Automated axe/Lighthouse audit on the live build.
- Author pages and pagination need focus/landmark review when added.
