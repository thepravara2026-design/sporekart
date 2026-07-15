# Sprint 24 Part 3 — Enterprise Admin: Product Creation Wizard & PIM

- **Date:** 2026-07-14
- **Status:** COMPLETE
- **Quality Gate:** SATISFIED
- **Mode:** Mock Mode — **NO backend, NO API, NO persistence** implemented. Drafts live in `sessionStorage` only; product creation is simulated (mock id returned on confirmation).
- **Scope:** 7-step **Product Creation Wizard (PIM authoring)** on top of the Part 1 Product foundation + Part 2 browsing surface. Authoring/existing only — no catalogue-wide mutation wiring.
- **Code location:** `src/admin/modules/products/creation/` (within `frontend/web-app`)
- **Routes:** `/preview/products/create`, `/preview/products/wizard`, `/preview/products/forms`, `/preview/products/review`, `/preview/products/confirmation`; real route `/products/create`
- **Untouched:** the design system, Part 1/Part 2 Product foundation, Part 2 catalog, `adminNavigation`, customer sites, auth, and RBAC were **NOT** modified — this sprint only reuses them.

---

## 1. Objective

Deliver an **Enterprise Admin Product Creation Wizard** (PIM authoring surface) in Mock Mode. The wizard guides an admin through 7 steps — Basic Info, Classification, Packaging, Pricing, SEO, Review, Confirmation — with a reusable form-composition pattern, a validation framework, a draft workflow backed by `sessionStorage`, a live preview panel, and a full set of empty/error states. All of it is built from certified Phase 8 design-system primitives and the Part 1 Product module shell.

## 2. Context

- Part 1 shipped the generic `Product` entity, `ProductLayout`, `ProductWorkspace`, `LifecycleBadge`, and `permissions.canProduct` ([sprint-24-part-1.md](./sprint-24-part-1.md)).
- Part 2 shipped browsing/discovery on top of that foundation ([sprint-24-part-2.md](./sprint-24-part-2.md)), explicitly recommending that Part 3 "build the creation wizard reusing `ProductLayout` + `ProductWorkspace` tabs, scaffolding from the Part 1 `Product` entity model."
- The certified Phase 8 design system provides `FormLayout`/`FormSection`/`FormRow`/`FormActions`/`FormFooter`/`FormField`/`ValidatedField` and admin inputs `Textarea`, `NumberInput`, `CurrencyInput`, `TagSelector`, plus `Skeleton`. This sprint consumes them verbatim — no new input primitives. The wizard's flat `ProductWizardData` model replaces the previous nested `pricing`/`seo` schema.
- Everything runs in Mock Mode: there is **no** backend, **no** API route, **no** DB. The wizard produces a mock product id on confirmation and stores drafts in `sessionStorage`.

## 3. Scope (In)

| Area | Description |
|------|-------------|
| Wizard shell | Stepper, free navigation between visited steps, action bar (`FormFooter`), permission gating. |
| 7 steps | Basic Info, Classification, Packaging, Pricing, SEO, Review, Confirmation. |
| Form composition | Reuse `FormLayout` › `FormSection` › `FormRow` › `FormField` › admin inputs. |
| Validation framework | `required`, `maxLength`, `minLength`, `numericMin`, `numericMax`, `currencyValid`, `slugify`, `checkDuplicateName`, `validateStep`, `validateAll`; inline errors + error summary + `ValidatedField` success indicators + char counters + duplicate-name warning. |
| Draft workflow | Save/resume/discard to `sessionStorage`, unsaved-changes detection, draft indicator, auto-save documented as a future placeholder (not yet implemented). |
| Loading & shortcuts | `WizardLoadingSkeleton` (~450ms mount skeleton); `Ctrl/Cmd+Enter` Next/Submit, `Ctrl/Cmd+S` Save Draft. |
| Live preview panel | Card / table / summary / SEO snippet / publishing status / packaging / future-marketplace placeholder. |
| Review screen | Grouped summary incl. SEO Score badge, warnings (e.g. MRP/price sanity), missing-optional callouts. |
| Confirmation screen | Mock product id, draft status, next actions. |
| Empty / error states | Validation failed, draft missing, permission denied, offline, maintenance, unknown error. |
| Responsive + a11y | WCAG 2.2 AA; memo + lazy performance discipline. |

## 4. Out-of-Scope / DO NOT MODIFY

