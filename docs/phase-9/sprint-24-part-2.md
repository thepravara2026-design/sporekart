# Sprint 24 Part 2 — Enterprise Product Catalog Experience & Product Discovery

- **Date:** 2026-07-14
- **Status:** COMPLETE
- **Quality Gate:** SATISFIED
- **Mode:** Mock Mode — **NO backend, NO API, NO persistence** implemented. All data is read-only from `src/admin/modules/products/mock/catalogMock.ts`.
- **Scope:** Product BROWSING & DISCOVERY experience on top of the Part 1 Product foundation. No create/edit/delete.
- **Code location:** `src/admin/modules/products/catalog/` (within `frontend/web-app`)
- **Routes:** `/products/catalog`, `/preview/products/catalog` (+ `/table`, `/cards`, `/search`, `/filters`, `/pagination`)
- **Untouched:** existing admin platform, customer site, and the design system were NOT modified — this sprint only reuses them.

---

## 1. Product Catalog Summary

`ProductCatalog` is the discovery shell. It composes a toolbar, a filter panel, a primary render surface (table / card / compact / grid), a pagination bar, and a quick-preview drawer. State is owned by `useCatalogState` and persisted to `sessionStorage` (view mode + active view + pageSize).

```tsx
// src/admin/modules/products/catalog/ProductCatalog.tsx
import { CatalogToolbar } from './CatalogToolbar';
import { FilterPanel } from './FilterPanel';
import { ProductTableView } from './ProductTableView';
import { ProductCardView } from './ProductCardView';
import { ProductCompactView } from './ProductCompactView';
import { PaginationBar } from './PaginationBar';
import { ProductQuickPreview } from './ProductQuickPreview';
import { useCatalogState } from './useCatalogState';
import { ProductLayout } from '../layout/ProductLayout';

export function ProductCatalog() {
  const catalog = useCatalogState();
  return (
    <ProductLayout
      crumbs={[{ label: 'Products' }, { label: 'Catalog' }]}
      actions={<CatalogToolbar {...catalog} />}
    >
      <div className="catalog">
        <FilterPanel {...catalog} />
        <section className="catalog__surface" aria-label="Product results">
          {catalog.viewMode === 'table' && <ProductTableView {...catalog} />}
          {catalog.viewMode === 'cards' && <ProductCardView {...catalog} />}
          {catalog.viewMode === 'compact' && <ProductCompactView {...catalog} />}
        </section>
        <PaginationBar {...catalog.pagination} />
        {catalog.preview && <ProductQuickPreview product={catalog.preview} onClose={catalog.closePreview} />}
      </div>
    </ProductLayout>
  );
}
```

See [product-catalog.md](./product-catalog.md).

## 2. Table Framework Summary

`ProductTableView` renders a dense, keyboard-navigable table driven by `catalogColumns` config (reusing Part 1 `DataGridColumn` conventions). Columns: thumbnail, name, SKU, category, brand, type, price, status (`StatusBadge`), stock, updated, bulk-select (`Checkbox`), quick-preview action. Rows are memoised; selection is lifted into `useCatalogState.selectedIds`.

```tsx
// src/admin/modules/products/catalog/ProductTableView.tsx
import { memo } from 'react';
import { catalogColumns } from './catalogColumns';
import { Checkbox } from 'src/design-system/components/core/Checkbox';
import { StatusBadge } from 'src/admin/components/status/StatusBadge';
import { Icon } from 'src/design-system/icons/Icon';

export const ProductTableView = memo(function ProductTableView({ rows, selectedIds, onToggle, onPreview }: CatalogTableProps) {
  return (
    <table className="catalog-table" role="grid" aria-label="Product catalog">
      <thead>{/* catalogColumns map to <th scope="col"> */}</thead>
      <tbody>
        {rows.map((p) => (
          <tr key={p.id} aria-selected={selectedIds.has(p.id)}>
            <td><Checkbox checked={selectedIds.has(p.id)} onChange={() => onToggle(p.id)} aria-label={`Select ${p.name}`} /></td>
            <td><button onClick={() => onPreview(p)}><Icon name="eye" aria-hidden /></button></td>
          </tr>
        ))}
      </tbody>
    </table>
  );
});
```

See [product-catalog.md](./product-catalog.md).

## 3. Card Design Summary

`ProductCardView` uses the Part 1 `ProductCard` composite inside a CSS-variable grid. Each card shows the thumbnail, name, SKU, `LifecycleBadge`/`StatusBadge`, price, category/brand, and a hover quick-preview affordance. `ProductCompactView` is a condensed list-card variant for high-density scanning.

