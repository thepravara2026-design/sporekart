# Design System Architecture

## Overview

The SporeKart Design System provides a scalable, accessible, and themable component library built on a layered architecture. Each layer has strict dependency boundaries to ensure predictable styling and behavior.

## Component Hierarchy

```
primitives → core → composite → feedback → layout
```

| Layer | Description | Example Components |
|-------|-------------|-------------------|
| **Primitives** | Atomic HTML elements with zero opinion | Box, Text, Icon, VisuallyHidden |
| **Core** | Single-purpose interactive components | Button, Input, Select, Checkbox |
| **Composite** | Multi-part compound components | DatePicker, DataTable, FormGroup |
| **Feedback** | Overlay and notification components | Toast, Dialog, NotificationBanner |
| **Layout** | Page-level structure components | PageShell, Grid, Container, Stack |

## Token Architecture

```
primitives → semantic → component
```

- **Primitive Tokens** — Raw values (color hex, spacing px, font rem). Never change with theme.
- **Semantic Tokens** — Aliases mapped to primitives (`color.bg.primary`). Change with theme.
- **Component Tokens** — Scoped to a component's parts (`button.bg.hover`). Reference semantic tokens.

## Provider Architecture

```
<ErrorBoundary>
  <ThemeProvider>
    <AccessibilityProvider>
      <LocalizationProvider>
        <FeatureFlagProvider>
          <PerformanceProvider>
            <ToastProvider>
              <DialogProvider>
                <NotificationProvider>
                  {children}
                </NotificationProvider>
              </DialogProvider>
            </ToastProvider>
          </PerformanceProvider>
        </FeatureFlagProvider>
      </LocalizationProvider>
    </AccessibilityProvider>
  </ThemeProvider>
</ErrorBoundary>
```

Providers wrap bottom-up: global concerns outermost, UI state innermost.

## File Structure

```
src/design-system/
├── tokens/          — Design token definitions and CSS variables
├── primitives/      — Primitive components (Box, Text, etc.)
├── core/            — Core interactive components
├── composite/       — Compound components
├── feedback/        — Overlay and notification components
├── layout/          — Page structure components
├── hooks/           — Shared React hooks
├── utils/           — Utility functions and helpers
├── providers/       — React context providers
├── styles/          — Global styles, reset, theme CSS
├── icons/           — Icon registry and components
├── playground/      — Playground route pages
└── index.ts         — Public API barrel exports
```

## Dependency Flow

- Primitives depend only on `tokens` and `utils`.
- Core depends on primitives, tokens, hooks, utils.
- Composite depends on core, primitives, tokens, hooks, utils.
- Feedback depends on composite, core, primitives, tokens, hooks, utils.
- Layout depends on primitives, tokens.

No layer may depend on a sibling or higher layer. Circular imports are prevented via ESLint `import/no-restricted-paths`.
