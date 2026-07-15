# Search Framework

> Search doc for [Sprint 24 Part 2](./sprint-24-part-2.md). Code: `src/admin/modules/products/catalog`. Mock Mode.

## Overview

Search lives in `CatalogToolbar` and feeds `useCatalogState.search`. It reuses the admin `SearchBar`
(`src/admin/components/data-grid/SearchBar.tsx`) — debounced (~250ms) so typing does not re-filter on every keystroke.

## Fields Searched

A product is matched if the query (case-insensitive, trimmed) appears in any of:

| Field | Source | Notes |
|-------|--------|-------|
| `name` | `CatalogProduct.name` | primary match |
| `sku` | `CatalogProduct.sku` | exact-ish prefix match |
| `barcode` | `CatalogProduct.barcode` | numeric match |
| `category` | `CatalogProduct.category` | facet label |
| `brand` | `CatalogProduct.brand` | facet label |
| `tags` | `CatalogProduct.tags[]` | any tag contains query |
| `productType` | `CatalogProduct.productType` | type label |
| `publishingStatus` | `CatalogProduct.publishingStatus` | status label |

```tsx
// src/admin/modules/products/catalog/useCatalogState.ts
const matchesSearch = (p: CatalogProduct, q: string) => {
  const s = q.trim().toLowerCase();
  if (!s) return true;
  return [p.name, p.sku, p.barcode, p.category, p.brand, p.productType, p.publishingStatus, ...p.tags]
    .some((v) => String(v).toLowerCase().includes(s));
};
```

## Recent Searches & Suggestions

Recent queries are stored in `sessionStorage` (`sk.catalog.recentSearches`, capped at 5, de-duped).
On focus, `SearchBar` shows the recent list plus live suggestions derived from matching `name`/`sku`/`brand` tokens.

```tsx
// src/admin/modules/products/catalog/CatalogToolbar.tsx
import { SearchBar } from 'src/admin/components/data-grid/SearchBar';

<SearchBar
  value={search}
  onChange={setSearch}
  debounceMs={250}
  placeholder="Search name, SKU, barcode, brand…"
  recentSearches={recentSearches}
  suggestions={suggestions}
  onClear={() => setSearch('')}
  aria-label="Search products"
/>;
```

## Clear Search

A clear (`x`) affordance resets `search` to `''` while leaving active **filters** and **sort** intact —
search is an orthogonal dimension composed in `useCatalogState`.

## Integration with Catalog State

Search is one of three composing dimensions (search + filters + sort) combined in a single `useMemo`:

```tsx
// src/admin/modules/products/catalog/useCatalogState.ts
const results = useMemo(
  () => catalogMock.filter((p) => matchesSearch(p, search) && matchesFilters(p, filters)),
  [search, filters],
);
const sorted = useMemo(() => sortResults(results, sort), [results, sort]);
```

Search never triggers a network call — `catalogMock` (100+ `CatalogProduct`) is filtered in-memory.

Related: [product-catalog.md](./product-catalog.md), [filter-framework.md](./filter-framework.md).
