# Communication Platform — Inbox Architecture

## Overview

The Student Inbox provides a unified message center for all student communications. Supports tab-based filtering (All, Unread, Starred), per-message read/star actions, and shared search/filter from CommunicationContext.

## Features

- Tab navigation: All / Unread / Starred with live counts
- Per-item read toggle (mark as read/unread)
- Per-item star toggle
- Priority and type badges on each message
- Color-coded unread indicator (blue border)
- Sender, course, and date display

## Data Model

```typescript
InboxItem {
  id, messageId, studentId, studentName, studentPhoto,
  type (CommunicationType), title, subtitle, body,
  priority (Priority), isRead, isStarred, isImportant, isPinned,
  receivedDate, readDate, category, sender, courseId, courseName
}
```

## Future Enhancements

- Pinned messages
- Conversations / threading
- Attachments
- Search within inbox
- Bulk actions (select all, mark all read)
- Pagination for large inboxes
