# Table Component

## Overview

The Table component provides an enterprise-grade data table with sorting, selection, pagination, expandable rows, sticky headers, and full keyboard navigation. Supports loading, empty, and error states.

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| columns | `Column<T>[]` | — | Column definitions |
| data | `T[]` | — | Row data |
| variant | `'default' \| 'striped' \| 'bordered' \| 'minimal'` | `'default'` | Visual variant |
| size | `'sm' \| 'md' \| 'lg'` | `'md'` | Row density |
| sortable | `boolean` | `false` | Enable column sorting |
| defaultSortColumn | `string` | — | Initial sort column key |
| defaultSortDirection | `'asc' \| 'desc'` | `'asc'` | Initial sort direction |
| selectable | `boolean` | `false` | Enable row selection |
| selectedRows | `Set<string>` | — | Controlled selected rows |
| onSelectionChange | `(selected: Set<string>) => void` | — | Selection callback |
| pagination | `PaginationConfig` | — | Pagination configuration |
| loading | `boolean` | `false` | Loading state |
| emptyState | `EmptyStateProps` | — | Custom empty state |
| error | `{ message: string; onRetry: () => void }` | — | Error state |
| expandable | `boolean` | `false` | Enable expandable rows |
| expandedRender | `(row: T) => ReactNode` | — | Expanded content renderer |
| stickyHeader | `boolean` | `false` | Stick header on scroll |
| maxHeight | `string` | — | Max height before scroll |
| onRowClick | `(row: T) => void` | — | Row click handler |
| className | `string` | — | Additional CSS classes |

## Column Definition Interface

```ts
interface Column<T> {
  key: string;
  header: string;
  accessor: (row: T) => ReactNode;
  sortKey?: string;
  sortFn?: (a: T, b: T) => number;
  width?: string;
  minWidth?: string;
  align?: 'left' | 'center' | 'right';
  hidden?: boolean;
  cellClassName?: string;
  headerClassName?: string;
}
```

## Sorting

- Click column header to toggle sort direction (`asc` → `desc` → none)
- Shift+Click for multi-column sort
- Sort indicator arrow shown on active column header
- Custom sort functions supported via `sortFn` on column def

## Selection

- Header checkbox selects/deselects all visible rows
- Individual row checkboxes for partial selection
- `selectedRows` set tracks selected row IDs
- `onSelectionChange` fires on any selection change
- Indeterminate checkbox state when some (not all) rows selected

## Pagination

```ts
interface PaginationConfig {
  page: number;
  pageSize: number;
  total: number;
  onPageChange: (page: number) => void;
  onPageSizeChange: (size: number) => void;
  pageSizeOptions?: number[];
}
```

| Feature | Description |
|---------|-------------|
| Page controls | Previous, Next, page numbers with ellipsis |
| Page size | Dropdown to change rows per page |
| Total count | Displayed as "Showing X–Y of Z" |
| First/Last | Quick jump to first/last page |

## States

| State | Behavior |
|-------|----------|
| Loading | Skeleton rows (same column count as data) with shimmer animation |
| Empty | Centered empty state in table body (uses EmptyState component) |
| Error | Error banner with retry button above table |
| Data | Normal table rendering |

## Expandable Rows

- Click row expand chevron to toggle expanded content
- Expanded content renders via `expandedRender` prop
- Multiple rows can be expanded simultaneously
- Animated expand/collapse with `max-height` transition

## Responsive Behavior

On viewports below `768px`, the table enables horizontal scroll:

- Wrapper div with `overflow-x: auto`
- Table maintains fixed column widths
- First column (typically label/name) remains left-aligned
- Scroll indicator shown on right edge

## Keyboard Navigation

| Key | Action |
|-----|--------|
| `Tab` / `Shift+Tab` | Navigate between focusable elements in header |
| `Arrow Up` / `Arrow Down` | Navigate rows |
| `Space` | Toggle row selection (when selectable) |
| `Enter` | Activate row click / expand row |
| `Arrow Right` / `Arrow Left` | Expand/collapse row |
| `Home` / `End` | Jump to first/last row |
| `Page Up` / `Page Down` | Scroll page up/down |
| `Ctrl+A` | Select all rows |

## ARIA Attributes

| Attribute | Usage |
|-----------|-------|
| `role="table"` | Table element |
| `role="columnheader"` | Header cells |
| `role="row"` | Each row (header and body) |
| `aria-sort` | Sort state on active column header |
| `aria-selected` | Row selection state |
| `aria-expanded` | Expandable row state |
| `aria-controls` | Expanded content association |
| `aria-rowindex` | Row position in table |
| `aria-colindex` | Column position |
| `aria-labelledby` | Table caption association |

## Design Tokens

| Token | Mapping |
|-------|---------|
| `--table-bg` | `--color-surface` |
| `--table-border` | `--color-border-subtle` |
| `--table-header-bg` | `--color-surface-secondary` |
| `--table-header-text` | `--color-text-secondary` |
| `--table-row-hover` | `--color-surface-hover` |
| `--table-row-selected` | `--color-primary-subtle` |
| `--table-stripe` | `--color-surface-striped` |
| `--table-radius` | `--radius-md` |
| `--table-cell-padding` | `--spacing-sm` / `--spacing-md` |

## Usage Example

```tsx
<Table
  columns={[
    { key: 'name', header: 'Name', accessor: (r) => r.name, sortKey: 'name' },
    { key: 'status', header: 'Status', accessor: (r) => <Badge label={r.status} /> },
    { key: 'amount', header: 'Amount', accessor: (r) => `₹${r.amount}`, align: 'right' },
  ]}
  data={orders}
  sortable
  selectable
  pagination={{
    page: 1,
    pageSize: 10,
    total: 100,
    onPageChange: setPage,
    onPageSizeChange: setPageSize,
  }}
  stickyHeader
  maxHeight="600px"
  variant="striped"
  onRowClick={(row) => navigate(`/orders/${row.id}`)}
/>
```
