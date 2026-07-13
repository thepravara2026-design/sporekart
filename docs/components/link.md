# Link

> **Status:** v1.0 — Component spec for Sprint 20 Part 2

## Overview

The Link component renders an accessible `<a>` element or navigational text. 5 variants: `inline`, `navigation`, `external`, `text`, `disabled`.

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `variant` | `'inline' \| 'navigation' \| 'external' \| 'text' \| 'disabled'` | `'inline'` | Visual style |
| `size` | `'sm' \| 'md' \| 'lg'` | `'md'` | Font size |
| `underline` | `'always' \| 'hover' \| 'none'` | `'hover'` | Underline visibility |
| `leftIcon` | `IconName` | — | Icon before label |
| `rightIcon` | `IconName` | — | Icon after label |
| `disabled` | `boolean` | `false` | Disables interaction |
| `href` | `string` | — | URL for navigation |
| `target` | `string` | — | Link target |
| `rel` | `string` | — | Relationship attribute |

## States

| State | Visual |
|-------|--------|
| Default | As per variant token |
| Hover | Underline visible (if `hover`), color change |
| Focus | `3px solid var(--focus-ring)` with offset 2px |
| Active | `color: var(--color-text-link-pressed)` |
| Disabled | Opacity 0.4, `cursor: not-allowed`, `aria-disabled="true"` |

## Sizes

| Size | Font Size |
|------|-----------|
| `sm` | 14px |
| `md` | 16px |
| `lg` | 18px |

### External Link Indicator

When `variant="external"`, the component automatically:
- Adds `target="_blank"` and `rel="noopener noreferrer"`
- Appends an external link icon after the label
- Adds `aria-label="Opens in new tab"` suffix to the label

## Usage Examples

```tsx
// Inline link
<Link href="/products">View Products</Link>

// Navigation link
<Link variant="navigation" href="/dashboard">Dashboard</Link>

// External link
<Link variant="external" href="https://example.com">Documentation</Link>

// Disabled link
<Link variant="disabled" disabled>Archived Page</Link>

// With icon
<Link href="/profile" leftIcon="user">Profile</Link>

// Text link (no underline)
<Link variant="text" underline="none" href="/terms">Terms</Link>
```

## Design Tokens Used

| Token | Example |
|-------|---------|
| `color.text.link.default` | `--color-text-link-default` |
| `color.text.link.hover` | `--color-text-link-hover` |
| `color.text.link.visited` | `--color-text-link-visited` |
| `color.text.disabled` | `--color-text-disabled` |
| `typography.fontSize.{size}` | `--font-size-md` |
| `elevation.focus` | `--focus-ring` |

## Accessibility

- Uses native `<a>` element when `href` is provided
- `aria-disabled="true"` when disabled (link remains focusable but non-functional)
- No `href="#"` — use a `<button>` for JavaScript-only actions
- External links announced to screen readers via `aria-label`
- Focus ring always visible
- Underline on hover for inline links (WCAG 1.4.1 use of color)
