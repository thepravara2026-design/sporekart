# Review & Confirmation Screens

> Companion to [sprint-24-part-3.md](./sprint-24-part-3.md) and [validation-framework.md](./validation-framework.md). Code: `src/admin/modules/products/creation/steps/ReviewStep.tsx`, `ConfirmationStep.tsx`. Mock Mode.

## 1. ReviewStep (Step 6)

`ReviewStep` is a **read-only** audit of the entire `draft`, grouped by the 7 wizard steps into `Card`s. It reuses `Card`, `StatusBadge`, and `Icon` (no new primitives). It is the gate before submission: the **Create product** action in `FormFooter` is enabled only when `validateAll(draft)` returns no errors.

### 1.1 Summary grouping

Each `Card` corresponds to a step and lists its fields as key/value rows rendered from `draft`:

| Card | Fields shown |
|------|--------------|
| Basic Info | name, shortDescription, description (truncated), productType, brand, manufacturer, sku, barcode, category, collection (chips), tags (chips), status |
| Classification | productFamily, productGroup, mushroomType, growingMethod, season, productNature, attributes (chips) |
| Packaging | packagingType, packageSize, unitsPerPack, weight, weightUnit, dimensions, packageWeight, packageWeightUnit, shelfLife, storageConditions, countryOfOrigin, gst, hsnCode, packagingNotes |
| Pricing | mrp (strikethrough/original), price (selling, required), wholesalePrice, discount (%), cost, currency, taxClass, stockKeepingUnit, hsnCode, gst, priceNotes |
| SEO | metaTitle, metaDescription, slug (slugified), keywords (chips), canonicalUrl, ogTitle, ogDescription, **SEO Score badge (mock 0–100)** |
| Publishing | `LifecycleBadge` = `draft` (mock) |
| Packaging preview | `PackagingPreview` mini-readout |

Each row is clickable and jumps back to the originating step (`onJump(stepIndex)`).

### 1.2 Warnings (blocking? no — advisory, amber)

Warnings are **non-blocking** cross-field checks surfaced as amber `StatusBadge`/`Icon` callouts. They do not stop submission but draw attention. Examples:

```ts
// src/admin/modules/products/creation/steps/ReviewStep.tsx (excerpt)
const warnings: string[] = [];
if (draft.mrp != null && draft.price != null && draft.mrp < draft.price)
  warnings.push('MRP is below the selling price — the strikethrough preview will look inverted.');
if (draft.discount != null && draft.discount > 90)
  warnings.push('Discount above 90% — confirm this is intended.');
if (!draft.slug)
  warnings.push('No SEO slug — one will be auto-generated from the product name on publish.');
if ((draft.seoScore ?? 0) < 50)
  warnings.push('SEO Score is low — consider filling more SEO fields.');
```

Warnings render in a `Card` with `variant="warning"` at the top of the step, each with an `Icon name="alert-triangle"` and `aria-label`.

### 1.3 Missing-optional callouts

A separate `Card` lists **optional** fields the admin left blank, so nothing is forgotten:

```ts
const missingOptional: string[] = [];
if (!draft.shortDescription) missingOptional.push('Short description');
if (!draft.description)      missingOptional.push('Long description');
if (draft.tags.length === 0) missingOptional.push('Tags');
if (draft.keywords.length === 0) missingOptional.push('SEO keywords');
if (!draft.metaDescription)  missingOptional.push('Meta description');
```

Each item links back to its step. These are informational only — the product can still be "created" (mocked) without them.

## 2. ConfirmationStep (Step 7)

`ConfirmationStep` is the terminal receipt. It shows a **mock product id**, the resulting lifecycle state, and **next actions**. No network call is made.

```tsx
// src/admin/modules/products/creation/steps/ConfirmationStep.tsx (excerpt)
const mockId = `SKU-${draft.sku.slice(0,4).toUpperCase()}-${Math.random().toString(36).slice(2,6).toUpperCase()}`;

<Card>
  <StatusBadge status="draft" variant="neutral" />   {/* publishingStatus = draft */}
  <h2>Product created (draft)</h2>
  <p>Mock product id: <code>{mockId}</code></p>
  <p>Your draft is saved in this session only. Promotion to a published product is a later workflow.</p>

  <div className="confirmation__actions">
    <Button variant="outline" onClick={onEditDraft}>Edit draft</Button>
    <Button variant="secondary" onClick={onGoToCatalog}>Go to catalog</Button>
    <Button variant="primary" onClick={onCreateAnother}>Create another</Button>
  </div>
</Card>
```

| Element | Value | Notes |
|---------|-------|-------|
| Mock id | `SKU-XXXX-XXXX` | Client-generated; **not** persisted. Phase 1 replaces with server id (see [draft-workflow.md](./draft-workflow.md)). |
| Draft status | `LifecycleBadge` = `draft` | Matches `product-lifecycle.md` initial state. |
| Next actions | Edit draft / Go to catalog / Create another | `Create another` resets `useWizardState` (`discard` + fresh mount). |

> The confirmation explicitly states the product is a **session draft** — reinforcing Mock Mode and the absence of a backend. The future Marketplace publish lives in `preview/MarketplacePlaceholder.tsx` (Phase 1).

## 3. Cross-references

- Validation gate: [validation-framework.md](./validation-framework.md)
- Draft lifecycle: [draft-workflow.md](./draft-workflow.md)
- Lifecycle states: [product-lifecycle.md](./product-lifecycle.md)
- Wizard UX: [product-creation-wizard.md](./product-creation-wizard.md)

(End of file)
