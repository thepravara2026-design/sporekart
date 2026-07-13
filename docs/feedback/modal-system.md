# Modal System

## Overview

The Modal system provides overlay containers for focused content interaction — forms, media viewing, wizards, and detailed information. Modals differ from Dialogs in that they are primarily content containers rather than decision prompts. All modals use Portal rendering and FocusTrap isolation.

### Component Hierarchy

```
Modal
├── StandardModal
├── LargeModal
├── FullscreenModal
├── ImageModal
├── VideoModal
├── ScrollableModal
├── ResponsiveModal
├── PersistentModal
└── WizardModal
```

---

## When to Use Each Variant

| Variant | Use Case |
|---------|----------|
| **StandardModal** | Default modal for most content (forms, details, settings). 480px max-width. |
| **LargeModal** | Content-heavy views (data tables, multi-column layouts). 720px max-width. |
| **FullscreenModal** | Immersive experiences (code editors, rich text, complex dashboards). |
| **ImageModal** | Lightbox-style image viewing with zoom, pan, and caption. |
| **VideoModal** | Embedded video player (YouTube, Vimeo, self-hosted). |
| **ScrollableModal** | Long content with header/footer pinned. Scrollable body. |
| **ResponsiveModal** | Adapts to viewport: centered modal on desktop, bottom sheet on mobile. |
| **PersistentModal** | Blocks interaction until explicitly dismissed. No overlay click close. |
| **WizardModal** | Multi-step forms with progress indicator, back/next navigation. |

---

## Props

### Base Modal Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `open` | `boolean` | `false` | Controls visibility |
| `onClose` | `() => void` | — | Close handler |
| `title` | `string` | — | Modal header title |
| `children` | `ReactNode` | — | Modal body content |
| `footer` | `ReactNode` | — | Modal footer content |
| `size` | `'sm' \| 'md' \| 'lg' \| 'xl' \| 'fullscreen'` | `'md'` | Modal sizing |
| `closeOnOverlay` | `boolean` | `true` | Close on backdrop click |
| `closeOnEscape` | `boolean` | `true` | Close on Escape key |
| `preventScroll` | `boolean` | `true` | Lock body scroll |
| `showCloseButton` | `boolean` | `true` | Show X close button |
| `zIndex` | `number` | token | Custom z-index |
| `portalTarget` | `HTMLElement` | `document.body` | Portal render target |
| `ariaLabel` | `string` | — | ARIA label override |
| `ariaDescribedBy` | `string` | — | ARIA description ID |
| `onOpen` | `() => void` | — | Callback after open animation |
| `onCloseComplete` | `() => void` | — | Callback after close animation |
| `initialFocusRef` | `RefObject<HTMLElement>` | — | Element to focus on open |

### StandardModal Specific

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `padding` | `'none' \| 'compact' \| 'normal' \| 'relaxed'` | `'normal'` | Content padding density |

### ImageModal Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `src` | `string` | — | Image source URL |
| `alt` | `string` | — | Alt text |
| `caption` | `string` | — | Optional caption below image |
| `zoomable` | `boolean` | `true` | Enable click-to-zoom |
| `downloadable` | `boolean` | `false` | Show download button |
| `aspectRatio` | `'auto' \| '16/9' \| '4/3' \| '1/1'` | `'auto'` | Image container ratio |

### VideoModal Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `src` | `string` | — | Video URL |
| `provider` | `'youtube' \| 'vimeo' \| 'self'` | — | Video provider |
| `autoplay` | `boolean` | `true` | Auto-play on open |
| `controls` | `boolean` | `true` | Show player controls |
| `poster` | `string` | — | Thumbnail image URL |

### PersistentModal Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `dismissLabel` | `string` | `'Dismiss'` | Required dismiss button label |
| `persistKey` | `string` | — | LocalStorage key to remember dismissal |

### WizardModal Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `steps` | `WizardStep[]` | — | Step definitions |
| `currentStep` | `number` | `0` | Active step index |
| `onStepChange` | `(step: number) => void` | — | Step navigation handler |
| `onComplete` | `() => void` | — | Final step completion |
| `onCancel` | `() => void` | — | Cancel/close handler |
| `allowSkip` | `boolean` | `false` | Allow skipping steps |
| `stepLabels` | `'default' \| 'numbered' \| 'icons'` | `'numbered'` | Step indicator style |

```typescript
interface WizardStep {
  id: string;
  title: string;
  description?: string;
  component: React.ComponentType<{ onNext: () => void; onBack: () => void }>;
  validation?: () => boolean | Promise<boolean>;
}
```

