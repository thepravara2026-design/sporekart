# Chip Component

## Overview

Chip is a compact UI element for filters, actions, tags, and selection. Available in 5 types, 7 colors, and 3 sizes.

## Chip Types

| Type | Description |
|------|-------------|
| filter | Toggleable filter chip, often used in filter bars |
| action | Chip that triggers an action on click |
| selectable | Chips that can be selected/deselected like a chip group |
| removable | Chip with a dismiss (×) icon |
| tag | Static label chip (non-interactive) |

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| label | `string` | — | Chip content |
| variant | `'default' \| 'primary' \| 'success' \| 'warning' \| 'danger' \| 'info' \| 'neutral'` | `'default'` | Color variant |
| size | `'sm' \| 'md' \| 'lg'` | `'md'` | Chip size |
| type | `'filter' \| 'action' \| 'selectable' \| 'removable' \| 'tag'` | `'tag'` | Chip behavior type |
| selected | `boolean` | `false` | Selected state (filter/selectable types) |
| disabled | `boolean` | `false` | Disabled state |
| icon | `IconName` | — | Leading icon |
| onSelect | `(selected: boolean) => void` | — | Selection callback |
| onRemove | `() => void` | — | Remove/dismiss callback |
| onClick | `() => void` | — | Click handler |
| className | `string` | — | Additional CSS classes |

## Selected/Unselected States

| State | Visual |
|-------|--------|
| Unselected | Outlined, subtle border |
| Selected | Filled background (`--color-{variant}-subtle`) with border |
| Hover | Elevation increase |
| Focus | Focus ring (`--focus-ring`) |
| Disabled | 50% opacity, no pointer events |

## Colors

Each of the 7 variants produces a distinct color mapping:

| Variant | Unselected Border | Selected BG | Selected Text |
|---------|-------------------|-------------|---------------|
| default | `--color-border` | `--color-surface-secondary` | `--color-text` |
| primary | `--color-primary` | `--color-primary-subtle` | `--color-primary` |
| success | `--color-success` | `--color-success-subtle` | `--color-success` |
| warning | `--color-warning` | `--color-warning-subtle` | `--color-warning` |
| danger | `--color-danger` | `--color-danger-subtle` | `--color-danger` |
| info | `--color-info` | `--color-info-subtle` | `--color-info` |
| neutral | `--color-neutral` | `--color-neutral-subtle` | `--color-neutral` |

## Sizes

| Size | Height | Font | Padding |
|------|--------|------|---------|
| sm | 24px | 12px | 8px horizontal |
| md | 32px | 14px | 12px horizontal |
| lg | 40px | 16px | 16px horizontal |

## Keyboard Navigation

| Key | Action |
|-----|--------|
| `Tab` | Navigate between chips |
| `Enter` / `Space` | Toggle selection / activate action |
| `Backspace` | Remove chip (removable type) |

## Design Tokens

| Token | Mapping |
|-------|---------|
| `--chip-radius` | `--radius-full` |
| `--chip-border` | Variant-specific |
| `--chip-bg-selected` | Variant-specific subtle |
| `--chip-text` | `--color-text` |
| `--chip-gap` | `--spacing-xs` |
| `--chip-focus-ring` | `--focus-ring` |

## Usage Example

```tsx
<Chip
  type="filter"
  variant="primary"
  label="Organic"
  selected={filters.includes('organic')}
  onSelect={(sel) => toggleFilter('organic', sel)}
/>

<Chip
  type="removable"
  variant="info"
  label="Filter: Price > 500"
  onRemove={() => clearFilter('price')}
/>

<Chip type="tag" variant="success" label="In Stock" />
```
