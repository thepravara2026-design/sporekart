# OTPInput

> **Status:** v1.0 — Component spec for Sprint 20 Part 2

## Overview

OTPInput renders individual input boxes for one-time passcodes. Configurable length with auto-focus, paste support, arrow key navigation, and backspace behavior.

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `length` | `number` | `6` | Number of input fields |
| `value` | `string` | — | Controlled value |
| `onChange` | `(value: string) => void` | — | Change handler |
| `onComplete` | `(value: string) => void` | — | Fires when all fields filled |
| `disabled` | `boolean` | `false` | Disables all inputs |
| `error` | `string` | — | Error state |
| `size` | `'sm' \| 'md' \| 'lg'` | `'md'` | Input size |
| `autoFocus` | `boolean` | `true` | Auto-focus on mount |

## Auto-Focus Behavior

On mount, the first empty input receives focus. If all fields are pre-filled, the last field receives focus.

## Paste Support

Pasting a code string distributes characters across all input boxes. The paste event is intercepted via `onPaste`, value is split by character, and each input field is populated sequentially.

## Arrow Key Navigation

| Key | Action |
|-----|--------|
| `ArrowLeft` | Focus previous input |
| `ArrowRight` | Focus next input |
| `ArrowUp` | Focus first input |
| `ArrowDown` | Focus last input |

## Backspace Behavior

| Action | Result |
|--------|--------|
| Backspace on filled field | Clears field, stays on same field |
| Backspace on empty field | Clears previous field and focuses it |

## onComplete Callback

Fires when all `length` fields contain a single digit/character. Receives the full concatenated string. Useful for auto-submitting OTP verification.

## States

| State | Visual |
|-------|--------|
| Default | Standard border per field |
| Filled | Field shows value, border unchanged |
| Error | `border: var(--color-border-error)`, all fields turn red, error text below |

## Usage Examples

```tsx
// Basic 6-digit OTP
<OTPInput length={6} onComplete={(code) => verifyCode(code)} />

// 4-digit with error
<OTPInput
  length={4}
  error="Invalid code. Please try again."
  onComplete={(code) => verifyCode(code)}
/>

// Disabled
<OTPInput length={6} disabled />
```

## Design Tokens Used

| Token | Example |
|-------|---------|
| `color.border.default` | `--color-border-default` |
| `color.border.focus` | `--color-border-focus` |
| `color.border.error` | `--color-border-error` |
| `color.bg.input` | `--color-bg-input` |
| `color.text.default` | `--color-text-default` |
| `radius.input` | `--radius-input` |
| `sizing.icon.{size}` | `--sizing-icon-md` |
| `spacing.otp.gap` | `--spacing-otp-gap` |

## Accessibility

- Each input has `aria-label="Digit {n} of {length}"`
- Container has `role="group"` and `aria-label="One-time code"`
- Error state linked via `aria-describedby` on each input
- Inputs are `type="text"` with `inputMode="numeric"` and `pattern="[0-9]*"`
