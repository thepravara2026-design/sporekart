# Categories (`/blog/category/:slug`)

Twelve categories cover the knowledge hub: Mushroom Farming, Spawn Production,
Fresh Mushrooms, Dry Mushrooms, Training, Business, Organic Farming, Technology,
Government Schemes, Recipes, Storage, Research.

## Page
- **Hero** — category icon (token chip), H1 name, description, and a placeholder
  article count.
- **Articles** — `getArticlesByCategory(slug)` grid; empty state with suggested
  categories when none exist yet.
- **Browse all topics** — `CategoryGrid` for discovery.

## Data
- `CATEGORIES` (name, description, icon, `count` placeholder).
- `getCategoryBySlug`, `getArticlesByCategory`.

## SEO
- Title/description/canonical per category; `BreadcrumbList` JSON-LD.
- `CategoryCard` links use SEO-friendly slugs (`/blog/category/<slug>`).

## Notes
- Counts are illustrative placeholders pending CMS volume.
