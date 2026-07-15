# Product Catalog

> Catalog architecture doc for [Sprint 24 Part 2](./sprint-24-part-2.md). Code: `src/admin/modules/products/catalog`. Mock Mode.

## Purpose

`ProductCatalog` is the **browsing & discovery** shell built on the Part 1 Product foundation. It lets an admin
scan, search, filter, sort, paginate, multi-select, and quick-preview products — **read-only, no create/edit**.

## View Modes

| Mode | Component | Use |
|------|-----------|-----|
| `table` | `ProductTableView` | dense, sortable, bulk-select |
| `cards` | `ProductCardView` | visual browsing, reuses `ProductCard` |
| `compact` | `ProductCompactView` | high-density list scanning |
| `grid` | `ProductCardView` (alias) | large-screen visual grid |

The active mode is persisted to `sessionStorage` (`sk.catalog.viewMode`) so a reload restores the last view.

```tsx
// src/admin/modules/products/catalog/useCatalogState.ts
const [viewMode, setViewMode] = useState<ViewMode>(
  () => (sessionStorage.getItem('sk.catalog.viewMode') as ViewMode) ?? 'table',
);
useEffect(() => sessionStorage.setItem('sk.catalog.viewMode', viewMode), [viewMode]);
```

## Composition

```
ProductCatalog
├── ProductLayout            (Part 1 shell: header + breadcrumb + toolbar + content)
│   └── CatalogToolbar      (SearchBar, SortMenu, SavedViews, view switch, bulk bar)
├── FilterPanel             (facets, price/date ranges, toggles, chips)
├── surface
│   ├── ProductTableView    (table mode)
│   ├── ProductCardView     (cards/grid mode)
│   └── ProductCompactView  (compact mode)
├── PaginationBar           (admin Pagination)
└── ProductQuickPreview     (Dialog drawer)
```

Loading and empty states are handled by `CatalogLoading` (design-system `Skeleton`)
and `CatalogEmptyStates` respectively.

## Reuse of Enterprise Components

| Need | Component | Path |
|------|-----------|------|
| Surface card | `Card` | `src/design-system/components/composite/Card.tsx` |
| Status pill | `StatusBadge` | `src/admin/components/status/StatusBadge.tsx` |
| Buttons | `Button` | `src/design-system/components/core/Button.tsx` |
| Icons | `Icon` | `src/design-system/icons/Icon.tsx` |
| Loading | `Skeleton` | `src/design-system/components/display/Skeleton.tsx` |
| Drawer | `Dialog` | `src/design-system/components/feedback/Dialog.tsx` |
| Row select | `Checkbox` | `src/design-system/components/core/Checkbox.tsx` |
| Pagination | `Pagination` | `src/admin/components/data-grid/Pagination.tsx` |
| Search | `SearchBar` | `src/admin/components/data-grid/SearchBar.tsx` |
| Saved views | `SavedViews` | `src/admin/components/data-grid/SavedViews.tsx` |

## Reuse of Part 1

- `ProductLayout` — `src/admin/modules/products/layout/ProductLayout.tsx`
- `ProductWorkspace` — `src/admin/modules/products/workspace/ProductWorkspace.tsx`
- `ProductCard` — `src/admin/modules/products/components/ProductCard.tsx`
- `LifecycleBadge` — `src/admin/modules/products/lifecycle/LifecycleBadge.tsx`
- `permissions` (`PermissionGate`, `FeatureGate`, `usePermissions`) — `src/admin/modules/products/permissions/`

## File Map (under `catalog/`)

| File | Responsibility |
|------|----------------|
| `ProductCatalog.tsx` | top-level shell + composition |
| `ProductTableView.tsx` | memoised table surface |
| `ProductCardView.tsx` | card/grid surface |
| `ProductCompactView.tsx` | compact list surface |
| `FilterPanel.tsx` | facet/range/toggle filters + chips |
| `CatalogToolbar.tsx` | search, sort, saved views, view switch, bulk |
| `SortMenu.tsx` | sort options menu |
| `PaginationBar.tsx` | wraps admin `Pagination` |
| `ProductQuickPreview.tsx` | `Dialog` quick-preview drawer |
| `CatalogLoading.tsx` | skeleton loading state |
| `CatalogEmptyStates.tsx` | empty / no-results states |
| `useCatalogState.ts` | state hook (search/filter/sort/page/select/preview) |
| `catalogColumns.tsx` | table column config (DataGridColumn-style) |
| `bulkActions.tsx` | bulk action bar (mock) |
| `types.ts` | `CatalogProduct`, `CatalogFilter`, `ViewMode`, etc. |

## Design Tokens

Styled entirely via CSS variables — no hardcoded colours/spacing:

```css
.catalog-grid { display: grid; gap: var(--space-component-gap); }
.catalog-table { border-radius: var(--radius-card); color: var(--text-primary); }
.catalog__surface { background: var(--color-bg-surface-default); }
```

Related: [search-framework.md](./search-framework.md), [filter-framework.md](./filter-framework.md),
[pagination-guide.md](./pagination-guide.md), [saved-views.md](./saved-views.md),
[quick-preview.md](./quick-preview.md).