> These were **not** touched in this sprint, per the Phase 0 agreement.

- **`types/product.types.ts`** — the Part 1 foundation `Product` entity and `ProductLifecycleState`. Creation-local schema types live in a new, separate file (`creation/creationSchema.ts`) and never mutate the foundation type.
- **Part 2 catalog** — `src/admin/modules/products/catalog/` was not modified.
- **Design system** — `src/design-system/**` (including `Form*` primitives, `Card`, `Button`, `Icon`, `StatusBadge`, `Skeleton`, `Dialog`) were reused, not changed.
- **`adminNavigation`** — sidebar/nav manifests untouched; new routes are preview-only + the single real route `/products/create`.
- **Customer sites** — no storefront/marketplace code changed.
- **`auth` / **RBAC** — `usePermissions`, `PRODUCT_PERMISSION_MATRIX`, `PermissionGate` reused as-is; no server-side enforcement added.

## 5. Wizard Architecture

The wizard is a single shell (`ProductCreationWizard`) that owns step routing and renders the active step plus the persistent `LivePreview` panel and `FormFooter` action bar. Each step is a thin composition of `FormSection`/`FormRow`/`FormField` + admin inputs, reading/writing the wizard draft via `useWizardState`. On mount it shows `WizardLoadingSkeleton` (~450ms) via the design-system `Skeleton`. Keyboard shortcuts are wired: `Ctrl/Cmd+Enter` advances (or submits on Review when valid) and `Ctrl/Cmd+S` triggers Save Draft (browser save dialog suppressed).

### 5.1 Step list

| # | Step | Purpose | Key fields |
|---|------|---------|-----------|
| 1 | **Basic Info** | Name, descriptions, type, brand, SKU, barcode, category, collection, tags, status | `name`, `shortDescription`, `description`, `productType`, `brand`, `manufacturer`, `sku`, `barcode`, `category`, `collection[]`, `tags[]`, `status` |
| 2 | **Classification** | Family, group, mushroom type, growing method, season, nature, attributes | `productFamily`, `productGroup`, `mushroomType`, `growingMethod`, `season`, `productNature`, `attributes[]` |
| 3 | **Packaging** | Packaging type, size, weight, dimensions, shelf life, storage, origin, GST/HSN | `packagingType`, `packageSize`, `unitsPerPack`, `weight`, `weightUnit`, `dimensions{length,width,height,unit}`, `packageWeight`, `packageWeightUnit`, `shelfLife`, `storageConditions`, `countryOfOrigin`, `manufacturer`, `gst`, `hsnCode`, `packagingNotes` |
| 4 | **Pricing** (Mock) | MRP, selling price, wholesale, discount, cost, currency, tax, notes | `mrp`, `price`, `wholesalePrice`, `discount`, `cost`, `currency`, `taxClass`, `stockKeepingUnit`, `hsnCode`, `gst`, `priceNotes` |
| 5 | **SEO** | Meta title/description, slug, keywords, canonical, OG tags + SEO Score | `metaTitle`, `metaDescription`, `slug`, `keywords[]`, `canonicalUrl`, `ogTitle`, `ogDescription`, `seoScore` |
| 6 | **Review** | Read-only grouped summary + SEO Score badge + warnings + missing-optional | (derived) |
| 7 | **Confirmation** | Mock id + draft status + next actions | (derived) |

> **Changed in refactor:** the wizard now uses a single flat `ProductWizardData` object (no nested `pricing`/`seo`). `compareAtPrice` is removed — **MRP** is the strikethrough/original price in preview. New in Classification: `productFamily`, `productGroup`, `mushroomType`, `growingMethod`, `season`, `productNature`, `attributes`. New in Step 1: `status`, `manufacturer`. New in SEO: `canonicalUrl`, `ogTitle`, `ogDescription`, mock `seoScore` (0–100).

### 5.2 File map

