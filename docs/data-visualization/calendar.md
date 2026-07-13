# Calendar Components

## Overview

Four calendar views covering month grid, week agenda, list view, and date range selection. All views share a common event model and support keyboard navigation, theming, and responsive layouts.

---

## CalendarMonth

Full month grid view with days, events, and navigation.

### Props

| Prop | Type | Default | Required | Description |
|------|------|---------|----------|-------------|
| `month` | `Date` | current month | No | The month to display |
| `events` | `CalendarEvent[]` | `[]` | No | Events to render |
| `onMonthChange` | `(date: Date) => void` | — | No | Month navigation callback |
| `onEventClick` | `(event: CalendarEvent) => void` | — | No | Event click handler |
| `onDayClick` | `(date: Date) => void` | — | No | Day click handler |
| `minDate` | `Date` | — | No | Minimum selectable date |
| `maxDate` | `Date` | — | No | Maximum selectable date |
| `showWeekNumbers` | `boolean` | `false` | No | Show ISO week numbers |
| `className` | `string` | — | No | Additional CSS classes |

### CalendarEvent

```ts
interface CalendarEvent {
  id: string;
  title: string;
  start: Date;
  end?: Date;
  allDay?: boolean;
  color?: string;
  status?: 'confirmed' | 'tentative' | 'cancelled';
  description?: string;
}
```

---

## CalendarWeek

Week/day view for detailed scheduling.

### Props

| Prop | Type | Default | Required | Description |
|------|------|---------|----------|-------------|
| `date` | `Date` | current week | No | The week to display |
| `events` | `CalendarEvent[]` | `[]` | No | Events to render |
| `onDateChange` | `(date: Date) => void` | — | No | Week navigation callback |
| `onEventClick` | `(event: CalendarEvent) => void` | — | No | Event click handler |
| `onSlotClick` | `(date: Date) => void` | — | No | Time slot click handler |
| `startHour` | `number` | `8` | No | First displayed hour (0–23) |
| `endHour` | `number` | `18` | No | Last displayed hour |
| `className` | `string` | — | No | Additional CSS classes |

---

## CalendarAgenda

Scrollable list of upcoming events.

### Props

| Prop | Type | Default | Required | Description |
|------|------|---------|----------|-------------|
| `events` | `CalendarEvent[]` | `[]` | Yes | Events to display |
| `maxItems` | `number` | — | No | Limit displayed items |
| `showViewAll` | `boolean` | `false` | No | Show "View all" link |
| `onViewAll` | `() => void` | — | No | View all handler |
| `onEventClick` | `(event: CalendarEvent) => void` | — | No | Event click handler |
| `groupByDate` | `boolean` | `true` | No | Group events by date |
| `className` | `string` | — | No | Additional CSS classes |

---

## CalendarDateRange

Date range selector for reports and filters.

### Props

| Prop | Type | Default | Required | Description |
|------|------|---------|----------|-------------|
| `startDate` | `Date \| null` | — | No | Selected start date |
| `endDate` | `Date \| null` | — | No | Selected end date |
| `onChange` | `(range: { start: Date \| null, end: Date \| null }) => void` | — | Yes | Selection callback |
| `minDate` | `Date` | — | No | Minimum date |
| `maxDate` | `Date` | — | No | Maximum date |
| `presets` | `DateRangePreset[]` | — | No | Quick-select presets |
| `singleMonth` | `boolean` | `false` | No | Show single month |
| `className` | `string` | — | No | Additional CSS classes |

### DateRangePreset

```ts
interface DateRangePreset {
  label: string;
  getRange: () => { start: Date; end: Date };
}
```

---

## Keyboard Navigation

| Key | CalendarMonth | CalendarWeek | CalendarAgenda | CalendarDateRange |
|-----|---------------|--------------|----------------|-------------------|
| `Arrow Left/Right` | Previous/next day | Previous/next day | — | Move focus between dates |
| `Arrow Up/Down` | Previous/next week | Previous/next week | Previous/next item | Move week up/down |
| `Enter/Space` | Select day / open event | Select slot / open event | Open event | Confirm selection |
| `Tab` | Move to next interactive element | Move to next element | Move to next element | Move between start/end/presets |
| `Escape` | Close popover | Close popover | — | Close picker |
| `Home/End` | First/last day of month | Start/end of week | — | — |
| `Page Up/Down` | Previous/next month | Previous/next week | — | Previous/next month |

All interactive elements have visible focus indicators and appropriate `tabindex` values.

---

## Usage Examples

### Month calendar with events

```tsx
import { CalendarMonth } from '@sporekart/ui';

const events = [
  { id: '1', title: 'Team Standup', start: new Date(2026, 6, 14, 9, 0), allDay: false, color: 'var(--color-primary)' },
  { id: '2', title: 'Sprint Review', start: new Date(2026, 6, 17, 14, 0), allDay: false },
];

<CalendarMonth
  month={new Date(2026, 6, 1)}
  events={events}
  onDayClick={(d) => console.log(d)}
  onEventClick={(e) => navigate(`/events/${e.id}`)}
/>
```

### Week view with time slots

```tsx
import { CalendarWeek } from '@sporekart/ui';

<CalendarWeek
  date={new Date(2026, 6, 13)}
  events={weekEvents}
  startHour={6}
  endHour={20}
  onSlotClick={(d) => openNewEventModal(d)}
/>
```

### Agenda list

```tsx
import { CalendarAgenda } from '@sporekart/ui';

<CalendarAgenda
  events={upcomingEvents}
  maxItems={5}
  showViewAll
  onViewAll={() => navigate('/calendar')}
/>
```

### Date range with presets

```tsx
import { CalendarDateRange } from '@sporekart/ui';

const presets = [
  { label: 'Last 7 days', getRange: () => ({ start: subDays(new Date(), 7), end: new Date() }) },
  { label: 'Last 30 days', getRange: () => ({ start: subDays(new Date(), 30), end: new Date() }) },
  { label: 'This quarter', getRange: () => ({ start: startOfQuarter(new Date()), end: new Date() }) },
];

<CalendarDateRange
  startDate={reportStart}
  endDate={reportEnd}
  onChange={(range) => setReportRange(range)}
  presets={presets}
/>
```
