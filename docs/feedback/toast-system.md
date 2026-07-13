# Toast System

## Overview

The Toast system provides lightweight, non-blocking notifications for ephemeral feedback. Toasts appear temporarily, auto-dismiss, and support action buttons. A queue manages overflow to ensure no more than 5 visible toasts at a time.

### Component Hierarchy

```
Toast
├── SuccessToast
├── ErrorToast
├── WarningToast
├── InformationToast
├── LoadingToast
├── ToastContainer
├── ToastQueue
└── useToastQueue
```

---

## Types

| Type | Icon | Color | Usage |
|------|------|-------|-------|
| **Success** | Checkmark | `--color-success` (green) | Operation completed successfully |
| **Error** | X-circle | `--color-danger` (red) | Operation failed |
| **Warning** | Triangle | `--color-warning` (amber) | Non-blocking warning |
| **Info** | Info-circle | `--color-info` (blue) | General information |
| **Loading** | Spinner | `--color-primary` | Operation in progress |

---

## Toast Queue and Container

The `ToastContainer` renders at a fixed position and manages the visible toast stack. The `ToastQueue` handles:

- **Max visible**: 5 toasts at any time
- **Overflow**: Older toasts are pushed to a virtualized queue
- **Deduplication**: Duplicate messages within 3s are suppressed
- **Priority**: Error toasts display above others

```typescript
interface ToastAction {
  label: string;
  onClick: () => void;
  variant?: 'primary' | 'secondary' | 'danger';
}

interface ToastOptions {
  id?: string;
  title: string;
  description?: string;
  type: 'success' | 'error' | 'warning' | 'info' | 'loading';
  duration?: number; // ms, 0 = persistent
  position?: ToastPosition;
  action?: ToastAction;
  onClose?: () => void;
  onDismiss?: () => void;
}

type ToastPosition =
  | 'top-right'
  | 'top-left'
  | 'bottom-right'
  | 'bottom-left'
  | 'top-center'
  | 'bottom-center';

interface UseToastQueueReturn {
  toast: (options: ToastOptions) => string;  // returns toast ID
  success: (title: string, options?: Partial<ToastOptions>) => string;
  error: (title: string, options?: Partial<ToastOptions>) => string;
  warning: (title: string, options?: Partial<ToastOptions>) => string;
  info: (title: string, options?: Partial<ToastOptions>) => string;
  loading: (title: string, options?: Partial<ToastOptions>) => string;
  dismiss: (id: string) => void;
  dismissAll: () => void;
  update: (id: string, options: Partial<ToastOptions>) => void;
  toasts: ToastItem[];
}
```

---

## Auto-Dismiss and Manual Close

| Behavior | Default | Configurable |
|----------|---------|-------------|
| Auto-dismiss duration | 5000ms (info/warning/success), 8000ms (error) | `duration` option |
| Persistent toast | — | `duration: 0` |
| Manual close | Close button on hover | Always available |
| Loading auto-dismiss | Never | Must call `dismiss()` explicitly |
| Pause on hover | Hover pauses timer | Always enabled |

---

## Position Options

```
top-left       top-center       top-right
   ┌─┐            ┌─┐             ┌─┐
   │ │            │ │             │ │
   └─┘            └─┘             └─┘


   ┌─┐            ┌─┐             ┌─┐
   │ │            │ │             │ │
   └─┘            └─┘             └─┘
bottom-left    bottom-center    bottom-right
```

Default position: `top-right`. Use `bottom-center` for mobile-optimized layouts.

---

## Accessibility

| Feature | Implementation |
|---------|---------------|
| **Live Region** | `aria-live="polite"` for info/success/warning. `aria-live="assertive"` for error. |
| **Role** | `role="status"` for non-critical, `role="alert"` for errors. |
| **Focus** | Toasts do not steal focus. Close button focusable via Tab. |
| **Announcement** | Screen reader announces title + description on appearance. |
| **Dismiss** | Close button has `aria-label="Close notification"`. |
| **Motion** | Respects `prefers-reduced-motion`. Slide animation replaced with fade. |

---

## Examples

### Basic usage with convenience methods

```tsx
import { useToastQueue, ToastContainer } from '@sporekart/ui';

function App() {
  const toast = useToastQueue();

  const handleSave = async () => {
    try {
      await saveData();
      toast.success('Changes saved successfully');
    } catch (err) {
      toast.error('Failed to save changes', {
        description: err.message,
        action: { label: 'Retry', onClick: handleSave },
      });
    }
  };

  return (
    <>
      <Button onClick={handleSave}>Save</Button>
      <ToastContainer position="top-right" />
    </>
  );
}
```

### Loading toast with update

```tsx
function FileUpload() {
  const toast = useToastQueue();

  const handleUpload = async (file: File) => {
    const id = toast.loading(`Uploading ${file.name}...`);

    try {
      await uploadFile(file);
      toast.update(id, {
        type: 'success',
        title: `${file.name} uploaded`,
        duration: 5000,
      });
    } catch (err) {
      toast.update(id, {
        type: 'error',
        title: `Upload failed`,
        description: err.message,
        duration: 0,
      });
    }
  };

  return <Button onClick={() => handleUpload(file)}>Upload</Button>;
}
```

### Action button toast

```tsx
function EmailSentNotification() {
  const toast = useToastQueue();

  const handleSend = () => {
    toast.success('Email sent', {
      description: 'Your invoice has been sent to john@example.com',
      action: {
        label: 'View',
        onClick: () => navigate('/sent-emails/123'),
      },
      duration: 8000,
    });
  };

  return <Button onClick={handleSend}>Send Invoice</Button>;
}
```

### Custom position

```tsx
function AppLayout() {
  return (
    <>
      <Navbar />
      <MainContent />
      <ToastContainer position="bottom-left" />
    </>
  );
}
```

### Error with persistent toast

```tsx
function CriticalErrorHandler() {
  const toast = useToastQueue();

  useEffect(() => {
    if (criticalError) {
      toast.error('Connection lost', {
        description: 'Reconnecting...',
        duration: 0, // persistent
        action: { label: 'Retry Now', onClick: reconnect },
      });
    }
  }, [criticalError]);

  return null;
}
```

---

## Best Practices

- **One action per toast**: If you need multiple actions, use a Notification instead.
- **Keep it short**: Titles: 2-5 words. Descriptions: 10-30 words max.
- **Loading toasts**: Always transition loading toasts to success/error via `update()`.
- **Don't overuse**: Reserve toasts for ephemeral feedback. Use NotificationCenter for persistent notifications.
- **Error specificity**: Include actionable information in error toasts. "Failed to save" is better than "Error 500".
- **Rate limiting**: Rapid-fire toasts (e.g., batch operations) should use a single toast with progress, not individual toasts.
- **Mobile consideration**: On mobile, use bottom-center position so toasts don't block the top navigation bar.
- **Auto-dismiss timing**: Adjust duration based on content length — longer content needs more time to read.