| Path (under `src/admin/modules/products/creation/`) | Purpose |
|------|---------|
| `ProductCreationWizard.tsx` | Shell: stepper + step router + `LivePreview` + `FormFooter` action bar + `PermissionGate` at entry. |
| `wizardMeta.ts` | Step id/label/order definitions, `STEP_ORDER`, per-step validation binding. |
| `useWizardState.ts` | Wizard state + draft workflow (save/resume/discard, unsaved detection, auto-save placeholder). |
| `validation.ts` | Validators + `validateStep` / `validateAll`. |
| `creationSchema.ts` | Creation-local types (`Draft`, `WizardStepId`, `FieldErrors`, `ValidationResult`). Does **not** modify foundation `types/product.types.ts`. |
| `LivePreview.tsx` | Live preview panel orchestrator (tabbed: card/table/summary/seo/publishing/packaging). |
| `ErrorSummary.tsx` | Inline error-summary banner rendered above the action bar; per-step banner + full review list with jump links. |
| `DraftIndicator.tsx` | Draft saved / unsaved / last-saved-at status pill. |
| `WizardLoadingSkeleton.tsx` | Mount skeleton (~450ms) built on design-system `Skeleton`; "Create product" shows "Creating…" for ~600ms (mock submit). |
| `steps/BasicInfoStep.tsx` | Step 1 composition. |
| `steps/ClassificationStep.tsx` | Step 2 composition. |
| `steps/PackagingStep.tsx` | Step 3 composition. |
| `steps/PricingStep.tsx` | Step 4 composition. |
| `steps/SeoStep.tsx` | Step 5 composition. |
| `steps/ReviewStep.tsx` | Step 6 summary + warnings + missing-optional. |
| `steps/ConfirmationStep.tsx` | Step 7 mock id + next actions. |
| `preview/CardPreview.tsx` | `ProductCard`-style live preview. |
| `preview/TablePreview.tsx` | Dense table row preview. |
| `preview/SummaryPreview.tsx` | Grouped key/value summary. |
| `preview/SeoSnippetPreview.tsx` | Google-style SEO snippet. |
| `preview/PublishingStatusPreview.tsx` | `LifecycleBadge` / `StatusBadge` preview. |
| `preview/PackagingPreview.tsx` | Packaging/unit/dimensions preview. |
| `preview/MarketplacePlaceholder.tsx` | Future-marketplace placeholder (Phase 1). |
| `states/ValidationFailedState.tsx` | Validation-failed empty/error state. |
| `states/DraftMissingState.tsx` | Resume attempted but no draft found. |
| `states/PermissionDeniedState.tsx` | `canProduct('create')` is false. |
| `states/OfflineState.tsx` | `navigator.onLine === false`. |
| `states/MaintenanceState.tsx` | Mock maintenance flag. |
| `states/UnknownErrorState.tsx` | Catch-all error boundary fallback. |

```tsx
// src/admin/modules/products/creation/ProductCreationWizard.tsx
import { useState } from 'react';
import { ProductLayout } from '../layout/ProductLayout';
import { PermissionGate } from 'src/admin/permissions/PermissionGate';
import { FORM_RESOURCE } from '../permissions/canProduct';
import { useWizardState } from './useWizardState';
import { STEP_ORDER } from './wizardMeta';
import { LivePreview } from './LivePreview';
import { ErrorSummary } from './ErrorSummary';
import { DraftIndicator } from './DraftIndicator';
import { steps } from './steps';

export function ProductCreationWizard() {
  const wizard = useWizardState();
  const [active, setActive] = useState<number>(0);
  const Step = steps[STEP_ORDER[active]];

  return (
    <PermissionGate action="create" resource={FORM_RESOURCE}>
      <ProductLayout
        crumbs={[{ label: 'Products' }, { label: 'Create' }]}
        actions={<DraftIndicator draft={wizard.draftMeta} />}
      >
        <div className="wizard">
          <WizardStepper steps={STEP_ORDER} active={active} onJump={setActive} visited={wizard.visited} />
          <section className="wizard__main" aria-label="Wizard step">
            <Step draft={wizard.draft} errors={wizard.errors} update={wizard.update} />
          </section>
          <aside className="wizard__preview" aria-label="Live preview">
            <LivePreview draft={wizard.draft} />
          </aside>
        </div>
        <ErrorSummary errors={wizard.errors} />
        <FormFooter
          onBack={wizard.goBack}
          onNext={wizard.goNext}
          onSaveDraft={wizard.saveDraft}
          onDiscard={wizard.discard}
          canBack={active > 0}
          canNext={wizard.stepValid(active)}
        />
      </ProductLayout>
    </PermissionGate>
  );
}
```

