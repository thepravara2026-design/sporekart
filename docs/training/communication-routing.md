# Enterprise Communication Platform — Routing

**Sprint 26 · Part 10.** Route table, redirects, lazy-loading, and section navigation for the
mock-mode Communication feature.

## 1. Route Table

All routes are nested under `/admin/training/communication/*`. The parent
`CommunicationWorkspaceRoute` renders the section nav and an `<Outlet/>`; each child is a lazy
screen.

| Path | Component | Notes |
| --- | --- | --- |
| `/admin/training/communication/overview` | `CommunicationOverviewPage` | Default landing (via redirect). |
| `/admin/training/communication/announcements` | `CommunicationAnnouncementsPage` | Filter/search/sort/paginate. |
| `/admin/training/communication/notifications` | `CommunicationNotificationsPage` | Read/unread feed. |
| `/admin/training/communication/scheduled` | `CommunicationScheduledPage` | Future-dated queue. |
| `/admin/training/communication/templates` | `CommunicationTemplatesPage` | Active + future channel templates. |
| `/admin/training/communication/history` | `CommunicationHistoryPage` | Published/archived/expired table. |
| `/admin/training/communication/delivery` | `CommunicationDeliveryQueuePage` | Simulated delivery pipeline. |
| `/admin/training/communication/channels` | `CommunicationFutureChannelsPage` | Provider roadmap. |
| `/admin/training/communication/statistics` | `CommunicationStatisticsPage` | Aggregate breakdowns. |

## 2. Redirect Behavior

- A bare `/admin/training/communication` (no child segment) **redirects to `/overview`**.
- The index route (`/admin/training/communication/`) also **redirects to `/overview`**.

This guarantees a valid screen is always rendered and no empty outlet is shown.

## 3. Lazy Loading / Code-Splitting

Each child page is loaded via `React.lazy`, so every screen becomes its own JavaScript chunk.
This keeps the initial Training Workspace bundle small and defers the communication feature's
data/formatters/component code until a sub-route is first visited. The parent
`CommunicationWorkspaceRoute` itself is the lazy boundary entry for the section.

Conceptual emitted chunks (per lazy boundary):

```
comm-overview.[hash].js
comm-announcements.[hash].js
comm-notifications.[hash].js
comm-scheduled.[hash].js
comm-templates.[hash].js
comm-history.[hash].js
comm-delivery.[hash].js
comm-channels.[hash].js
comm-statistics.[hash].js
```

(Exact names/hashes are produced by the bundler; the principle is one chunk per route.)

## 4. Section Navigation (CommunicationWorkspaceRoute)

The section tabs are rendered locally (not from global nav config) as a flex-wrapping row of
`NavLink` elements with `aria-label="Communication sections"` on the `<nav>`
(`pages/CommunicationWorkspaceRoute.tsx:44`):

| key | label | path | icon | badge |
| --- | --- | --- | --- | --- |
| overview | Overview | `/overview` | message-square | — |
| announcements | Announcements | `/announcements` | speaker | — |
| notifications | Notifications | `/notifications` | bell | unread count |
| scheduled | Scheduled | `/scheduled` | clock | — |
| templates | Templates | `/templates` | file | — |
| history | History | `/history` | archive | — |
| delivery | Delivery Queue | `/delivery` | send | — |
| channels | Future Channels | `/channels` | radio | — |
| statistics | Statistics | `/statistics` | bar-chart | — |

The notifications tab shows an unread badge computed from `MOCK_NOTIFICATIONS`
(`const unread = MOCK_NOTIFICATIONS.filter((n) => !n.read).length;`), capped at `99+`.
Active link styling uses `--color-bg-primary-weak` / `--color-primary` tokens.

## 5. Global Navigation Unchanged

**The global application navigation configuration was NOT modified.** Section navigation is
self-contained inside `CommunicationWorkspaceRoute`. Adding the communication section to the
app shell required no edits to shared nav/route registries beyond mounting this lazy boundary
at the `/admin/training/communication/*` path.
