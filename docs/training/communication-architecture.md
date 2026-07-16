# Enterprise Communication Platform — Architecture

**Sprint 26 · Part 10** — Documentation-only reference for the mock-mode Communication
feature inside the SporeKart LMS Training Workspace.

## 1. Scope and Operating Mode

The platform is **MOCK ONLY**. There is no backend, API, SMTP, WhatsApp, SMS, Push, or
third-party integration. All delivery, reach, view, deliver/fail, and usage numbers are
illustrative placeholders. Nothing is ever sent, queued for real dispatch, or persisted.

The feature is mounted under:

```
/admin/training/communication/*
```

It is a sibling of other training-workspace sub-features (rather than under
`/admin/analytics`) because communication is an operational, authoring-and-queue workflow,
not a reporting surface. Analytics widgets are *reused* for display, but the feature owns
its own data and routes.

## 2. Layered Structure

The feature follows feature-first organisation with strict Atomic Design boundaries:

| Layer | Path | Responsibility |
| --- | --- | --- |
| Data | `data/` | Domain types, taxonomy, mock generators, options/presentation maps, pure formatters. No React. |
| State | `state/` | Custom hooks that filter/sort/paginate mock data and hold transient UI state. |
| Components | `components/` | Presentational atoms/molecules/organisms. All wrapped in `React.memo`. |
| Pages | `pages/` | Route targets that compose hooks + components into screen-level views. |

```
pages/ (route targets)
   │  render
   ▼
components/ (presentational, memoized)
   │  consume
   ▼
state/ (hooks: use*ListState / use*FeedState / useRichTextEditorState)
   │  read
   ▼
data/ (types, mock datasets, options, formatters)
```

## 3. Data Flow

1. `data/communicationMockData.ts` generates **frozen** datasets once at module load using a
   seeded PRNG (`mulberry32`) and a fixed `BASE_NOW = 2026-07-16T09:00:00Z`.
2. State hooks import those frozen constants and apply client-side filtering, search, sort,
   pagination, and read-state overrides. Derived values (`filtered`, `paged`, `unreadCount`)
   are memoised with `useMemo`.
3. Pages call hooks, then pass plain data to presentational components.
4. Components render using design-system `Icon`/`Dialog` and analytics `KpiCard`/`WidgetGrid`/
   `WidgetCard`, plus CSS custom-property tokens only.

There is **no upward data mutation**: components never write back to the datasets. The only
stateful interactions are local UI state (filters, pagination, read/unread toggles, dialog
open state, editor state), all held in hooks/components.

## 4. Reuse of Design-System and Analytics

| Reused module | Import path | Where used |
| --- | --- | --- |
| `Icon` | `design-system/icons/Icon` | Every component, badges, timeline, editor toolbar |
| `Dialog` | `design-system/components/feedback/Dialog` | Announcement detail, template preview |
| `KpiCard` | `analytics/components/KpiCard` | Overview, Statistics |
| `WidgetGrid` | `analytics/components/WidgetGrid` | Overview, Statistics |
| `WidgetCard` | `analytics/components/WidgetCard` | Overview, Statistics |

`KpiCard`/`WidgetGrid`/`WidgetCard` are thin shells that wrap `MetricTile`/`ChartSkeleton`
internals; the communication feature does not import those inner primitives directly, keeping
the dependency surface stable.

## 5. Route Mounting Rationale

- Mounted under `/admin/training/*` to keep it within the Training Workspace navigation domain.
- Not under `/admin/analytics` because it is an authoring/operations surface, not a BI surface.
- **Global navigation config was NOT modified.** Section navigation is rendered locally inside
  `CommunicationWorkspaceRoute` via `NavLink` elements (`pages/CommunicationWorkspaceRoute.tsx:44`).
- All routes are lazy-loaded (`React.lazy`) so each screen is a separate code chunk.

## 6. Separation of Concerns

- **Types vs data**: `communicationTypes.ts` declares contracts; `communicationMockData.ts`
  implements them. Future real services can replace the mock module without touching components.
- **Options vs types**: `communicationOptions.ts` derives `SelectOption[]`, sort keys, priority
  weights, and tone/icon maps from the taxonomy — presentation intent stays out of the type layer.
- **Formatters are pure**: `communicationFormatters.ts` has no side effects and no React imports.
- **State is hook-local**: business rules (filtering, sorting) live in hooks, testable in isolation.

## 7. Mock-Mode Boundary

The mock-mode boundary is enforced by convention and structure:

- No `fetch`/`axios`/WebSocket/SMTP imports anywhere in the feature tree.
- All *real-world* outcome fields are suffixed `Placeholder` (e.g. `viewsPlaceholder`,
  `reachPlaceholder`, `usageCountPlaceholder`, `deliveredPlaceholder`, `failedPlaceholder`).
- `TimelineEvent.placeholder` flags synthetic activity rows (e.g. "Email delivery skipped").
- The Delivery Queue page labels every external channel as a placeholder and states that no
  dispatch occurs.
- Future channels/providers exist only as `IntegrationProvider` records with `status: 'planned'`
  and as disabled affordances in `FutureChannelCard`.

This keeps a clean seam: when a real provider is integrated, only the `data/` layer and the
state hooks need to change; pages and components remain valid.
