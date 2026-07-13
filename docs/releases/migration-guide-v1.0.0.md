# Migration Guide — v1.0.0

| | |
|---|---|
| **From** | None (v1.0.0 is the first release) |
| **To** | v1.0.0 |
| **Release Date** | 2026-07-13 |

---

## Setup Steps

1. **Install dependencies** — Ensure your project uses React 18+ and TypeScript 5+.
2. **Wrap application with `ThemeProvider`** — This provides design tokens and theme context to all components.
3. **Add required providers** — `ToastProvider`, `DialogProvider`, `NotificationProvider` must be included before their respective components are used.
4. **Import components** — Import from the `design-system/` directory in the monorepo.
5. **Use design tokens** — Reference tokens via CSS custom properties (`var(--<token-name>)`).

---

## Import Conventions

```tsx
// Core
import { Button } from './design-system/components/core/Button';
import { Input } from './design-system/components/core/Input';
import { Icon } from './design-system/components/core/Icon';

// Composite
import { Card } from './design-system/components/composite/Card';
import { Avatar } from './design-system/components/composite/Avatar';

// Feedback
import { Dialog } from './design-system/components/feedback/Dialog';
import { Toast } from './design-system/components/feedback/Toast';
import { Alert } from './design-system/components/feedback/Alert';

// Navigation
import { AppShell } from './design-system/components/navigation/AppShell';
import { Breadcrumb } from './design-system/components/navigation/Breadcrumb';
import { Tabs } from './design-system/components/navigation/Tabs';

// Forms
import { FormProvider } from './design-system/components/forms/FormProvider';
import { Select } from './design-system/components/forms/Select';

// Charts
import { ChartContainer } from './design-system/components/charts/ChartContainer';
import { BarChart } from './design-system/components/charts/BarChart';

// Layout
import { Container } from './design-system/components/layout/Container';
import { Stack } from './design-system/components/layout/Stack';
```

---

## Provider Setup

Wrap your application root with the required providers in the correct hierarchy:

```tsx
import { ThemeProvider } from './design-system/providers/ThemeProvider';
import { ToastProvider } from './design-system/providers/ToastProvider';
import { DialogProvider } from './design-system/providers/DialogProvider';
import { NotificationProvider } from './design-system/providers/NotificationProvider';
import { ErrorBoundary } from './design-system/providers/ErrorBoundary';

function App() {
  return (
    <ErrorBoundary>
      <ThemeProvider defaultTheme="light">
        <ToastProvider>
          <DialogProvider>
            <NotificationProvider>
              {/* Application routes and content */}
            </NotificationProvider>
          </DialogProvider>
        </ToastProvider>
      </ThemeProvider>
    </ErrorBoundary>
  );
}
```

---

## Token Usage

Design tokens are exposed as CSS custom properties on the `:root` element by the `ThemeProvider`. Reference them in inline styles or CSS modules:

```tsx
// Inline style
<Button style={{ backgroundColor: 'var(--color-primary-500)' }} />

// CSS module
.card {
  padding: var(--spacing-md);
  border-radius: var(--radius-lg);
  box-shadow: var(--elevation-2);
  font-size: var(--typography-body-md);
}
```

**Token categories:**

| Category | Prefix | Example |
|---|---|---|
| Color | `--color-*` | `--color-primary-500` |
| Typography | `--typography-*` | `--typography-heading-xl` |
| Spacing | `--spacing-*` | `--spacing-md` |
| Radius | `--radius-*` | `--radius-lg` |
| Elevation | `--elevation-*` | `--elevation-2` |
| Animation | `--animation-*` | `--animation-duration-normal` |

---

## Responsive Patterns

Use the built-in responsive grid and container classes:

```tsx
import { Container } from './design-system/components/layout/Container';
import { Grid } from './design-system/components/layout/Grid';

<Container>
  <Grid columns={{ xs: 1, sm: 2, md: 3, lg: 4 }}>
    {/* Grid items */}
  </Grid>
</Container>
```

**Breakpoints:**

| Name | Min Width |
|---|---|
| xs | 0 |
| sm | 640px |
| md | 1024px |
| lg | 1440px |

---

## Accessibility Checklist

- **ARIA attributes**: All components include required ARIA labels, roles, and states
- **Keyboard navigation**: All interactive components are keyboard accessible (Tab, Enter, Escape, Arrow keys)
- **Focus management**: Visible focus indicators are applied via `--color-focus-ring` token
- **Screen readers**: Components use semantic HTML and `aria-live` regions for dynamic content
- **Color contrast**: All color combinations meet WCAG 2.2 AA contrast ratios (4.5:1 normal, 3:1 large)
- **Reduced motion**: Respects `prefers-reduced-motion` media query
- **Touch targets**: All interactive elements are at least 44×44px

---

## Common Pitfalls

| Pitfall | Solution |
|---|---|
| Hardcoding colors, spacing, or other visual values | Always use design tokens (`var(--<token-name>)`) |
| Bypassing provider hierarchy (e.g., using Dialog without DialogProvider) | Always include all required providers at the app root |
| Importing from internal paths (e.g., `components/core/Button/Button.tsx`) | Import only from the public component directory |
| Using components outside of ThemeProvider | Always wrap the app with ThemeProvider |
| Direct DOM manipulation of component internals | Use the public API and props only |

---

## FAQ

### Q: Can I use individual components without the full provider stack?
**A:** No. All components depend on `ThemeProvider`. Feedback components additionally require their respective providers (`ToastProvider`, `DialogProvider`, `NotificationProvider`).

### Q: Can I create a custom theme?
**A:** Yes. Pass a custom theme object to `ThemeProvider`'s `theme` prop. Override any subset of design tokens.

### Q: Are there any peer dependencies?
**A:** React 18+ and TypeScript 5+ are required. The design system has no other peer dependencies.

### Q: How do I switch between light and dark themes?
**A:** Toggle the `theme` prop on `ThemeProvider` between `"light"` and `"dark"`. All components respond automatically via CSS custom properties.

### Q: Can I use the design system with Next.js?
**A:** Yes. Import components as client components and ensure the provider hierarchy is set up in the root layout.

### Q: How do I report a bug or request a feature?
**A:** File an issue in the project repository with the label `design-system`.
