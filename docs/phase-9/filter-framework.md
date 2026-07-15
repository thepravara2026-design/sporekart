# Filter Framework

> Filter doc for [Sprint 24 Part 2](./sprint-24-part-2.md). Code: `src/admin/modules/products/catalog`. Mock Mode.

## Overview

`FilterPanel` provides smart, multi-select facets over `catalogMock`. Filters compose with search and sort
inside `useCatalogState` (see [search-framework.md](./search-framework.md)). All selection is mock-only.

## Facets

### Multi-select (Checkbox groups)

| Facet | Field | Component |
|-------|-------|-----------|
| Categories | `category` | `Checkbox` (`src/design-system/components/core/Checkbox.tsx`) |
| Brands | `brand` | `Checkbox` |
| Collections | `collection` | `Checkbox` |
| Status | `publishingStatus` | `Checkbox` (+ `StatusBadge` label) |
| Type | `productType` | `Checkbox` |

```tsx
// src/admin/modules/products/catalog/FilterPanel.tsx
<fieldset>
  <legend>Categories</legend>
  {categories.map((c) => (
    <label key={c}>
      <Checkbox checked={filters.categories.includes(c)} onChange={() => toggle('categories', c)} />
      {c}
    </label>
  ))}
</fieldset>
```

### Ranges

- **Price range** — `minPrice` / `maxPrice` over `pricing.sellingPrice` (number inputs / slider).
- **Created date range** — `createdAt` from/to.
- **Updated date range** — `updatedAt` from/to.

### Toggles

| Toggle | Field | Effect |
|--------|-------|--------|
| Has images | `media.images.length > 0` | only products with media |
| Featured | `featured` | only featured |
| Draft | `publishingStatus === 'draft'` | only drafts |
| Published | `publishingStatus === 'published'` | only published |
| Archived | `publishingStatus === 'archived'` | only archived |

## Filter Chips + Count + Clear-All

Active filters render as removable chips above the results surface, each showing a count of matching rows.
A **Clear all** control resets every facet/range/toggle at once.

```tsx
// src/admin/modules/products/catalog/FilterPanel.tsx
{activeChips.map((chip) => (
  <button key={chip.key} onClick={() => clearFilter(chip.key)} aria-label={`Remove ${chip.label}`}>
    {chip.label} ✕
  </button>
))}
<Button variant="ghost" onClick={clearAll}>Clear all</Button>
```

## Composition with Search + Sort

The single `useMemo` selector in `useCatalogState` applies, in order: search → filters → sort → pagination slice.

```tsx
// src/admin/modules/products/catalog/useCatalogState.ts
const matchesFilters = (p: CatalogProduct, f: CatalogFilter) =>
  (f.categories.length === 0 || f.categories.includes(p.category)) &&
  (f.brands.length === 0 || f.brands.includes(p.brand)) &&
  (f.statuses.length === 0 || f.statuses.includes(p.publishingStatus)) &&
  (f.minPrice == null || p.pricing.sellingPrice >= f.minPrice) &&
  (f.maxPrice == null || p.pricing.sellingPrice <= f.maxPrice) &&
  (!f.hasImages || p.media.images.length > 0) &&
  (!f.featured || p.featured) &&
  (!f.draft || p.publishingStatus === 'draft') &&
  (!f.published || p.publishingStatus === 'published') &&
  (!f.archived || p.publishingStatus === 'archived');
```

Related: [product-catalog.md](./product-catalog.md), [search-framework.md](./search-framework.md),
[saved-views.md](./saved-views.md).
