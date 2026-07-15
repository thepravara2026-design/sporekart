# Enterprise Widget Framework

## WidgetCard

Reusable widget wrapper with title bar and menu.

```tsx
import { WidgetCard } from '../dashboard/widgets';

<WidgetCard widget={widget} onPin={handlePin} onRemove={handleRemove}>
  {/* Widget content */}
</WidgetCard>
```

### Props
| Prop | Type | Description |
|------|------|-------------|
| `widget` | `WidgetConfig` | Widget configuration |
| `children` | `ReactNode` | Widget content |
| `onPin` | `(id: string) => void` | Pin/unpin handler |
| `onRemove` | `(id: string) => void` | Remove widget handler |
| `loading` | `boolean` | Show loading spinner |

### WidgetConfig
```tsx
interface WidgetConfig {
  id: string;
  title: string;
  type: string;
  icon: string;
  width: 1 | 2 | 3 | 4;    // Grid column span
  height: 1 | 2;            // Grid row span
  pinned?: boolean;
  visible: boolean;
}
```

## WidgetGrid

4-column CSS Grid for widget layout.

```tsx
<WidgetGrid widgets={widgets} onPin={handlePin} onRemove={handleRemove} />
```

- Widgets with `width: 2` span 2 columns
- Widgets with `height: 2` span 2 rows
- Empty state when no visible widgets
- Smooth transitions on pin/remove

## Available Widget Types

| Type | Component | Description |
|------|-----------|-------------|
| `sales-overview` | `SalesOverviewWidget` | Weekly bar chart placeholder |
| `recent-orders` | `RecentOrdersWidget` | 5 recent orders with status |
| `inventory-status` | `InventoryStatusWidget` | Circular progress (SVG) |
| `customer-growth` | `CustomerGrowthWidget` | 6-period bar chart |
| `revenue` | `RevenueWidget` | KPI with progress bar |
| `training-overview` | `TrainingOverviewWidget` | 4 progress bars |
| `tasks` | `TasksWidget` | 5-item checklist |
| `calendar` | `CalendarWidget` | Date + mini week view |
| `notifications` | `NotificationsWidget` | 3 notification items |
| `quick-stats` | `QuickStatsWidget` | 4-value stat grid |

## Adding a New Widget Type

1. Add the type string to `WidgetConfig`
2. Create a component in `WidgetContent.tsx`
3. Add a case to the `switch` statement
4. Add mock configuration in `mockData.ts`
