# Error Boundaries

## Architecture

Four-level error boundary hierarchy using a shared `BaseErrorBoundary` class component. Each level provides appropriate fallback UI with retry and reload actions.

```
BaseErrorBoundary
  ├─ GlobalErrorBoundary (wraps entire app)
  ├─ ModuleErrorBoundary (wraps feature modules)
  ├─ PageErrorBoundary (wraps individual pages)
  └─ ComponentErrorBoundary (wraps isolated widgets)
```

## Usage

```tsx
// Global
<GlobalErrorBoundary>
  <App />
</GlobalErrorBoundary>

// Module
<ModuleErrorBoundary moduleName="Orders" onRetry={handleRetry}>
  <OrdersModule />
</ModuleErrorBoundary>

// Page
<PageErrorBoundary pageName="Order Details">
  <OrderDetailsPage />
</PageErrorBoundary>

// Component (with custom fallback)
<ComponentErrorBoundary componentName="SalesChart" fallback={<ChartFallback />}>
  <SalesChart />
</ComponentErrorBoundary>
```

## Fallback UI

Each boundary renders an accessible alert with:
- Error icon and message
- "Try again" button (calls `onRetry` or resets error state)
- "Reload page" button (calls `window.location.reload()`)
- Custom fallback can be provided via the `fallback` prop

## Extension Points

- `onError` callback for logging/reporting
- `onRetry` for module-level recovery logic
- Custom fallback for component-level boundaries
