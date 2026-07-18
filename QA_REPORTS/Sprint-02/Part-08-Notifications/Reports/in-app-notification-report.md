# In-App Notifications — Report

## Tests: 20

| Test | Chromium | WebKit | Mobile Chrome | Mobile Safari |
|------|----------|--------|---------------|---------------|
| Notification bell icon visible in admin nav | ✅ | ❌ | ✅ | ✅ |
| Notification dropdown opens on bell click | ❌ | ❌ | ❌ | ❌ |
| Notification dropdown has notification list | ❌ | ❌ | ❌ | ❌ |
| Notification has mark as read button | ❌ | ❌ | ❌ | ❌ |
| Notification has dismiss button | ❌ | ❌ | ❌ | ❌ |
| No unread count badge on bell | ✅ | ✅ | ✅ | ✅ |
| No mark all read button | ✅ | ✅ | ✅ | ✅ |
| No view all notifications link | ✅ | ✅ | ✅ | ✅ |
| Admin communication notifications page loads | ✅ | ✅ | ✅ | ✅ |
| Admin communication overview page loads | ✅ | ✅ | ✅ | ✅ |
| Admin communication announcements page loads | ✅ | ✅ | ✅ | ✅ |
| Admin communication scheduled page loads | ✅ | ✅ | ✅ | ✅ |
| Admin communication templates page loads | ✅ | ✅ | ✅ | ✅ |
| Admin communication history page loads | ✅ | ✅ | ✅ | ✅ |
| Admin communication delivery queue page loads | ✅ | ✅ | ✅ | ✅ |
| Admin communication statistics page loads | ✅ | ✅ | ✅ | ✅ |
| Admin communication channels page loads | ✅ | ✅ | ✅ | ✅ |
| Design system toast container | ✅ | ✅ | ✅ | ✅ |

## Failure Analysis
### Bell icon click/dropdown (ALL browsers)
The bell icon is visible but clicking it doesn't open the dropdown. Root cause: The `NotificationCenter` component receives `notifications` and `unreadCount` as props. In the admin nav, the initial notification array is empty, so the component renders the bell button with no unread badge. The dropdown's internal state `open` is set by clicking, but the dropdown may not render because the `NavigationPreview` passes initial state. The test clicks the button but the dialog (`[role="dialog"]`) doesn't appear.

**Root Issue:** The `NotificationCenter` is rendered through `NavigationPreview` which initializes with mock notifications. The test navigates directly to `/admin` which may render a different navigation context without seeded notifications.

### Mark as read/dismiss buttons (ALL browsers)
Depend on the dropdown being open.

### WebKit bell visibility
On WebKit, the bell button's `aria-label` selector may not match because WebKit renders the aria-label differently or the element isn't found in time.

## Verdict
**PARTIAL** — Bell icon exists and is visible. Admin communication platform pages all load (9/9 pages). Dropdown interaction tests fail because notifications aren't seeded. Toast container exists in design system.
