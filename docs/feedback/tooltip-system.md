# Tooltip System

## Overview

Tooltips provide contextual information on hover, focus, or delay. They display concise text or rich content next to an element. The system uses a TooltipProvider for global configuration and handles positioning with auto-flip and boundary detection.

### Component Hierarchy

```
Tooltip
├── RichTooltip
├── IconTooltip
├── DelayedTooltip
└── TooltipProvider
```

---

## Variants

| Variant | Description | Max Width | Content |
|---------|-------------|-----------|---------|
| **Standard** | Default text-only tooltip | 240px | Plain text |
| **Rich** | Rich content with formatting, links, or icons | 320px | ReactNode |
| **Icon** | Small tooltip for icon-only buttons | 200px | Short text |
| **Delayed** | Appears after a configurable delay | 240px | Same as standard |

### When to Use Each

| Variant | Use Case |
|---------|----------|
| **Standard** | Button/icon descriptions, label supplements |
| **Rich** | Feature explanations, help content with examples |
| **Icon** | Icon-only control labels (edit, delete, settings) |
| **Delayed** | Power user interfaces where frequent tooltips would be distracting |

---

## Positions

```
       top-start    top    top-end
            ┌─────────────┐
left-start  │   ELEMENT   │  right-start
     left   │             │  right
left-end    │             │  right-end
            └─────────────┘
     bottom-start  bottom  bottom-end
```

All positions support auto-flip when the tooltip would overflow the viewport.

---

## Props

### Tooltip Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `content` | `string \| ReactNode` | — | Tooltip content |
| `children` | `ReactNode` | — | Trigger element (must be a single focusable element) |
| `position` | `'top' \| 'bottom' \| 'left' \| 'right'` | `'top'` | Preferred position |
| `align` | `'start' \| 'center' \| 'end'` | `'center'` | Alignment along the main axis |
| `delay` | `number \| { show: number; hide: number }` | `{ show: 500, hide: 150 }` | Show/hide delay in ms |
| `disabled` | `boolean` | `false` | Disable tooltip |
| `maxWidth` | `number \| string` | `240` | Max width in px |
| `zIndex` | `number` | token | Custom z-index |
| `arrow` | `boolean` | `true` | Show arrow pointing to trigger |
| `interactive` | `boolean` | `false` | Allow mouse interaction with tooltip content |
| `onOpen` | `() => void` | — | Open callback |
| `onClose` | `() => void` | — | Close callback |
| `portalTarget` | `HTMLElement` | `document.body` | Portal render target |

### TooltipProvider Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `delay` | `number \| { show: number; hide: number }` | `{ show: 500, hide: 150 }` | Global default delay |
| `position` | `'top' \| 'bottom' \| 'left' \| 'right'` | `'top'` | Global default position |
| `disabled` | `boolean` | `false` | Globally disable all tooltips |
| `skipDelayOnTouch` | `boolean` | `true` | Show immediately on touch devices |
| `children` | `ReactNode` | — | Application tree |

---

## Accessibility

| Feature | Implementation |
|---------|---------------|
| **Trigger** | Tooltip trigger must be a focusable element (button, link, or with tabIndex). |
| **Description** | Uses `aria-describedby` to link tooltip content to trigger. |
| **Dismiss** | Pressing Escape hides the tooltip. |
| **Hover** | Tooltip shows on mouse hover and keyboard focus. |
| **Touch** | On touch devices, tooltip appears on long-press or first tap (configurable). |
| **Delay** | Show delay prevents tooltip from appearing during casual mouse movement. |
| **Interactive** | `interactive` mode allows mouse entering the tooltip (for links, copy buttons). |
| **Reduced Motion** | Fade animation (no movement) when `prefers-reduced-motion` is set. |

---

## Examples

### Standard tooltip

```tsx
import { Tooltip } from '@sporekart/ui';

function SaveButton() {
  return (
    <Tooltip content="Save changes (Ctrl+S)">
      <IconButton aria-label="Save" onClick={handleSave}>
        <SaveIcon />
      </IconButton>
    </Tooltip>
  );
}
```

### RichTooltip with formatted content

```tsx
import { RichTooltip } from '@sporekart/ui';

function StatusBadge() {
  return (
    <RichTooltip
      content={
        <div>
          <strong>Processing</strong>
          <p>Your order is being verified. This usually takes 2-3 minutes.</p>
          <a href="/status">Check status →</a>
        </div>
      }
      position="right"
      maxWidth={320}
    >
      <Badge variant="warning">Processing</Badge>
    </RichTooltip>
  );
}
```

### IconTooltip for icon-only buttons

```tsx
import { IconTooltip } from '@sporekart/ui';

function ActionToolbar() {
  return (
    <Toolbar>
      <IconTooltip content="Edit">
        <IconButton aria-label="Edit"><EditIcon /></IconButton>
      </IconTooltip>
      <IconTooltip content="Delete">
        <IconButton aria-label="Delete"><TrashIcon /></IconButton>
      </IconTooltip>
      <IconTooltip content="Share">
        <IconButton aria-label="Share"><ShareIcon /></IconButton>
      </IconTooltip>
    </Toolbar>
  );
}
```

### DelayedTooltip for power users

```tsx
import { DelayedTooltip } from '@sporekart/ui';

function DataGrid() {
  return (
    <DelayedTooltip
      content="Click to sort ascending. Shift+click for multi-column sort."
      delay={{ show: 1000, hide: 200 }}
    >
      <ColumnHeader>Revenue</ColumnHeader>
    </DelayedTooltip>
  );
}
```

### TooltipProvider for global configuration

```tsx
import { TooltipProvider, Tooltip } from '@sporekart/ui';

function App() {
  return (
    <TooltipProvider
      delay={{ show: 300, hide: 100 }}
      position="bottom"
      skipDelayOnTouch
    >
      <Application />
    </TooltipProvider>
  );
}
```

---

## Best Practices

- **Concise content**: Tooltips should be 1-10 words. For longer content, use Popover or RichTooltip.
- **Complement, not repeat**: Don't repeat visible text. Tooltips should add value.
- **Keyboard accessible**: Always ensure the trigger element is focusable and the tooltip appears on focus.
- **Avoid essential info**: Don't put critical information only in tooltips. Some users may not see them.
- **Interactive tooltips**: Use `interactive` when the tooltip contains links or copyable text.
- **Delay defaults**: 500ms show delay for desktop, 0ms for touch. This prevents flickering during mouse travel.
- **Position priority**: Set preferred position; the system auto-flips to keep tooltip in viewport.
- **Disabled elements**: Tooltips on disabled buttons need a wrapping `<span>` or `pointer-events: none` handling to capture hover events.
