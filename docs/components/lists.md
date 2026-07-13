# List Component

## Overview

The List component renders ordered or unordered lists in multiple visual variants. Supports both static and interactive modes.

## List Variants

| Variant | Description |
|---------|-------------|
| simple | Plain text items with optional dividers |
| icon | Each item has a leading icon |
| description | Each item has a title and description |
| media | Each item includes a thumbnail/avatar |
| interactive | Clickable items with hover and selected states |

## ListItem Interface

```ts
interface ListItem {
  id: string;
  label: string;
  description?: string;
  icon?: IconName;
  media?: { src: string; alt: string };
  badge?: { label: string; variant: BadgeVariant };
  disabled?: boolean;
  meta?: string;
  onClick?: () => void;
  href?: string;
}
```

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| variant | `'simple' \| 'icon' \| 'description' \| 'media' \| 'interactive'` | `'simple'` | Visual variant |
| items | `ListItem[]` | — | Array of list items |
| ordered | `boolean` | `false` | Render as `<ol>` instead of `<ul>` |
| divided | `boolean` | `false` | Show dividers between items |
| selectable | `boolean` | `false` | Enable selection with radio/checkbox |
| selected | `string[]` | — | Array of selected item IDs |
| onSelectionChange | `(selected: string[]) => void` | — | Selection callback |
| selectionType | `'single' \| 'multiple'` | `'single'` | Selection mode |
| loading | `boolean` | `false` | Loading state |
| emptyState | `EmptyStateProps` | — | Empty state |
| error | `{ message: string; onRetry: () => void }` | — | Error state |
| maxHeight | `string` | — | Max height with scroll |
| role | `string` | `'list'` | ARIA role |
| className | `string` | — | Additional CSS classes |

## States

| State | Behavior |
|-------|----------|
| Default | Static list rendering |
| Loading | Skeleton items (3 by default) with shimmer |
| Empty | Centered empty state with optional CTA |
| Error | Error state with retry action |

## Divided Mode

When `divided={true}`, a `<hr>`-style divider appears between each item. Dividers respect the `--list-divider` token and collapse at the start/end of the list.

## Selectable Mode

- `selectionType="single"` — radio buttons per item
- `selectionType="multiple"` — checkboxes per item
- Selected items receive `aria-selected="true"` and background highlight
- Checkbox in header appears when `selectionType="multiple"` for select-all

## Keyboard Navigation

| Key | Action |
|-----|--------|
| `Tab` | Enter/exit the list |
| `Arrow Up` / `Arrow Down` | Navigate items |
| `Space` | Toggle selection (when selectable) |
| `Enter` | Activate item click / follow link |
| `Home` / `End` | Jump to first/last item |

## Design Tokens

| Token | Mapping |
|-------|---------|
| `--list-bg` | `--color-surface` |
| `--list-item-padding` | `--spacing-sm` / `--spacing-md` |
| `--list-item-gap` | `--spacing-sm` |
| `--list-item-hover` | `--color-surface-hover` |
| `--list-item-selected` | `--color-primary-subtle` |
| `--list-divider` | `--color-border-subtle` |
| `--list-radius` | `--radius-sm` |

## Usage Example

```tsx
<List
  variant="description"
  items={[
    { id: '1', label: 'Setup Profile', description: 'Complete your profile information' },
    { id: '2', label: 'Add Products', description: 'List your products on the marketplace' },
    { id: '3', label: 'Go Live', description: 'Publish your store to customers' },
  ]}
  divided
  selectable
  selectionType="single"
  onSelectionChange={handleSelect}
/>
```
