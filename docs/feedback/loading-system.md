# Loading Experience

## Overview

The Loading system provides a family of components to communicate loading states across the application — from full-page transitions to inline spinners and skeleton placeholders. Choose the right loader based on the scope and expected duration of the operation.

### Component Hierarchy

```
Loading
├── GlobalLoadingOverlay
├── SectionLoader
├── InlineLoader
├── PageLoader
├── Spinner
├── ShimmerLoader
└── ProgressiveLoader
```

---

## When to Use Each

| Component | Scope | Duration | Use Case |
|-----------|-------|----------|----------|
| **GlobalLoadingOverlay** | Full viewport | 1-10s | Route transitions, auth checks, initial app load |
| **SectionLoader** | Content region | 1-5s | Dashboard widgets, list refreshes |
| **InlineLoader** | Small element | < 2s | Button loading, small data fetches |
| **PageLoader** | Full page | 1-3s | Page navigation, skeleton transition |
| **Spinner** | Any scope | Variable | Configurable standalone spinner |
| **ShimmerLoader** | Content region | 1-5s | Skeleton placeholder for cards, tables, text |
| **ProgressiveLoader** | Full page | 3+ seconds | Multi-stage loading with status updates |

---

## GlobalLoadingOverlay

Full-screen overlay with centered spinner. Used for application-level loading states.

### Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `loading` | `boolean` | `false` | Controls visibility |
| `label` | `string` | `'Loading...'` | Screen reader label |
| `spinner` | `ReactNode` | Default spinner | Custom spinner |
| `blur` | `boolean` | `true` | Blur background content |
| `opacity` | `number` | `0.85` | Overlay opacity |
| `zIndex` | `number` | token | Custom z-index |
| `minDisplayMs` | `number` | `300` | Minimum display time to avoid flash |

---

## SectionLoader

Replaces a content region with a loading indicator.

### Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `loading` | `boolean` | — | Loading state |
| `children` | `ReactNode` | — | Content to show when not loading |
| `skeleton` | `ReactNode` | — | Custom skeleton. Defaults to centered spinner. |
| `height` | `string \| number` | — | Fixed height during loading to prevent layout shift |
| `aria-label` | `string` | — | Loading announcement |

---

## InlineLoader

Small loading indicator placed inline with text or within buttons.

### Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `size` | `'xs' \| 'sm' \| 'md'` | `'sm'` | Spinner size |
| `label` | `string` | — | Screen reader text |
| `color` | `string` | `currentColor` | Inherits text color by default |

---

## PageLoader

Full-height loader for page transitions. Supports linear progress bar at the top of the page.

### Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `loading` | `boolean` | — | Loading state |
| `progress` | `'bar' \| 'spinner' \| 'skeleton'` | `'skeleton'` | Loader style |
| `skeleton` | `ReactNode` | — | Custom skeleton layout |
| `delay` | `number` | `200` | Delay before showing (prevents flash on fast loads) |

---

## Spinner

Highly configurable standalone spinner component.

### Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `size` | `'xs' \| 'sm' \| 'md' \| 'lg' \| 'xl'` | `'md'` | Dimensions |
| `color` | `string` | `--color-primary` | Spinner color |
| `speed` | `'slow' \| 'normal' \| 'fast'` | `'normal'` | Animation speed |
| `thickness` | `number` | `3` | Stroke width |
| `label` | `string` | `'Loading'` | Screen reader label |
| `variant` | `'circle' \| 'dots' \| 'pulse'` | `'circle'` | Animation style |

---

## ShimmerLoader

Skeleton placeholder that mimics content layout. Animates with a shimmer effect.

### Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `variant` | `'text' \| 'card' \| 'table' \| 'image' \| 'avatar' \| 'custom'` | `'text'` | Preset skeleton shape |
| `lines` | `number` | `3` | Number of text lines (text variant) |
| `width` | `string` | — | Custom width |
| `height` | `string` | — | Custom height |
| `rounded` | `boolean \| 'sm' \| 'md' \| 'lg' \| 'full'` | `'md'` | Border radius |
| `count` | `number` | `1` | Repeat skeleton N times (for lists) |
| `gap` | `string` | `'16px'` | Gap between repeated items |
| `children` | `ReactNode` | — | Custom skeleton layout when variant="custom" |

---

## ProgressiveLoader

Multi-phase loader used for longer operations. Shows status updates as loading progresses.

### Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `phases` | `Phase[]` | — | Ordered loading phases |
| `currentPhase` | `number` | `0` | Active phase index |
| `error` | `string` | — | Error message if a phase fails |
| `onRetry` | `() => void` | — | Retry handler on failure |

```typescript
interface Phase {
  id: string;
  label: string;
  status: 'pending' | 'active' | 'completed' | 'failed';
}
```

---

## Examples

### GlobalLoadingOverlay for route transitions

```tsx
import { GlobalLoadingOverlay } from '@sporekart/ui';

function App() {
  const { isRouteLoading } = useRouter();

  return (
    <>
      <Routes />
      <GlobalLoadingOverlay loading={isRouteLoading} label="Loading page..." />
    </>
  );
}
```

### PageLoader with skeleton

```tsx
import { PageLoader, ShimmerLoader } from '@sporekart/ui';

function UserProfilePage() {
  const { loading, user } = useUserProfile();

  return (
    <PageLoader
      loading={loading}
      progress="skeleton"
      delay={300}
      skeleton={
        <div style={{ padding: 24 }}>
          <ShimmerLoader variant="avatar" width={80} height={80} rounded="full" />
          <ShimmerLoader variant="text" lines={2} width="60%" />
          <ShimmerLoader variant="card" count={3} gap="12px" />
        </div>
      }
    >
      <UserProfileContent user={user} />
    </PageLoader>
  );
}
```

### SectionLoader for dashboard widgets

```tsx
import { SectionLoader } from '@sporekart/ui';

function RevenueWidget() {
  const { loading, data } = useRevenueData();

  return (
    <SectionLoader
      loading={loading}
      height={200}
      aria-label="Loading revenue chart"
    >
      <RevenueChart data={data} />
    </SectionLoader>
  );
}
```

### InlineLoader within button

```tsx
import { InlineLoader, Button } from '@sporekart/ui';

function SaveButton() {
  const [saving, setSaving] = useState(false);

  return (
    <Button disabled={saving} onClick={handleSave}>
      {saving && <InlineLoader size="sm" />}
      {saving ? 'Saving...' : 'Save'}
    </Button>
  );
}
```

### ProgressiveLoader for multi-step setup

```tsx
import { ProgressiveLoader } from '@sporekart/ui';

const phases: Phase[] = [
  { id: 'config', label: 'Configuring environment', status: 'completed' },
  { id: 'migrate', label: 'Migrating data', status: 'active' },
  { id: 'verify', label: 'Verifying integrity', status: 'pending' },
  { id: 'deploy', label: 'Deploying', status: 'pending' },
];

function SetupProgress({ currentPhase, error, onRetry }: ProgressiveLoaderProps) {
  return (
    <ProgressiveLoader
      phases={phases}
      currentPhase={currentPhase}
      error={error}
      onRetry={onRetry}
    />
  );
}
```

### ShimmerLoader for card list

```tsx
import { ShimmerLoader } from '@sporekart/ui';

function ProductListSkeleton() {
  return (
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: 16 }}>
      {Array.from({ length: 6 }).map((_, i) => (
        <ShimmerLoader
          key={i}
          variant="card"
          height={280}
          rounded="lg"
        />
      ))}
    </div>
  );
}
```

---

## Best Practices

- **Minimum display time**: Use `minDisplayMs` on GlobalLoadingOverlay (300ms) to prevent flash-of-loading for fast operations.
- **Delay before showing**: Use `delay` on PageLoader (200ms) to avoid showing loaders for operations that resolve quickly.
- **Skeleton matching**: ShimmerLoader skeletons should closely match the content layout they replace to minimize layout shift (CLS).
- **Avoid full-screen spinners**: For partial content updates (dashboard widgets, list refresh), use SectionLoader instead of blocking the entire page.
- **Progressive disclosure**: For operations > 3s, use ProgressiveLoader with phase labels. Users tolerate waiting better when they see progress.
- **Button loading**: Use InlineLoader inside buttons instead of replacing button text with a spinner. This maintains button width and accessibility.
- **Spinner variants**: Use 'circle' for standard loading, 'dots' for chat/communication, 'pulse' for pending network operations.
- **Accessibility**: All loaders must have `aria-label` or `aria-labelledby`. Use `aria-live="polite"` region to announce loading completion.
- **Reduced motion**: Replace spinning animations with a fade or pulse when `prefers-reduced-motion` is set.
