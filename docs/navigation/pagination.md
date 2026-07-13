# Pagination

## Overview

Pagination splits large data sets into pages. It supports standard and compact variants, page size selection, page jumping, sibling count, ellipsis, and full keyboard navigation.

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `currentPage` | `number` | — | Current active page (1-indexed) |
| `totalPages` | `number` | — | Total number of pages |
| `onPageChange` | `(page: number) => void` | — | Page change callback |
| `variant` | `'standard' \| 'compact'` | `'standard'` | Layout variant |
| `siblingCount` | `number` | `1` | Pages to show on each side of current |
| `showFirstLast` | `boolean` | `true` | Show first/last page buttons |
| `showPrevNext` | `boolean` | `true` | Show prev/next buttons |
| `showPageSize` | `boolean` | `false` | Show page size selector |
| `showPageJump` | `boolean` | `false` | Show direct page input |
| `disabled` | `boolean` | `false` | Disable all interactions |
| `className` | `string` | — | Additional CSS classes |

## PageSizeSelector

Dropdown to change the number of items per page.

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `pageSize` | `number` | — | Current page size |
| `options` | `number[]` | `[10, 20, 50, 100]` | Size options |
| `onChange` | `(size: number) => void` | — | Size change callback |

## PageJump

Direct input field to navigate to a specific page.

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `currentPage` | `number` | — | Current page |
| `totalPages` | `number` | — | Total pages |
| `onJump` | `(page: number) => void` | — | Jump callback |

## Sibling Count and Ellipsis

`siblingCount` controls the number of page buttons on each side of the current page. Ellipsis (`...`) is shown when there are gaps:

```
[<] [1] [...] [4] [5] [6] [...] [20] [>]
   siblingCount=1, totalPages=20, currentPage=5
```

## Prev/Next and First/Last Buttons

| Button | Condition | Description |
|--------|-----------|-------------|
| `First` | `showFirstLast` and `currentPage > 1` | Go to page 1 |
| `Prev` | `showPrevNext` and `currentPage > 1` | Go to previous page |
| `Next` | `showPrevNext` and `currentPage < totalPages` | Go to next page |
| `Last` | `showFirstLast` and `currentPage < totalPages` | Go to last page |

## Responsive Behavior

| Breakpoint | Behavior |
|------------|----------|
| `xs`, `sm` | Compact variant: prev/next only or page numbers hidden |
| `md+` | Standard variant with full page numbers |

## Keyboard Navigation

| Key | Action |
|-----|--------|
| `Arrow Left` | Previous page |
| `Arrow Right` | Next page |
| `Home` | First page |
| `End` | Last page |
| `Enter` | Activate focused page button |

## ARIA Attributes

| Attribute | Usage |
|-----------|-------|
| `nav` | Wrapper with `aria-label="Pagination"` |
| `aria-current="page"` | Current page button |
| `aria-label` | Prev/Next/First/Last buttons |
| `aria-disabled` | Disabled buttons |
| `role="navigation"` | Navigation landmark |

## Design Tokens Used

| Token | Usage |
|-------|-------|
| `--color-pagination-active` | Active page background |
| `--color-pagination-hover` | Hover background |
| `--color-pagination-disabled` | Disabled color |
| `--color-pagination-text` | Text color |
| `--spacing-pagination` | Gap between items |
| `--radius-pagination` | Button border radius |
| `--font-size-pagination` | Page number size |
