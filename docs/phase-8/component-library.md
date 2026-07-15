# Phase 8 Enterprise Admin Component Library

## Design Principles

1. **No duplication**: If a component exists in the design system (`src/design-system/`), it is re-exported through admin barrel files — never re-implemented.
2. **Admin-specific only**: New components are created only when no equivalent exists in the design system AND the use case is specific to admin workflows.
3. **Form-first**: The admin platform has unique form needs (currency, phone, time, tags) that the customer-facing site doesn't require.
4. **Status visibility**: Admin workflows demand rich status/priority indicators across orders, inventory, users, and system health.
5. **Composable**: Components are designed to compose together (e.g., `PanelContainer` wraps any content; `Pagination` integrates with `Table`).

## Component Categories

### Forms (`src/admin/components/forms/`)

| Component | Design System Alternative | Why New |
|-----------|--------------------------|---------|
| `Textarea` | Basic `<textarea>` | Adds character count, resize handle, label+error styling |
| `NumberInput` | Basic `<input type="number">` | Adds +/- stepper buttons, keyboard support, min/max enforcement |
| `CurrencyInput` | None | Locale-aware (₹/$/€), format on blur, prefix/suffix |
| `EmailInput` | Basic `<input type="email">` | Adds format validation feedback, suggested domain dropdown |
| `PhoneInput` | None | Country code dropdown, format masking, E.164 preparation |
| `TimePicker` | None | Hour/minute selection, 12/24h toggle, keyboard accessible |
| `TagSelector` | None | Chip display, type-to-add, backspace-to-remove, suggestion dropdown |

### Data Display (`src/admin/components/data/`)

| Component | Design System Alternative | Why New |
|-----------|--------------------------|---------|
| `PanelContainer` | `Card` | Adds title bar, description, actions toolbar, footer slot |
| `NoResults` | `EmptyState` | Specialized compact empty state for search/filter results |

### Navigation (`src/admin/components/navigation/`)

| Component | Design System Alternative | Why New |
|-----------|--------------------------|---------|
| `Tabs` | Basic nav | Supports 3 variants (underline, pills, buttons), badges |
| `Accordion` | None | Collapsible sections for settings/config panels |
| `Pagination` | None | Full-featured: page numbers, ellipsis, page size, go-to-page |

### Status Indicators (`src/admin/components/status/`)

| Component | Design System Alternative | Why New |
|-----------|--------------------------|---------|
| `StatusBadge` | None | Dot + pulse animation, 6+ color variants |
| `PriorityBadge` | None | Semantic priority tiers (critical → low) |
| `OrderBadge` | None | 8 order lifecycle states with distinct colors |

### Re-exports (`src/admin/components/{buttons,feedback,loading}/`)

These directories contain barrel files (`index.ts`) that re-export design system components without modification:
- `buttons/`: `Button`, `IconButton`, `SplitButton`, `ButtonGroup`, `DropdownButton`
- `feedback/`: `Alert`, `Dialog`, `Modal`, `Toast`, `Popover`, `Tooltip`
- `loading/`: `Skeleton`, `CardSkeleton`, `TableSkeleton`, `PageSkeleton`, `Spinner`

## Usage Pattern

```tsx
// Import from admin barrel for consistency
import { Button } from '../components/buttons';
import { Card } from '../components/layout';   // would be added when created
import { Textarea } from '../components/forms';
import { StatusBadge } from '../components/status';

// Always use named imports, never default imports
<Textarea label="Description" rows={4} maxLength={500} />
<StatusBadge status="active" variant="success" />
```

## Preview Routes

Every category has a dedicated preview route under `/preview/admin/components/`:
- `.../components` — Landing grid
- `.../components/buttons` — All button variants/sizes/states
- `.../components/forms` — All form components
- `.../components/cards` — Card variants
- `.../components/tables` — Table states
- `.../components/dialogs` — Feedback & dialogs
- `.../components/loading` — Skeleton & spinner
- `.../components/badges` — Status/priority/order badges
- `.../components/tabs` — Tabs, accordion, pagination

## Accessibility

All new components follow WCAG 2.1 AA:
- Form inputs have associated `<label>` elements
- Interactive elements are keyboard navigable
- Color-coded badges include text labels (not color-only)
- Focus indicators visible on all interactive elements
- ARIA attributes used where semantic HTML is insufficient
