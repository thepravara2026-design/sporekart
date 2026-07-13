# SearchInput

> **Status:** v1.0 — Component spec for Sprint 20 Part 2

## Overview

SearchInput extends Input with a search icon prefix, clear button, and loading indicator. Optimized for search/filter interactions.

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `value` | `string` | — | Controlled value |
| `placeholder` | `string` | `'Search...'` | Placeholder text |
| `onChange` | `(e) => void` | — | Change handler |
| `onClear` | `() => void` | — | Clear button callback |
| `onSearch` | `(value: string) => void` | — | Enter key / search submit |
| `loading` | `boolean` | `false` | Shows spinner in suffix |
| `debounceMs` | `number` | `300` | Debounce delay for `onSearch` |

All base [Input](./input.md) props are also accepted.

## Search Icon

A `search` icon is rendered in the prefix slot. Always present.

## Clear Button

- Visible when `value.length > 0`
- Renders an `x` icon in the suffix slot (or replaces loading spinner)
- Calls `onClear` and resets value to `''`
- Has `aria-label="Clear search"`

## Loading Indicator

- When `loading={true}`, a spinner replaces the suffix content
- Overrides the clear button while loading
- `aria-busy="true"` on input

## Enter Keyboard Handler

Pressing `Enter` triggers `onSearch(value)`. Useful for executing search queries rather than filtering on every keystroke.

## Usage Examples

```tsx
// Basic
<SearchInput onSearch={(q) => fetchResults(q)} />

// With debounce
<SearchInput
  debounceMs={500}
  onSearch={(q) => fetchResults(q)}
/>

// Loading state
<SearchInput loading onSearch={(q) => fetchResults(q)} />
```

## Design Tokens Used

| Token | Example |
|-------|---------|
| `color.icon.muted` | `--color-icon-muted` |
| `color.icon.default` | `--color-icon-default` |
| `sizing.icon.sm` | `--sizing-icon-sm` |
| `color.border.default` | `--color-border-default` |
| `color.border.focus` | `--color-border-focus` |
| `radius.input` | `--radius-input` |
