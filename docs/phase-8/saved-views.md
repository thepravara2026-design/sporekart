# Enterprise Saved Views

## SavedViews

Save, load, and delete view configurations (search, sort, filters, columns, page size).

```tsx
import { SavedViews } from '../components/saved-views';

<SavedViews
  views={savedViews}
  onSave={handleSaveView}
  onLoad={handleLoadView}
  onDelete={handleDeleteView}
/>
```

## Features

| Feature | Description |
|---------|-------------|
| Save | Current query state saved to localStorage |
| Load | Restore a saved view configuration |
| Delete | Remove a saved view |
| Pinned | Views marked as pinned appear at top |
| Default | Default view shown separately with star icon |

## Storage

Views are persisted in `localStorage` under the key `dg_saved_views`:

```json
[
  {
    "id": "view-1680000000000",
    "name": "Active Users",
    "queryState": {
      "search": "admin",
      "sort": [{"key": "name", "direction": "asc"}],
      "page": 1,
      "pageSize": 50,
      "columnConfig": [...]
    },
    "pinned": true,
    "recent": true
  }
]
```

## Integration with DataGrid

The `DataGrid` component includes `SavedViews` in its toolbar and wires save/load automatically:

```tsx
<DataGrid columns={columns} data={data}>
  {/* SavedViews appears as "Views" button */}
</DataGrid>
```

## API

```tsx
interface SavedView {
  id: string;
  name: string;
  queryState: Partial<QueryState>;
  pinned?: boolean;
  recent?: boolean;
  default?: boolean;
}
```

## Future Enhancements
- Rename views
- Duplicate views
- Share views with other users
- Server-side persistence
- View categories/folders
