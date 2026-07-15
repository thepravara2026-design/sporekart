# Enterprise Search Framework

## SearchBar

Debounced text search for page-level filtering.

```tsx
import { SearchBar } from '../components/search';

<SearchBar
  value={search}
  onChange={setSearch}
  placeholder="Search records..."
  debounceMs={300}
  instant={false}
/>
```

### Props
| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `value` | `string` | required | Current search value |
| `onChange` | `(value: string) => void` | required | Change handler |
| `placeholder` | `string` | "Search..." | Input placeholder |
| `debounceMs` | `number` | 300 | Debounce delay in ms |
| `instant` | `boolean` | `false` | Skip debounce (immediate onChange) |
| `autoFocus` | `boolean` | `false` | Auto-focus on mount |

### Behavior
- Escape key clears the search
- Enter key fires immediate onChange (bypasses debounce)
- Clear button appears when value is non-empty

## GlobalSearch

Combobox-style global search with Ctrl+K shortcut, categorized suggestions, and recent searches.

```tsx
import { GlobalSearch } from '../components/search';

<GlobalSearch
  onSearch={(query) => handleSearch(query)}
  suggestions={[
    { id: '1', label: 'Dashboard', category: 'Pages', icon: 'layout' },
    { id: '2', label: 'Create Product', category: 'Actions', icon: 'plus' },
  ]}
  recentSearches={['Dashboard settings', 'User permissions']}
/>
```

### Props
| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `onSearch` | `(query: string) => void` | required | Search submission handler |
| `suggestions` | `Suggestion[]` | `[]` | Suggestions with category grouping |
| `recentSearches` | `string[]` | `[]` | Recent search history |
| `placeholder` | `string` | "Search anything... (Ctrl+K)" | Input placeholder |

### Keyboard Navigation
| Key | Action |
|-----|--------|
| `Ctrl+K` | Open/focus search |
| `ArrowDown` | Next suggestion |
| `ArrowUp` | Previous suggestion |
| `Enter` | Select suggestion or submit query |
| `Escape` | Close panel |
