# Avatar Component

## Overview

Avatar displays user or entity images with fallback to initials. Available in 6 sizes with status indicators and group overlapping.

## Avatar Types

| Type | Description |
|------|-------------|
| image | Image-based avatar with fallback |
| initials | Initials-based avatar when no image available |

## Avatar Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| src | `string` | — | Image URL |
| alt | `string` | — | Alt text for image |
| initials | `string` | — | Initials (1–3 characters) |
| size | `'xs' \| 'sm' \| 'md' \| 'lg' \| 'xl' \| '2xl'` | `'md'` | Avatar size |
| variant | `'default' \| 'primary' \| 'neutral'` | `'default'` | Initials background color |
| status | `'online' \| 'offline' \| 'busy' \| 'away'` | — | Presence indicator |
| statusPosition | `'top-right' \| 'bottom-right' \| 'top-left' \| 'bottom-left'` | `'bottom-right'` | Status dot position |
| loading | `boolean` | `false` | Loading state |
| onError | `() => void` | — | Image load error callback |
| className | `string` | — | Additional CSS classes |

## Sizes

| Size | Dimension | Font | Status Dot |
|------|-----------|------|------------|
| xs | 24px | 10px | 6px |
| sm | 32px | 12px | 8px |
| md | 40px | 14px | 10px |
| lg | 48px | 16px | 12px |
| xl | 64px | 20px | 14px |
| 2xl | 80px | 24px | 16px |

## Status Indicators

| Status | Color | Description |
|--------|-------|-------------|
| online | `--color-success` | Active/present |
| offline | `--color-neutral` | Not present |
| busy | `--color-danger` | Do not disturb |
| away | `--color-warning` | Away/idle |

Status is rendered as a colored dot overlayed on the avatar. When `pulse` is set, the status dot shows a subtle ring animation for `online` status.

## AvatarGroup

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| avatars | `AvatarGroupItem[]` | — | Array of avatar configs |
| max | `number` | `4` | Max visible avatars before overflow |
| size | `AvatarSize` | `'sm'` | Size of each avatar |
| overlap | `'sm' \| 'md' \| 'lg'` | `'md'` | Overlap amount |

The overflow count is shown as a `+{n}` badge on the last visible position.

```tsx
<AvatarGroup
  avatars={[
    { src: '/user1.jpg', alt: 'User 1' },
    { src: '/user2.jpg', alt: 'User 2' },
    { initials: 'AK', alt: 'Aarav Kumar' },
  ]}
  max={3}
  size="md"
  overlap="md"
/>
```

## Fallback Behavior

1. If `src` is provided and loads successfully → render image
2. If `src` fails (404, network error) or is not provided → render initials
3. If initials not provided → render fallback icon (user silhouette)
4. Error state → render fallback icon with error styling

## Loading / Error States

| State | Behavior |
|-------|----------|
| Loading | Circular skeleton shimmer in avatar dimensions |
| Image Error | Falls back to initials or fallback icon |
| Empty | Fallback icon (user silhouette) |

## Design Tokens

| Token | Mapping |
|-------|---------|
| `--avatar-radius` | `--radius-full` (circular) |
| `--avatar-initials-bg` | Variant-specific |
| `--avatar-initials-text` | `--color-on-{variant}` |
| `--avatar-border` | `--color-surface` (ring for separation in groups) |
| `--avatar-status-dot-size` | Scale based on avatar size |
| `--avatar-overlap` | `--spacing-sm` / `--spacing-md` |
