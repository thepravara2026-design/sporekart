# Checkbox

> **Status:** v1.0 — Component spec for Sprint 20 Part 2

## Overview

The Checkbox component is a styled native checkbox with support for indeterminate state. CheckboxGroup composes multiple checkboxes in a layout.

## Props — Checkbox

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `checked` | `boolean` | `false` | Checked state |
| `indeterminate` | `boolean` | `false` | Indeterminate (parent checkbox) |
| `disabled` | `boolean` | `false` | Disabled state |
| `error` | `boolean` | `false` | Error state |
| `success` | `boolean` | `false` | Success state |
| `size` | `'sm' \| 'md' \| 'lg'` | `'md'` | Checkbox size |
| `label` | `string` | — | Visible label |
| `onChange` | `(checked: boolean) => void` | — | Change handler |

## States

| State | Visual |
|-------|--------|
| Unchecked | Empty box, standard border |
| Checked | Filled with checkmark, primary bg |
| Indeterminate | Filled with dash, primary bg |
| Disabled | Opacity 0.4, no interaction |
| Error | Red border (unchecked) |
| Success | Green border (when checked) |

## Sizes

| Size | Box Size | Font Size |
|------|----------|-----------|
| `sm` | 16px | 14px |
| `md` | 20px | 16px |
| `lg` | 24px | 18px |

## Props — CheckboxGroup

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `orientation` | `'horizontal' \| 'vertical'` | `'vertical'` | Layout direction |
| `options` | `{ label: string; value: string; disabled?: boolean }[]` | — | Checkbox options |
| `value` | `string[]` | — | Selected values |
| `onChange` | `(values: string[]) => void` | — | Change handler |
| `label` | `string` | — | Group label |

## Usage Examples

```tsx
// Single checkbox
<Checkbox label="Accept terms" onChange={(c) => setAccepted(c)} />

// Indeterminate parent
<Checkbox
  indeterminate={partiallySelected}
  checked={allSelected}
  label="Select all"
/>

// Checkbox group
<Checkbox.Group
  label="Notifications"
  orientation="vertical"
  options={[
    { label: 'Email', value: 'email' },
    { label: 'SMS', value: 'sms', disabled: true },
    { label: 'Push', value: 'push' },
  ]}
  value={selected}
  onChange={setSelected}
/>
```

## Design Tokens Used

| Token | Example |
|-------|---------|
| `color.border.default` | `--color-border-default` |
| `color.border.focus` | `--color-border-focus` |
| `color.bg.primary.default` | `--color-bg-primary-default` |
| `color.text.on-primary` | `--color-text-on-primary` |
| `color.text.default` | `--color-text-default` |
| `color.text.disabled` | `--color-text-disabled` |
| `radius.checkbox` | `--radius-checkbox` |
| `elevation.focus` | `--focus-ring` |

## Accessibility

- Underlying `<input type="checkbox">` with `role="checkbox"`
- `aria-checked="true"` / `"false"` / `"mixed"` for indeterminate
- `aria-disabled="true"` when disabled
- `<label>` wrapping is used
- CheckboxGroup rendered as `<fieldset>` with `<legend>`
- Visible focus ring on the native checkbox
