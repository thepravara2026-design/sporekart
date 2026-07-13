# Alert System

## Overview

Alerts provide contextual feedback messages for user actions. Unlike toasts (which are ephemeral and float), alerts are embedded in the page layout and persist until dismissed or the condition resolves.

### Component Hierarchy

```
Alert
├── InlineAlert
├── PageAlert
├── DismissibleAlert
├── SuccessAlert
├── WarningAlert
├── InformationAlert
├── ErrorAlert
└── PersistentAlert
```

---

## Types

| Type | Icon | Color Token | Usage |
|------|------|-------------|-------|
| **Success** | Checkmark circle | `--color-success` | Positive confirmation |
| **Warning** | Triangle | `--color-warning` | Non-critical caution |
| **Info** | Info circle | `--color-info` | General information |
| **Error** | X circle | `--color-danger` | Error or failure message |

---

## Variants

| Variant | Position | Dismissible | Usage |
|---------|----------|-------------|-------|
| **Inline** | Within content flow | Configurable | Form validation, field-level feedback |
| **Page** | Top of page/section | Configurable | Page-level status messages |
| **Dismissible** | Any position | Yes (X button) | User can dismiss |
| **Persistent** | Any position | No | Critical messages requiring action |

### When to Use Each

| Scenario | Variant |
|----------|---------|
| Form validation error | InlineAlert, error type |
| Successful form submission | PageAlert, success type, dismissible |
| Feature deprecation notice | PageAlert, warning type, dismissible |
| System maintenance warning | PageAlert, info type, persistent |
| API rate limit reached | InlineAlert, warning type |
| Critical security issue | PageAlert, error type, persistent |

---

## Props

### Base Alert Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `type` | `'success' \| 'warning' \| 'info' \| 'error'` | `'info'` | Alert type/severity |
| `variant` | `'inline' \| 'page' \| 'dismissible' \| 'persistent'` | `'inline'` | Visual variant |
| `title` | `string` | — | Alert title |
| `children` | `ReactNode` | — | Alert body content |
| `icon` | `ReactNode` | — | Custom icon override |
| `action` | `{ label: string; onClick: () => void }` | — | Call-to-action button |
| `onDismiss` | `() => void` | — | Dismiss handler |
| `dismissLabel` | `string` | `'Dismiss'` | Close button aria-label |
| `role` | `'alert' \| 'status'` | `'alert'` | ARIA role |
| `animate` | `boolean` | `true` | Enable enter/exit animation |
| `compact` | `boolean` | `false` | Reduced padding variant |

### DismissibleAlert Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `onDismiss` | `() => void` | — | Required dismiss callback |
| `storageKey` | `string` | — | LocalStorage key to persist dismissed state |
| `autoDismissAfter` | `number` | — | Auto-dismiss after N ms |

### PageAlert Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `sticky` | `boolean` | `false` | Stick to top on scroll |
| `fullWidth` | `boolean` | `true` | Span full container width |
| `border` | `'left' \| 'top' \| 'full' \| 'none'` | `'left'` | Border accent style |

---

## Accessibility

| Feature | Implementation |
|---------|---------------|
| **ARIA Role** | `role="alert"` for error/alerts. `role="status"` for informational. |
| **Live Region** | `aria-live="assertive"` for error type. `aria-live="polite"` for info/success. |
| **Focus** | PageAlert with `role="alert"` may receive focus if critical. |
| **Dismiss Button** | `aria-label="Dismiss alert: {title}"` on close button. |
| **Color Independence** | Icon + text label ensures meaning without relying on color alone. |
| **Reduced Motion** | Respects `prefers-reduced-motion` for slide/fade animations. |

---

## Examples

### InlineAlert for form validation

```tsx
import { InlineAlert } from '@sporekart/ui';

function EmailField() {
  const [error, setError] = useState<string | null>(null);

  return (
    <Field>
      <Label>Email</Label>
      <Input type="email" onChange={validateEmail} />
      {error && (
        <InlineAlert type="error" title="Invalid email">
          Please enter a valid email address.
        </InlineAlert>
      )}
    </Field>
  );
}
```

### PageAlert for status messages

```tsx
import { PageAlert } from '@sporekart/ui';

function DashboardPage() {
  return (
    <>
      <PageAlert
        type="warning"
        title="Scheduled maintenance"
        variant="dismissible"
        onDismiss={dismissMaintenanceBanner}
        action={{ label: 'View details', onClick: openMaintenanceModal }}
      >
        The system will be unavailable on Sunday, March 15 from 2:00 AM to 6:00 AM EST.
      </PageAlert>
      <DashboardContent />
    </>
  );
}
```

### PersistentAlert for critical errors

```tsx
import { PersistentAlert } from '@sporekart/ui';

function PaymentPage() {
  const [paymentError, setPaymentError] = useState<string | null>(null);

  return (
    <>
      {paymentError && (
        <PersistentAlert type="error" title="Payment failed">
          Your payment could not be processed. Please try a different payment method
          or contact support.
        </PersistentAlert>
      )}
      <PaymentForm />
    </>
  );
}
```

### Compact inline alert

```tsx
<InlineAlert type="success" compact title="Changes saved">
  Your preferences have been updated.
</InlineAlert>
```

### DismissibleAlert with storage persistence

```tsx
<DismissibleAlert
  type="info"
  title="New feature available"
  storageKey="feature-announcement-v2"
  onDismiss={handleDismiss}
>
  You can now export reports in PDF format. Check the export menu.
</DismissibleAlert>
```

---

## Best Practices

- **Alert placement**: Inline alerts belong next to the relevant field. Page alerts belong at the top of the content area.
- **Actionable alerts**: Include an action button when the user needs to take action (e.g., "Retry", "View details").
- **Don't over-alert**: If an error auto-resolves, remove or dismiss the alert. Stale alerts erode user trust.
- **Single subject**: One alert = one message. Don't list multiple issues in a single alert.
- **Avoid alert stacks**: If multiple alerts are needed, use a Toast for transient issues and keep only the most critical Alert.
- **Compact mode**: Use `compact` for space-constrained areas like sidebars or tables.
- **Animation**: Dismissible alerts should animate out smoothly (200ms fade + slide up).
- **Persistent sparingly**: Only use PersistentAlert for conditions that truly block user progress.
