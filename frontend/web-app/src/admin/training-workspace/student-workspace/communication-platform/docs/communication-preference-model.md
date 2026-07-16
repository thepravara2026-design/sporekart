# Communication Platform — Communication Preference Model

## Overview

The Communication Preference Model manages per-student channel and notification type preferences for future omnichannel delivery.

## Preference Model

```typescript
CommunicationPreference {
  id, studentId, studentName,
  email: boolean,
  sms: boolean,
  whatsapp: boolean,
  push: boolean,
  inApp: boolean,
  announcements: boolean,
  reminders: boolean,
  marketing: boolean,
  academic: boolean,
  quietHoursStart: string | null,
  quietHoursEnd: string | null,
  createdDate, lastUpdated
}
```

## Channels

| Channel | Description | Future Provider |
|---|---|---|
| email | Email notifications | SendGrid, SES, SMTP |
| sms | SMS notifications | Twilio, MSG91 |
| whatsapp | WhatsApp notifications | WhatsApp Business API |
| push | Push notifications | Firebase, OneSignal |
| in-app | In-app notifications | Always active |

## Notification Types

| Type | Description |
|---|---|
| announcements | Organization/course/batch announcements |
| reminders | Assignment/assessment/learning reminders |
| marketing | Promotional/marketing communications |
| academic | Academic notifications |

## Quiet Hours

Optional start/end time (e.g., 22:00–07:00) during which non-critical notifications are suppressed.
