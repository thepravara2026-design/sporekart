# Enterprise Communication Platform — Data Model

**Sprint 26 · Part 10.** Field-by-field reference for every exported type in
`data/communicationTypes.ts`. All `…Placeholder` fields are illustrative mock values; nothing
is computed from a live system.

## 1. String-Union Taxonomies

### CommunicationStatus
`draft | scheduled | published | archived | expired`

| Value | Label (`COMMUNICATION_STATUS_LABELS`) |
| --- | --- |
| `draft` | Draft |
| `scheduled` | Scheduled |
| `published` | Published |
| `archived` | Archived |
| `expired` | Expired |

### CommunicationPriority
`low | normal | high | urgent | critical`

| Value | Label (`PRIORITY_LABELS`) |
| --- | --- |
| `low` | Low |
| `normal` | Normal |
| `high` | High |
| `urgent` | Urgent |
| `critical` | Critical |

### CommunicationVisibility
`public | internal | private`

| Value | Label (`VISIBILITY_LABELS`) |
| --- | --- |
| `public` | Public |
| `internal` | Internal |
| `private` | Private |

### CommunicationCategory
`general | academic | enrollment | batch | certificate | system | emergency | marketing | event`

| Value | Label (`CATEGORY_LABELS`) |
| --- | --- |
| `general` | General |
| `academic` | Academic |
| `enrollment` | Enrollment |
| `batch` | Batch |
| `certificate` | Certificate |
| `system` | System |
| `emergency` | Emergency |
| `marketing` | Marketing |
| `event` | Event |

### AudienceScope
`everyone | students | trainers | admins | managers | batch | course | category | role | geography | organization`

| Value | Label (`AUDIENCE_LABELS`) | Notes |
| --- | --- | --- |
| `everyone` | Everyone | active |
| `students` | Students | active |
| `trainers` | Trainers | active |
| `admins` | Admins | active |
| `managers` | Managers | active |
| `batch` | Specific Batch | active (with `refId`/`refLabel`) |
| `course` | Specific Course | active (with `refId`/`refLabel`) |
| `category` | Specific Category | active (with `refId`/`refLabel`) |
| `role` | Role-based (future) | `FUTURE_AUDIENCE_SCOPES` — disabled in UI |
| `geography` | Geography (future) | `FUTURE_AUDIENCE_SCOPES` — disabled in UI |
| `organization` | Organization (future) | `FUTURE_AUDIENCE_SCOPES` — disabled in UI |

### NotificationType
`enrollment-approved | enrollment-pending | enrollment-rejected | course-published | course-updated | batch-scheduled | batch-cancelled | trainer-assigned | certificate-ready | assignment-reminder | assessment-reminder | system-maintenance | general-update | emergency-notice`

Described by `NotificationTypeMeta` and enumerated in `NOTIFICATION_TYPES`:

```ts
interface NotificationTypeMeta {
  type: NotificationType;
  label: string;
  category: CommunicationCategory;
  defaultPriority: CommunicationPriority;
  icon: string;       // design-system icon registry name
}
```

### DeliveryChannel
`in-app | email | whatsapp | sms | push`

| Value | Label (`CHANNEL_LABELS`) | Status |
| --- | --- | --- |
| `in-app` | In-App | active |
| `email` | Email | `FUTURE_CHANNELS` — placeholder only |
| `whatsapp` | WhatsApp | `FUTURE_CHANNELS` — placeholder only |
| `sms` | SMS | `FUTURE_CHANNELS` — placeholder only |
| `push` | Push | `FUTURE_CHANNELS` — placeholder only |

### TemplateKind
`announcement | reminder | certificate | enrollment | batch | marketing | system-alert | email | whatsapp | sms | push`

The first seven are active; the final four (`email | whatsapp | sms | push`) are
`FUTURE_TEMPLATE_KINDS` — channel-specific placeholders.

### TimelineEventType
`created | scheduled | published | archived | viewed | delivered | failed | retry`

## 2. Composite Entities

### AudienceTarget
| Field | Type | Notes |
| --- | --- | --- |
| `scope` | `AudienceScope` | required |
| `refId` | `string?` | present for `batch`/`course`/`category` |
| `refLabel` | `string?` | human label for scoped targets |

### AttachmentPlaceholder
| Field | Type | Notes |
| --- | --- | --- |
| `id` | `string` | |
| `name` | `string` | |
| `kind` | `'document' \| 'image' \| 'video' \| 'link'` | |
| `sizeLabel` | `string` | e.g. `"480 KB"`; `"—"` for links |

