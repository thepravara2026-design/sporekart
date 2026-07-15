# Coding Standards

Actionable rules for all `src/admin/` code. Cross-cutting rules (error handling, commit/branch, logging) live in [`../standards/`](../standards/) and [`../coding-standards/README.md`](../coding-standards/README.md) — follow them, do not duplicate.

## Naming conventions

- **Components**: `PascalCase` (e.g., `ProductsPage`, `LoyaltyForm`).
- **Hooks**: `use*` prefix, `camelCase` (e.g., `useQueryState`, `useSession`).
- **Component files**: `PascalCase.tsx`; **util files**: `camelCase.ts`.
- **Types/interfaces**: `PascalCase`, exported from a `types.ts` (e.g., `DataGridColumn`, `MockModule`).
- **Module folder**: `kebab-case` / singular id (`products`, `loyalty`).

## Folder structure rules

```
src/admin/modules/<id>/        # one folder per module
  <Id>Page.tsx                 # Page (ModulePage pattern)
  types.ts                     # exported interfaces + DataGridColumn[]
  data.ts                      # mock/initial data
  hooks.ts                     # data hooks
  components/                  # module-only presentational pieces
  index.ts                     # barrel
```

## File naming

- One primary component per file; co-locate tiny helpers in the same file.
- `types.ts` holds shared interfaces; `index.ts` re-exports the public surface.

## Import rules

The project uses **relative imports** (no absolute `src/` aliases inside `admin`):

- Within `admin`, use `../` (e.g., `../components/data-grid/DataGrid`, `../permissions/PermissionGate`, `../feature-flags/FeatureGate`).
- Design-system from deeper admin folders uses `../../../design-system/...` (e.g., `../../../design-system/components/forms`, `../../../design-system/components/feedback/Dialog`).
- Confirmed pattern: `ModulePage.tsx` uses `../components/...`, `../permissions/...`, `../feature-flags/...`; `adminNavigation.tsx` uses `../../design-system/...`.

## Component composition

- Memoize page/table components with `React.memo` (e.g., `export const ModulePage = memo(function ModulePage() { … })`).
- Keep templates **stateless** where possible; lift state to the page.
- Do NOT use `EnterpriseTable` standalone — it requires `DataGridProvider` context that only `DataGrid` supplies. Always use `DataGrid`.

## Hook usage

- Lift table state (sort/filter/page/search) with `useQueryState` from `../hooks/useQueryState`.
- Keep hooks at the page level; pass data/handlers down as props.

## Type definitions

- Prefer `interface` exported from `types.ts`.
- For grid columns use `DataGridColumn<T>` — **not** the internal `ColumnConfig`. `ColumnConfig` is a private type used by `useQueryState`/grid internals.
- Use `KPIData` and `WidgetConfig` from `../dashboard/types` for dashboard widgets.

## State management

- Page-only state; no global store for module data.
- Table view-state belongs in `useQueryState`; form state in the form component; loading/error/success in the page.

## Performance rules

- Memoize components (`React.memo`); keep render callbacks stable (avoid inline object/function props where they break memo — use `useCallback`/`useMemo`).
- Use **CSS grid / flex** for layout; never compute layout in JS.
- Provide `rowKey` to `DataGrid` for stable identity (selection/virtualization).
- Avoid re-creating `columns` arrays each render — define them at module scope or memoize.

## Accessibility rules

- Semantic HTML (`<h1>` per page, `<table>` semantics via `DataGrid`, labelled `<button>`/`<input>`).
- ARIA where native semantics are insufficient; `Dialog` handles focus trap.
- Full keyboard navigation; focusable controls reachable and operable.
- **Color is never the only signal** — pair status colors with text/icon (e.g., status label + color).
- Form fields must have associated `label`/`id` (via `FormField`).

## Design-system usage

- **Always use design tokens**, never hardcoded colors — except the documented KPI hex palette in `KPIData.color`.
- Color tokens: `var(--color-text-primary)`, `var(--color-text-secondary)`, `var(--color-text-tertiary)`, `var(--color-border)`, `var(--color-surface)`, `var(--color-surface-hover)`, `var(--color-primary)`, `var(--color-primary-alpha)`, `var(--color-success)`, `var(--color-error)`, `var(--color-warning)`.
- Text scale: `var(--text-h1)` … `var(--text-caption)`.
- Radius: `var(--radius-sm|md|lg)`. Spacing: `var(--space-component-gap)`, `var(--space-page-x)`, `var(--space-section-gap)`, `var(--space-stack-xs)`.
- KPI palette (documented, allowed): green `#2f6f4f`, blue `#1d9bf0`, purple `#7c3aed`, amber `#d97706`, red `#ef4444`.
