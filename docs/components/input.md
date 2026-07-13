# Input

> **Status:** v1.0 — Component spec for Sprint 20 Part 2

## Overview

The Input component is the base text field. Supports 7 types: `text`, `email`, `tel`, `url`, `number`, `search`, `password`. Includes label, helper text, character counter, and prefix/suffix slots.

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `type` | `'text' \| 'email' \| 'tel' \| 'url' \| 'number' \| 'search' \| 'password'` | `'text'` | Input type |
| `size` | `'sm' \| 'md' \| 'lg'` | `'md'` | Input size |
| `label` | `string` | — | Visible label |
| `required` | `boolean` | `false` | Shows required indicator |
| `optional` | `boolean` | `false` | Shows "(optional)" text |
| `placeholder` | `string` | — | Placeholder text |
| `helperText` | `string` | — | Helper text below input |
| `error` | `string` | — | Error message |
| `success` | `string` | — | Success message |
| `warning` | `string` | — | Warning message |
| `maxLength` | `number` | — | Character limit |
| `disabled` | `boolean` | `false` | Disabled state |
| `readOnly` | `boolean` | `false` | Read-only state |
| `prefix` | `ReactNode` | — | Content before input |
| `suffix` | `ReactNode` | — | Content after input |
| `onChange` | `(e) => void` | — | Change handler |

## Types

| Type | Description |
|------|-------------|
| `text` | General text input |
| `email` | Email format validation |
| `tel` | Telephone number |
| `url` | URL format validation |
| `number` | Numeric input (no spinner) |
| `search` | Search field (see [SearchInput](./search-input.md)) |
| `password` | Password field (see [PasswordInput](./password-input.md)) |

## States

| State | Visual |
|-------|--------|
| Default | `border: var(--color-border-default)` |
| Focus | Border primary + `--focus-ring` |
| Disabled | Opacity 0.4, `cursor: not-allowed` |
| ReadOnly | Grey background, normal cursor |
| Error | `border: var(--color-border-error)`, error text below |
| Success | `border: var(--color-border-success)`, check icon |
| Warning | `border: var(--color-border-warning)`, warning icon |

## Label Indicators

- **Required:** `<label>{text}<span aria-hidden="true"> *</span></label>`
- **Optional:** `<label>{text}<span class="optional"> (optional)</span></label>`

## Helper Text

Placed below the input. Linked via `aria-describedby`. Supports `error`, `success`, and `warning` variants with contextual colors.

## Character Counter

When `maxLength` is set, a counter (`{current}/{max}`) appears below the input on the right. When approaching limit (≥80%), counter turns warning; at limit, turns error.

## Prefix / Suffix Slots

- **Prefix:** Renders before the input (e.g., currency symbol, country code)
- **Suffix:** Renders after the input (e.g., unit, clear button)

## Sizes

| Size | Height | Font Size | Padding X |
|------|--------|-----------|-----------|
| `sm` | 32px | 14px | 12px |
| `md` | 40px | 16px | 16px |
| `lg` | 48px | 18px | 24px |

## Usage Examples

```tsx
// Basic
<Input label="Full Name" placeholder="Enter your name" />

// Required with error
<Input label="Email" type="email" required error="Invalid email" />

// With helper text
<Input label="Phone" type="tel" helperText="Include country code" />

// With prefix
<Input label="Amount" type="number" prefix={<span>$</span>} />

// Character limit
<Input label="Bio" maxLength={200} />
```

## Design Tokens Used

| Token | Example |
|-------|---------|
| `color.border.default` | `--color-border-default` |
| `color.border.focus` | `--color-border-focus` |
| `color.border.error` | `--color-border-error` |
| `color.text.label` | `--color-text-label` |
| `color.text.helper` | `--color-text-helper` |
| `color.text.error` | `--color-text-error` |
| `spacing.padding.{size}` | `--spacing-padding-md` |
| `radius.input` | `--radius-input` |
| `elevation.focus` | `--focus-ring` |

## Accessibility

- `<label>` associated via `for`/`id` pairing
- `aria-describedby` links to helper text and error message IDs
- `aria-required="true"` when required
- `aria-invalid="true"` / `aria-invalid="false"` based on validation state
- `aria-readonly="true"` for read-only
- Error messages use `role="alert"` for screen reader announcement
- Placeholder never replaces label (WCAG 2.2)
