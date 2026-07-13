# Dialog System

## Overview

The Dialog system provides a family of modal overlay components for user confirmations, alerts, information, and status communications. All dialogs are rendered via Portal, use FocusTrap for focus isolation, and support queue management for sequential display.

### Component Hierarchy

```
Dialog
├── ConfirmationDialog
├── AlertDialog
├── InformationDialog
├── SuccessDialog
├── WarningDialog
├── ErrorDialog
├── LoadingDialog
├── FullscreenDialog
├── ResponsiveDialog
├── NestedDialog
├── DialogQueue
└── useDialog
```

---

## Usage Guidelines

| Variant | Use Case |
|---------|----------|
| **ConfirmationDialog** | Destructive or irreversible actions. Requires explicit confirmation/cancel. |
| **AlertDialog** | Critical system messages requiring user acknowledgment. Single action (OK). |
| **InformationDialog** | Informational content that does not require immediate action. |
| **SuccessDialog** | Positive outcome feedback after an operation completes. |
| **WarningDialog** | Non-blocking warnings about potential issues. |
| **ErrorDialog** | Operation failures or system errors requiring user attention. |
| **LoadingDialog** | Blocking operations with indeterminate or determinate progress. |
| **FullscreenDialog** | Complex forms, media viewing, or content requiring maximum space. |
| **ResponsiveDialog** | Adapts between modal (desktop) and bottom-sheet (mobile) presentation. |
| **NestedDialog** | Sequential confirmations (e.g., "Are you sure?" then "Confirm delete?"). |

---

## Props

### Base Dialog Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `open` | `boolean` | `false` | Controls visibility |
| `onClose` | `() => void` | — | Close handler |
| `title` | `string` | — | Dialog title |
| `description` | `string` | — | Descriptive text |
| `children` | `ReactNode` | — | Custom content |
| `size` | `'sm' \| 'md' \| 'lg' \| 'xl' \| 'fullscreen'` | `'md'` | Dialog size |
| `closeOnOverlay` | `boolean` | `true` | Close on backdrop click |
| `closeOnEscape` | `boolean` | `true` | Close on Escape key |
| `preventScroll` | `boolean` | `true` | Lock body scroll |
| `zIndex` | `number` | token | Custom z-index |
| `portalTarget` | `HTMLElement` | `document.body` | Portal render target |
| `ariaLabel` | `string` | — | ARIA label override |
| `ariaDescribedBy` | `string` | — | ARIA description ID |
| `onOpenFocus` | `() => HTMLElement` | first focusable | Focus target on open |
| `onCloseFocus` | `() => HTMLElement` | trigger element | Focus return target |

### ConfirmationDialog Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `confirmLabel` | `string` | `'Confirm'` | Confirm button text |
| `cancelLabel` | `string` | `'Cancel'` | Cancel button text |
| `onConfirm` | `() => void \| Promise<void>` | — | Confirm action handler |
| `onCancel` | `() => void` | — | Cancel action handler |
| `confirmDisabled` | `boolean` | `false` | Disable confirm button |
| `confirmLoading` | `boolean` | `false` | Show loading on confirm |
| `variant` | `'primary' \| 'danger' \| 'warning'` | `'primary'` | Button color variant |
| `destructive` | `boolean` | `false` | Style as destructive action |

### AlertDialog Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `label` | `string` | `'OK'` | Acknowledge button text |
| `onAcknowledge` | `() => void` | — | Acknowledge handler |

### SuccessDialog / WarningDialog / ErrorDialog / InformationDialog Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `icon` | `ReactNode` | variant default | Custom icon override |
| `action` | `{ label: string; handler: () => void }` | — | Optional action button |
| `secondaryAction` | `{ label: string; handler: () => void }` | — | Secondary action |

### FullscreenDialog Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `showCloseButton` | `boolean` | `true` | Show close button |
| `closeButtonLabel` | `string` | `'Close'` | Close button aria-label |

### LoadingDialog Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `progress` | `number` | — | Determinate progress (0-100). Omit for indeterminate. |
| `label` | `string` | `'Loading...'` | Status message |
| `showCancel` | `boolean` | `false` | Show cancel button |
| `onCancel` | `() => void` | — | Cancel handler |

### useDialog Hook

```typescript
interface UseDialogOptions {
  defaultOpen?: boolean;
  closeOnOverlay?: boolean;
  closeOnEscape?: boolean;
  onClose?: () => void;
  onOpenFocus?: () => HTMLElement;
  onCloseFocus?: () => HTMLElement;
}

const {
  open,
  setOpen,
  openDialog,
  closeDialog,
  toggleDialog,
  isClosing,
} = useDialog(options?: UseDialogOptions);
```

### DialogQueue

```typescript
interface DialogQueueItem {
  id: string;
  component: React.ComponentType<DialogProps>;
  props: DialogProps;
  priority: 'high' | 'normal' | 'low';
}

// Queue processes items sequentially, showing one dialog at a time.
// High priority items jump to front of queue.
const { enqueue, dequeue, clear, current, remaining } = useDialogQueue();
```

---

## Accessibility

