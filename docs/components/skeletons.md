# Skeleton Component

## Overview

Skeleton provides loading placeholders that match the shape and size of actual content. Includes a base primitive and 7 preset variants.

## Base Skeleton Primitive

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| variant | `'text' \| 'circular' \| 'rectangular' \| 'rounded'` | `'text'` | Shape |
| width | `string \| number` | — | Width |
| height | `string \| number` | — | Height |
| animation | `'pulse' \| 'wave'` | `'pulse'` | Animation type |
| lines | `number` | `1` | Number of text lines (text variant only) |
| lineHeight | `string` | `'1em'` | Line height for text lines |
| gap | `string` | `--spacing-xs` | Gap between text lines |
| className | `string` | — | Additional CSS classes |

## Animation Types

| Type | Description |
|------|-------------|
| pulse | Opacity oscillation (0.4 → 1.0 → 0.4) |
| wave | Shimmer sweep left-to-right gradient |

Both animations respect `prefers-reduced-motion` and disable when reduced motion is preferred.

## Skeleton Variants

| Variant | Description | Default Dimensions |
|---------|-------------|--------------------|
| Card | Card-shaped placeholder | `width: 100%, height: 200px`, rounded |
| Table | Table row placeholder | `height: 48px`, 4–6 uneven text bars per row |
| List | List item placeholder | `height: 40px`, icon + 2 text bars |
| Form | Form field placeholder | Label bar + field bar per field |
| Avatar | Circular avatar placeholder | Circular, configurable size |
| Dashboard | Dashboard widget placeholder | Header + 2–3 metric bars |
| ProductGrid | Product card skeleton | Image block + title + price bars |

## Skeleton Variant Props

### SkeletonCard

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| lines | `number` | `3` | Content lines inside card |
| hasImage | `boolean` | `false` | Include image block |
| imageHeight | `string` | `'160px'` | Image block height |

### SkeletonTable

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| rows | `number` | `5` | Number of table rows |
| columns | `number` | `4` | Number of columns |
| header | `boolean` | `true` | Show header row |

### SkeletonList

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| items | `number` | `5` | Number of list items |
| hasIcon | `boolean` | `true` | Show leading icon circle |

### SkeletonForm

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| fields | `number` | `3` | Number of form fields |
| columns | `1 \| 2` | `1` | Layout columns |

### SkeletonAvatar

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| size | `AvatarSize` | `'md'` | Avatar size |

### SkeletonDashboard

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| cards | `number` | `4` | Number of metric cards |
| columns | `number` | `4` | Grid columns |

### SkeletonProductGrid

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| items | `number` | `6` | Number of product cards |
| columns | `number` | `3` | Grid columns |

## Usage Examples

```tsx
// Replacing a loaded card with skeleton
{loading ? (
  <SkeletonCard lines={4} hasImage imageHeight="120px" />
) : (
  <ProductCard {...product} />
)}

// Table loading
{loading ? (
  <SkeletonTable rows={8} columns={5} />
) : (
  <Table columns={columns} data={data} />
)}

// Primitive usage
<Skeleton variant="text" width="60%" lines={2} gap="8px" />
<Skeleton variant="circular" width={40} height={40} />
<Skeleton variant="rectangular" width="100%" height={200} />
```

## Design Tokens

| Token | Mapping |
|-------|---------|
| `--skeleton-bg` | `--color-surface-secondary` |
| `--skeleton-highlight` | `--color-surface-tertiary` |
| `--skeleton-radius` | `--radius-sm` (text), `--radius-md` (rounded) |
| `--skeleton-duration` | `1.5s` (pulse), `2s` (wave) |
| `--skeleton-opacity` | `0.4` (pulse minimum) |
