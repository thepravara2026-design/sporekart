# Form Architecture — Composition Pattern

> Companion to [sprint-24-part-3.md](./sprint-24-part-3.md) and [product-creation-wizard.md](./product-creation-wizard.md). Code: `src/admin/modules/products/creation/steps/**`. Mock Mode.

## 1. The composition hierarchy

Every editable wizard step is built from the **same** certified Phase 8 hierarchy — no bespoke layout code:

```
FormLayout
└── FormSection        (grouped block w/ heading + description)
    └── FormRow        (1–2 fields per row on desktop, stacked on mobile)
        └── FormField  (label + control + help + error + char-limit wrapper)
            └── <admin input>   ← Textarea | NumberInput | CurrencyInput | TagSelector
```

`ValidatedField` wraps `FormField` to auto-show the `success` (✓) state on valid filled fields; `FormField` also supports a `warning` state (used by `checkDuplicateName`).

| Component | Source | Responsibility |
|-----------|--------|----------------|
| `FormLayout` | `src/design-system/components/form/FormLayout.tsx` | Page-level form frame, spacing, scroll region. |
| `FormSection` | `.../form/FormSection.tsx` | Titled/described grouping of rows. |
| `FormRow` | `.../form/FormRow.tsx` | Horizontal pairing of `FormField`s; collapses on mobile. |
| `FormActions` | `.../form/FormActions.tsx` | Inline action cluster (used inside sections if needed). |
| `FormFooter` | `.../form/FormFooter.tsx` | Sticky bottom action bar (Back/Save/Next/Discard). |
| `FormField` | `.../form/FormField.tsx` | **The wrapper**: label, `aria-invalid`, error text, char counter, `success`/`warning` states. |
| `ValidatedField` | `.../form/ValidatedField.tsx` | `FormField` wrapper that auto-shows the `success` ✓ on valid filled fields. |
| `Textarea` | `src/admin/components/inputs/Textarea.tsx` | Multi-line text + char limit. |
| `NumberInput` | `.../inputs/NumberInput.tsx` | Numeric, min/max aware. |
| `CurrencyInput` | `.../inputs/CurrencyInput.tsx` | Localised currency amount. |
| `TagSelector` | `.../inputs/TagSelector.tsx` | Add/remove tag chips. |

## 2. The "no duplicate inputs" rule

> **Rule:** The creation module MUST NOT introduce any new input component. Every editable field renders through one of the certified admin inputs (`Textarea`, `NumberInput`, `CurrencyInput`, `TagSelector`) wrapped by `FormField` (typically via `ValidatedField`).

Rationale (Phase 8 compliance):
- Accessibility, theming, and validation wiring already exist in the certified inputs — duplicating them fractures a11y and design tokens.
- `FormField` is the single integration point that adds label/error/char-limit; inputs stay "dumb" controls.
- If a genuinely new control type is needed (e.g. a date picker), it must be added to the **design system**, not to the creation module.

## 3. Correct usage (TSX)

```tsx
// src/admin/modules/products/creation/steps/BasicInfoStep.tsx
import { FormLayout } from 'src/design-system/components/form/FormLayout';
import { FormSection } from 'src/design-system/components/form/FormSection';
import { FormRow } from 'src/design-system/components/form/FormRow';
import { FormField } from 'src/design-system/components/form/FormField';
import { Textarea } from 'src/admin/components/inputs/Textarea';
import { NumberInput } from 'src/admin/components/inputs/NumberInput';
import type { Draft, FieldErrors } from '../creationSchema';

export function BasicInfoStep({ draft, errors, update }: {
  draft: Draft; errors: FieldErrors; update: (patch: Partial<Draft>) => void;
}) {
  return (
    <FormLayout>
      <FormSection title="Identity" description="Core product identity fields.">
        <FormRow>
          <FormField
            label="Product name"
            required
            error={errors.name}
            charLimit={120}
            value={draft.name}
          >
            <Textarea
              value={draft.name}
              maxLength={120}
              invalid={!!errors.name}
              onChange={(v) => update({ name: v })}
              aria-label="Product name"
            />
          </FormField>

          <FormField label="SKU" required error={errors.sku} value={draft.sku}>
            <NumberInput
              value={draft.sku}
              invalid={!!errors.sku}
              onChange={(v) => update({ sku: v })}
              aria-label="SKU"
            />
          </FormField>
        </FormRow>

        <FormRow>
          <FormField label="Short description" error={errors.shortDescription}
                     charLimit={200} value={draft.shortDescription}>
            <Textarea
              value={draft.shortDescription}
              maxLength={200}
              onChange={(v) => update({ shortDescription: v })}
              aria-label="Short description"
            />
          </FormField>
        </FormRow>
      </FormSection>
    </FormLayout>
  );
}
```

