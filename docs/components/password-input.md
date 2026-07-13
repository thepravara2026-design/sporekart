# PasswordInput

> **Status:** v1.0 — Component spec for Sprint 20 Part 2

## Overview

PasswordInput extends Input with a visibility toggle and optional strength indicator. Built on the base [Input](./input.md) component.

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `showStrengthIndicator` | `boolean` | `false` | Show password strength bar |
| `onVisibilityToggle` | `(visible: boolean) => void` | — | Callback when visibility changes |
| `value` | `string` | — | Password value |
| `defaultVisible` | `boolean` | `false` | Initial visibility state |

All base [Input](./input.md) props are also accepted.

## Visibility Toggle

- Icon button in suffix slot
- **Visible:** `eye` icon
- **Hidden:** `eye-off` icon
- Toggles `type` between `password` and `text`

## Strength Indicator

| Level | Bar Color | Condition |
|-------|-----------|-----------|
| Empty | — | `value.length === 0` |
| Weak | Red | `< 8 chars` |
| Fair | Orange | `>= 8 chars` no mix |
| Good | Yellow | Mix of letters + numbers |
| Strong | Green | Letters + numbers + symbols, `>= 12 chars` |

Strength indicator renders below the input as a segmented bar with label text.

## Validation States

Same as [Input](./input.md): default, focus, disabled, error, success, warning.

## Accessibility

- `aria-label="Toggle password visibility"` on the toggle button
- `aria-pressed` reflects current visibility state
- `aria-pressed="true"` when password is visible
- Input retains `aria-invalid`, `aria-describedby` from base Input
- Strength indicator has `role="status"` and `aria-live="polite"`

## Usage Examples

```tsx
// Basic
<PasswordInput label="Password" />

// With strength indicator
<PasswordInput label="New Password" showStrengthIndicator required />

// Controlled visibility
<PasswordInput
  label="Password"
  defaultVisible={false}
  onVisibilityToggle={(visible) => console.log(visible)}
/>
```

## Design Tokens Used

| Token | Example |
|-------|---------|
| `color.border.default` | `--color-border-default` |
| `color.border.error` | `--color-border-error` |
| `color.icon.default` | `--color-icon-default` |
| `color.bg.weak` | `--color-bg-weak` |
| `color.bg.fair` | `--color-bg-fair` |
| `color.bg.good` | `--color-bg-good` |
| `color.bg.strong` | `--color-bg-strong` |
| `sizing.icon.sm` | `--sizing-icon-sm` |
