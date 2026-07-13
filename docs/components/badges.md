# Badge Component

## Overview

Badge is a small visual indicator for counts, statuses, notifications, and annotations. Available in 7 colors, 5 types, and 3 sizes.

## Badge Variants (Colors)

| Variant | Usage |
|---------|-------|
| default | Neutral, general purpose |
| primary | Brand primary |
| success | Positive confirmation |
| warning | Caution |
| danger | Error, critical |
| info | Informational |
| neutral | Subtle, non-emphasized |

## Badge Types

| Type | Description |
|------|-------------|
| status | Dot or label indicating state (online, offline, etc.) |
| count | Numeric count (e.g. notification count) |
| notification | Icon + count combination |
| verification | Verified/trusted indicator |
| progress | Percentage or step indicator |

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| label | `string \| number` | — | Badge content |
| variant | `'default' \| 'primary' \| 'success' \| 'warning' \| 'danger' \| 'info' \| 'neutral'` | `'default'` | Color variant |
| size | `'sm' \| 'md' \| 'lg'` | `'md'` | Badge size |
| type | `'status' \| 'count' \| 'notification' \| 'verification' \| 'progress'` | `'status'` | Badge type |
| dot | `boolean` | `false` | Dot-only mode (no label) |
| pulse | `boolean` | `false` | Animated pulse for live indicators |
| icon | `IconName` | — | Leading icon |
| max | `number` | `99` | Max count before `99+` truncation |
| placement | `'top-right' \| 'top-left' \| 'bottom-right' \| 'bottom-left'` | `'top-right'` | Position when used as overlay |
| className | `string` | — | Additional CSS classes |

## Sizes

| Size | Height | Font | Padding |
|------|--------|------|---------|
| sm | 16px | 10px | 4px horizontal |
| md | 20px | 12px | 6px horizontal |
| lg | 24px | 14px | 8px horizontal |

## Dot Mode

Renders a small colored circle (`8px` diameter) without label. Used for presence indicators and live status.

## Pulse Animation

When `pulse={true}`, the badge displays a subtle pulsing ring animation. Respects `prefers-reduced-motion` media query (no animation when reduced motion is preferred).

## Usage Examples

### On Buttons

```tsx
<Button>
  Notifications
  <Badge type="count" variant="danger" size="sm" label={3} />
</Button>
```

### On Avatars

```tsx
<Avatar src="/user.jpg" size="lg">
  <Badge dot variant="success" pulse placement="bottom-right" />
</Avatar>
```

### On Cards

```tsx
<Badge variant="warning" label="Pending Review" />
<Badge type="count" variant="primary" label={42} max={99} />
<Badge type="verification" variant="success" label="Verified" />
```

## Design Tokens

| Token | Mapping |
|-------|---------|
| `--badge-bg` | Variant-specific (`--color-{variant}`) |
| `--badge-text` | `--color-on-{variant}` |
| `--badge-radius` | `--radius-full` (pill shape) |
| `--badge-font-size` | Scale based on size prop |
| `--badge-dot-size` | `8px` (dot mode) |
| `--badge-pulse-color` | `--badge-bg` at 40% opacity |
