# Accessibility — Feedback & Overlay System

## Overview

All feedback and overlay components are built with accessibility as a core requirement. This document covers focus management, ARIA attributes, keyboard navigation, screen reader support, motion preferences, and testing methodology.

---

## Focus Management

### FocusTrap (Core Utility)

All overlay components (Dialogs, Modals, Drawers) use the `FocusTrap` utility to isolate keyboard focus.

| Feature | Behavior |
|---------|----------|
| **First focus** | On open, focus moves to the first focusable element or a configured `initialFocusRef`. |
| **Tab cycling** | Tab cycles through focusable elements within the trap. Shift+Tab reverses. |
| **Focus wrap** | When Tab reaches the last element, focus wraps to the first. |
| **Escape** | Escape dismisses the overlay and returns focus to the trigger. |
| **Focus return** | On close, focus returns to the element that triggered the overlay. |
| **Nested traps** | A stack manages nested traps (e.g., dialog on top of modal). Only the topmost trap is active. |
| **Inert background** | Elements outside the trap receive `inert` attribute where supported, or `aria-hidden="true"` polyfill. |

```typescript
interface FocusTrapProps {
  active: boolean;
  initialFocusRef?: RefObject<HTMLElement>;
  finalFocusRef?: RefObject<HTMLElement>;
  returnFocusRef?: RefObject<HTMLElement>;
  children: ReactNode;
}
```

### Focus Restoration Map

| Component | Initial Focus | Return Focus |
|-----------|---------------|--------------|
| Dialog | First focusable element or confirm button | Trigger element |
| Modal | Close button or first form field | Trigger element |
| Drawer | Close button or first focusable | Trigger element |
| Popover | First menu item or close button | Trigger element |
| Toast | None (toast does not steal focus) | N/A |
| Alert | First focusable (if `role="alert"`) | N/A |

---

## ARIA Labels and Roles

### Component ARIA Mapping

| Component | Role | Attributes |
|-----------|------|-------------|
| Dialog | `dialog` or `alertdialog` | `aria-labelledby` (title), `aria-describedby` (description), `aria-modal="true"` |
| Modal | `dialog` | `aria-labelledby` (title), `aria-modal="true"` |
| Drawer | `dialog` | `aria-labelledby` (title), `aria-modal="true"` |
| Toast (success/info/warning) | `status` | `aria-live="polite"` |
| Toast (error) | `alert` | `aria-live="assertive"` |
| NotificationCenter | `dialog` | `aria-label="Notifications"` |
| Alert (error/critical) | `alert` | `aria-live="assertive"` |
| Alert (info/success) | `status` | `aria-live="polite"` |
| Banner | `banner` | `aria-label` describing purpose |
| Banner (warning/offline) | `alert` | `aria-live="assertive"` |
| Tooltip | `tooltip` | `aria-describedby` on trigger, `role="tooltip"` on tooltip |
| Popover | `dialog` or `menu` | `aria-labelledby` (trigger id) |
| Progress (determinate) | `progressbar` | `aria-valuenow`, `aria-valuemin="0"`, `aria-valuemax="100"` |
| Progress (indeterminate) | `progressbar` | `aria-label` only (no value attributes) |
| StatusIndicator | `img` (if icon) or `status` | `aria-label` describing the status |

### Dynamic Content Announcements

| Component | Technique |
|-----------|-----------|
| Toast appearance | Toast container is `aria-live="polite"`. New toasts are announced. |
| Toast error | `aria-live="assertive"` for error toasts. |
| Loading completion | `aria-live="polite"` region announces "Loading complete" or error. |
| Notification count | Badge announces "{count} unread notifications" via `aria-label`. |
| Dialog open | Focus moves into dialog. `aria-modal="true"` signals screen reader. |
| Page transition | `aria-live="polite"` on page container announces new page title. |

---

## Keyboard Navigation

### Global Keyboard Shortcuts

