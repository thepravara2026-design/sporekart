# Notification System

## Overview

The Notification system provides a persistent, centralized inbox for user notifications delivered via a slide-out panel (NotificationCenter), with an unread badge indicator (NotificationBadge). It supports grouping, priority levels, categories, and real-time updates.

### Component Hierarchy

```
NotificationCenter
├── NotificationBadge
├── NotificationGroup
├── NotificationItem
├── NotificationCategory
├── NotificationPriority
└── NotificationEmpty
```

---

## NotificationCenter

A slide-out panel (drawer) from the right edge of the viewport displaying the user's notification history.

### Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `open` | `boolean` | `false` | Panel visibility |
| `onClose` | `() => void` | — | Close handler |
| `notifications` | `Notification[]` | `[]` | Notification list |
| `loading` | `boolean` | `false` | Loading state |
| `hasMore` | `boolean` | `false` | Pagination flag |
| `onLoadMore` | `() => void` | — | Load next page |
| `onMarkRead` | `(id: string) => void` | — | Mark single as read |
| `onMarkAllRead` | `() => void` | — | Mark all as read |
| `onDismiss` | `(id: string) => void` | — | Remove notification |
| `onAction` | `(id: string, action: string) => void` | — | Action button click |
| `emptyState` | `ReactNode` | — | Custom empty state |
| `maxHeight` | `string` | `'100vh'` | Panel max height |
| `width` | `string` | `'400px'` | Panel width (desktop) |
| `title` | `string` | `'Notifications'` | Panel header title |

### NotificationItem

```typescript
interface Notification {
  id: string;
  title: string;
  description?: string;
  category: NotificationCategory;
  priority: NotificationPriority;
  read: boolean;
  timestamp: string | Date;
  actions?: NotificationAction[];
  avatar?: string;  // user avatar URL for social notifications
  link?: string;     // deep link URL
  metadata?: Record<string, unknown>;
}

interface NotificationAction {
  label: string;
  value: string;
  variant?: 'primary' | 'secondary' | 'ghost';
}
```

---

## NotificationBadge

Displays unread count or a presence dot on the notification bell/icon.

### Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `count` | `number` | `0` | Unread count |
| `max` | `number` | `99` | Maximum displayed count (99+) |
| `dot` | `boolean` | `false` | Show dot instead of count |
| `pulse` | `boolean` | `false` | Pulse animation for new notifications |
| `color` | `string` | token | Badge color |
| `children` | `ReactNode` | — | Trigger element |
| `onClick` | `() => void` | — | Click handler |

---

## Grouped Notifications

Notifications can be grouped by category, date, or a custom key.

```typescript
interface NotificationGroup {
  key: string;
  label: string;
  notifications: Notification[];
}

// Default grouping strategies:
// 'date': Today, Yesterday, This Week, Earlier
// 'category': Groups by NotificationCategory
// 'none': Flat list

<NotificationCenter
  notifications={notifications}
  groupBy="date"
  renderGroupHeader={(group) => (
    <GroupHeader label={group.label} count={group.notifications.length} />
  )}
/>
```

---

## Priority Levels

| Priority | Color | Behavior |
|----------|-------|----------|
| **Critical** | Red (`--color-danger`) | Highest visual emphasis. Stays at top. Screen reader announces as "Critical". |
| **High** | Orange (`--color-warning`) | Prominent. Rendered above normal. |
| **Normal** | Blue (`--color-info`) | Default priority. |
| **Low** | Gray (`--color-muted`) | Minimal styling. May be collapsed by default. |

```typescript
type NotificationPriority = 'critical' | 'high' | 'normal' | 'low';
```

---

## Categories

```typescript
type NotificationCategory =
  | 'system'      // System updates, maintenance
  | 'security'    // Login alerts, permission changes
  | 'billing'     // Invoices, payments, subscriptions
  | 'social'      // Mentions, comments, follows
  | 'task'        // Assignment, status changes, reminders
  | 'approval'    // Pending approvals, workflow updates
  | 'error';      // Failure alerts, critical errors
```

Each category has a default icon and color mapping from design tokens.

---

## Empty State

When there are no notifications, the NotificationCenter displays a default empty state with illustration and message.

