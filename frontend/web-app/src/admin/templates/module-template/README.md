# SporeKart Admin — Module Template Library

This folder (`src/admin/templates/`) is a **reusable code-template library** for building new
Enterprise Admin modules quickly and consistently. Every template reuses the real, verified
Enterprise primitives — never re-implement them.

## Standard Module Folder Structure

When scaffolding a new module under `src/admin/modules/<ModuleName>/`, use this layout:

```
<Module>/
  Pages/          # Route-level page components (List, Details, Create, Edit)
  Components/     # Module-specific presentational components
  Dialogs/        # Module-specific dialogs (built on design-system Dialog)
  Forms/          # Form compositions built on design-system forms
  Tables/         # DataGrid column definitions and table wrappers
  Cards/          # Card / tile components
  Services/       # API/service layer (data access, mutations)
  Hooks/          # Custom React hooks (data fetching, state)
  Types/          # Local TypeScript types and interfaces
  Utils/          # Pure helper functions
  Constants/      # Module constants (labels, enums, option lists)
  MockData/       # In-memory mock data for previews/tests
  Tests/          # Unit / integration tests
  Documentation/  # Module-specific docs and runbooks
```

### What goes where

- **Pages/ & Components/ & Cards/ & Dialogs/ & Forms/ & Tables/** — UI built by composing the
  real reusable primitives below. Keep business logic out of these; delegate to `Services/` + `Hooks/`.
- **Services/ & Hooks/** — all data access and stateful behavior. Never put raw `fetch` calls in a component.
- **Types/ & Constants/ & Utils/ & MockData/** — framework-agnostic, pure, and fully typed.
- **Tests/ & Documentation/** — keep every module verifiable and self-documenting.

### Verified real primitives to reuse (do NOT re-implement)

- **Tables:** `src/admin/components/data-grid/DataGrid` + `.../types` (`DataGridColumn`, `FilterConfig`).
- **Permissions:** `src/admin/permissions/PermissionGate` (`action`, `resource`).
- **Feature flags:** `src/admin/feature-flags/FeatureGate` (`flag`, `requiredState`).
- **Forms:** `src/design-system/components/forms` (`FormLayout`, `FormSection`, `FormField`,
  `FormRow`, `FormActions`, `FormFooter`) and `src/admin/components/forms` inputs
  (`Textarea`, `NumberInput`, `CurrencyInput`, `PhoneInput`, `EmailInput`, `TimePicker`, `TagSelector`).
- **Feedback:** `src/design-system/components/feedback/Dialog`.
- **Dashboard:** `src/admin/dashboard/kpi/KPIGrid` + `src/admin/dashboard/types` (`KPIData`, `WidgetConfig`).
- **Icons:** `src/design-system/icons/Icon`.

### Canonical reusable page

`PageTemplate.tsx` is the canonical, generic, memoized page. Pass it a `ModuleConfig<T>`
(defined in `types.ts`) and it renders the header, wraps content in `PermissionGate` +
`FeatureGate`, optional `KPIGrid`, and a `DataGrid` — all with design tokens.

### Previews

`preview/` contains standalone, self-contained preview components (not wired into `App.tsx`)
that demonstrate each template in isolation.
