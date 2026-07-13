# Tags (`/blog/tag/:slug`)

Sixteen popular tags (Beginner, Oyster, Button Mushroom, Spawn, Substrate,
Organic, Training, Subsidy, Packaging, Nutrition, Climate, Contamination, Yield,
Marketing, Storage, Technology) provide cross-cutting discovery.

## Page
- **Hero** — "#tag" eyebrow chip, H1 "Tagged "tag"", and a placeholder count.
- **Articles** — `getArticlesByTag(slug)` grid; empty state with suggested tags.
- **Explore more tags** — `TagCloud` for discovery.

## Components
- `TagChip` (link to `/blog/tag/<slug>`, prefixed with `#`) and `TagCloud`
  (responsive wrap).

## SEO
- Per-tag title/description/canonical; `BreadcrumbList` JSON-LD.
- Tags are clickable, keyboard-navigable chips with hover/focus token styling.

## Notes
- Tag names displayed with `#` prefix in UI; URLs use the raw slug.
