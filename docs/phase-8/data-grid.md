# Enterprise Data Grid

## Overview

`DataGrid` is the main composite component. It combines search, filters, table, pagination, column management, export, and saved views into a single cohesive interface.

## Usage

```tsx
import { DataGrid } from '../components/data-grid';
import type { DataGridColumn } from '../components/data-grid';

interface User {
  id: number;
  name: string;
  email: string;
  role: string;
  status: string;
}

const columns: DataGridColumn<User>[] = [
  { key: 'id', header: 'ID', width: '60px', sortable: true },
  { key: 'name', header: 'Name', sortable: true, filterable: true, filterType: 'dropdown' },
  { key: 'email', header: 'Email', sortable: true },
  { key: 'role', header: 'Role', sortable: true, filterable: true, filterType: 'dropdown', filterOptions: [
    { label: 'Admin', value: 'admin' },
    { label: 'Editor', value: 'editor' },
  ]},
  { key: 'status', header: 'Status' },
];

<DataGrid
  columns={columns}
  data={users}
  selectable
  sortable
  filterable
  searchable
  exportable
  resizableColumns
  stickyHeader
  emptyMessage="No users found"
  emptyDescription="Try adjusting your search or filters."
/>
```

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `columns` | `DataGridColumn[]` | required | Column definitions |
| `data` | `T[]` | required | Data rows |
| `total` | `number` | `data.length` | Total record count (for server-side) |
| `loading` | `boolean` | `false` | Show skeleton loading |
| `emptyMessage` | `string` | "No data found" | Empty state title |
| `emptyDescription` | `string` | — | Empty state description |
| `pageSize` | `number` | 25 | Default page size |
| `sortable` | `boolean` | `true` | Enable column sorting |
| `filterable` | `boolean` | `true` | Enable filter bar |
| `selectable` | `boolean` | `true` | Enable row selection |
| `searchable` | `boolean` | `true` | Show search bar |
| `exportable` | `boolean` | `true` | Show export button |
| `resizableColumns` | `boolean` | `true` | Enable column resize |
| `stickyHeader` | `boolean` | `true` | Sticky table header |
| `rowKey` | `string \| function` | `'id'` | Unique row identifier |
| `onRowClick` | `function` | — | Row click handler |
| `renderCard` | `function` | — | Card render for card view |

## DataGridColumn

| Prop | Type | Description |
|------|------|-------------|
| `key` | `string` | Unique column identifier (maps to row data) |
| `header` | `string` | Column header text |
| `render` | `(row, index) => ReactNode` | Custom cell renderer |
| `sortable` | `boolean` | Enable sorting for this column |
| `filterable` | `boolean` | Enable filtering for this column |
| `filterType` | `FilterConfig['type']` | Filter UI type |
| `filterOptions` | `{label, value}[]` | Filter options |
| `width` | `string` | Column width |
| `pinned` | `'left' \| 'right' \| false` | Pin column |
| `resizable` | `boolean` | Allow resize |
| `align` | `'left' \| 'center' \| 'right'` | Text alignment |
| `cellStyle` | `CSSProperties` | Cell style overrides |

## Context API

Use `useDataGrid()` hook inside `DataGridProvider` children to access query state programmatically:

```tsx
const { search, setSearch, filters, setFilters, sort, setSort, page, setPage, resetAll } = useDataGrid();
```