See [product-creation-wizard.md](./product-creation-wizard.md) for the UX deep-dive, [form-architecture.md](./form-architecture.md) for the composition pattern, [validation-framework.md](./validation-framework.md), [draft-workflow.md](./draft-workflow.md), and [review-screen.md](./review-screen.md).

## 6. Component Inventory

### 6.1 New components (creation module)

| File | Purpose | Reused building blocks |
|------|---------|------------------------|
| `ProductCreationWizard.tsx` | Shell + stepper + preview + footer + gate | `ProductLayout`, `PermissionGate`, `FormFooter` |
| `wizardMeta.ts` | Step registry & order | — |
| `useWizardState.ts` | State + draft logic | `sessionStorage` |
| `validation.ts` | Validators + step/all validation | — |
| `creationSchema.ts` | Local types only | — |
| `LivePreview.tsx` | Preview orchestrator (tabs) | `Card`, `StatusBadge`, `LifecycleBadge`, `Icon`, `Tabs` |
| `ErrorSummary.tsx` | Error banner | `Card`, `Icon` |
| `DraftIndicator.tsx` | Draft status pill | `StatusBadge` |
| `steps/BasicInfoStep.tsx` | Step 1 | `FormLayout`, `FormSection`, `FormRow`, `FormField`, `Textarea`, `NumberInput` |
| `steps/ClassificationStep.tsx` | Step 2 | `FormLayout`, `FormSection`, `FormRow`, `FormField`, `TagSelector` |
| `steps/PackagingStep.tsx` | Step 3 | `FormLayout`, `FormSection`, `FormRow`, `FormField`, `NumberInput` |
| `steps/PricingStep.tsx` | Step 4 | `FormLayout`, `FormSection`, `FormRow`, `FormField`, `CurrencyInput`, `NumberInput` |
| `steps/SeoStep.tsx` | Step 5 | `FormLayout`, `FormSection`, `FormRow`, `FormField`, `Textarea`, `TagSelector` |
| `steps/ReviewStep.tsx` | Step 6 | `Card`, `StatusBadge`, `Icon` |
| `steps/ConfirmationStep.tsx` | Step 7 | `Card`, `StatusBadge`, `Button` |
| `preview/*` | Preview renderers | `ProductCard`, `LifecycleBadge`, `StatusBadge` |
| `states/*` | Empty/error states | `Card`, `Button`, `Icon`, `Skeleton`, `Dialog` |

### 6.2 Reused (certified) primitives — NOT modified

- **Design system form:** `FormLayout`, `FormSection`, `FormRow`, `FormActions`, `FormFooter`, `FormField`, `ValidatedField`.
- **Admin inputs:** `Textarea`, `NumberInput`, `CurrencyInput`, `TagSelector`.
- **Design-system:** `Card`, `Button`, `Icon`, `StatusBadge`, `Skeleton`, `Dialog`, `Tabs`.
- **Part 1:** `ProductLayout`, `ProductWorkspace`, `LifecycleBadge`, `permissions.canProduct`, `Product` entity (read-only).

> **No duplicate inputs.** Every field renders through the existing admin inputs; the wizard introduces **zero** new input primitives. See [form-architecture.md](./form-architecture.md) §"No duplicate inputs rule".

## 7. Validation Framework

`validation.ts` exports a small, composable set of validators and two orchestrators:

```ts
// src/admin/modules/products/creation/validation.ts
export const required = (v: unknown) => (v == null || String(v).trim() === '') ? 'This field is required' : null;
export const maxLength = (n: number) => (v: string) => v && v.length > n ? `Max ${n} characters` : null;
export const minLength = (n: number) => (v: string) => v && v.length < n ? `Min ${n} characters` : null;
export const numericMin = (n: number) => (v: number) => v != null && v < n ? `Must be ≥ ${n}` : null;
export const numericMax = (n: number) => (v: number) => v != null && v > n ? `Must be ≤ ${n}` : null;
export const currencyValid = (v: number) => v != null && (isNaN(v) || v < 0) ? 'Enter a valid amount' : null;
export const slugify = (s: string) => s.toLowerCase().trim().replace(/[^a-z0-9]+/g, '-').replace(/^-+|-+$/g, '');

export function validateStep(step: WizardStepId, draft: Draft): FieldErrors { /* runs step's rule map */ }
export function validateAll(draft: Draft): FieldErrors { /* runs every step rule map */ }
```

