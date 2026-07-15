# Product Creation Wizard — UX Design

> Companion to [sprint-24-part-3.md](./sprint-24-part-3.md). Code: `src/admin/modules/products/creation/`. Mock Mode.

## 1. Overview

The Product Creation Wizard is a 7-step PIM authoring flow that lets an Enterprise admin scaffold a `Product` (per the Part 1 foundation entity — see [product-domain-architecture.md](./product-domain-architecture.md)) before it is ever submitted for publishing. It is intentionally **authoring-only**: no mutation to the catalogue occurs, and the resulting product exists only as a `sessionStorage` draft until confirmation (which yields a client-generated mock id).

The wizard is framed by the Part 1 `ProductLayout` ([product-layout.md](./product-layout.md)) and gated by `canProduct('create')` ([product-permissions.md](./product-permissions.md)), so it visually and permission-wise matches the rest of the Product module.

## 2. The 7-step flow

| # | Step | Authoring intent | Maps to `ProductWizardData` fields |
|---|------|------------------|--------------------------|
| 1 | **Basic Info** | Identity + copy | `name`, `shortDescription`, `description`, `productType`, `brand`, `manufacturer`, `sku`, `barcode`, `category`, `collection[]`, `tags[]`, `status` |
| 2 | **Classification** | Taxonomy + discoverability | `productFamily`, `productGroup`, `mushroomType`, `growingMethod`, `season`, `productNature`, `attributes[]` |
| 3 | **Packaging** | Fulfilment attributes | `packagingType`, `packageSize`, `unitsPerPack`, `weight`, `weightUnit`, `dimensions{length,width,height,unit}`, `packageWeight`, `packageWeightUnit`, `shelfLife`, `storageConditions`, `countryOfOrigin`, `manufacturer`, `gst`, `hsnCode`, `packagingNotes` |
| 4 | **Pricing** (Mock) | Commercial model | `mrp`, `price`, `wholesalePrice`, `discount`, `cost`, `currency`, `taxClass`, `stockKeepingUnit`, `hsnCode`, `gst`, `priceNotes` |
| 5 | **SEO** | Search presence | `metaTitle`, `metaDescription`, `slug`, `keywords[]`, `canonicalUrl`, `ogTitle`, `ogDescription`, `seoScore` |
| 6 | **Review** | Final read-only audit | (derived summary + SEO Score + warnings) |
| 7 | **Confirmation** | Mock creation receipt | (mock id + lifecycle `draft`) |

> **Changed in refactor:** the wizard now holds a flat `ProductWizardData` object. `compareAtPrice` is removed — **MRP** is the strikethrough/original price in preview. Basic Info gains `status`/`manufacturer`/`description` (long); Classification gains `productFamily`, `productGroup`, `mushroomType`, `growingMethod`, `season`, `productNature`, `attributes`; SEO gains `canonicalUrl`, `ogTitle`, `ogDescription` and a mock `seoScore` (0–100).

Steps 1–5 are editable forms. Step 6 is read-only and gates submission. Step 7 is terminal for the session.

## 3. Stepper & free navigation

The `WizardStepper` is a horizontal list of 7 nodes rendered with `Icon` + label. It supports **free navigation**:

- Steps already **visited** (tracked in `useWizardState.visited`) are clickable — an admin can jump back/forward without losing data.
- A step shows a **status dot**: `complete` (valid), `active`, `available` (visited, possibly invalid), `locked` (not yet visited).
- Clicking a forward, unvisited step is disabled until the current step passes `validateStep`.
- The stepper exposes `aria-current="step"` on the active node and is fully keyboard-operable (arrow keys + `Enter`).