### TimelineEvent
| Field | Type | Notes |
| --- | --- | --- |
| `id` | `string` | |
| `type` | `TimelineEventType` | |
| `label` | `string` | |
| `actor` | `string` | author name (mock) |
| `timestamp` | `string` | ISO-8601 |
| `detail` | `string?` | |
| `placeholder` | `boolean?` | true = synthetic/outcome row, never real |

### Announcement
| Field | Type | Notes |
| --- | --- | --- |
| `id` | `string` | `ann-NNNN` |
| `title` | `string` | |
| `summary` | `string` | |
| `body` | `string` | HTML string (rendered as plain text via `stripHtml`) |
| `category` | `CommunicationCategory` | |
| `priority` | `CommunicationPriority` | |
| `status` | `CommunicationStatus` | |
| `visibility` | `CommunicationVisibility` | |
| `audience` | `AudienceTarget[]` | |
| `channels` | `DeliveryChannel[]` | always includes `in-app` |
| `author` | `string` | |
| `createdAt` | `string` | ISO |
| `updatedAt` | `string` | ISO |
| `publishedAt` | `string?` | |
| `scheduledFor` | `string?` | |
| `expiresAt` | `string?` | |
| `pinned` | `boolean` | |
| `featured` | `boolean` | |
| `viewsPlaceholder` | `number` | **placeholder** |
| `reachPlaceholder` | `number` | **placeholder** |
| `attachments` | `AttachmentPlaceholder[]` | |
| `timeline` | `TimelineEvent[]` | |

### NotificationItem
| Field | Type | Notes |
| --- | --- | --- |
| `id` | `string` | `ntf-NNNN` |
| `type` | `NotificationType` | |
| `title` | `string` | |
| `message` | `string` | |
| `priority` | `CommunicationPriority` | |
| `category` | `CommunicationCategory` | |
| `audience` | `AudienceTarget[]` | |
| `channels` | `DeliveryChannel[]` | |
| `createdAt` | `string` | ISO |
| `read` | `boolean` | base read flag; UI override via hook |

### ScheduledMessage
| Field | Type | Notes |
| --- | --- | --- |
| `id` | `string` | `sch-NNNN` |
| `title` | `string` | |
| `kind` | `TemplateKind` | active kinds only in seed |
| `audience` | `AudienceTarget[]` | |
| `channels` | `DeliveryChannel[]` | |
| `scheduledFor` | `string` | ISO (future) |
| `status` | `'scheduled' \| 'paused'` | |
| `createdBy` | `string` | |

### MessageTemplate
| Field | Type | Notes |
| --- | --- | --- |
| `id` | `string` | `tpl-NNNN` |
| `name` | `string` | |
| `kind` | `TemplateKind` | |
| `subject` | `string` | may be `"—"` for some channels |
| `body` | `string` | HTML string with `{{vars}}` |
| `variables` | `string[]` | e.g. `['student_name', 'course_name']` |
| `channels` | `DeliveryChannel[]` | |
| `updatedAt` | `string` | ISO |
| `usageCountPlaceholder` | `number` | **placeholder**; 0 for future templates |
| `future` | `boolean` | true for channel-specific placeholder kinds |

### DeliveryQueueItem
| Field | Type | Notes |
| --- | --- | --- |
| `id` | `string` | `dq-NNNN` |
| `title` | `string` | |
| `channel` | `DeliveryChannel` | |
| `audienceLabel` | `string` | display label only |
| `state` | `'queued' \| 'processing' \| 'delivered' \| 'failed' \| 'retry'` | |
| `attempts` | `number` | |
| `queuedAt` | `string` | ISO |

### CommunicationStats
| Field | Type | Notes |
| --- | --- | --- |
| `totalAnnouncements` | `number` | |
| `published` | `number` | |
| `draft` | `number` | |
| `archived` | `number` | |
| `scheduled` | `number` | |
| `pending` | `number` | scheduled messages count |
| `expired` | `number` | |
| `totalNotifications` | `number` | |
| `templates` | `number` | active (non-future) template count |
| `audienceReachPlaceholder` | `number` | **placeholder** |
| `deliveredPlaceholder` | `number` | **placeholder** |
| `failedPlaceholder` | `number` | **placeholder** |

### IntegrationProvider
| Field | Type | Notes |
| --- | --- | --- |
| `id` | `string` | `int-*` |
| `name` | `string` | |
| `category` | `IntegrationCategory` | `email \| whatsapp \| sms \| push \| calendar \| crm \| erp \| commerce` |
| `status` | `'planned'` | always `'planned'` — interfaces only |
| `description` | `string` | |

## 3. Plural / Aggregate Note

`IntegrationCategory` extends the channel taxonomy with `calendar | crm | erp | commerce` to
describe roadmap integrations. Every `IntegrationProvider` record currently has `status: 'planned'`,
and there is **no provider implementation, credential, or network call** anywhere.
