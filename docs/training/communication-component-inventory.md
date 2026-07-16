# Enterprise Communication Platform — Component Inventory

**Sprint 26 · Part 10.** All 13 presentational components and 9 pages plus the workspace
route, classified by Atomic Design level. Every component is wrapped in `React.memo`.

## 1. Components (13)

| # | Name | File | Atomic level | Purpose | Key props | Reused deps |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | `PriorityBadge` | `PriorityBadge.tsx` | Atom | Renders a priority pill using tone tokens. | `priority: CommunicationPriority`, `size?: 'sm' \| 'md'` | `toneTokens`, `PRIORITY_TONE`, `PRIORITY_LABELS` |
| 2 | `StatusBadge` | `StatusBadge.tsx` | Atom | Renders a status pill using tone tokens. | `status: CommunicationStatus`, `size?: 'sm' \| 'md'` | `toneTokens`, `STATUS_TONE`, `COMMUNICATION_STATUS_LABELS` |
| 3 | `ChannelChip` | `ChannelChip.tsx` | Atom | Renders a delivery-channel chip with icon. | `channel: DeliveryChannel`, `future?: boolean` | `Icon`, `CHANNEL_LABELS`, `CHANNEL_ICON` |
| 4 | `CommEmptyState` | `CommEmptyState.tsx` | Molecule | Centered empty/zero-state message. | `icon?: string`, `title: string`, `description?: string`, `action?: ReactNode` | `Icon` |
| 5 | `SectionTabs` | `SectionTabs.tsx` | Molecule | ARIA `tablist` tab bar. | `tabs: SectionTab[]`, `activeKey: string`, `onChange: (key) => void`, `ariaLabel?: string` | `Icon` |
| 6 | `CommPagination` | `CommPagination.tsx` | Molecule | Page controls + rows-per-page select. | `page`, `pageCount`, `pageSize`, `total`, `onPageChange`, `onPageSizeChange` | `Icon`, `PAGE_SIZE_OPTIONS` |
| 7 | `CommToolbar` | `CommToolbar.tsx` | Molecule | Search (`role="search"`) + filter selects + actions. | `search`, `onSearchChange`, `selects?: FilterSelectConfig[]`, `activeFilterCount?`, `onReset?`, `actions?: ReactNode` | `Icon` |
| 8 | `AnnouncementCard` | `AnnouncementCard.tsx` | Molecule | Card summarising an announcement. | `announcement: Announcement`, `onOpen?: (a) => void` | `Icon`, `PriorityBadge`, `StatusBadge`, `ChannelChip`, formatters |
| 9 | `CommTimeline` | `CommTimeline.tsx` | Molecule | Vertical activity timeline (`<ol>`). | `events: TimelineEvent[]` | `Icon`, `toneTokens`, `formatDateTime` |
| 10 | `RichTextEditor` | `RichTextEditor.tsx` | Organism | Mock formatting editor + sanitized preview. | `initialValue?: string`, `label?: string`, `helperText?: string` | `Icon`, `useRichTextEditorState` |
| 11 | `AudienceSelector` | `AudienceSelector.tsx` | Molecule | `fieldset/legend` audience toggles (future disabled). | `selected: AudienceScope[]`, `onToggle: (s) => void`, `label?: string` | `Icon`, `AUDIENCE_OPTIONS`, `FUTURE_AUDIENCE_SCOPES` |
| 12 | `TemplateCard` | `TemplateCard.tsx` | Molecule | Card for a message template (future dashed). | `template: MessageTemplate`, `onPreview?: (t) => void` | `Icon`, `ChannelChip`, `TEMPLATE_KIND_LABELS`, formatters |
| 13 | `FutureChannelCard` | `FutureChannelCard.tsx` | Molecule | Roadmap card for a planned provider (disabled). | `provider: IntegrationProvider`, `icon: string` | `Icon` |

`SectionTab` shape: `{ key: string; label: string; icon?: string; badge?: number }`.
`FilterSelectConfig` shape: `{ id: string; label: string; value: string; options: SelectOption[]; onChange: (value: string) => void }`.

## 2. Pages (9) + Workspace Route

| # | Name | File | Rendered at | Composition |
| --- | --- | --- | --- | --- |
| R | `CommunicationWorkspaceRoute` | `CommunicationWorkspaceRoute.tsx` | `/admin/training/communication/*` | Section `NavLink` nav + `<Outlet/>` |
| 1 | `CommunicationOverviewPage` | `CommunicationOverviewPage.tsx` | `/overview` | `KpiCard` ×6, `WidgetGrid`/`WidgetCard`, `AnnouncementCard` |
| 2 | `CommunicationAnnouncementsPage` | `CommunicationAnnouncementsPage.tsx` | `/announcements` | `useAnnouncementListState`, `CommToolbar`, `AnnouncementCard`, `CommPagination`, `Dialog` |
| 3 | `CommunicationNotificationsPage` | `CommunicationNotificationsPage.tsx` | `/notifications` | `useNotificationFeedState`, read `tablist`, `CommToolbar`, `PriorityBadge`, `CommEmptyState` |
| 4 | `CommunicationScheduledPage` | `CommunicationScheduledPage.tsx` | `/scheduled` | `MOCK_SCHEDULED`, `ChannelChip`, `CommEmptyState` |
| 5 | `CommunicationTemplatesPage` | `CommunicationTemplatesPage.tsx` | `/templates` | `MOCK_TEMPLATES`, `CommToolbar`, `TemplateCard`, `Dialog`, `RichTextEditor` |
| 6 | `CommunicationHistoryPage` | `CommunicationHistoryPage.tsx` | `/history` | `CommToolbar`, horizontal `<table>`, `StatusBadge`, `PriorityBadge` |
| 7 | `CommunicationDeliveryQueuePage` | `CommunicationDeliveryQueuePage.tsx` | `/delivery` | `MOCK_DELIVERY_QUEUE`, state `tablist`, `CommEmptyState` |
| 8 | `CommunicationFutureChannelsPage` | `CommunicationFutureChannelsPage.tsx` | `/channels` | `INTEGRATION_PROVIDERS`, `FutureChannelCard` |
| 9 | `CommunicationStatisticsPage` | `CommunicationStatisticsPage.tsx` | `/statistics` | `KpiCard`, `WidgetGrid`/`WidgetCard`, internal `BarRow` |

## 3. Barrel Exports

- `components/index.ts` re-exports all 13 components and their prop types.
- `pages/index.ts` re-exports `CommunicationWorkspaceRoute` and all 9 pages (note: the route
  is exported alongside pages for lazy-loading/route-table convenience).

## 4. Atomic Design Summary

- **Atoms**: `PriorityBadge`, `StatusBadge`, `ChannelChip`.
- **Molecules**: `CommEmptyState`, `SectionTabs`, `CommPagination`, `CommToolbar`,
  `AnnouncementCard`, `CommTimeline`, `AudienceSelector`, `TemplateCard`, `FutureChannelCard`.
- **Organisms**: `RichTextEditor` (composes toolbar, textarea, live preview).
- **Templates/screens**: the 9 pages + `CommunicationWorkspaceRoute`.
