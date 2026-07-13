# Providers

## Overview

Providers layer context into the React tree. Wrap them at the app root — outermost providers are global, innermost are UI-related.

## All Providers

| # | Provider | Layer | Description |
|---|----------|-------|-------------|
| 1 | **ErrorBoundary** | Outermost | Catches unhandled errors; renders fallback UI |
| 2 | **ThemeProvider** | Global | Manages light/dark/high-contrast/system theme |
| 3 | **AccessibilityProvider** | Global | Provides `prefers-reduced-motion`, `prefers-contrast`, screen reader detection |
| 4 | **LocalizationProvider** | Global | Manages locale, direction (LTR/RTL), number/date formatting |
| 5 | **FeatureFlagProvider** | Global | Provides feature flag state to components |
| 6 | **PerformanceProvider** | App | Monitors render counts, CPU metrics; logs in dev |
| 7 | **ToastProvider** | UI | Manages toast notification queue and rendering |
| 8 | **DialogProvider** | UI | Manages modal dialog stack (z-index management, escape handling) |
| 9 | **NotificationProvider** | UI | Manages inline notification banners |

---

### ThemeProvider

See [theme-provider.md](./theme-provider.md).

### ToastProvider

```tsx
import { ToastProvider, useToast } from '@/design-system';

function App() {
  return (
    <ToastProvider position="bottom-right">
      <Main />
    </ToastProvider>
  );
}

function Main() {
  const { toast } = useToast();
  return <button onClick={() => toast('Item saved')}>Save</button>;
}
```

### DialogProvider

```tsx
import { DialogProvider, useDialog } from '@/design-system';

<DialogProvider>
  <ConfirmDelete />
</DialogProvider>;

function ConfirmDelete() {
  const { confirm } = useDialog();
  const handleDelete = async () => {
    const ok = await confirm({ title: 'Delete?', variant: 'danger' });
    if (ok) deleteItem();
  };
}
```

### NotificationProvider

```tsx
import { NotificationProvider, useNotification } from '@/design-system';

<NotificationProvider maxVisible={3}>
  <AlertsPanel />
</NotificationProvider>;
```

### ErrorBoundary

```tsx
import { ErrorBoundary } from '@/design-system';

<ErrorBoundary fallback={<ErrorFallback />}>
  <MyComponent />
</ErrorBoundary>;
```

### FeatureFlagProvider

```tsx
import { FeatureFlagProvider, useFeatureFlag } from '@/design-system';

<FeatureFlagProvider flags={{ newCheckout: true }}>
  <App />
</FeatureFlagProvider>;
```

### PerformanceProvider

```tsx
import { PerformanceProvider } from '@/design-system';

<PerformanceProvider enabled={process.env.NODE_ENV === 'development'}>
  <App />
</PerformanceProvider>;
```

### AccessibilityProvider

```tsx
import { AccessibilityProvider } from '@/design-system';

<AccessibilityProvider>
  <App />
</AccessibilityProvider>;

// Hook: const { reducedMotion, highContrast } = useAccessibility();
```

### LocalizationProvider

```tsx
import { LocalizationProvider } from '@/design-system';

<LocalizationProvider locale="en-IN" dir="ltr">
  <App />
</LocalizationProvider>;
```
