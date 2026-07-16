# Enterprise Communication Platform — Mock Data

**Sprint 26 · Part 10.** How the seeded, deterministic datasets are produced in
`data/communicationMockData.ts`. This is the **single source of truth** for the feature.

## 1. Single Source of Truth Principle

All views read from frozen exports in this module. No page generates data, and no component
mutates the datasets. Replacing this file (or the generators) with a real service adapter is
the only change required to move off mock mode — pages and components remain untouched.

## 2. Deterministic PRNG (mulberry32)

```ts
function createRng(seed: number): () => number {
  let a = seed >>> 0;
  return function next(): number {
    a |= 0;
    a = (a + 0x6d2b79f5) | 0;
    let t = Math.imul(a ^ (a >>> 15), 1 | a);
    t = (t + Math.imul(t ^ (t >>> 7), 61 | t)) ^ t;
    return ((t ^ (t >>> 14)) >>> 0) / 4294967296;
  };
}
```

Each generator is seeded with a distinct fixed integer so the full dataset is reproducible
across reloads and environments. Helpers:

- `pick(rng, arr)` — random element.
- `intBetween(rng, min, max)` — inclusive integer.
- `isoOffsetDays(days, hoursJitter)` — ISO timestamp relative to `BASE_NOW`.

## 3. Fixed "Now"

```ts
const BASE_NOW = new Date('2026-07-16T09:00:00.000Z').getTime();
```

All relative times, schedules, and timelines are computed against this fixed instant. The
formatter module mirrors it with its own `MOCK_NOW` for `formatRelative`, so "x days ago" is
stable regardless of the real clock.

## 4. Dataset Volumes

| Dataset | Generator seed | Volume | Frozen export |
| --- | --- | --- | --- |
| Announcements | `20260710` | 48 | `MOCK_ANNOUNCEMENTS` |
| Notifications | `20260711` | 64 | `MOCK_NOTIFICATIONS` |
| Scheduled messages | `20260712` | 18 | `MOCK_SCHEDULED` |
| Templates | `20260713` | 11 (7 active + 4 future) | `MOCK_TEMPLATES` |
| Delivery queue | `20260714` | 30 | `MOCK_DELIVERY_QUEUE` |

`MOCK_ANNOUNCEMENTS` is frozen with `Object.freeze`; the first 3 items are forced to
`published` for a stable "recent" set on Overview. Notifications are sorted newest-first;
scheduled messages are sorted by `scheduledFor` ascending.

## 5. Frozen Exports

```ts
export const MOCK_ANNOUNCEMENTS: readonly Announcement[] = Object.freeze(generateAnnouncements(48));
export const MOCK_NOTIFICATIONS: readonly NotificationItem[] = Object.freeze(generateNotifications(64));
export const MOCK_SCHEDULED:    readonly ScheduledMessage[] = Object.freeze(generateScheduled(18));
export const MOCK_TEMPLATES:    readonly MessageTemplate[] = Object.freeze(generateTemplates());
export const MOCK_DELIVERY_QUEUE: readonly DeliveryQueueItem[] = Object.freeze(generateDeliveryQueue(30));
```

`Object.freeze` makes the top-level array reference immutable (defensive against accidental
push/splice). The module is also side-effect-free beyond generation at import.

## 6. Derived Statistics

```ts
export function computeStats(): CommunicationStats { /* counts over frozen datasets */ }
export const MOCK_STATS: CommunicationStats = computeStats();
```

`computeStats()` derives status counts, pending scheduled count, active template count, and
the placeholder aggregates (`audienceReachPlaceholder`, `deliveredPlaceholder`,
`failedPlaceholder`). It is called once at module load.

## 7. Integration Providers (Roadmap)

```ts
export const INTEGRATION_PROVIDERS: IntegrationProvider[] = [ /* 6 planned providers */ ];
```

Six `IntegrationProvider` records (`email`, `whatsapp`, `sms`, `push`, `calendar`, `crm`), all
`status: 'planned'`, shown on the Future Channels page. These are interfaces only.

## 8. Placeholder Guarantee

- Every field named `…Placeholder` is an illustrative integer/figure, never from a live system.
- `TimelineEvent.placeholder` marks synthetic outcome rows (e.g. "Email delivery skipped
  (Provider not configured)").
- Generator code inserts copy such as "recipients (placeholder)" and "views (placeholder)".
- **Nothing is sent, queued for real dispatch, or persisted.** The Delivery Queue and Scheduled
  pages explicitly state simulated behaviour.

## 9. Mock Content Vocab

Static vocabularies drive generation: `AUTHORS`, `TITLE_STEMS`, `BODY_STEMS`, category/priority
arrays, course/batch/category audience refs (`COURSE_REFS`, `BATCH_REFS`, `CATEGORY_REFS`,
`BROAD_AUDIENCES`), and `TEMPLATE_SEED` (the 11 template definitions, 4 flagged `future`).