| Key | Component | Action |
|-----|-----------|--------|
| **Tab** | All overlay components | Move focus forward within trap |
| **Shift+Tab** | All overlay components | Move focus backward within trap |
| **Escape** | Dialog, Modal, Drawer, Popover, Tooltip | Close/dismiss |
| **Escape** | Toast (focused) | Dismiss |
| **Enter/Space** | Popover menu | Select focused item |
| **Arrow Up/Down** | Popover menu, Notification list | Navigate items |
| **Arrow Left/Right** | Nested popover | Open/close submenu |
| **Arrow Left/Right** | Step progress | Navigate steps (when focused) |

### Component-Specific Keyboard Support

| Component | Keys | Action |
|-----------|------|--------|
| Dialog | Escape, Tab, Shift+Tab, Enter (on confirm) | Close, navigate, confirm |
| Modal | Escape, Tab, Shift+Tab | Close, navigate |
| Drawer | Escape, Tab, Shift+Tab | Close, navigate |
| Popover (action) | Escape, Tab, Enter, Arrow Up/Down | Close, navigate, select |
| Popover (nested) | Escape, Arrow Left/Right, Enter | Close, navigate menus |
| Tooltip | Escape | Dismiss |
| NotificationCenter | Escape, Tab, Arrow Up/Down | Close, navigate items |
| StepProgress | Tab (to enter), Arrow Left/Right | Navigate steps |

---

## Screen Reader Announcements

### Toast Announcements

```typescript
// Toast container renders:
<div
  aria-live="polite"
  aria-relevant="additions"
  aria-atomic="false"
>
  {/* Toasts rendered here */}
</div>
```

### Loading Announcements

```typescript
// Section loader renders:
<div
  role="status"
  aria-live="polite"
  aria-label={loadingLabel}
>
  <Spinner />
  <span className="sr-only">{loadingLabel}</span>
</div>
```

### Progress Announcements

- Determinate progress announces value changes at 10% intervals (debounced).
- Upload progress announces file name and percentage.
- Task progress announces "Task {completed} of {total} completed" on completion.

### Screen Reader Only Text

```css
.sr-only {
  position: absolute;
  width: 1px;
  height: 1px;
  padding: 0;
  margin: -1px;
  overflow: hidden;
  clip: rect(0, 0, 0, 0);
  white-space: nowrap;
  border-width: 0;
}
```

All loading indicators, status dots, and icon-only buttons include `.sr-only` text for screen readers.

---

## Reduced Motion

### Implementation

```css
/* All animations check prefers-reduced-motion */
.animate-in {
  animation: slideIn 200ms ease-out;
}

@media (prefers-reduced-motion: reduce) {
  .animate-in,
  .animate-out {
    animation: none;
    opacity: 1;
    transform: none;
    transition: none;
  }

  /* Loading spinners: static display */
  .spinner {
    animation: none;
    border-color: currentColor;
  }

  /* Pulse indicators: static */
  .pulse {
    animation: none;
  }

  /* Shimmer: static gray placeholder */
  .shimmer {
    background: var(--color-surface-alt);
    animation: none;
  }
}
```

### Animations Affected

| Component | Default Animation | Reduced Motion Fallback |
|-----------|------------------|------------------------|
| Dialog / Modal | Fade + scale (200ms) | Instant opacity toggle |
| Drawer | Slide from edge (300ms) | Instant opacity toggle |
| Toast | Slide in + fade (300ms) | Fade only (no slide) |
| Alert | Slide down + fade (200ms) | Fade only |
| Banner | Slide down (300ms) | Instant show/hide |
| Tooltip | Fade (150ms) | Instant show/hide |
| Popover | Fade + scale (150ms) | Instant opacity toggle |
| Spinner | Rotate (1s) | Static icon |
| Shimmer | Gradient sweep (1.5s) | Static gray background |
| Progress bar | Width transition (300ms) | Instant width change |
| Pulse indicator | Opacity pulse (2s) | Static |

---

## High Contrast

### Windows High Contrast Mode (WHCM)

```css
/* Ensure visibility in forced-colors mode */
@media (forced-colors: active) {
  .status-dot {
    border: 2px solid ButtonText;
  }

  .toast-error {
    border: 2px solid ButtonText;
  }

  .spinner {
    border: 3px solid ButtonText;
    border-top-color: Highlight;
  }

  .shimmer {
    background: Canvas;
    border: 1px solid ButtonText;
  }
}
```

