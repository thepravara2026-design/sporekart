# ButtonGroup

> **Status:** v1.0 — Component spec for Sprint 20 Part 2

## Overview

ButtonGroup renders a set of buttons as a cohesive group. Supports horizontal and vertical orientations. Buttons are visually attached with shared border radius.

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `orientation` | `'horizontal' \| 'vertical'` | `'horizontal'` | Layout direction |
| `variant` | `'primary' \| 'secondary' \| 'outline' \| 'ghost'` | `'outline'` | Variant applied to all children |
| `size` | `'sm' \| 'md' \| 'lg'` | `'md'` | Size applied to all children |
| `children` | `React.ReactElement<Button>[]` | — | Buttons in group |
| `aria-label` | `string` | `'Button group'` | Accessible label |

## States

ButtonGroup itself has no interactive state. Children inherit standard [Button](./button.md) states. The first button in the group should be focusable via Tab; subsequent buttons are navigated via arrow keys.

## Sizes

Same as [Button](./button.md) sizes. All buttons in a group must share the same size.

## Usage Examples

```tsx
// Horizontal group
<ButtonGroup aria-label="View options">
  <Button variant="outline">Day</Button>
  <Button variant="outline">Week</Button>
  <Button variant="outline">Month</Button>
</ButtonGroup>

// Vertical group
<ButtonGroup orientation="vertical" aria-label="Sort options">
  <Button variant="ghost">Ascending</Button>
  <Button variant="ghost">Descending</Button>
</ButtonGroup>

// With active state
<ButtonGroup aria-label="Pagination">
  <Button variant="outline" disabled>1</Button>
  <Button variant="primary">2</Button>
  <Button variant="outline">3</Button>
</ButtonGroup>
```

## Keyboard Navigation

| Key | Action |
|-----|--------|
| `Tab` | Focuses first/focused button in group |
| `ArrowRight` / `ArrowDown` | Moves focus to next button |
| `ArrowLeft` / `ArrowUp` | Moves focus to previous button |
| `Enter` / `Space` | Activates focused button |
| `Home` / `End` | Moves focus to first / last button |

## Accessibility

- `role="group"` on container
- `aria-label` identifying the group purpose
- Roving tabindex: only one button in the group has `tabindex="0"`
- Focus is managed via arrow key handlers, not Tab
- Each button retains its own `aria-label` if needed

## Design Tokens Used

| Token | Purpose |
|-------|---------|
| `color.border.{variant}.default` | Shared borders between buttons |
| `radius.button` | Outer corners rounded, inner corners squared |
| `spacing.padding.{size}` | Internal button padding |
