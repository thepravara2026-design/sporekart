# Empty State Component

## Overview

Empty state displays placeholder content when no data is available. The system provides 8 built-in types with default content following the **Explain → Guide → Act** formula.

## Built-in Types

| Type | Default Title | Default Description | Default Action |
|------|---------------|---------------------|----------------|
| general | "No data available" | "There's nothing to display yet." | "Add Data" |
| search | "No results found" | "Try adjusting your search or filters." | "Clear Filters" |
| cart | "Your cart is empty" | "Browse products and add items to your cart." | "Browse Products" |
| orders | "No orders yet" | "Place your first order to get started." | "Place Order" |
| products | "No products listed" | "Add your first product to the marketplace." | "Add Product" |
| notifications | "All caught up" | "You have no new notifications." | — |
| favorites | "No favorites saved" | "Save items to your favorites for quick access." | "Browse Favorites" |
| messages | "No messages" | "Start a conversation to see messages here." | "New Message" |

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| type | `'general' \| 'search' \| 'cart' \| 'orders' \| 'products' \| 'notifications' \| 'favorites' \| 'messages'` | `'general'` | Predefined type |
| title | `string` | Type default | Custom title |
| description | `string` | Type default | Custom description |
| icon | `IconName` | Type default | Custom icon |
| action | `{ label: string; onClick: () => void }` | Type default | Call to action |
| compact | `boolean` | `false` | Compact variant (smaller padding, smaller icon) |
| className | `string` | — | Additional CSS classes |

## Compact Mode

When `compact={true}`, the empty state renders with reduced padding (`--spacing-md` instead of `--spacing-lg`), a smaller icon (48px vs 80px), and smaller font sizes. Suitable for embedding within cards and tables.

## Usage Examples

```tsx
// Default search empty state
<EmptyState type="search" />

// Custom empty state
<EmptyState
  type="general"
  title="No farms found"
  description="No farms match your current filters. Try adjusting your criteria."
  action={{ label: "Reset Filters", onClick: resetFilters }}
  compact
/>

// Cart empty state
<EmptyState
  type="cart"
  action={{ label: "Start Shopping", onClick: () => navigate('/shop') }}
/>
```

## Design Tokens

| Token | Mapping |
|-------|---------|
| `--empty-state-padding` | `--spacing-lg` (`--spacing-md` compact) |
| `--empty-state-icon-size` | `80px` (48px compact) |
| `--empty-state-icon-color` | `--color-text-disabled` |
| `--empty-state-title-font` | `--font-size-lg` |
| `--empty-state-title-color` | `--color-text-primary` |
| `--empty-state-desc-font` | `--font-size-sm` |
| `--empty-state-desc-color` | `--color-text-secondary` |
| `--empty-state-gap` | `--spacing-md` |