---

## Accessibility

| Feature | Implementation |
|---------|---------------|
| **Focus Trap** | Full Tab cycle isolation within modal content. Shift+Tab wraps in reverse. |
| **ARIA Role** | `role="dialog"` for all modals. |
| **ARIA Modal** | `aria-modal="true"` to indicate modal blocking. |
| **ARIA Label** | Title linked via `aria-labelledby`. |
| **ARIA Description** | Optional description linked via `aria-describedby`. |
| **Escape Key** | Closes modal (configurable). |
| **Overlay Click** | Closes modal (configurable; disabled for PersistentModal). |
| **Focus Return** | Focus restored to trigger element on close. |
| **Body Scroll** | `overflow: hidden` applied to prevent background scroll. |
| **Reduced Motion** | Respects `prefers-reduced-motion`. |

---

## Examples

### StandardModal with form

```tsx
import { StandardModal, useDialog } from '@sporekart/ui';

function CreateProjectModal() {
  const { open, openDialog, closeDialog } = useDialog();

  return (
    <>
      <Button onClick={openDialog}>New Project</Button>
      <StandardModal
        open={open}
        onClose={closeDialog}
        title="Create Project"
        footer={
          <>
            <Button variant="ghost" onClick={closeDialog}>Cancel</Button>
            <Button type="submit" form="project-form">Create</Button>
          </>
        }
      >
        <ProjectForm id="project-form" onSubmit={handleSubmit} />
      </StandardModal>
    </>
  );
}
```

### ImageModal for gallery

```tsx
import { ImageModal } from '@sporekart/ui';

function GalleryImage({ src, alt }: { src: string; alt: string }) {
  const { open, openDialog, closeDialog } = useDialog();

  return (
    <>
      <img src={src} alt={alt} onClick={openDialog} style={{ cursor: 'pointer' }} />
      <ImageModal
        open={open}
        onClose={closeDialog}
        src={src}
        alt={alt}
        caption="Photo by John Doe"
        zoomable
        downloadable
      />
    </>
  );
}
```

### WizardModal for multi-step setup

```tsx
import { WizardModal } from '@sporekart/ui';

const setupSteps: WizardStep[] = [
  { id: 'account', title: 'Account', component: AccountStep },
  { id: 'billing', title: 'Billing', component: BillingStep },
  { id: 'confirm', title: 'Confirmation', component: ConfirmStep },
];

function SetupWizard() {
  const { open, openDialog, closeDialog } = useDialog();

  return (
    <>
      <Button onClick={openDialog}>Start Setup</Button>
      <WizardModal
        open={open}
        onCancel={closeDialog}
        steps={setupSteps}
        onComplete={handleComplete}
        allowSkip={false}
      />
    </>
  );
}
```

### PersistentModal for mandatory notice

```tsx
import { PersistentModal } from '@sporekart/ui';

function ComplianceNotice() {
  return (
    <PersistentModal
      open={!localStorage.getItem('compliance-accepted')}
      onClose={() => {}}
      title="Data Processing Notice"
      description="..."
      dismissLabel="Acknowledge"
      persistKey="compliance-accepted"
    >
      <ComplianceContent />
    </PersistentModal>
  );
}
```

### ResponsiveModal

```tsx
import { ResponsiveModal } from '@sporekart/ui';

function MobileFilterModal() {
  // On viewport >= 768px: centered StandardModal
  // On viewport < 768px: bottom sheet with drag handle
  return (
    <ResponsiveModal open={open} onClose={closeDialog} title="Filters">
      <FilterForm onSubmit={applyFilters} />
    </ResponsiveModal>
  );
}
```

---

## Best Practices

- **Match modal size to content**: Use StandardModal for simple forms, LargeModal for data tables, FullscreenModal for complex editors.
- **Pinned footer**: Always place primary action buttons in the modal footer, not the body.
- **Avoid modal-on-modal**: If you need to show a dialog within a modal (e.g., "Unsaved changes"), use a Dialog component over the modal, or use NestedDialog.
- **Wizard progress**: Show step count (Step 2 of 5) and allow back-navigation. Validate each step before proceeding.
- **Media modals**: ImageModal and VideoModal should preload content and show proper loading states.
- **Close confirmation**: If a modal contains unsaved form data, warn before closing via `onClose` interception.
- **Persistent modals**: Use sparingly. They block all interaction — ensure the dismiss action is clearly labeled and accessible.
- **Animation**: Enter: 200ms ease-out, Exit: 150ms ease-in. Fullscreen modals slide up.