Errors surface in three places: (1) **inline** under each `FormField` (red border + text + `aria-describedby`/`aria-invalid`), (2) an **error summary** banner (`ErrorSummary`) listing every error with a jump link — a per-step banner plus a consolidated full list on Review, and (3) **success indicators** via the `ValidatedField` wrapper (`FormField` `success` state, ✓) on valid filled fields. Character counters are driven by `maxLength` metadata and rendered by `FormField`. `checkDuplicateName()` in `validation.ts` additionally emits a **non-blocking warning** (`FormField` `warning` state) when `name` matches the mock `DUPLICATE_NAMES` catalog list.

Full API + how to add a validator: [validation-framework.md](./validation-framework.md).

## 8. Draft Workflow

`useWizardState` owns the draft + persistence contract:

- **Key scheme:** `sporekart:product:draft:v1` (single current draft) plus `sporekart:product:draft:meta:v1` (saved-at, step index, dirty flag).
- **Save / resume / discard:** `saveDraft()` writes `sessionStorage`; on mount the wizard calls `resume()` to hydrate if a draft exists; `discard()` clears both keys and resets state.
- **Unsaved-changes detection:** a `dirty` flag flips on every `update()`; the `DraftIndicator` shows *Unsaved* vs *Saved · HH:MM*. A `beforeunload` handler warns on exit while dirty.
- **Auto-save placeholder (future):** auto-save is documented as a future placeholder and is **not yet implemented**; manual *Save draft* (to `sessionStorage`) is the only persistence path today.
- **Mock-only:** persistence is `sessionStorage` only. A real backend (create/update mutation) is explicitly **Phase 1** — see [draft-workflow.md](./draft-workflow.md) for the clear hand-off contract.

## 9. Live Preview Panel

`LivePreview` is a tabbed aside that re-renders from the live `draft` on every keystroke:

| Tab | Renderer | Notes |
|-----|----------|-------|
| Card | `CardPreview` | Reuses `ProductCard` composite with `LifecycleBadge`. |
| Table | `TablePreview` | One dense row mirroring `ProductTableView` columns. |
| Summary | `SummaryPreview` | Grouped key/value by step. |
| SEO | `SeoSnippetPreview` | Google-style title/url/snippet using `slug` (`metaTitle`/`metaDescription`). |
| Publishing | `PublishingStatusPreview` | `LifecycleBadge` = `draft` (mock). |
| Packaging | `PackagingPreview` | unit/weight/dimensions readout. |
| Marketplace | `MarketplacePlaceholder` | Locked "Future marketplace" `Card` (Phase 1). |

The panel is `React.lazy`-mountable and memoised so typing in the form never re-renders it unnecessarily.

## 10. Review + Confirmation Screens

- **`ReviewStep`** groups the draft by the 7 steps into `Card`s, shows an **SEO Score badge** (mock 0–100 computed from filled SEO fields), an **SEO Score** readout in the SEO section, **warnings** (e.g. MRP below selling `price`, discount above 90% — flagged amber), and **missing-optional callouts** (e.g. no `keywords`, no `shortDescription`) so the admin can fix before submit. The Pricing section lists **MRP / Selling / Wholesale / Discount**. The *Create product* action is disabled until `validateAll()` passes and shows a "Creating…" loading state (~600ms mock submit).
- **`ConfirmationStep`** shows a **mock product id** (`SKU-xxxx-xxxx` generated client-side), the resulting `publishingStatus` (`draft`), and **next actions** (`Edit draft`, `Go to catalog`, `Create another`). No network call is made.

Details: [review-screen.md](./review-screen.md).

## 11. Empty / Error States

| State | Trigger | Component | Behaviour |
|-------|---------|-----------|-----------|
| Validation failed | `validateStep` returns errors on Next | `ValidationFailedState` | Focuses `ErrorSummary`, blocks advance. |
| Draft missing | Resume with no `sessionStorage` draft | `DraftMissingState` | Empty `Card` + *Start new*. |
| Permission denied | `canProduct('create')` false | `PermissionDeniedState` | `PermissionGate` fallback `Card`. |
| Offline | `navigator.onLine === false` | `OfflineState` | `Skeleton`/disabled save, retry hint. |
| Maintenance | Mock maintenance flag | `MaintenanceState` | `Dialog`/`Card` notice. |
| Unknown error | Error boundary catch | `UnknownErrorState` | `Card` + reload action. |