```tsx
// src/admin/modules/products/creation/wizardMeta.ts
export const STEP_ORDER = [
  'basic', 'classification', 'packaging', 'pricing', 'seo', 'review', 'confirmation',
] as const;

export type WizardStepId = typeof STEP_ORDER[number];

export const STEP_META: Record<WizardStepId, { label: string; icon: string }> = {
  basic:         { label: 'Basic Info',    icon: 'info' },
  classification:{ label: 'Classification',icon: 'tag' },
  packaging:     { label: 'Packaging',     icon: 'box' },
  pricing:       { label: 'Pricing',       icon: 'rupee' },
  seo:           { label: 'SEO',           icon: 'search' },
  review:        { label: 'Review',        icon: 'check-circle' },
  confirmation:  { label: 'Confirmation',  icon: 'rocket' },
};
```

## 4. Action bar (FormFooter)

`FormFooter` (certified Phase 8 primitive) is pinned at the bottom of the wizard and renders a consistent action set:

- **Back** — `onBack`; disabled on step 1.
- **Save draft** — `onSaveDraft`; writes `sessionStorage` (see [draft-workflow.md](./draft-workflow.md)).
- **Next** — `onNext`; disabled unless `useWizardState.stepValid(active)` (i.e. `validateStep` for the current step is clean).
- **Discard** — `onDiscard`; clears the draft after a `Dialog` confirm.
- On **Review** the primary becomes **Create product** (disabled until `validateAll()` passes); on **Confirmation** it becomes **Create another**.

The footer is wrapped so it stays inline on desktop and collapses to a sticky bottom bar on mobile.

## 5. Permission gating

The entire wizard is wrapped in:

```tsx
<PermissionGate action="create" resource={PRODUCT_RESOURCE}>
  <ProductCreationWizard />
</PermissionGate>
```

If `canProduct('create')` resolves false, the gate renders `states/PermissionDeniedState.tsx` (a `Card` explaining the lack of permission) instead of the wizard. This keeps the gate logic in the existing `PermissionGate`/`PRODUCT_PERMISSION_MATRIX` — no new RBAC code (see [product-permissions.md](./product-permissions.md)).

## 6. Live preview during authoring

A `LivePreview` aside (right rail on desktop, bottom sheet on mobile) reflects the draft on every keystroke. Tabs: Card, Table, Summary, SEO, Publishing, Packaging, Marketplace (placeholder). The SEO tab renders a Google-style snippet from `slug` (`metaTitle`/`metaDescription`). The preview is purely derived — it never mutates the draft. See `sprint-24-part-3.md` §9.

## 6a. Loading & keyboard shortcuts (refactor)

- **Loading:** on mount the wizard renders `WizardLoadingSkeleton` (~450ms) built on the certified `Skeleton`; the *Create product* action shows a "Creating…" button-loading state for ~600ms (mock submit).
- **Keyboard shortcuts:** `Ctrl/Cmd + Enter` advances to the next step (or submits on Review when valid); `Ctrl/Cmd + S` triggers *Save draft* and suppresses the browser save dialog.

## 7. Extending with real persistence (Future / Phase 1)

> The following is **not** implemented in this sprint. It is the documented hand-off contract for Phase 1.

Today `useWizardState` persists to `sessionStorage` and `ConfirmationStep` fabricates a mock id. To promote the wizard to a real PIM create flow:

1. **Replace the persistence adapter.** Introduce `creation/api.ts` with `saveDraft(product)`, `loadDraft(id)`, `discardDraft(id)`, `createProduct(draft)` returning a server id + `publishingStatus`. Keep the `useWizardState` public surface (`draft`, `update`, `saveDraft`, `resume`, `discard`) identical so no step component changes.
2. **Swap mock id for server id.** In `ConfirmationStep`, read the id from the create mutation result instead of the client generator.
3. **Add server-side validation.** Mirror `validateStep`/`validateAll` server-side; the client framework remains the fast path.
4. **Wire auto-save.** Replace the `AutoSaveStub` placeholder with a debounced `saveDraft` call.
5. **Promote the Marketplace placeholder** to a real publish target once the marketplace sub-domain lands.

Because the wizard only depends on `useWizardState` and the certified form primitives, none of the 7 step components or the validation framework need to change when the backend arrives.

(End of file)
