# Display Components — Usage Guidelines

## When to Use Card vs Table vs List

| Scenario | Recommended Component |
|----------|----------------------|
| Single item summary / metric | Card (StatCard, MetricCard) |
| Collection with rich content (images, badges, actions) | Card Grid |
| Tabular data with multiple columns | Table |
| Simple item list with minimal metadata | List |
| Item list with leading icons/avatars | List (icon variant) |
| Item list with titles and descriptions | List (description variant) |
| Interactive selection from items | List (selectable) or Table (selectable) |

| Pattern | Preference |
|---------|------------|
| `< 10 items` | Card grid or List |
| `10–100 items` | Table with pagination |
| `> 100 items` | Table with pagination + server-side sorting |
| Read-only metadata | Card or List |
| Editable/selectable | Table with selection |

## Card Hierarchy and Grouping Patterns

- **Primary cards** (elevated shadow) — main content focus
- **Secondary cards** (outlined) — supporting information
- **Ghost cards** — background content, no container emphasis

Grouping pattern for dashboards:
```
Row 1: Primary MetricCards (key KPIs)
Row 2: StatCards (secondary metrics)
Row 3: InfoCard / OrderCard (detail)
Row 4: SummaryCard (aggregation)
```

## Avatar Placement Guidelines

- Use `AvatarGroup` for collaborative contexts (team members, reviewers)
- Max visible avatars: 4–6 before overflow count
- Overlapping order: right-to-left, most recent/relevant first
- Status indicators only when real-time presence is meaningful
- Size `xs`–`sm` for inline use (comments, mentions)
- Size `md`–`lg` for profiles and cards
- Size `xl`–`2xl` for profile pages and hero sections

## Badge Count Limits

Count badges display `99+` when the value exceeds the `max` prop (default: 99). This prevents layout shifting from large numbers.

| Context | Max | Behavior |
|---------|-----|----------|
| Navigation badges | 99 | Show `99+` above 99 |
| Table cell counts | 999 | Show `999+` above 999 |
| Inline badges | 99 | Show `99+` above 99 |

## Empty State vs Loading State Decisions

| Condition | Show |
|-----------|------|
| Initial load, no cache | Skeleton |
| Page transition | Skeleton |
| Data loaded, zero results | Empty state |
| Error on load | Error empty state |
| First visit, no data | Empty state (first-time variant) |
| After applying filters, zero results | Empty state (search/filtered variant) |
| Refresh/refetching | Keep previous data + loading indicator |

**Rule:** Never show an empty state while data is loading. Always show skeletons first, then transition to empty state only after confirming there is no data.

## Skeleton Placement and Timing

- Show skeleton immediately on navigation (no delay)
- Minimum skeleton duration: 300ms (avoid flash of loading for fast responses)
- Transition: fade-out skeleton → fade-in content (300ms crossfade)
- Only skeletonize areas that change — keep persistent UI (sidebar, header) static
- Use skeleton variants that match the content shape (SkeletonTable for table, SkeletonCard for cards)

## Layout Composition Best Practices

1. **Start with Container** — Wrap pages in `Container` with appropriate `maxWidth`
2. **Use Stack for vertical rhythm** — Consistent vertical spacing between sections
3. **Use Inline for horizontal groupings** — Button bars, filter rows, avatar groups
4. **Use Grid for card layouts** — Prefer `auto-fit` with `minColumnWidth` for responsive grids
5. **Nest layout primitives** — Stack > Inline > Cluster for complex layouts
6. **Avoid hardcoded margins** — Always use layout helper gap props

## Responsive Patterns

| Pattern | Desktop | Tablet | Mobile |
|---------|---------|--------|--------|
| Card grid | 3–4 columns | 2 columns | 1 column |
| Table | Full table | Horizontal scroll | Horizontal scroll |
| Stack (form) | 2-column fields | 2-column fields | 1 column |
| Inline (buttons) | Inline | Wrap | Stack vertical |
| Container padding | `--spacing-lg` | `--spacing-md` | `--spacing-sm` |

## Accessibility Notes

- All interactive cards must have `role="button"` or use `<a>` with `href`
- Table headers use `<th>` with `scope="col"` or `scope="row"`
- Skeleton elements use `aria-hidden="true"` to hide from screen readers
- Empty states include `role="status"` and `aria-live="polite"`
- Badges on interactive elements include `aria-label` for context
- Avatar images require `alt` text; initials avatars require `aria-label`
