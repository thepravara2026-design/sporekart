# Product Editing — Architecture & Experience

## Overview
The Product Editing Workspace (`editing/ProductEditingWorkspace.tsx`) is the permanent enterprise PIM editing surface. It is Mock Mode only and reuses the certified Phase 8 design system, the Phase 9 product permission framework, and the Phase 9 product creation form architecture.

## Sections (module-local navigation)
`overview · basic · classification · packaging · pricing · seo · publishing · activity · history · settings`

Editable sections render the **reused creation steps** (`BasicInfoStep`, `ClassificationStep`, `PackagingStep`, `PricingStep`, `SeoStep`) via the shared `StepProps { data, errors, setField }` contract. No form is duplicated.

## State ownership
`useProductEditState` is the single source of truth:
- `data` (working copy), `original` (last-saved baseline)
- `errors` = `validateAll(data)` (reused)
- `changed` = `diffProducts(original, data)` (new `changeDetection.ts`)
- `modifiedSections`, `unsaved`, `lifecycle`, `versions`, `activity`, `settings`
- Actions: `setField`, `saveDraft`, `discardChanges`, `resetSection`, `submit`, `applyTransition`, `submitForReview`, `approve`, `reject`, `schedulePublish`, `publish`, `unpublish`, `archive`, `restore`, `remove`, `previewVersion`, `restoreVersion`, `duplicateVersion`, `duplicateProduct`.

## Change Detection
- `changeDetection.ts` maps every `ProductWizardData` field to a section and produces `ChangedField[]` (field, label, section, oldValue, newValue).
- `ChangeDetectionPanel` lists changed fields grouped by section with **original → new** values, a "Discard all changes" action, and a per-section **"Reset section"** action.
- Header `UnsavedBanner` appears whenever `unsaved` is true, offering Save Draft / Discard.

## Unsaved Changes Protection
`UnsavedChangesGuard` (`editing/UnsavedChangesGuard.tsx`):
- Registers a `beforeunload` handler when `unsaved` (covers refresh / close tab / browser navigation).
- Provides `useUnsavedGuard().requestLeave(onLeave)` used by the editing nav: switching sections while unsaved triggers a "Leave & discard / Stay" confirmation `Dialog`.
- `onDiscard` is wired to `discardChanges`.

> Note: SPA route-exit blocking requires a data router (`createBrowserRouter`); the current `BrowserRouter` cannot use `useBlocker`. This is logged as technical debt for a platform sprint.

## Review Panel
`ReviewPanel` aggregates validation + readiness into one card: completeness score, error/warning/changed badges, SEO Readiness (reused `computeSeoScore`), Packaging Readiness, Publishing Readiness, Validation Health, plus a validation summary and missing/recommended field list (reused `computeWarnings`).

## Permissions
`can(action)` is derived from `permissions.canProduct(CURRENT_ROLE, action)`. Editing fields are disabled for non-`update` roles; publishing/approve/archive/restore/delete are gated on their respective permissions.

## Extensibility
The workspace is structured so a future backend can replace `mockEditData` + the in-memory reducers in `useProductEditState` with API calls, without changing any presentational component. Audit logging, approval workflows, inventory linkage, and marketplace sync are all forward-compatible by design.