### Color Independence

- No information is conveyed by color alone. Every status/severity has:
  - Icon or symbol
  - Text label
  - Distinct shape or border style
- Focus indicators use `outline` (not `box-shadow` or color-only).
- Minimum contrast ratio: 4.5:1 for text, 3:1 for UI components.

---

## Testing Methodology

### Automated Testing

```typescript
// Example test patterns for each component

// 1. Focus trap
test('traps focus within dialog on Tab', async () => {
  render(<TestDialog open />);
  const first = screen.getByTestId('first-focusable');
  const last = screen.getByTestId('last-focusable');
  await userEvent.tab();
  expect(first).toHaveFocus();
  await userEvent.tab();
  expect(last).toHaveFocus();
  await userEvent.tab();
  expect(first).toHaveFocus(); // wraps
});

// 2. Escape key dismisses
test('closes dialog on Escape', async () => {
  const onClose = vi.fn();
  render(<TestDialog open onClose={onClose} />);
  await userEvent.keyboard('{Escape}');
  expect(onClose).toHaveBeenCalled();
});

// 3. ARIA attributes
test('has correct ARIA roles and labels', () => {
  render(<TestDialog open title="Confirm" description="Are you sure?" />);
  expect(screen.getByRole('dialog')).toHaveAttribute('aria-modal', 'true');
  expect(screen.getByRole('dialog')).toHaveAttribute('aria-labelledby');
});

// 4. Focus return
test('returns focus to trigger on close', async () => {
  const { user } = render(<TestWithTrigger />);
  const trigger = screen.getByTestId('trigger');
  await user.click(trigger);
  await user.keyboard('{Escape}');
  expect(trigger).toHaveFocus();
});

// 5. Reduced motion
test('respects prefers-reduced-motion', () => {
  const { container } = render(
    <TestDialog open />,
    { wrapper: withReducedMotion }
  );
  expect(container.querySelector('.animate-in')).toHaveStyle({ animation: 'none' });
});
```

### Manual Testing Checklist

| Test | Component | Criteria |
|------|-----------|----------|
| Keyboard navigation | All overlays | Tab through all focusable elements without getting stuck |
| Escape dismiss | Dialog, Modal, Popover | Pressing Escape closes the overlay |
| Focus trap | Dialog, Modal | Focus does not leave the overlay |
| Screen reader | Toast | New toast is announced without interrupting other content |
| Screen reader | Dialog | Dialog title and description are announced on open |
| Reduced motion | All | Toggle `prefers-reduced-motion: reduce` — no animations play |
| High contrast | All | Enable Windows High Contrast — all elements are visible |
| Zoom 200% | All | Layout does not break at 200% zoom |
| Touch | Drawer, Popover | Touch inputs work correctly |
| VoiceOver / NVDA | All | Full screen reader flow works |

### Tools

| Tool | Purpose |
|------|---------|
| axe-core | Automated accessibility audit in CI |
| Lighthouse | Accessibility score and audit |
| Storybook a11y addon | Component-level accessibility checks |
| NVDA / VoiceOver | Screen reader testing |
| Colour Contrast Analyser | Color contrast ratio verification |
| prefers-reduced-motion emulation | Chrome DevTools Rendering tab |
| Windows High Contrast emulation | BrowserStack or physical device |

---

## Accessibility Checklist (per component)

- [ ] `role` attribute is correct for the component type
- [ ] `aria-labelledby` or `aria-label` is provided
- [ ] `aria-describedby` is provided where applicable
- [ ] `aria-modal="true"` for blocking overlays
- [ ] `aria-live` region is configured for dynamic content
- [ ] Focus trap is active and cycles correctly
- [ ] Escape key dismisses the overlay
- [ ] Focus returns to trigger on close
- [ ] All icons have text alternatives (`aria-label` or `.sr-only`)
- [ ] Color is not the sole means of conveying information
- [ ] Animations respect `prefers-reduced-motion`
- [ ] High contrast mode (forced-colors) is supported
- [ ] Touch targets are at least 44x44px
- [ ] Keyboard operates all interactive elements
- [ ] Screen reader correctly announces state changes
