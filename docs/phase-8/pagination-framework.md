# Enterprise Pagination Framework

## DataGridPagination

Full-featured pagination component used inside `EnterpriseTable` and compatible with `DataGridProvider`.

```tsx
import { DataGridProvider } from '../components/data-grid';
import { DataGridPagination } from '../components/table';

<DataGridProvider columns={columns} data={data}>
  <DataGridPagination />
</DataGridProvider>
```

## Features

| Feature | Description |
|---------|-------------|
| First/Last | Jump to first or last page |
| Prev/Next | Navigate one page at a time |
| Page Numbers | Smart ellipsis for large page counts |
| Page Size | Selector: 10, 20, 50, 100 |
| Record Count | "Showing 21–40 of 1,248 records" |
| Go-to-Page | Direct page number input with Enter |
| Keyboard | ArrowLeft, ArrowRight, Home, End |
| URL Sync | `?p=3&ps=50` |
| Persistence | Page size saved to `localStorage` |

## Display Format

```
Showing 21–40 of 1,248 records    [10/page ▼]
  [«] [‹] [1] […] [3] [4] [5] […] [50] [›] [»]
  Go to [___]
```

## Props

Rendered automatically by `DataGridProvider`. No direct props needed.

## Configuring Page Size Options

```tsx
<DataGridProvider columns={columns} data={data}>
  {/* Defaults: [10, 20, 50, 100] */}
  <DataGridPagination />
</DataGridProvider>
```

## Accessibility

- `role="navigation"` with `aria-label="Pagination"`
- Each page button: `aria-label="Page {n}"` and `aria-current="page"`
- First/Prev/Next/Last buttons have `aria-label`
- Keyboard navigation: ArrowLeft, ArrowRight, Home, End
- Page size selector: `aria-label="Records per page"`
- Go-to-page: supports Enter key