```tsx
import { NotificationEmpty } from '@sporekart/ui';

// Default empty state:
<NotificationEmpty
  title="No notifications yet"
  description="We'll notify you when something arrives."
  action={{ label: 'Explore', onClick: () => navigate('/dashboard') }}
/>

// Custom via slot:
<NotificationCenter
  emptyState={
    <CustomEmptyState
      icon={<BellIcon />}
      message="All caught up!"
    />
  }
/>
```

---

## Examples

### Full notification center setup

```tsx
import {
  NotificationCenter,
  NotificationBadge,
  useNotificationCenter,
} from '@sporekart/ui';

function AppHeader() {
  const {
    open,
    setOpen,
    notifications,
    unreadCount,
    markRead,
    markAllRead,
    loadMore,
    loading,
    hasMore,
    dismiss,
  } = useNotificationCenter({
    fetchNotifications: api.fetchNotifications,
    markAsRead: api.markAsRead,
    markAllAsRead: api.markAllAsRead,
  });

  return (
    <header>
      <NotificationBadge count={unreadCount} onClick={() => setOpen(true)}>
        <BellIcon />
      </NotificationBadge>

      <NotificationCenter
        open={open}
        onClose={() => setOpen(false)}
        notifications={notifications}
        loading={loading}
        hasMore={hasMore}
        onLoadMore={loadMore}
        onMarkRead={markRead}
        onMarkAllRead={markAllRead}
        onDismiss={dismiss}
        groupBy="date"
      />
    </header>
  );
}
```

### Bell icon with dot indicator

```tsx
<NotificationBadge dot pulse={hasUnread}>
  <IconButton aria-label="Notifications">
    <BellIcon />
  </IconButton>
</NotificationBadge>
```

### Custom notification action

```tsx
const notifications = [
  {
    id: 'n1',
    title: 'Purchase order #123 requires approval',
    description: 'PO value exceeds $10,000. Review required.',
    category: 'approval' as const,
    priority: 'high' as const,
    read: false,
    timestamp: new Date(),
    actions: [
      { label: 'Review', value: 'review', variant: 'primary' as const },
      { label: 'Delegate', value: 'delegate', variant: 'ghost' as const },
    ],
  },
];

<NotificationCenter
  notifications={notifications}
  onAction={(id, action) => {
    if (action === 'review') navigate(`/approvals/${id}`);
    if (action === 'delegate') showDelegateDialog(id);
  }}
/>
```

---

## Real-Time Integration Hooks

```typescript
interface UseNotificationCenterOptions {
  fetchNotifications: (params: FetchParams) => Promise<PaginatedResult<Notification>>;
  markAsRead: (id: string) => Promise<void>;
  markAllAsRead: () => Promise<void>;
  dismissNotification?: (id: string) => Promise<void>;
  realtime?: {
    enabled: boolean;
    provider?: 'websocket' | 'sse' | 'polling';
    url?: string;
    onNotification: (notification: Notification) => void;
  };
}

function useNotificationCenter(
  options: UseNotificationCenterOptions
): {
  open: boolean;
  setOpen: (open: boolean) => void;
  notifications: Notification[];
  unreadCount: number;
  loading: boolean;
  hasMore: boolean;
  markRead: (id: string) => void;
  markAllRead: () => void;
  loadMore: () => void;
  dismiss: (id: string) => void;
  error: Error | null;
}
```

### Real-time via WebSocket

```typescript
useNotificationCenter({
  fetchNotifications: api.fetchNotifications,
  markAsRead: api.markAsRead,
  markAllAsRead: api.markAllAsRead,
  realtime: {
    enabled: true,
    provider: 'websocket',
    url: `${WS_BASE}/notifications`,
    onNotification: (notification) => {
      playNotificationSound();
      showToast(notification);
    },
  },
});
```

---

## Best Practices

- **Badge max**: Cap the count display at 99+. Show "99+" for high volumes to avoid layout issues.
- **Grouping**: Always group notifications by date at minimum. Category grouping adds further organization.
- **Virtual scroll**: For 1000+ notifications, use virtual scrolling in the NotificationCenter to maintain performance.
- **Sound**: Play a subtle notification sound only for critical/high priority notifications.
- **Read state**: Use a semi-bold weight for unread items, normal for read. Visual distinction without relying solely on color.
- **Batch actions**: Always provide "Mark all as read" in the header.
- **Deep links**: Each notification should link to the relevant page/entity. Use `link` property for navigation.
- **Empty state**: Never show a bare panel. Always show empty state with helpful message.
- **Pagination**: Load 20-50 items initially. Use infinite scroll or "Load more" button.
