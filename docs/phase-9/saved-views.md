# Saved Views

> Saved views doc for [Sprint 24 Part 2](./sprint-24-part-2.md). Code: `src/admin/modules/products/catalog`. Mock Mode.

## Overview

Saved views let an admin switch between curated layouts (filters + sort + column visibility + view mode)
without rebuilding them each session. The UI reuses the admin `SavedViews` component
(`src/admin/components/data-grid/SavedViews.tsx`), wired in `CatalogToolbar`.

> **Session-only.** Views (including the 7 presets and any custom views) live in `sessionStorage`
> (`sk.catalog.views`, `sk.catalog.activeView`). There is **no backend persistence** — they are lost on logout/reload-across-session.

## Reuse Contract

```tsx
// src/admin/modules/products/catalog/CatalogToolbar.tsx
import { SavedViews } from 'src/admin/components/data-grid/SavedViews';

<SavedViews
  views={PRESET_VIEWS}            // seeded presets below
  activeId={activeViewId}
  onSelect={applyView}            // sets filters + sort + columns + viewMode
  onCreate={saveCustomView}       // session-only
  onDelete={removeView}
/>;
```

A "view" object:

```ts
// src/admin/modules/products/catalog/types.ts
interface CatalogView {
  id: string;
  label: string;
  filters: CatalogFilter;
  sort: SortOption;
  viewMode: ViewMode;
  columnIds?: string[]; // for table mode
}
```

## The 7 Named Presets

| Preset | Filters applied | Sort |
|--------|-----------------|------|
| **Default** | none | name ↑ |
| **My Products** | `createdBy === currentUser` (mock) | updatedAt ↓ |
| **Published** | `publishingStatus === 'published'` | name ↑ |
| **Drafts** | `publishingStatus === 'draft'` | updatedAt ↓ |
| **Recently Updated** | none | `updatedAt ↓` |
| **Out of Stock** | `stock === 0` (mock flag) | name ↑ |
| **Featured** | `featured === true` | name ↑ |

```tsx
// src/admin/modules/products/catalog/useCatalogState.ts
export const PRESET_VIEWS: CatalogView[] = [
  { id: 'default', label: 'Default', filters: EMPTY_FILTERS, sort: { key: 'name', dir: 'asc' }, viewMode: 'table' },
  { id: 'my', label: 'My Products', filters: { ...EMPTY_FILTERS, createdByMe: true }, sort: { key: 'updatedAt', dir: 'desc' }, viewMode: 'table' },
  { id: 'published', label: 'Published', filters: { ...EMPTY_FILTERS, statuses: ['published'] }, sort: { key: 'name', dir: 'asc' }, viewMode: 'cards' },
  { id: 'drafts', label: 'Drafts', filters: { ...EMPTY_FILTERS, statuses: ['draft'] }, sort: { key: 'updatedAt', dir: 'desc' }, viewMode: 'table' },
  { id: 'recent', label: 'Recently Updated', filters: EMPTY_FILTERS, sort: { key: 'updatedAt', dir: 'desc' }, viewMode: 'table' },
  { id: 'oos', label: 'Out of Stock', filters: { ...EMPTY_FILTERS, outOfStock: true }, sort: { key: 'name', dir: 'asc' }, viewMode: 'cards' },
  { id: 'featured', label: 'Featured', filters: { ...EMPTY_FILTERS, featured: true }, sort: { key: 'name', dir: 'asc' }, viewMode: 'cards' },
];
```

## Applying a View

`applyView` overwrites the active `filters`, `sort`, `viewMode`, and `columnIds` in `useCatalogState`
and persists `activeViewId` to `sessionStorage`. Selecting a preset does not mutate `search` (kept orthogonal).

## Technical Debt

- Views are **session-scoped** — no user-profile persistence yet (see [sprint-24-part-2.md](./sprint-24-part-2.md) §14).
- `My Products` and `Out of Stock` rely on mock fields (`createdBy`, `stock`) because inventory/auth are deferred.

Related: [product-catalog.md](./product-catalog.md), [filter-framework.md](./filter-framework.md).
