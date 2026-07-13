# Status Indicators

## Overview

Status indicators are compact visual badges that communicate the state of an entity, process, or connection. They use color, icon, and optional animation to convey meaning at a glance.

### Component Hierarchy

```
StatusIndicator
```

A single component with configurable `status`, `size`, and `variant` props.

---

## Status Types

| Status | Color Token | Color | Icon | Meaning |
|--------|-------------|-------|------|---------|
| **Online** | `--color-success` | Green | Checkmark circle | Service/user is active and available |
| **Offline** | `--color-muted` | Gray | Minus circle | Service/user is disconnected |
| **Busy** | `--color-warning` | Amber | Clock | Currently occupied, will retry |
| **Pending** | `--color-warning` | Amber | Hourglass | Awaiting processing or approval |
| **Processing** | `--color-info` | Blue | Spinner / Sync | Actively being processed (animated) |
| **Completed** | `--color-success` | Green | Checkmark | Process finished successfully |
| **Failed** | `--color-danger` | Red | X circle | Operation terminated with error |
| **Queued** | `--color-muted` | Gray | List | In queue, waiting to be processed |
| **Draft** | `--color-muted` | Gray | Edit / File | Saved but not submitted |
| **Archived** | `--color-muted-2` | Darker gray | Archive | Moved to archive, no longer active |

---

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `status` | `StatusType` | — | Status identifier |
| `size` | `'xs' \| 'sm' \| 'md' \| 'lg' \| 'xl'` | `'md'` | Indicator size |
| `variant` | `'dot' \| 'icon' \| 'badge' \| 'text'` | `'dot'` | Display variant |
| `label` | `string` | status name | Visible text label (only for `badge` and `text` variants) |
| `showIcon` | `boolean` | `true` | Show status icon |
| `pulse` | `boolean` | `false` | Enable pulse animation |
| `animate` | `boolean` | — | Auto-enable pulse for processing/pending states |
| `inverse` | `boolean` | `false` | Light text on dark bg |
| `className` | `string` | — | Additional CSS classes |

```typescript
type StatusType =
  | 'online'
  | 'offline'
  | 'busy'
  | 'pending'
  | 'processing'
  | 'completed'
  | 'failed'
  | 'queued'
  | 'draft'
  | 'archived';
```

---

## Size Variants

| Size | Dot (px) | Badge Height | Use Case |
|------|----------|--------------|----------|
| `xs` | 6 | 16px | Inline with text, table cells |
| `sm` | 8 | 20px | List items, compact layouts |
| `md` | 12 | 24px | Default, detail views |
| `lg` | 16 | 32px | Cards, profile headers |
| `xl` | 20 | 40px | Dashboard widgets, large cards |

---

## Variants

### Dot

Minimal colored circle without text or icon.

```
● Online (green)        ○ Offline (gray)
● Busy (amber)          ● Processing (blue, pulsing)
● Completed (green)     ✕ Failed (red)
```

### Icon

Status icon with visual indicator. Combines color + icon for redundancy.

```
✓ Online    ○ Offline    ⏳ Pending    ⟳ Processing
✓ Completed  ✕ Failed    📋 Queued     ✏️ Draft
```

### Badge

Pill-shaped badge with status icon and text label.

```
[✓ Online]    [○ Offline]    [⟳ Processing]    [✕ Failed]
```

### Text

Colored text label without icon. Space-constrained layouts.

```
Online    Offline    Processing    Failed
```

---

## Pulse Animation

Applied automatically to `processing` and `busy` states. Configurable via the `pulse` prop for custom use.

```css
@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}

/* Respects reduced motion */
@media (prefers-reduced-motion: reduce) {
  .pulse { animation: none; }
}
```

- **Processing**: Continuous slow pulse (2s cycle)
- **Busy**: Gentle pulse (3s cycle)
- **Other**: No animation by default

---

## Examples

### Status dot in a table

```tsx
import { StatusIndicator } from '@sporekart/ui';

function ServiceRow({ service }: { service: Service }) {
  return (
    <tr>
      <td>
        <StatusIndicator
          status={service.status}
          size="xs"
          variant="dot"
        />
      </td>
      <td>{service.name}</td>
    </tr>
  );
}
```

### Status badge in detail view

```tsx
import { StatusIndicator } from '@sporekart/ui';

function OrderDetail({ order }: { order: Order }) {
  return (
    <Card>
      <CardHeader>
        <StatusIndicator
          status={order.status}
          size="md"
          variant="badge"
          pulse={order.status === 'processing'}
        />
      </CardHeader>
      <CardContent>
        <p>Order #{order.id}</p>
      </CardContent>
    </Card>
  );
}
```

### Processing state with pulse

```tsx
<StatusIndicator
  status="processing"
  size="lg"
  variant="icon"
  animate
/>
```

### Inline status with text variant

```tsx
<Text>
  Status:{' '}
  <StatusIndicator
    status={entity.status}
    size="sm"
    variant="text"
  />
</Text>
```

### Status icon in notification

```tsx
<StatusIndicator
  status={notification.priority === 'critical' ? 'failed' : 'pending'}
  size="sm"
  variant="icon"
/>
```

---

## Usage Guidelines

| Principle | Guideline |
|-----------|-----------|
| **Color + Icon** | Never rely on color alone. Every status has a distinct icon for accessibility. |
| **Reduced motion** | Pulse animation respects `prefers-reduced-motion`. Falls back to static dot. |
| **Contextual text** | Use `label` prop on badge/text variants for screen reader announcements. |
| **Consistent mapping** | Do not repurpose status colors. Online = green always, Failed = red always. |
| **Processing duration** | Processing status should auto-transition to Completed or Failed within 10s. |
| **Draft indicator** | Use muted colors for non-active states (draft, archived, offline, queued). |
| **Size in context** | Use `xs`/`sm` for tables and lists. Use `md`/`lg` for cards and detail views. |
| **Animation only for processing** | Only processing and busy states should animate. Static indicators for completed/failed states. |
