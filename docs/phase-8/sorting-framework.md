# Enterprise Sorting Framework

## Overview

Sorting is integrated into the `EnterpriseTable` and `DataGridProvider`. Column headers become clickable when `sortable` is enabled on the column and at the grid level.

## How It Works

1. Click a column header to sort ascending
2. Click again to sort descending
3. Click a third time to remove sort
4. Sort state is synced to URL (`?s=name:asc,role:desc`)
5. Multi-column sorting supported (comma-separated in URL)

## Configuration

```tsx
// Grid-level
<DataGrid
  sortable={true}  // enables sorting across all sortable columns
  columns={[
    { key: 'name', header: 'Name', sortable: true },
    { key: 'email', header: 'Email', sortable: true },
    { key: 'role', header: 'Role', sortable: false },  // not sortable
  ]}
/>

// Default sort on load
<DataGridProvider
  // Pass defaultSort via useQueryState options
>
```

## Programmatic Access

```tsx
const { sort, setSort } = useDataGrid();

// Set single sort
setSort([{ key: 'name', direction: 'asc' }]);

// Toggle existing sort
setSort((prev) => {
  const existing = prev.find(s => s.key === 'name');
  if (!existing) return [{ key: 'name', direction: 'asc' }];
  if (existing.direction === 'asc') return [{ key: 'name', direction: 'desc' }];
  return [];
});
```

## Accessibility

- Sortable headers have `cursor: pointer`
- Active sort shown with chevron icon
- `aria-sort` attribute set on header (`"ascending"` / `"descending"`)
- Keyboard accessible (click interaction)

## Performance

- Sort state changes reset to page 1
- Multi-column sort serialized in URL
- Stable sort (preserves original order for equal values)
