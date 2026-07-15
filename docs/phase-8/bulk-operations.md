# Enterprise Bulk Operations

## BulkActions

Appears above the table when rows are selected. Shows selection count and action buttons.

```tsx
import { BulkActions } from '../components/table';

<BulkActions />
```

Must be used inside `DataGridProvider` with `selectable: true`.

## Features

| Feature | Description |
|---------|-------------|
| Selection Count | Shows "X selected" with checkbox icon |
| Clear Selection | Deselects all rows |
| Bulk Export | Export selected rows (placeholder) |
| Bulk Update | Update selected rows (placeholder) |
| Bulk Delete | Delete selected rows (placeholder with error color) |

## Row Selection

### Select All
- Checkbox in header toggles all visible rows
- Supports indeterminate state (some selected)

### Individual Selection
- Each row has a checkbox
- Clicking checkbox stops propagation (does not trigger row click)

### Programmatic Access

```tsx
const { selectedRows, toggleRowSelection, selectAllRows, clearSelection } = useDataGrid();

// selectedRows is a Set<string> of row keys
// toggleRowSelection(key) toggles a single row
// selectAllRows(keys) selects all given keys
// clearSelection() deselects all
```

## Context Menu

Right-click on any row opens a context menu with:
- Edit
- Duplicate
- View Details
- Delete (separator + danger color)

Closes on click outside or on `mouseleave`.

## Configuration

```tsx
<DataGrid
  selectable={true}
  rowKey="id"   // or (row) => row.customId
/>
```
