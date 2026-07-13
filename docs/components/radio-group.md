# RadioGroup

> **Status:** v1.0 — Component spec for Sprint 20 Part 2

## Overview

RadioGroup renders a set of radio buttons where exactly one option can be selected. Supports horizontal and vertical orientation, individual disabled options, and error state.

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `orientation` | `'horizontal' \| 'vertical'` | `'vertical'` | Layout direction |
| `options` | `{ label: string; value: string; disabled?: boolean }[]` | — | Radio options |
| `value` | `string` | — | Currently selected value |
| `onChange` | `(value: string) => void` | — | Selection handler |
| `label` | `string` | — | Group label (legend) |
| `error` | `string` | — | Error message |
| `disabled` | `boolean` | `false` | Disables all options |
| `size` | `'sm' \| 'md' \| 'lg'` | `'md'` | Radio size |

## States

| State | Visual |
|-------|--------|
| Default (unselected) | Empty circle, standard border |
| Selected | Filled dot, primary bg |
| Disabled (option) | Opacity 0.4 |
| Disabled (group) | All options opacity 0.4 |
| Error | Red border on unselected, red text below |

## Sizes

| Size | Circle Size | Font Size |
|------|-------------|-----------|
| `sm` | 16px | 14px |
| `md` | 20px | 16px |
| `lg` | 24px | 18px |

## Usage Examples

```tsx
// Vertical
<RadioGroup
  label="Shipping Method"
  options={[
    { label: 'Standard (5-7 days)', value: 'standard' },
    { label: 'Express (2-3 days)', value: 'express' },
    { label: 'Overnight', value: 'overnight', disabled: true },
  ]}
  value={shippingMethod}
  onChange={setShippingMethod}
/>

// Horizontal with error
<RadioGroup
  label="Gender"
  orientation="horizontal"
  options={[
    { label: 'Male', value: 'male' },
    { label: 'Female', value: 'female' },
  ]}
  error="Please select a gender"
/>
```

## Design Tokens Used

| Token | Example |
|-------|---------|
| `color.border.default` | `--color-border-default` |
| `color.border.focus` | `--color-border-focus` |
| `color.border.error` | `--color-border-error` |
| `color.bg.primary.default` | `--color-bg-primary-default` |
| `color.text.on-primary` | `--color-text-on-primary` |
| `color.text.default` | `--color-text-default` |
| `color.text.error` | `--color-text-error` |
| `radius.full` | `--radius-full` (50%) |
| `elevation.focus` | `--focus-ring` |

## Keyboard Navigation

| Key | Action |
|-----|--------|
| `Tab` | Focuses selected radio (or first if none selected) |
| `ArrowUp` / `ArrowLeft` | Selects previous option |
| `ArrowDown` / `ArrowRight` | Selects next option |
| `Space` | Selects focused option |

Roving tabindex: only the selected radio has `tabindex="0"`.

## Accessibility

- Rendered as `<fieldset>` with `<legend>` for the group label
- `role="radiogroup"` on the group container
- Each option has `role="radio"` with `aria-checked="true"` / `"false"`
- `aria-disabled="true"` on disabled options
- `aria-invalid="true"` when error is present
- Error message linked via `aria-describedby`
