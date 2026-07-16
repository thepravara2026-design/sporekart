# Communication Platform — State Management

## Architecture

CommunicationProvider wraps the CommunicationIndex and provides global state via React Context.

## State Shape

```typescript
interface CommunicationState {
  messages: CommunicationMessage[];
  announcements: Announcement[];
  inbox: InboxItem[];
  timeline: TimelineEvent[];
  preferences: CommunicationPreference[];
  templates: NotificationTemplate[];
  analytics: CommunicationAnalytics;
  dashboard: EngagementDashboard;
  filters: {
    search: string;
    type: CommunicationType | 'all';
    priority: Priority | 'all';
    status: MessageStatus | 'all';
    course: string;
    batch: string;
    category: string;
  };
}
```

## Actions

| Method | Description |
|---|---|
| setSearch | Filters by title search |
| setTypeFilter | Filters by communication type |
| setPriorityFilter | Filters by priority level |
| setStatusFilter | Filters by message status |
| setCourseFilter | Filters by course ID |
| setBatchFilter | Filters by batch ID |
| getFilteredMessages | Returns filtered messages |
| getFilteredAnnouncements | Returns filtered announcements |
| getFilteredInbox | Returns filtered inbox items |
| toggleStar | Toggles star on inbox item |
| toggleRead | Toggles read status on inbox item |
| updatePreference | Updates a student's communication preference |

## Data Flow

1. On mount, 8 mock data generators populate state
2. Context provides both raw and filtered data accessors
3. Each page consumes only the slices it needs
4. Filters are shared across all tabs via SharedFilters
5. Inbox actions (read/star) update local state immutably
