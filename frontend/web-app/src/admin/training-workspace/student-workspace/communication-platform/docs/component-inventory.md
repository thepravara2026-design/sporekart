# Communication Platform — Component Inventory

## Display Components (10)

| Component | Props | Description |
|---|---|---|
| StatusBadge | status (MessageStatus) | Color-coded status chip (10 variants) |
| PriorityBadge | priority (Priority) | Color-coded priority chip (5 variants) |
| TypeBadge | type (CommunicationType) | Color-coded type chip (18 variants) |
| MessageCard | message (CommunicationMessage) | Full notification card with badges + metadata |
| InboxItemCard | item, onToggleRead, onToggleStar | Inbox row with read/star actions |
| AnnouncementCard | announcement (Announcement) | Announcement card with category + status |
| TimelineEvent | event, isLast | Timeline entry with icon + vertical connector |
| PreferenceCard | preference, onToggle | Preference panel with toggle switches |
| TemplateCard | template (NotificationTemplate) | Template card with variables + body preview |
| MetricCard | label, value, color, icon | Single stat display card |

## Support Components (4)

| Component | Props | Description |
|---|---|---|
| DashboardWidget | title, subtitle, children, actions | Reusable widget wrapper |
| EmptyState | type (EmptyStateType), onClearFilters | 6 typed empty states |
| DashboardSkeleton | - | Dashboard skeleton loader |
| ListSkeleton | rows | List row skeleton loader |
| SharedFilters | currentPage | Search bar + type/priority/course/batch dropdowns |
