# Pagination Guide

> Pagination doc for [Sprint 24 Part 2](./sprint-24-part-2.md). Code: `src/admin/modules/products/catalog`. Mock Mode.

## Overview

`PaginationBar` wraps the admin `Pagination` component
(`src/admin/components/data-grid/Pagination.tsx`). Pagination is purely client-side over the
pre-filtered/sorted `results` array from `useCatalogState`.

## API (reuse contract)

```tsx
// src/admin/modules/products/catalog/PaginationBar.tsx
import { Pagination } from 'src/admin/components/data-grid/Pagination';

<Pagination
  page={page}                 // 1-based current page
  pageSize={pageSize}         // rows per page
  total={total}               // total matching results
  onPageChange={onPageChange}
  onPageSizeChange={onPageSizeChange}
  pageSizeOptions={[10, 25, 50, 100]}
/>;
```

| Prop | Type | Notes |
|------|------|-------|
| `page` | `number` | current page (1-based) |
| `pageSize` | `number` | rows per page |
| `total` | `number` | total results (post filter/sort) |
| `onPageChange` | `(p: number) => void` | clamped to `[1, totalPages]` |
| `onPageSizeChange` | `(n: number) => void` | resets to page 1 |

## Page Counter & Total Results

`PaginationBar` renders a textual counter using design tokens:

```tsx
// "Showing 1–25 of 312 products"
const start = (page - 1) * pageSize + 1;
const end = Math.min(page * pageSize, total);
<span className="pagination__counter">
  Showing {start}–{end} of {total} products
</span>
```

## First / Last / Jump

- **First / Previous** disabled on page 1; **Next / Last** disabled on the final page.
- **Jump** input lets a user type a page number (validated, clamped).
- Page numbers render with ellipsis for large page counts.

## Responsive

| Viewport | Behaviour |
|----------|-----------|
| Desktop / Laptop | full pager + counter inline |
| Tablet | pager wraps; counter above |
| Mobile (≤480) | pager collapses to `Prev / Page x of y / Next` + compact page-size selector |

Styled via `--space-inline-xs`, `--radius-control`, `--text-secondary`.

## Rows-per-Page Options

`pageSizeOptions={[10, 25, 50, 100]}` (default 25). The chosen `pageSize` is persisted to
`sessionStorage` (`sk.catalog.pageSize`) so it survives reload.

## Performance Note

Pagination is a slice of the `useMemo` `sorted` array — changing page does not re-run search/filter/sort,
only a `.slice()` over already-computed data.

Related: [product-catalog.md](./product-catalog.md), [filter-framework.md](./filter-framework.md).
