# Quality Checklist — Pre-Merge

Every future module MUST pass this checklist before merge. A module team cannot merge unless all applicable items are checked. Cross-cutting rules reference [`../standards/`](../standards/).

## Architecture

- [ ] Page uses the `ModulePage` pattern (`PermissionGate` + `FeatureGate` + `DataGrid` / `FormLayout` / `KPIGrid`).
- [ ] `DataGrid` used (not `EnterpriseTable` standalone).
- [ ] Module folder matches the standard tree (`<id>/<Id>Page.tsx`, `types.ts`, `data.ts`, `index.ts`).
- [ ] Route registered in `App.tsx`; route element wrapped in an error boundary.
- [ ] No framework/primitive changes required to ship the module.

## Responsive Design

- [ ] `DataGrid` has `cardViewBreakpoint` + `renderCard` for mobile.
- [ ] Layout uses token spacing (`var(--space-page-x)`, `var(--space-section-gap)`); no fixed full-width assumptions.
- [ ] Verified at desktop + mobile breakpoints.

## Accessibility

- [ ] Single `<h1>` per page; headings ordered.
- [ ] Form controls have `label`/`id` via `FormField`.
- [ ] `Dialog` used for modals (focus trap, ESC/overlay close).
- [ ] Status conveyed by text/icon + color (not color alone).
- [ ] Full keyboard operability.

## Performance

- [ ] Page/table components wrapped in `React.memo`.
- [ ] `columns` defined at module scope or memoized; no per-render allocation where avoidable.
- [ ] `DataGrid` has a stable `rowKey`.
- [ ] Table state lifted via `useQueryState`.
- [ ] Layout via CSS grid/flex only.

## Error Handling

- [ ] Page wrapped in `ModuleErrorBoundary` / `PageErrorBoundary`.
- [ ] Server/dataset errors surface in `FormField error` or an error state (see [`../standards/error-handling.md`](../standards/error-handling.md)).
- [ ] No uncaught promise rejections on load/save.

## Loading States

- [ ] `DataGrid` receives `loading`; skeletons shown during fetch.
- [ ] Submit buttons reflect `saving` state; no double-submit.

## Permissions

- [ ] View wrapped in `<PermissionGate action="view" resource={module.id}>`.
- [ ] Mutations gated: `create`/`update`/`delete`/`export`/`import`/`approve`/`publish`/`archive`/`bulk_actions` as appropriate.
- [ ] `FeatureGate flag={module.id}` wraps the page.

## Documentation

- [ ] Module references `docs/platform/*` for construction.
- [ ] No duplication of cross-cutting rules (link [`../standards/`](../standards/), [`../coding-standards/README.md`](../coding-standards/README.md)).
- [ ] KPIs (if any) registered in `dashboardWidgets.ts`.
- [ ] Sidebar (`adminNavigation.tsx`) and role-nav (`roleNavigation.ts`) entries added.

## Tests

- [ ] Column config renders without crashing.
- [ ] Permission gate falls back correctly without the permission/flag.
- [ ] Empty/loading/error states render.
- [ ] (Recommended) a smoke render of the Page with mock data passes.