```tsx
// src/admin/modules/products/catalog/ProductCardView.tsx
import { ProductCard } from '../components/ProductCard';
import { LifecycleBadge } from '../lifecycle/LifecycleBadge';

export function ProductCardView({ rows, onPreview }: CatalogCardProps) {
  return (
    <div className="catalog-grid" style={{ gap: 'var(--space-component-gap)' }}>
      {rows.map((p) => (
        <ProductCard
          key={p.id}
          product={p}
          badge={<LifecycleBadge status={p.publishingStatus} />}
          onPreview={() => onPreview(p)}
        />
      ))}
    </div>
  );
}
```

See [product-catalog.md](./product-catalog.md).

## 4. Search Framework Report

Search is powered by the admin `SearchBar` (debounced ~250ms) wired into `useCatalogState.search`. It indexes: `name`, `sku`, `barcode`, `category`, `brand`, `tags`, `productType`, `publishingStatus`. Recent searches are kept in `sessionStorage`; a suggestions dropdown appears on focus. Clearing search preserves filters and sort.

```tsx
// src/admin/modules/products/catalog/CatalogToolbar.tsx
import { SearchBar } from 'src/admin/components/data-grid/SearchBar';

<SearchBar
  value={search}
  onChange={setSearch}
  placeholder="Search name, SKU, barcode, brand…"
  recentSearches={recentSearches}
  onClear={() => setSearch('')}
/>;
```

See [search-framework.md](./search-framework.md).

## 5. Filter Framework Report

`FilterPanel` exposes multi-select facets (category, brand, collection, status, type), a price range, created/updated date ranges, and toggles (has-images, featured, draft, published, archived). Active filters render as removable chips with counts; a clear-all resets. Filters compose with search + sort inside `useCatalogState` via a single `useMemo` selector.

```tsx
// src/admin/modules/products/catalog/FilterPanel.tsx
import { Checkbox } from 'src/design-system/components/core/Checkbox';

<Checkbox checked={f.hasImages} onChange={(v) => setFilter('hasImages', v)}>Has images</Checkbox>
<Checkbox checked={f.featured} onChange={(v) => setFilter('featured', v)}>Featured</Checkbox>
```

See [filter-framework.md](./filter-framework.md).

## 6. Pagination Report

`PaginationBar` reuses the admin `Pagination` (`src/admin/components/data-grid/Pagination.tsx`) with `page`, `pageSize`, `total`, `onPageChange`, `onPageSizeChange`. It shows a page counter + total results, first/last/jump, and collapses on mobile. Rows-per-page options: 10/25/50/100.

```tsx
// src/admin/modules/products/catalog/PaginationBar.tsx
import { Pagination } from 'src/admin/components/data-grid/Pagination';

<Pagination
  page={page}
  pageSize={pageSize}
  total={total}
  onPageChange={onPageChange}
  onPageSizeChange={onPageSizeChange}
  pageSizeOptions={[10, 25, 50, 100]}
/>;
```

See [pagination-guide.md](./pagination-guide.md).

## 7. Bulk Operations Summary

`bulkActions` defines the selectable row set + a bulk action bar (export, archive, publish — gated by `PermissionGate`). Selection is mock-only: actions are no-ops that surface a toast/confirmation. Real mutation wiring is deferred to Part 3.

```tsx
// src/admin/modules/products/catalog/bulkActions.tsx
import { PermissionGate } from 'src/admin/permissions/PermissionGate';
import { Button } from 'src/design-system/components/core/Button';

<PermissionGate action="bulk_actions" resource="product">
  <Button variant="secondary">Export ({selectedIds.size})</Button>
</PermissionGate>;
```

## 8. Saved Views Summary

`SavedViews` (admin component) provides session-scoped view management. Seven named presets are seeded: **Default, My Products, Published, Drafts, Recently Updated, Out of Stock, Featured** — each is a saved filter+sort+column layout. Views are session-only (no persistence).

```tsx
// src/admin/modules/products/catalog/CatalogToolbar.tsx
import { SavedViews } from 'src/admin/components/data-grid/SavedViews';

<SavedViews
  views={PRESET_VIEWS}
  activeId={activeViewId}
  onSelect={setActiveView}
/>;
```

See [saved-views.md](./saved-views.md).

## 9. Quick Preview Summary

`ProductQuickPreview` is a `Dialog` (role=dialog) drawer showing a gallery, basic info, pricing, category/brand/tags, publishing status, and dates. Inventory/orders/analytics render as placeholders (inventory deferred). No edit affordance.

