# Enterprise Query State Management

## useQueryState

URL-synced state hook that manages search, filters, sorting, pagination, column configuration, and row selection.

```tsx
import { useQueryState } from '../hooks/useQueryState';

function MyComponent() {
  const queryState = useQueryState(
    columnDefs,  // { key, header, sortable, filterable, resizable }[]
    {
      defaultPageSize: 25,
      defaultSort: [{ key: 'name', direction: 'asc' }],
      defaultFilters: [],
    }
  );
}
```

## State Shape

```tsx
interface QueryState {
  search: string;
  filters: FilterConfig[];
  sort: SortConfig[];
  page: number;
  pageSize: number;
  columnConfig: ColumnConfig[];
  selectedRows: Set<string>;
  viewId: string | null;
}
```

## URL Synchronization

| URL Param | State | Example |
|-----------|-------|---------|
| `q` | search | `?q=admin` |
| `f` | filters | `?f=[{"id":"role","value":"admin"}]` |
| `s` | sort | `?s=name:asc,role:desc` |
| `p` | page | `?p=3` |
| `ps` | pageSize | `?ps=50` |
| `c` | columns | `?c=[{"k":"name","v":true,"o":0}]` |

### Behavior
- URL updates are debounced (150ms) to prevent excessive history entries
- Uses `replace: true` to avoid cluttering browser history
- Page resets to 1 when search, filters, or pageSize change
- Page size persisted in localStorage

## Return Value

```tsx
const {
  queryState,        // Current full state
  search, setSearch,
  filters, setFilters,
  sort, setSort,
  page, setPage,
  pageSize, setPageSize,
  columnConfig, setColumnConfig,
  visibleColumns,    // Filtered + sorted by order
  orderedColumns,    // All columns sorted by order
  selectedRows, toggleRowSelection, selectAllRows, clearSelection,
  viewId, setViewId,
  resetAll,          // Restore all defaults
  paginatedData,     // { start, end } for slicing
} = useQueryState(columnDefs, options);
```

## Integration with DataGridProvider

`DataGridProvider` wraps `useQueryState` internally and exposes the full API via `useDataGrid()` context hook. All child components (FilterBar, EnterpriseTable, DataGridPagination, etc.) consume this context.