| Feature | Implementation |
|---------|---------------|
| **Focus Trap** | FocusTrap isolates Tab cycling within the dialog. Shift+Tab wraps in reverse. |
| **Focus Return** | On close, focus returns to the element that triggered the dialog. |
| **ARIA Role** | `role="dialog"` for standard dialogs, `role="alertdialog"` for AlertDialog. |
| **ARIA Label** | `aria-labelledby` links to the title element. |
| **ARIA Description** | `aria-describedby` links to the description element. |
| **Escape Key** | Closes the dialog (configurable via `closeOnEscape`). |
| **Overlay Click** | Closes the dialog (configurable via `closeOnOverlay`). |
| **Body Scroll Lock** | `overflow: hidden` applied to `document.body` while open. |
| **Reduced Motion** | Respects `prefers-reduced-motion` for enter/exit animations. |

---

## Examples

### ConfirmationDialog for destructive action

```tsx
import { ConfirmationDialog, useDialog } from '@sporekart/ui';

function DeleteUserButton({ userId }: { userId: string }) {
  const { open, openDialog, closeDialog } = useDialog();

  const handleConfirm = async () => {
    await deleteUser(userId);
    closeDialog();
  };

  return (
    <>
      <Button onClick={openDialog} variant="danger">Delete User</Button>
      <ConfirmationDialog
        open={open}
        onClose={closeDialog}
        title="Delete user account?"
        description="This action cannot be undone. All associated data will be permanently removed."
        confirmLabel="Delete Account"
        cancelLabel="Cancel"
        onConfirm={handleConfirm}
        onCancel={closeDialog}
        destructive
        confirmLoading
      />
    </>
  );
}
```

### AlertDialog for system messages

```tsx
import { AlertDialog, useDialog } from '@sporekart/ui';

function SessionExpiredBanner() {
  const { open, openDialog, closeDialog } = useDialog({ defaultOpen: true });

  return (
    <AlertDialog
      open={open}
      onClose={closeDialog}
      title="Session expired"
      description="Your session has expired. Please sign in again to continue."
      label="Sign In"
      onAcknowledge={() => navigate('/login')}
    />
  );
}
```

### SuccessDialog after completion

```tsx
import { SuccessDialog, useDialog } from '@sporekart/ui';

function UploadResult({ success }: { success: boolean }) {
  const { open, openDialog, closeDialog } = useDialog({ defaultOpen: !!success });

  return (
    <SuccessDialog
      open={open}
      onClose={closeDialog}
      title="Upload complete"
      description="Your file has been uploaded and processed successfully."
      action={{ label: 'View File', handler: () => navigate('/files/123') }}
    />
  );
}
```

### ResponsiveDialog for mobile support

```tsx
import { ResponsiveDialog } from '@sporekart/ui';

function FilterPanel() {
  const { open, openDialog, closeDialog } = useDialog();

  return (
    <>
      <Button onClick={openDialog}>Filters</Button>
      <ResponsiveDialog
        open={open}
        onClose={closeDialog}
        title="Apply Filters"
        // On mobile (< 768px): renders as bottom sheet
        // On desktop: renders as centered modal
      >
        <FilterForm onSubmit={closeDialog} />
      </ResponsiveDialog>
    </>
  );
}
```

### DialogQueue for sequential dialogs

```tsx
import { useDialogQueue, ConfirmationDialog, AlertDialog } from '@sporekart/ui';

function MultiStepDelete() {
  const queue = useDialogQueue();

  const handleDelete = () => {
    queue.enqueue({
      id: 'confirm-1',
      component: ConfirmationDialog,
      props: {
        title: 'Delete item?',
        description: 'This will move the item to trash.',
        onConfirm: () => {
          queue.enqueue({
            id: 'confirm-2',
            component: ConfirmationDialog,
            props: {
              title: 'Permanently delete?',
              description: 'Items in trash will be deleted after 30 days. Delete now?',
              destructive: true,
              onConfirm: () => performDelete(),
            },
          });
        },
      },
    });
  };

  return <Button onClick={handleDelete}>Delete</Button>;
}
```

---

## Best Practices

- **One dialog at a time**: Use DialogQueue when multiple confirmations are needed sequentially. Never stack dialogs manually.
- **Clear titles**: Keep titles concise (2-6 words). They should answer "What is this about?"
- **Descriptive body**: Explain the consequence and what the user should expect.
- **Actionable buttons**: Use verb-based labels ("Delete", "Save", "Discard") instead of generic "OK/Cancel".
- **Destructive confirmation**: Require the user to type a confirmation phrase for truly irreversible actions.
- **Loading state**: Use `confirmLoading` on ConfirmationDialog for async operations. Avoid blocking with LoadingDialog for < 3s operations.
- **Avoid nested dialogs**: If needed, use NestedDialog which manages focus trap stacking and z-index layering.
- **Mobile-first**: Use ResponsiveDialog for mobile-sensitive layouts. FullscreenDialog for complex content.
- **Escape hatch**: Always provide at least one way to dismiss (Escape key, Close button, or Cancel).
- **Animation duration**: Enter: 200ms, Exit: 150ms. Respect reduced motion preferences.
