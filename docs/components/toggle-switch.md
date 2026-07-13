# ToggleSwitch

> **Status:** v1.0 — Component spec for Sprint 20 Part 2

## Overview

ToggleSwitch is a binary on/off control with optional label, description, loading state, and validation feedback.

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `checked` | `boolean` | `false` | Toggle state |
| `onChange` | `(checked: boolean) => void` | — | Change handler |
| `disabled` | `boolean` | `false` | Disabled state |
| `loading` | `boolean` | `false` | Shows spinner, blocks interaction |
| `error` | `boolean` | `false` | Error state (red border) |
| `success` | `boolean` | `false` | Success state (green border) |
| `size` | `'sm' \| 'md' \| 'lg'` | `'md'` | Toggle size |
| `label` | `string` | — | Visible label |
| `description` | `string` | — | Helper text below label |
| `aria-label` | `string` | — | Label for accessibility (if no visible label) |

## States

| State | Visual |
|-------|--------|
| On | Track filled primary, knob right |
| Off | Track neutral, knob left |
| Disabled | Opacity 0.4, no interaction |
| Loading | Spinner replaces knob, `pointer-events: none` |
| Error | Red track border when applicable |
| Success | Green track border when applicable |

## Sizes

| Size | Track Width | Track Height | Knob Size |
|------|-------------|--------------|-----------|
| `sm` | 28px | 16px | 12px |
| `md` | 36px | 20px | 16px |
| `lg` | 44px | 24px | 20px |

## Usage Examples

```tsx
// Basic
<ToggleSwitch label="Enable notifications" onChange={setEnabled} />

// With description
<ToggleSwitch
  label="Dark mode"
  description="Apply dark theme across the app"
  checked={darkMode}
  onChange={setDarkMode}
/>

// Disabled
<ToggleSwitch label="Feature locked" disabled />

// Loading
<ToggleSwitch label="Processing" loading checked />

// Error state
<ToggleSwitch label="Terms required" error />
```

## Design Tokens Used

| Token | Example |
|-------|---------|
| `color.bg.switch.on` | `--color-bg-switch-on` |
| `color.bg.switch.off` | `--color-bg-switch-off` |
| `color.bg.switch.knob` | `--color-bg-switch-knob` |
| `color.bg.switch.disabled` | `--color-bg-switch-disabled` |
| `color.border.error` | `--color-border-error` |
| `elevation.switch.knob` | `--elevation-switch-knob` |
| `elevation.focus` | `--focus-ring` |
| `animation.duration.fast` | `--duration-fast` |

## Accessibility

- `role="switch"` on the toggle element
- `aria-checked="true"` / `"false"` reflects state
- `aria-disabled="true"` when disabled
- `aria-label` used when no visible label present
- `aria-busy="true"` during loading state
- Support `Space` key to toggle
- Visible focus ring
