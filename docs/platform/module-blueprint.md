# Module Blueprint — Reusable Architecture

Every admin module follows the same mandated shape. This blueprint maps each concern to a verified primitive and a file-location convention. Implement business logic only; do not re-implement the primitives.

## Mandatory sections per module

| # | Section | Enterprise primitive | File location convention |
| --- | --- | --- | --- |
| 1 | Overview | `MockModule` (id/label/icon/description) | `modules/<id>/types.ts` + `moduleData.ts` |
| 2 | Routes | React Router `<Route>` | `App.tsx` (registration only) |
| 3 | Layout | `ModulePage` shell (flex column + gap + padding) | `modules/ModulePage.tsx` (reuse) |
| 4 | Data Table | `DataGrid` (composite, memoized) | `modules/<id>/<Id>Page.tsx` |
| 5 | Filters | `DataGrid` `filterable` + column `filterType`/`filterOptions` | column config in `types.ts` |
| 6 | Search | `DataGrid` `searchable` | `<Id>Page.tsx` prop |
| 7 | Pagination | `DataGrid` `pageSize`/`pageSizeOptions` | `<Id>Page.tsx` props |
| 8 | Dialogs | `Dialog` (`open`/`onClose`/`title`/`actions`) | `modules/<id>/components/` or inline |
| 9 | Forms | `FormLayout` → `FormSection` → `FormField` + DS inputs | `modules/<id>/components/` |
| 10 | Validation | `FormField` `error`/`warning`/`success` + field-level checks | form component |
| 11 | Permissions | `PermissionGate` (`action`/`resource`) | `<Id>Page.tsx` (wrap `view`) |
| 12 | Loading States | `DataGrid` `loading` + `operational-states` | `<Id>Page.tsx` |
| 13 | Error States | `ModuleErrorBoundary` / `PageErrorBoundary` | wrap route element |
| 14 | Empty States | `DataGrid` `emptyMessage`/`emptyDescription` | `<Id>Page.tsx` props |
| 15 | Responsive Behaviour | `DataGrid` `cardViewBreakpoint` + `renderCard` | `<Id>Page.tsx` props |
| 16 | Accessibility | Semantic HTML + ARIA + `Dialog` focus trap + tokens | all components |
| 17 | Documentation | Link to `docs/platform/*`; module README optional | `docs/platform/` |

## Section detail

### 1. Overview
Define a `MockModule` (see `moduleData.ts`): `id`, `label`, `icon`, `description`, `columns: DataGridColumn[]`, `data`. The `id` is the single source of truth reused as `resource` (permissions) and `flag` (features).

### 2. Routes
Only register a route in `App.tsx`. Do not build bespoke layout/shell — the page component owns its shell via `ModulePage`.

### 3. Layout
Reuse `ModulePage` (header `h1` + description + `DataGrid` inside gates). For non-grid pages (forms/dashboards) follow [`page-template.md`](./page-template.md).

### 4. Data Table
Use `DataGrid` — it internally provides the `DataGridProvider` context. **Do NOT use `EnterpriseTable` standalone** (it requires context that only `DataGrid` supplies).

### 5–7. Filters / Search / Pagination
All provided by `DataGrid`. Declare per-column filters via `filterable: true`, `filterType: FilterConfig['type']` (`'text' | 'select' | 'date' | 'number' | 'boolean'`), and `filterOptions`. Lift state with `useQueryState` (see `hooks/useQueryState.ts`).

### 8. Dialogs
`import { Dialog } from '../../../design-system/components/feedback/Dialog';` — props: `open`, `onClose`, `title`, `children`, `actions`, `size`, `closeOnOverlay`, `closeOnEscape`, `showCloseButton`.

### 9–10. Forms / Validation
`import { FormLayout, FormSection, FormField, FormRow, FormActions, FormFooter } from '../../../design-system/components/forms';` Inputs from `../components/forms`: `Textarea`, `NumberInput`, `CurrencyInput`, `PhoneInput`, `EmailInput`, `TimePicker`, `TagSelector`. Validation surfaces via `FormField` `error`/`warning`/`success`.

### 11. Permissions
Wrap content in `<PermissionGate action="view" resource={module.id}>`. Guard mutations with specific actions: `create`/`update`/`delete`/`export`/`import`/`approve`/`publish`/`archive`/`bulk_actions`.

### 12–14. Loading / Error / Empty
Pass `loading` to `DataGrid`; wrap the page in `ModuleErrorBoundary`; supply `emptyMessage`/`emptyDescription`.

### 15. Responsive
Set `cardViewBreakpoint` (px) and `renderCard` for mobile card rendering. Use design tokens and CSS grid; never JS layout.

### 16. Accessibility
Use semantic headings, labelled form controls (`FormField` `label`/`id`), keyboard-operable `Dialog`, and color-plus-text cues (never color alone). See [`coding-standards.md`](./coding-standards.md).

### 17. Documentation
Reference `docs/platform/*`; do not duplicate cross-cutting rules from [`../standards/`](../standards/) or [`../coding-standards/README.md`](../coding-standards/README.md).
