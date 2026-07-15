# Enterprise Quick Actions

## QuickActions

Configurable grid of action buttons with icons.

```tsx
import { QuickActions } from '../dashboard/actions';

<QuickActions actions={actions} columns={4} />
```

### Props
| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `actions` | `QuickActionData[]` | required | Action items |
| `columns` | `number` | 4 | Grid columns |

### QuickActionData
```tsx
interface QuickActionData {
  id: string;
  label: string;      // Display text
  icon: string;       // Icon name
  path: string;       // Navigation route
  color: string;      // Theme color for icon background
}
```

### Default Actions
| Action | Icon | Route |
|--------|------|-------|
| Add Product | plus | `/admin/products/new` |
| Create Category | folder-plus | `/admin/categories/new` |
| Create Training | book | `/admin/training/new` |
| Manage Orders | shopping-cart | `/admin/orders` |
| Manage Inventory | archive | `/admin/inventory` |
| View Reports | bar-chart | `/admin/reports` |
| Customer Management | users | `/admin/customers` |
| Settings | settings | `/admin/settings` |

All routes navigate to placeholders (no business modules implemented).
