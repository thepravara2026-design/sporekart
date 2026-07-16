# Communication Platform — Template Architecture

## Overview

Notification Templates provide reusable message structures for all 18 communication types. Each template defines subject, body, and variable placeholders that can be populated at send time.

## Template Model

```typescript
NotificationTemplate {
  id, templateId, name, type (CommunicationType),
  subject, body, variables (string[]),
  isActive, createdDate, lastUpdated
}
```

## Variables

Templates support `{{variableName}}` placeholders:
- `{{studentName}}` — Recipient student name
- `{{courseName}}` — Associated course name
- `{{messageBody}}` — Dynamic message content
- `{{sender}}` — Sender display name

## Features

- Type filter to browse templates by communication type
- Active/inactive status toggle
- Variable chip display
- Body preview (truncated)
- 18 templates, one per communication type