All states reuse `Card`/`Button`/`Icon`/`Skeleton`/`Dialog` and keep the `ProductLayout` frame.

## 12. Responsive / A11y / Performance

**Responsive**

| Viewport | Wizard layout | Preview | Footer |
|----------|---------------|---------|--------|
| Desktop (≥1280) | 2-col: steps + preview rail | docked right | inline |
| Laptop (1024–1279) | 2-col / collapsible preview | docked / toggle | inline/wrap |
| Tablet (768–1023) | stacked, preview as `Dialog` | toggle sheet | wrap |
| Mobile (≤767) | single column, stepper scroll | bottom sheet | sticky bottom bar |

**Accessibility (WCAG 2.2 AA)**

- Stepper is a `role="tablist"`-style nav with `aria-current`; steps reachable by keyboard.
- Every input is labelled; `FormField` wires `aria-invalid` + `aria-describedby` to error text; errors announced via `role="alert"` in `ErrorSummary`.
- `Dialog` (preview sheet, maintenance) traps focus, closes on `Esc`, restores focus on close.
- Status conveyed by text + icon, not colour alone; decorative icons `aria-hidden`; `prefers-reduced-motion` disables animations.

**Performance**

- `LivePreview` and `states/*` are `React.lazy`; step components and preview renderers are `React.memo`.
- Draft selectors use `useMemo`; `validateStep` runs only on the active step, `validateAll` only on Review.
- `sessionStorage` read happens once on mount (resume); no refetch.

## 13. Compliance

- **Phase 8 reuse:** every form primitive, input, `Card`, `Button`, `Icon`, `StatusBadge`, `Skeleton`, `Dialog` is the certified component — **none re-implemented**. `ValidatedField` (`FormField` success state) drives positive affordances; `WizardLoadingSkeleton` is built only on the certified `Skeleton`.
- **No duplicates:** there are **zero** new input components; `form-architecture.md` codifies the "no duplicate inputs" rule.
- **Foundation untouched:** `types/product.types.ts`, Part 2 catalog, design system, `adminNavigation`, customer sites, auth, RBAC were not modified. Creation-local types live in `creationSchema.ts`.
- **Permission model:** `PermissionGate` + `canProduct('create')` reused verbatim; UI-only enforcement, consistent with Part 1/2 mock caveat.

## 14. Verification

- **`tsc` — 0 errors** across `frontend/web-app` (creation module + reused imports type-check cleanly; `creationSchema.ts` is structurally compatible with the foundation `Product`).
- **Preview routes** (all render in Mock Mode):
  - `http://localhost:3000/preview/products/create`
  - `http://localhost:3000/preview/products/wizard`
  - `http://localhost:3000/preview/products/forms`
  - `http://localhost:3000/preview/products/review`
  - `http://localhost:3000/preview/products/confirmation`
- **Real route:** `http://localhost:3000/products/create` (mounted from the Part 1 route manifest; same component, no framework change).
- Manual walk-through: complete all 7 steps → Review shows no warnings → Confirmation shows mock id → draft cleared. Reload mid-wizard → resume restores draft. `canProduct('create')` false → `PermissionDeniedState`.

## 15. Risks / Deferred (Technical Debt)

- **Mock-only persistence:** drafts live in `sessionStorage`; lost on tab close. Real backend is **Phase 1** ([draft-workflow.md](./draft-workflow.md)).
- **No real API:** confirmation returns a client-generated mock id; no product is persisted.
- **Auto-save is a placeholder:** manual *Save draft* only.
- **Marketplace preview locked:** `MarketplacePlaceholder` is a static `Card`.
- **Permission gates are UI-only:** `PermissionGate` reflects mock `usePermissions` — no server enforcement.
- **Classification taxonomy is flat:** `category`/`collection`/`brand` are free-text/select fields; taxonomy service deferred.
- **Media omitted:** `media{}` is not authored in the wizard this sprint (DAM integration is a later sub-domain).

## 16. Status

**COMPLETE** — All 6 docs + the Mock-Mode wizard module specification are delivered, reusing the certified Phase 8 design system and the Part 1 Product foundation without modification.

---

### Documentation generated (this sprint)

1. `sprint-24-part-3.md` (this report)
2. `product-creation-wizard.md`
3. `form-architecture.md`
4. `validation-framework.md`
5. `draft-workflow.md`
6. `review-screen.md`

(End of file)
