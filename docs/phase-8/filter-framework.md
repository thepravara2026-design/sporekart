# Enterprise Filter Framework

## FilterBar

Collapsible panel that renders filter controls based on column `filterType`.

```tsx
<FilterBar />
```

Must be used inside `DataGridProvider`. Filters auto-generate from column definitions with `filterable: true` and `filterType` set.

### Filter Types

| Type | Component | Description |
|------|-----------|-------------|
| `dropdown` | `DropdownFilter` | Single-select dropdown with "All" option |
| `checkbox` | `CheckboxFilter` | Multi-select checkboxes with "Clear" |
| `radio` | `RadioFilter` | Single-select radio group |
| `dateRange` | `DateRangeFilter` | From/to date inputs |
| `tag` | `TagFilter` | Toggleable tag chips |
| `multiSelect` | `MultiSelectFilter` | Dropdown with checkbox list |
| `boolean` | `BooleanFilter` | Yes/No/All segmented toggle |

### Enabling Filters on Columns

```tsx
const columns: DataGridColumn<User>[] = [
  {
    key: 'role',
    header: 'Role',
    filterable: true,
    filterType: 'dropdown',
    filterOptions: [
      { label: 'Admin', value: 'admin' },
      { label: 'Editor', value: 'editor' },
    ],
  },
  {
    key: 'department',
    header: 'Department',
    filterable: true,
    filterType: 'multiSelect',
    filterOptions: [
      { label: 'Engineering', value: 'engineering' },
      { label: 'Marketing', value: 'marketing' },
    ],
  },
  {
    key: 'joinDate',
    header: 'Join Date',
    filterable: true,
    filterType: 'dateRange',
  },
];
```

### Behavior
- Reset button clears all filters and returns to page 1
- Active filter count shown in badge
- Collapsible to reduce visual clutter
- Filters reset to page 1 on any change
- Each filter type handles its own clear/reset internally
