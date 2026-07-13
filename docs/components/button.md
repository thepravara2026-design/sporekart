# Button

> **Status:** v1.0 — Component spec for Sprint 20 Part 2

## Overview

The Button component triggers actions. 11 variants: `primary`, `secondary`, `outline`, `ghost`, `link`, `destructive`, `success`, `warning`, `loading`, `icon`, and `button-group` (see [ButtonGroup](./button-group.md)).

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `variant` | `'primary' \| 'secondary' \| 'outline' \| 'ghost' \| 'link' \| 'destructive' \| 'success' \| 'warning'` | `'primary'` | Visual style |
| `size` | `'sm' \| 'md' \| 'lg'` | `'md'` | Button size |
| `fullWidth` | `boolean` | `false` | Stretches to container width |
| `leftIcon` | `IconName` | — | Icon before label |
| `rightIcon` | `IconName` | — | Icon after label |
| `loading` | `boolean` | `false` | Shows spinner, disables interaction |
| `disabled` | `boolean` | `false` | Visually and functionally disabled |

## States

| State | Visual |
|-------|--------|
| Default | As per variant token |
| Hover | Darken bg 10% (primary), lighten bg (outline/ghost) |
| Focus | `3px solid var(--focus-ring)` with offset 2px |
| Active | Pressed state (scale 0.97 opt-in) |
| Disabled | Opacity 0.4, `cursor: not-allowed`, no interaction |
| Loading | Spinner replaces or precedes label, pointer-events none |

## Sizes

| Size | Height | Padding X | Font Size | Icon Size |
|------|--------|-----------|-----------|-----------|
| `sm` | 32px | 12px | 14px | 16px |
| `md` | 40px | 16px | 16px | 20px |
| `lg` | 48px | 24px | 18px | 24px |

## Usage Examples

```tsx
// Primary
<Button variant="primary">Submit</Button>

// With left icon
<Button variant="primary" leftIcon="plus">Create</Button>

// Loading state
<Button variant="primary" loading>Saving...</Button>

// Full width
<Button variant="secondary" fullWidth>Cancel</Button>

// Icon-only
<Button variant="ghost" icon="more-vertical" aria-label="More options" />
```

## Design Tokens Used

| Token | Example |
|-------|---------|
| `color.bg.{variant}.default` | `--color-bg-primary-default` |
| `color.bg.{variant}.hover` | `--color-bg-primary-hover` |
| `color.bg.{variant}.pressed` | `--color-bg-primary-pressed` |
| `color.text.{variant}.default` | `--color-text-on-primary` |
| `color.border.{variant}.default` | `--color-border-primary` |
| `sizing.icon.{size}` | `--sizing-icon-md` |
| `spacing.padding.{size}` | `--spacing-padding-md` |
| `radius.button` | `--radius-button` |
| `animation.duration.fast` | `--duration-fast` (150ms) |
| `elevation.focus` | `--focus-ring` |

## Keyboard Navigation

| Key | Action |
|-----|--------|
| `Enter` / `Space` | Activates button |
| `Tab` | Moves focus to next button |
| `Shift+Tab` | Moves focus to previous button |

## Accessibility

- `role="button"` on non-`<button>` elements
- `aria-disabled="true"` when disabled (not `aria-disabled`, use native `<button>` attribute)
- `aria-label` for icon-only buttons
- Loading state uses `aria-busy="true"`
- Minimum touch target 44×44px (WCAG 2.2)
- Focus ring never removed (`outline: none` prohibited)