```tsx
// src/admin/modules/products/catalog/ProductQuickPreview.tsx
import { Dialog } from 'src/design-system/components/feedback/Dialog';
import { LifecycleBadge } from '../lifecycle/LifecycleBadge';

<Dialog role="dialog" aria-modal="true" onClose={onClose} title={product.name}>
  <LifecycleBadge status={product.publishingStatus} />
  {/* gallery, pricing, tags, placeholders */}
</Dialog>;
```

See [quick-preview.md](./quick-preview.md).

## 10. Responsive Validation Report

| Viewport | Table | Cards | Filters | Toolbar |
|----------|:-----:|:-----:|:-------:|:-------:|
| Desktop (≥1280) | full table | 4-col grid | docked left rail | inline |
| Laptop (1024–1279) | full table | 3-col grid | docked / collapsible | inline/wrap |
| Tablet landscape (768–1023) | table→cards collapse | 2-col grid | drawer | wrap |
| Tablet portrait (481–767) | cards | 2-col grid | drawer | stacked |
| Mobile landscape (≤480 l) | compact list | 1-col | overlay sheet | stacked |
| Mobile portrait (≤480) | compact list | 1-col | overlay sheet | stacked |

- Table→card collapse triggers below the `cardViewBreakpoint` (CSS-variable grid `var(--breakpoint-md)`).
- Filters become a `Dialog`/sheet on tablet/mobile; chips remain visible inline.
- Toolbar wraps via `flex-wrap` + `gap: var(--space-inline-xs)`; bulk actions move to a sticky bottom bar on mobile.

## 11. Accessibility Report (WCAG 2.2 AA)

- **Keyboard nav:** rows (`ArrowUp/Down`), checkboxes (`Space`), actions (`Enter`), toolbar (`Tab`); Dialog traps focus and closes on `Esc`.
- **ARIA:** table uses `role="grid"`; filter chips `role="status"`; SearchBar labelled; StatusBadge exposes text; Dialog `aria-modal`, `aria-labelledby`.
- **Focus management:** opening the quick-preview drawer moves focus to the close button; on close, focus returns to the triggering row/action.
- **Screen readers:** status conveyed by text not colour alone; decorative icons `aria-hidden`.
- **Accessible table/filters/search:** all controls labelled; `prefers-reduced-motion` disables skeleton/pulse animations.

## 12. Performance Report

- **Memoization:** `ProductTableView`/`ProductCardView` wrapped in `React.memo`; row components memoised.
- **useMemo filtering:** search + filters + sort computed in a single `useMemo` over `catalogMock` to avoid re-scan on every render.
- **sessionStorage persistence:** view mode, active view, pageSize persisted — no refetch on reload.
- **Lazy loading:** `ProductQuickPreview` and `FilterPanel` can be `React.lazy`-mounted.
- **Virtual-ready:** `ProductTableView` is structured so a `windowing`/virtual-list adapter can be dropped in for 1k+ rows without changing the column contract.

## 13. Documentation Generated

1. `sprint-24-part-2.md` (this report)
2. `product-catalog.md`
3. `search-framework.md`
4. `filter-framework.md`
5. `pagination-guide.md`
6. `saved-views.md`
7. `quick-preview.md`

## 14. Technical Debt

- **Mock-only bulk actions:** export/archive/publish are no-ops — no backend call exists yet.
- **No real API:** all catalog data is static from `catalogMock.ts`; sorting/filtering/pagination are client-side simulations.
- **Session-based saved views:** presets and custom views do not persist across sessions (no user profile/backend).
- **Mock stock status:** `stock`/`outOfStock` flags are derived from mock fields because inventory is a deferred sub-domain (Part 1 placeholder).
- **Preview-only routes:** `/products/catalog` is manifest/illustrative; not the production data-mounted route.
- **Permission gates are UI-only:** `PermissionGate`/`FeatureGate` reflect mock `usePermissions` — no server-side enforcement.

## 15. Recommendations for Sprint 24 Part 3 (Enterprise Product Creation Wizard)

1. Build the creation wizard reusing `ProductLayout` + `ProductWorkspace` tabs, scaffolding from the Part 1 `Product` entity model.
2. Replace `catalogMock` with server-backed query/mutation, keeping the `useCatalogState` contract intact.
3. Promote bulk actions to real mutations (export, archive, publish) behind `PermissionGate`.
4. Persist saved views to the user profile / backend instead of `sessionStorage`.
5. Wire real inventory into stock status via the deferred Inventory sub-domain.
6. Add `React.lazy` + virtualization for the table when row counts exceed a few hundred.
7. Extend quick preview with real orders/analytics panels once those sub-domains land.
8. Mount `/products/catalog` from the Part 1 route manifest into the app router (no framework change).
