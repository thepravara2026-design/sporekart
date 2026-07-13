# Developer Guide — Using the Design System

## Import Paths

All public components, hooks, and utilities are exported from the design system barrel:

```typescript
// Components
import { Button, Input, Card, Modal } from '@sporekart/ui';

// Hooks
import { useTheme, useBreakpoint } from '@sporekart/ui';

// Providers
import { ThemeProvider, ToastProvider } from '@sporekart/ui';
```

Internal modules (tokens, playground, utils) are **not** part of the public API and should not be imported directly. If you need an internal export, submit a Design Change Request.

## Provider Setup

Wrap your application root with the required providers in the correct order:

```tsx
import { ErrorBoundary, ThemeProvider, AccessibilityProvider, ToastProvider, DialogProvider } from '@sporekart/ui';

function AppRoot({ children }) {
  return (
    <ErrorBoundary>
      <ThemeProvider defaultTheme="light">
        <AccessibilityProvider>
          <ToastProvider>
            <DialogProvider>
              {children}
            </DialogProvider>
          </ToastProvider>
        </AccessibilityProvider>
      </ThemeProvider>
    </ErrorBoundary>
  );
}
```

| Provider | Required | Purpose |
|----------|----------|---------|
| `ErrorBoundary` | Yes | Catches unhandled render errors |
| `ThemeProvider` | Yes | Provides theme context and CSS variables |
| `AccessibilityProvider` | Yes | Manages focus trapping, announcement, and reduced motion |
| `LocalizationProvider` | No | I18n support |
| `FeatureFlagProvider` | No | Feature flag context |
| `PerformanceProvider` | No | Performance monitoring |
| `ToastProvider` | Recommended | Toast notification stack |
| `DialogProvider` | Recommended | Modal/dialog management |
| `NotificationProvider` | No | Notification center context |

## Theme Switching

Themes are controlled via the `data-theme` attribute on `<html>` and the `ThemeProvider` context.

```typescript
import { useTheme } from '@sporekart/ui';

function ThemeToggle() {
  const { theme, setTheme } = useTheme();
  return (
    <button onClick={() => setTheme(theme === 'light' ? 'dark' : 'light')}>
      Switch to {theme === 'light' ? 'Dark' : 'Light'} Mode
    </button>
  );
}
```

### Available Themes

| Theme | Status | Activation |
|-------|--------|------------|
| `light` | ✅ Implemented | Default |
| `dark` | ✅ Implemented | `setTheme('dark')` or system preference |
| `high-contrast` | ✅ Implemented | `setTheme('high-contrast')` or OS setting |

### Theme Persistence

Theme preference is stored in `localStorage` and synced across tabs. System preference is detected via `prefers-color-scheme` media query.

## Responsive Classes

Use the breakpoint context in combination with CSS custom properties for responsive layouts:

```typescript
import { useBreakpoint } from '@sporekart/ui';

function ResponsiveCard() {
  const breakpoint = useBreakpoint();
  const columns = breakpoint === 'sm' ? 1 : breakpoint === 'md' ? 2 : 3;

  return (
    <div style={{ columns }}>
      {/* content */}
    </div>
  );
}
```

### Breakpoints

| Name | Min Width | Usage |
|------|-----------|-------|
| `sm` | 0px | Mobile |
| `md` | 768px | Tablet |
| `lg` | 1024px | Desktop |
| `xl` | 1280px | Wide desktop |
| `2xl` | 1536px | Ultra-wide |

CSS custom properties for responsive values:

```css
.my-element {
  padding: var(--spacing-md);
}

@media (min-width: 768px) {
  .my-element {
    padding: var(--spacing-lg);
  }
}
```

## Common Patterns

### Form Layout

```tsx
import { useForm, useField } from '@sporekart/ui/forms';
import { Button, Input } from '@sporekart/ui';

function LoginForm() {
  const form = useForm({ schema: loginSchema });
  const email = useField('email', form);
  const password = useField('password', form);

  return (
    <form onSubmit={form.handleSubmit}>
      <Input label="Email" {...email} />
      <Input label="Password" type="password" {...password} />
      <Button type="submit" variant="primary">Sign In</Button>
    </form>
  );
}
```

### Feedback Patterns

```tsx
import { useToast } from '@sporekart/ui';

function CopyButton({ text }) {
  const { addToast } = useToast();
  return (
    <button onClick={() => {
      navigator.clipboard.writeText(text);
      addToast({ type: 'success', message: 'Copied!' });
    }}>
      Copy
    </button>
  );
}
```

### Confirm Dialog

```tsx
import { useDialog } from '@sporekart/ui';

function DeleteButton({ onConfirm }) {
  const { confirm } = useDialog();
  return (
    <button onClick={() => confirm({
      title: 'Are you sure?',
      message: 'This action cannot be undone.',
      confirmLabel: 'Delete',
      variant: 'destructive'
    }).then(onConfirm)}>
      Delete
    </button>
  );
}
```

## Best Practices

1. **Do not import from internal paths** — always use `@sporekart/ui`
2. **Use design tokens** — never hardcode colors, spacing, or fonts
3. **Respect component boundaries** — do not override internal component styles via CSS; use the exposed API
4. **Use semantic HTML** — components handle ARIA; ensure your page structure uses landmarks (`<nav>`, `<main>`, `<aside>`)
5. **Test with theme switching** — verify your page in light, dark, and high-contrast modes
6. **Test at all breakpoints** — use the responsive preview at `/design-system/docs`
7. **Handle loading, empty, and error states** — every data-driven component must account for all three
8. **Use the form system** — `useForm`/`useField` provide built-in validation, error display, and accessibility
9. **Prefer composition over configuration** — build complex UIs by composing simple components
10. **Follow the dependency flow** — `primitives → core → composite → feedback → layout`

## Troubleshooting

| Symptom | Likely Cause | Fix |
|---------|--------------|-----|
| Styles not applied | Missing provider | Wrap app in `<ThemeProvider>` |
| Toast not showing | Missing provider | Add `<ToastProvider>` |
| Dark mode not working | `data-theme` not set | Use `useTheme().setTheme('dark')` |
| Components look wrong | Token mismatch | Ensure `@sporekart/tokens` version matches |
| Focus trapping broken | Missing `AccessibilityProvider` | Add it as a wrapper |
