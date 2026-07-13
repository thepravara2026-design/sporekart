# Timeline Components

## Overview

Five timeline variants cover different domain use cases. All share the same visual language (connector lines, nodes/events, content panels) while specialising for activities, orders, training, and audits.

---

## Timeline

Generic, configurable timeline. Used when you need full control over items.

### Props

| Prop | Type | Default | Required | Description |
|------|------|---------|----------|-------------|
| `items` | `TimelineItem[]` | — | Yes | Array of timeline events |
| `orientation` | `'vertical' \| 'horizontal'` | `'vertical'` | No | Layout direction |
| `align` | `'left' \| 'center' \| 'alternate'` | `'left'` | No | Node alignment |
| `lineColor` | `string` | theme border | No | Connector line color |
| `activeColor` | `string` | theme primary | No | Active/completed node color |
| `className` | `string` | — | No | Additional CSS classes |

### TimelineItem

```ts
interface TimelineItem {
  id: string;
  title: string;
  description?: string;
  date?: string;
  time?: string;
  status?: 'completed' | 'active' | 'pending' | 'error';
  icon?: ReactNode;
  content?: ReactNode;
}
```

---

## ActivityTimeline

Activity feed timeline for user actions, system events, and notifications.

### Props

| Prop | Type | Default | Required | Description |
|------|------|---------|----------|-------------|
| `activities` | `Activity[]` | — | Yes | Activity items |
| `maxItems` | `number` | — | No | Max displayed items |
| `showViewAll` | `boolean` | `false` | No | Show "View all" link |
| `onViewAll` | `() => void` | — | No | View all click handler |
| `className` | `string` | — | No | Additional CSS classes |

### Activity

```ts
interface Activity {
  id: string;
  user: { name: string; avatar?: string };
  action: string;
  target: string;
  timestamp: string;
  type?: 'create' | 'update' | 'delete' | 'comment' | 'system';
}
```

---

## OrderTimeline

Order status tracking timeline (ordered → confirmed → shipped → delivered).

### Props

| Prop | Type | Default | Required | Description |
|------|------|---------|----------|-------------|
| `status` | `OrderStatus` | — | Yes | Current order status |
| `history` | `OrderStatusEvent[]` | — | No | Full order history |
| `orderId` | `string` | — | No | Display order ID |
| `className` | `string` | — | No | Additional CSS classes |

### OrderStatus

```ts
type OrderStatus = 'pending' | 'confirmed' | 'processing' | 'shipped' | 'delivered' | 'cancelled' | 'returned';
```

---

## TrainingTimeline

Employee training session/progress timeline.

### Props

| Prop | Type | Default | Required | Description |
|------|------|---------|----------|-------------|
| `modules` | `TrainingModule[]` | — | Yes | Training modules |
| `currentModuleId` | `string` | — | No | Currently active module |
| `progress` | `number` | — | No | Overall progress 0–100 |
| `showProgress` | `boolean` | `true` | No | Show progress bar |
| `className` | `string` | — | No | Additional CSS classes |

### TrainingModule

```ts
interface TrainingModule {
  id: string;
  title: string;
  type: 'video' | 'quiz' | 'reading' | 'assignment';
  duration: string;
  status: 'locked' | 'available' | 'in-progress' | 'completed';
  score?: number;
}
```

---

## AuditTimeline

Audit trail timeline for compliance and security events.

### Props

| Prop | Type | Default | Required | Description |
|------|------|---------|----------|-------------|
| `events` | `AuditEvent[]` | — | Yes | Audit events |
| `filterable` | `boolean` | `false` | No | Enable event type filtering |
| `searchable` | `boolean` | `false` | No | Enable search |
| `className` | `string` | — | No | Additional CSS classes |

### AuditEvent

```ts
interface AuditEvent {
  id: string;
  actor: string;
  action: 'create' | 'update' | 'delete' | 'read' | 'export' | 'login';
  resource: string;
  resourceId: string;
  timestamp: string;
  ip?: string;
  details?: string;
  severity?: 'info' | 'warning' | 'critical';
}
```

---

## Usage Examples

### Basic timeline

```tsx
import { Timeline } from '@sporekart/ui';

const items = [
  { id: '1', title: 'Project Kickoff', date: '2026-01-15', status: 'completed' },
  { id: '2', title: 'Design Phase', date: '2026-02-01', status: 'active' },
  { id: '3', title: 'Development', date: '2026-03-01', status: 'pending' },
];

<Timeline items={items} />
```

### Activity feed

```tsx
import { ActivityTimeline } from '@sporekart/ui';

<ActivityTimeline
  activities={[
    { id: '1', user: { name: 'Alice' }, action: 'created', target: 'Order #1234', timestamp: '2m ago', type: 'create' },
    { id: '2', user: { name: 'Bob' }, action: 'updated', target: 'Invoice #567', timestamp: '15m ago', type: 'update' },
  ]}
  showViewAll
  onViewAll={() => navigate('/activities')}
/>
```

### Order tracking

```tsx
import { OrderTimeline } from '@sporekart/ui';

<OrderTimeline
  status="shipped"
  history={orderHistory}
  orderId="ORD-2026-0421"
/>
```

### Training progress

```tsx
import { TrainingTimeline } from '@sporekart/ui';

<TrainingTimeline
  modules={[
    { id: 'm1', title: 'Safety Basics', type: 'video', duration: '10min', status: 'completed', score: 100 },
    { id: 'm2', title: 'Equipment Handling', type: 'quiz', duration: '15min', status: 'in-progress' },
    { id: 'm3', title: 'Emergency Procedures', type: 'reading', duration: '20min', status: 'locked' },
  ]}
  currentModuleId="m2"
  progress={45}
/>
```

### Audit trail

```tsx
import { AuditTimeline } from '@sporekart/ui';

<AuditTimeline
  events={[
    { id: 'a1', actor: 'john.doe@example.com', action: 'export', resource: 'Report', resourceId: 'RPT-0421', timestamp: '2026-07-13T10:30:00Z', severity: 'info' },
    { id: 'a2', actor: 'admin', action: 'delete', resource: 'User', resourceId: 'USR-009', timestamp: '2026-07-13T09:15:00Z', severity: 'critical' },
  ]}
  filterable
  searchable
/>
```
