# Table Template — DataGrid Usage Standard

`DataGrid` is a composite, memoized component that internally provides the `DataGridProvider` context. **Use `DataGrid` — never `EnterpriseTable` standalone** (it requires the context that only `DataGrid` supplies).

```ts
import { DataGrid } from '../components/data-grid/DataGrid';
import type { DataGridColumn, FilterConfig } from '../components/data-grid/types';
```

## Column definition

`DataGridColumn<T>`:

| Field | Type | Notes |
| --- | --- | --- |
| `key` | `string` | stable field key |
| `header` | `string` | column label |
| `render` | `(row: T, index: number) => React.ReactNode` | custom cell |
| `sortable` | `boolean` | enable sort |
| `filterable` | `boolean` | enable filter |
| `filterType` | `FilterConfig['type']` | `'text' \| 'select' \| 'date' \| 'number' \| 'boolean'` |
| `filterOptions` | `{ label: string; value: string }[]` | for `select` |
| `width` / `minWidth` / `maxWidth` | `string` | **px string**, e.g. `'200px'` |
| `pinned` | `'left' \| 'right' \| false` | sticky column |
| `resizable` | `boolean` | resizable column |
| `hidden` | `boolean` | hide column |
| `align` | `'left' \| 'center' \| 'right'` | cell alignment |
| `cellStyle` / `headerStyle` | `React.CSSProperties` | overrides |

## Example columns config (4–5 columns)

```tsx
import type { DataGridColumn } from '../components/data-grid/types';

interface ProductRow {
  id: string;
  name: string;
  status: 'Active' | 'Draft' | 'Archived';
  category: string;
  price: number;
  stock: number;
}

export const productColumns: DataGridColumn<ProductRow>[] = [
  { key: 'id', header: 'ID', width: '80px', sortable: true, pinned: 'left' },
  {
    key: 'name', header: 'Name', width: '200px', sortable: true, filterable: true,
    filterType: 'text',
  },
  {
    key: 'status', header: 'Status', width: '120px', sortable: true, filterable: true,
    filterType: 'select',
    filterOptions: [
      { label: 'Active', value: 'Active' },
      { label: 'Draft', value: 'Draft' },
      { label: 'Archived', value: 'Archived' },
    ],
    render: (row) => (
      <span style={{ color: row.status === 'Active' ? 'var(--color-success)' : 'var(--color-text-tertiary)' }}>
        {row.status}
      </span>
    ),
  },
  {
    key: 'category', header: 'Category', width: '140px', filterable: true,
    filterType: 'select',
    filterOptions: [
      { label: 'Electronics', value: 'Electronics' },
      { label: 'Clothing', value: 'Clothing' },
    ],
  },
  { key: 'price', header: 'Price', width: '100px', sortable: true, align: 'right' },
];
```

## DataGrid props in use

```tsx
<DataGrid<ProductRow>
  columns={productColumns}
  data={rows}
  total={rows.length}
  loading={loading}
  emptyMessage="No products found"
  emptyDescription="Adjust filters or create a new product"
  pageSize={10}
  pageSizeOptions={[10, 25, 50]}
  sortable
  filterable
  searchable
  exportable
  resizableColumns
  stickyHeader
  stickyColumns
  selectable
  rowKey="id"                 // or (row) => row.id
  onRowClick={(row) => openDetails(row)}
  cardViewBreakpoint={768}    // mobile card mode
  renderCard={(row) => (
    <div style={{ padding: 12 }}>
      <strong style={{ color: 'var(--color-text-primary)' }}>{row.name}</strong>
      <div style={{ color: 'var(--color-text-secondary)' }}>{row.status}</div>
    </div>
  )}
/>
```

## Rules

- `width` must be a **px string** (`'200px'`), not a number. `minWidth`/`maxWidth` likewise.
- `rowKey` must be a stable string or function — required for selection and virtualization correctness.
- Provide `emptyMessage` + `emptyDescription` for every grid.
- Lift sort/filter/page/search state to the page with `useQueryState` (`../hooks/useQueryState`).
- For mobile, set `cardViewBreakpoint` and `renderCard`; never build a separate table manually.