Notes:
- `FormField` owns `label`, `required`, `error`, `charLimit`, and the success icon. Never put those on the input directly.
- `error` is supplied by `validateStep` (see [validation-framework.md](./validation-framework.md)); `FormField` sets `aria-invalid` and `aria-describedby` automatically.
- `charLimit` drives the live counter rendered by `FormField`; keep it in sync with the input's `maxLength`.

## 4. Classification / Packaging / Pricing / SEO usage

The same pattern repeats; only the input mix changes:

| Step | Inputs used |
|------|-------------|
| Basic Info | `Textarea` (shortDescription, description, name as long), `NumberInput` (sku, barcode), `TagSelector` (collection, tags), `Textarea`/`NumberInput` (category, brand, manufacturer) |
| Classification | `Textarea`/`NumberInput` (productFamily, productGroup, mushroomType, growingMethod, season, productNature), `TagSelector` (`attributes`) |
| Packaging | `NumberInput` (packageSize, unitsPerPack, weight, dimensions, packageWeight), `Textarea` (storageConditions, packagingNotes), `CurrencyInput` (gst), `FormField` grouping |
| Pricing | `CurrencyInput` (mrp, price, wholesalePrice, cost), `NumberInput` (discount %), `Textarea` (priceNotes) |
| SEO | `Textarea` (metaTitle, metaDescription, ogTitle, ogDescription, canonicalUrl), `TagSelector` (keywords), `FormField` showing derived `slug` |

```tsx
// src/admin/modules/products/creation/steps/PricingStep.tsx (excerpt)
<FormRow>
  <FormField label="MRP" required error={errors.mrp} value={draft.mrp}>
    <CurrencyInput value={draft.mrp} invalid={!!errors.mrp}
                   onChange={(v) => update({ mrp: v })} />
  </FormField>
  <FormField label="Selling price" required error={errors.price}
             value={draft.price}>
    <CurrencyInput value={draft.price} invalid={!!errors.price}
                   onChange={(v) => update({ price: v })} />
  </FormField>
  <FormField label="Wholesale price" error={errors.wholesalePrice} value={draft.wholesalePrice}>
    <CurrencyInput value={draft.wholesalePrice} invalid={!!errors.wholesalePrice}
                   onChange={(v) => update({ wholesalePrice: v })} />
  </FormField>
</FormRow>
```

## 5. How to add a NEW field

1. Add the field to the `Draft` type in `creationSchema.ts` (do **not** touch foundation `types/product.types.ts`).
2. Add a `FormField` + certified input inside the relevant `FormSection`/`FormRow`.
3. Add a validator binding in `validation.ts` (see [validation-framework.md](./validation-framework.md) §"Add a validator").
4. Surface it in `LivePreview` / `ReviewStep` if it should be visible there.

No new primitive, no layout component, no design-system change.

## 6. How to add a NEW step

1. Append the step id to `STEP_ORDER` in `wizardMeta.ts` and add its `STEP_META` entry.
2. Create `steps/<Name>Step.tsx` following the composition pattern above.
3. Register it in the `steps` map consumed by `ProductCreationWizard.tsx`.
4. Add its `validateStep` binding (or leave it validation-free for a pure-summary step).
5. The stepper, footer, preview, and draft logic pick it up automatically — no other changes.

(End of file)
