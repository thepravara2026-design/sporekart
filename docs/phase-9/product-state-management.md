# Product State Management

> Foundation doc for [Sprint 24 Part 1](./sprint-24-part-1.md). Code: `src/admin/modules/products/state`. Mock Mode.

## State owned by the module

```tsx
// src/admin/modules/products/state/product.state.ts (foundation)
import type { Product, ProductLifecycleState } from '../types/product.types';
import type { ProductWorkspaceSection } from '../workspace/ProductWorkspace';

export interface ProductState {
  currentProduct: Product | null;
  currentWorkspace: ProductWorkspaceSection;
  filters: FilterConfig[];           // reuses DataGrid FilterConfig
  currentView: string;
  selectedProducts: Set<string>;
  lifecycleState: ProductLifecycleState;
  loading: boolean;
  empty: boolean;
  error: string | null;
  search: string;
  pagination: { page: number; pageSize: number; total: number };
  futureApiState: null;               // slot for server data in Part 2
}
```

## Provider + hooks

```tsx
// src/admin/modules/products/state/ProductProvider.tsx (foundation)
import { createContext, useContext, useReducer } from 'react';

const ProductContext = createContext<ProductState | null>(null);

export const useProductState = () => {
  const ctx = useContext(ProductContext);
  if (!ctx) throw new Error('useProductState must be used within ProductProvider');
  return ctx;
};
```

Supporting selectors/hooks: `useProductFilters`, `useProductSelection`, `useProductLifecycle`.

## Operational patterns

- **loading / empty / error:** render `Skeleton` (design-system display) during `loading`, an empty-state
  `Card` when `empty`, and a feedback `Card` when `error` is set. No backend calls in the foundation.
- **search / pagination:** kept in module state and **synced to `DataGrid` query state** (see below).

## Integration with DataGrid query state

`DataGrid` already exposes a `QueryState` (`search`, `filters`, `sort`, `page`, `pageSize`, `columnConfig`,
`selectedRows`) via `DataGridProvider` / `useDataGrid` — `src/admin/components/data-grid/DataGrid.tsx`.

```tsx
// Bridge: module state <-> DataGrid query state
const { search, setSearch, page, setPage, pageSize, setPageSize, selectedRows } = useDataGrid<Product>();
// mirror into ProductState for cross-component access
```

This keeps the grid's internal query state as the source of truth for table interactions while the module
context exposes a stable snapshot for the workspace, dashboard, and toolbar.

## Future API-state extension

`futureApiState: null` is the reserved slot. In Part 2 it becomes a server-state object (query/mutation/cache)
without changing the rest of the contract — selectors and the DataGrid bridge remain unchanged.

## Real components used

- `DataGrid` + `DataGridProvider` / `useDataGrid` — `src/admin/components/data-grid/DataGrid.tsx`
- `Skeleton` — `src/design-system/components/display/Skeleton.tsx`
- `Card` — `src/design-system/components/composite/Card.tsx`
