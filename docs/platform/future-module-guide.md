# Future Module Guide — New Team Onboarding

This guide is for a **new business module team** (example: a hypothetical "Loyalty" module). It assumes the platform is READY (see [`README.md`](./README.md)). Your team owns data, types, and thin Page wrappers — **not** the framework.

## The contract

> A module = **data** + **a Page using `ModulePage`**. No framework changes needed.

- `data` — your records + a `DataGridColumn<T>[]` config.
- `Page` — a component that wraps `DataGrid` (or `FormLayout`/`KPIGrid`) inside `PermissionGate` (`action="view"`, `resource={module.id}`) + `FeatureGate` (`flag={module.id}`).

Everything else (sorting, filtering, search, pagination, export, responsive cards, gating, error boundaries) is provided.

## Step-by-step

### 1. Copy the module-template skeleton

Copy `src/admin/templates/ModulePage.template.tsx` into `src/admin/modules/loyalty/LoyaltyPage.tsx` and rename the component to `LoyaltyPage`.

### 2. Fill data / types / hooks / services

```ts
// modules/loyalty/types.ts
import type { DataGridColumn } from '../components/data-grid/types';
export interface LoyaltyTier { id: string; name: string; status: string; members: number; }
export const loyaltyColumns: DataGridColumn<LoyaltyTier>[] = [ /* … */ ];
```

- `types.ts` — interfaces + `DataGridColumn[]`.
- `data.ts` — mock/initial data (replace with your service call later).
- `hooks.ts` — data hooks; use `useQueryState` for table state.
- `services` — your API/client calls (platform-agnostic).

### 3. Reuse the templates

Pick the relevant skeletons from [`crud-template.md`](./crud-template.md) and [`form-template.md`](./form-template.md). Do not reimplement `DataGrid`, `Dialog`, `FormLayout`, or `KPIGrid`.

### 4. Run the quality checklist

Before merge, complete [`quality-checklist.md`](./quality-checklist.md) (Architecture, Responsive, Accessibility, Performance, Error Handling, Loading, Permissions, Documentation, Tests).

### 5. Register

Follow [`developer-guide.md`](./developer-guide.md) steps 4–7: route in `App.tsx`, sidebar in `adminNavigation.tsx`, role-nav in `roleNavigation.ts`, optional KPIs in `dashboardWidgets.ts`.

## What is NOT your responsibility

These are **platform** responsibilities, not module-team responsibilities:

- **Live command palette wiring** — the `CommandPalette` integration that surfaces your module is owned by the platform team.
- **Runtime smoke test** — platform-wide automated render checks are run by the platform/CI, not per module.
- Framework/primitive changes — if you think you need one, escalate; the blueprint should already cover it.

## Definition of done

- [ ] Page uses `ModulePage` pattern; `DataGrid` (no `EnterpriseTable`).
- [ ] `PermissionGate` + `FeatureGate` present with `resource`/`flag` = module id.
- [ ] Registered in route + both nav configs.
- [ ] Quality checklist fully passed.
- [ ] No framework code added.
