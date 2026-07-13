# Tag Component

## Overview

Tag is a lightweight label component for categorizing, status indication, and metadata display. Tags are smaller and less interactive than Chips, suited for read-only labeling.

## Tag Types

| Type | Description |
|------|-------------|
| category | Content categorization (e.g. "Fruits", "Seeds") |
| status | Status indicator (e.g. "Active", "Pending") |
| label | Generic metadata label |

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| label | `string` | — | Tag content |
| variant | `'default' \| 'primary' \| 'success' \| 'warning' \| 'danger' \| 'info' \| 'neutral'` | `'default'` | Color variant |
| size | `'sm' \| 'md' \| 'lg'` | `'sm'` | Tag size |
| type | `'category' \| 'status' \| 'label'` | `'label'` | Tag type |
| removable | `boolean` | `false` | Show remove (×) button |
| onRemove | `() => void` | — | Remove callback |
| icon | `IconName` | — | Leading icon |
| className | `string` | — | Additional CSS classes |

## Removable Tags

When `removable={true}`, a small × icon appears on the right side of the tag. This is used in tag input components and filter bars. The `onRemove` callback fires on click.

## Colors

Same 7 variant colors as Chip/Badge. Tags use lighter backgrounds and smaller text for a more subtle appearance.

| Variant | Background | Text |
|---------|------------|------|
| default | `--color-surface-secondary` | `--color-text-secondary` |
| primary | `--color-primary-subtle` | `--color-primary` |
| success | `--color-success-subtle` | `--color-success` |
| warning | `--color-warning-subtle` | `--color-warning` |
| danger | `--color-danger-subtle` | `--color-danger` |
| info | `--color-info-subtle` | `--color-info` |
| neutral | `--color-neutral-subtle` | `--color-neutral` |

## Sizes

| Size | Height | Font | Padding |
|------|--------|------|---------|
| sm | 18px | 10px | 6px horizontal |
| md | 22px | 12px | 8px horizontal |
| lg | 28px | 14px | 10px horizontal |

## Usage Examples

```tsx
<Tag type="category" variant="primary" label="Organic" />
<Tag type="status" variant="success" label="Verified" />
<Tag type="label" variant="info" label="New" />
<Tag removable variant="neutral" label="Seasonal" onRemove={() => removeTag('seasonal')} />
```

## Design Tokens

| Token | Mapping |
|-------|---------|
| `--tag-radius` | `--radius-sm` |
| `--tag-bg` | Variant-specific subtle |
| `--tag-text` | Variant-specific text |
| `--tag-font-size` | Scale based on size prop |
| `--tag-gap` | `--spacing-xs` |
