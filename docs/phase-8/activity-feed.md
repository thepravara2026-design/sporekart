# Enterprise Activity Feed

## ActivityFeed

Timeline of recent system activities.

```tsx
import { ActivityFeed } from '../dashboard/activity';

<ActivityFeed items={activities} maxItems={10} />
```

### Props
| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `items` | `ActivityItemData[]` | required | Activity items |
| `maxItems` | `number` | 10 | Max items to display |

### ActivityItemData
```tsx
interface ActivityItemData {
  id: string;
  user: string;
  avatar?: string;
  action: string;          // e.g., "created", "updated"
  target: string;          // e.g., "Order #10492"
  type: 'create' | 'update' | 'delete' | 'system' | 'warning' | 'success';
  timestamp: string;       // e.g., "5m ago"
  status?: 'completed' | 'pending' | 'failed';
  details?: string;        // Expandable detail text
}
```

### Type Styling
| Type | Icon | Color |
|------|------|-------|
| `create` | plus | green |
| `update` | edit | primary |
| `delete` | trash | red |
| `system` | settings | info |
| `warning` | alert-triangle | warning |
| `success` | check | green |

### Features
- Expandable details (click item to expand)
- Status indicator dot (completed/pending/failed)
- Empty state when no items
- Scrollable via container overflow
