# Product Layout

> Foundation doc for [Sprint 24 Part 1](./sprint-24-part-1.md). Code: `src/admin/modules/products/layout`. Mock Mode.

## Purpose

`ProductLayout` is a **self-contained shell that mirrors the admin app shell** — header, breadcrumbs,
toolbar, content, status. It provides the foundation's visual frame without modifying the global shell.

## Composition

```tsx
// src/admin/modules/products/layout/ProductLayout.tsx (foundation)
import { Breadcrumb, type Crumb } from 'src/design-system/components/navigation/Breadcrumb';
import { Button } from 'src/design-system/components/core/Button';
import { Icon } from 'src/design-system/icons/Icon';
import { StatusBadge } from 'src/admin/components/status/StatusBadge';
import { Skeleton } from 'src/design-system/components/display/Skeleton';

export function ProductLayout({ crumbs, actions, children }: {
  crumbs: Crumb[];
  actions?: React.ReactNode;
  children: React.ReactNode;
}) {
  return (
    <div className="product-layout">
      {/* Header */}
      <header className="product-layout__header">
        <Breadcrumb crumbs={crumbs} aria-label="Product breadcrumb" />
        <div className="product-layout__toolbar">
          {actions ?? <Skeleton variant="rounded" width={120} height={36} />}
        </div>
      </header>

      {/* Workspace header + quick actions */}
      <div className="product-layout__workspace-header">
        <StatusBadge status="draft" variant="neutral" />
        <Button variant="primary" leftIcon={<Icon name="plus" />}>New Product</Button>
      </div>

      {/* Content */}
      <main className="product-layout__content">{children}</main>

      {/* Status */}
      <footer className="product-layout__status">
        <StatusBadge status="Mock Mode" variant="info" />
      </footer>
    </div>
  );
}
```

## Regions

| Region | Component | Notes |
|--------|-----------|-------|
| Header | `Breadcrumb` (design-system nav) | `aria-label` set |
| Breadcrumbs | `Breadcrumb` | `Crumb[]` driven by `currentView` |
| Toolbar | `Button` (design-system core) + quick actions | wraps permission-gated actions |
| Workspace header | `StatusBadge` + `Button` | shows lifecycle state |
| Quick actions | `Button` + `Icon` | gated by `PermissionGate` |
| Content | children (workspace / dashboard) | themed by CSS variables |
| Status | `StatusBadge` | "Mock Mode" indicator |

## Mirroring the admin shell (without modifying it)

- The layout reuses the **same design tokens** (`--color-bg-surface-default`, `--radius-card`,
  `--space-component-gap`, `--text-*`) as the global shell, so it visually matches.
- It does **not** import or mutate the global shell component; it is a parallel, self-contained frame.
- Dark/light is inherited automatically because tokens flip with the theme context.

## Responsive

- Header/toolbar wrap via flex `gap: var(--space-inline-xs)`; toolbar stacks below breadcrumbs under 768px.
- Content area uses `var(--space-component-gap)` grid gutters, collapsing columns on small screens.
- Status footer stays inline on desktop, wraps on mobile.

## Real components used

- `Breadcrumb` — `src/design-system/components/navigation/Breadcrumb.tsx`
- `Button` — `src/design-system/components/core/Button.tsx`
- `Icon` — `src/design-system/icons/Icon.tsx`
- `StatusBadge` — `src/admin/components/status/StatusBadge.tsx`
- `Skeleton` — `src/design-system/components/display/Skeleton.tsx`
