# Validation Framework

> Companion to [sprint-24-part-3.md](./sprint-24-part-3.md) and [form-architecture.md](./form-architecture.md). Code: `src/admin/modules/products/creation/validation.ts`. Mock Mode.

## 1. Design principles

- **Composable validators:** each validator is a pure function `(value) => string | null` — returns an error message or `null` when valid.
- **Declarative step rules:** each step maps field → array of validators, so adding/changing rules never touches UI.
- **Two orchestrators:** `validateStep(step, draft)` for inline + Next-gating; `validateAll(draft)` for the Review gate.
- **No side effects:** validators are synchronous and pure; they read the `Draft`, never mutate it.

## 2. API

```ts
// src/admin/modules/products/creation/validation.ts
import type { Draft, WizardStepId, FieldErrors } from './creationSchema';

// --- Primitive validators (each: (value) => errorMsg | null) ---
export const required: (v: unknown) => string | null;
export const maxLength: (n: number) => (v: string) => string | null;
export const minLength: (n: number) => (v: string) => string | null;
export const numericMin: (n: number) => (v: number) => string | null;
export const numericMax: (n: number) => (v: number) => string | null;
export const currencyValid: (v: number) => string | null;

// --- Helpers ---
export const slugify: (s: string) => string;

// --- Orchestrators ---
export function validateStep(step: WizardStepId, draft: Draft): FieldErrors;
export function validateAll(draft: Draft): FieldErrors;
```

| Validator | Semantics | Example |
|-----------|-----------|---------|
| `required` | Empty/whitespace fails | `required(draft.name)` |
| `maxLength(n)` | String longer than `n` fails | `maxLength(120)` |
| `minLength(n)` | String shorter than `n` fails | `minLength(10)` |
| `numericMin(n)` | Number below `n` fails | `numericMin(0)` |
| `numericMax(n)` | Number above `n` fails | `numericMax(999999)` |
| `currencyValid` | `NaN` or negative fails | `currencyValid(draft.mrp)` |
| `slugify` | Lowercases, strips, hyphenates | `slugify(draft.metaTitle)` |

`FieldErrors` is a flat `Record<string, string>` keyed by field name (e.g. `'mrp'`, `'name'`, `'metaTitle'`). This keys directly into `FormField.error` (see [form-architecture.md](./form-architecture.md)).

`checkDuplicateName(name)` is a **non-blocking** helper: it returns a warning string when `name` matches the mock `DUPLICATE_NAMES` catalog list, surfaced via `FormField` `warning` (not via `FieldErrors`).

## 3. Step rule map

```ts
// src/admin/modules/products/creation/validation.ts (excerpt)
const STEP_RULES: Record<WizardStepId, Record<string, ((d: Draft) => string | null)[]>> = {
  basic: {
    name:             [required, maxLength(120)],
    sku:              [required, maxLength(64)],
    shortDescription: [maxLength(200)],
    description:      [maxLength(2000)],
    productType:      [required],
    category:         [required],
    status:           [required],
    tags:             [(d) => (d.tags.length === 0 ? 'Add at least one tag' : null)],
  },
  classification: {
    productFamily:    [required],
    productNature:    [required],
    mushroomType:     [maxLength(64)],
    growingMethod:    [maxLength(64)],
    season:           [(d) => (['spring','summer','monsoon','autumn','winter','all'].includes(d.season) ? null : 'Select a valid season')],
    attributes:       [],   // generic extension point — optional
  },
  packaging: {
    weight:        [(d) => numericMin(0)(d.weight)],
    packageWeight: [(d) => (d.packageWeight == null ? null : numericMin(0)(d.packageWeight))],
    gst:           [(d) => (d.gst == null ? null : currencyValid(d.gst))],
  },
  pricing: {
    mrp:      [required, currencyValid, numericMin(0)],
    price:    [required, currencyValid, numericMin(0)],
    wholesalePrice: [(d) => (d.wholesalePrice == null ? null : currencyValid(d.wholesalePrice))],
    cost:     [(d) => (d.cost == null ? null : currencyValid(d.cost))],
    discount: [(d) => (d.discount == null ? null : numericMin(0)(d.discount))],
  },
  seo: {
    metaTitle:       [maxLength(70)],
    metaDescription:  [maxLength(160)],
    slug:            [(d) => (d.slug && !/^[a-z0-9-]+$/.test(d.slug) ? 'Lowercase letters, numbers, hyphens only' : null)],
    keywords:        [(d) => (d.keywords.length === 0 ? 'Add at least one keyword' : null)],
    // seoScore is a computed placeholder (0–100) — not validated.
  },
  review: {},        // validated via validateAll
  confirmation: {},
};

export function validateStep(step: WizardStepId, draft: Draft): FieldErrors {
  const rules = STEP_RULES[step] ?? {};
  const errors: FieldErrors = {};
  for (const [field, fns] of Object.entries(rules)) {
    for (const fn of fns) {
      const msg = fn(draft);
      if (msg) { errors[field] = msg; break; }
    }
  }
  return errors;
}

export function validateAll(draft: Draft): FieldErrors {
  return (Object.keys(STEP_RULES) as WizardStepId[])
    .reduce((acc, step) => ({ ...acc, ...validateStep(step, draft) }), {} as FieldErrors);
}
```

## 4. How errors surface

1. **Inline** — `useWizardState.errors` (the result of `validateStep(active)`) is passed to the active step; each `FormField` receives `error={errors[field]}`, sets `aria-invalid`, `aria-describedby`, and renders red text + red border.
2. **Error summary** — `ErrorSummary.tsx` renders a `role="alert"` `Card` listing every error with a jump link to the offending `FormField` (focus moved on click). Shown above `FormFooter` whenever `errors` is non-empty.
3. **Success indicators** — `ValidatedField` (a `FormField` wrapper) auto-shows the `success` (✓) state when a field has no error and is non-empty, giving positive affordance. `checkDuplicateName` warnings use `FormField`'s `warning` state (amber, non-blocking).
4. **Char counters** — `maxLength` metadata drives the `FormField` counter (`12 / 120`); the input's `maxLength` enforces the hard cap.

```tsx
// src/admin/modules/products/creation/ErrorSummary.tsx (excerpt)
{Object.entries(errors).map(([field, msg]) => (
  <li key={field}>
    <a href={`#field-${field}`} onClick={() => focusField(field)}>{msg}</a>
  </li>
))}
```

## 5. How to add a NEW validator

1. Add a pure function to `validation.ts` returning `string | null`.
2. Optionally add a `STEP_RULES` entry (or extend an existing field's array).
3. Surface the field error via `FormField.error` (already wired by the step component).
4. If it is a cross-field rule (e.g. MRP below the selling `price`), add it as a function that reads `draft` directly in `STEP_RULES.pricing`:

```ts
mrp: [
  required, currencyValid, numericMin(0),
  (d) => (d.mrp != null && d.price != null && d.mrp < d.price
    ? 'MRP cannot be below the selling price' : null),
],
```

> Cross-field warnings (non-blocking, amber) are emitted by `ReviewStep`, not the blocking `validateStep` — see [review-screen.md](./review-screen.md). `checkDuplicateName` is another non-blocking warning helper surfaced via `FormField` `warning`.

## 6. Enterprise Validation Framework

> Companion to [sprint-24-part-10.md](./sprint-24-part-10.md). Mock Mode.

The Enterprise Validation Framework extends the basic form-level validation (sections 1–5 above) with a full workspace for product-level validation, quality assurance, compliance, certification, and publishing readiness.

### ValidationPage Workspace

The `ValidationPage` provides a 16-section workspace with sidebar navigation, toolbar, and content area. Each section addresses a specific validation domain:

| Section | Focus | Component |
|---------|-------|-----------|
| 1 | Dashboard | `ValidationDashboard` |
| 2 | Product Health | `ProductHealthDashboard` |
| 3 | Quality Assurance | `QualityAssuranceEngine` |
| 4 | Compliance | `ComplianceFramework` |
| 5 | Certification | `CertificationDashboard` |
| 6 | Marketplace Readiness | `MarketplaceReadiness` |
| 7 | Publishing Readiness | `PublishingReadiness` |
| 8 | Field Completeness | `FieldCompleteness` |
| 9 | Data Consistency | `DataConsistency` |
| 10 | Image Validation | `ImageValidator` |
| 11 | Video Validation | `VideoValidator` |
| 12 | Spec Validation | `SpecValidator` |
| 13 | Variant Validation | `VariantValidator` |
| 14 | Docs Validation | `DocsValidator` |
| 15 | Approval Queue | `ApprovalQueue` |
| 16 | Audit Trail | `ValidationAuditTrail` |

```ts
// Navigation config drives sidebar rendering
const VALIDATION_NAV: NavSection[] = [
  { id: 'dashboard',          label: 'Dashboard',             icon: 'LayoutDashboard' },
  { id: 'health',             label: 'Product Health',        icon: 'HeartPulse' },
  { id: 'qa',                 label: 'Quality Assurance',     icon: 'ShieldCheck' },
  { id: 'compliance',         label: 'Compliance',            icon: 'Scale' },
  { id: 'certification',      label: 'Certification',         icon: 'BadgeCheck' },
  { id: 'marketplace',        label: 'Marketplace Readiness', icon: 'Store' },
  { id: 'publishing',         label: 'Publishing Readiness',  icon: 'Send' },
  { id: 'completeness',       label: 'Field Completeness',    icon: 'CheckSquare' },
  { id: 'consistency',        label: 'Data Consistency',      icon: 'GitCompare' },
  { id: 'images',             label: 'Image Validation',      icon: 'Image' },
  { id: 'videos',             label: 'Video Validation',      icon: 'Video' },
  { id: 'specs',              label: 'Spec Validation',       icon: 'ListChecks' },
  { id: 'variants',           label: 'Variant Validation',    icon: 'Layers' },
  { id: 'docs',               label: 'Docs Validation',       icon: 'FileText' },
  { id: 'approvals',          label: 'Approval Queue',        icon: 'ClipboardCheck' },
  { id: 'audit',              label: 'Audit Trail',           icon: 'History' },
];
```

### Product Health Dashboard

8 health categories visualized as score rings:

| Category | What It Measures |
|----------|-----------------|
| Field Completeness | % of required fields populated |
| Data Consistency | Cross-field logical consistency |
| Image Quality | Image presence, resolution, aspect ratio |
| Video Quality | Video presence, duration checks |
| Specification Completeness | Spec fields populated vs required |
| Variant Consistency | Variant attribute uniformity |
| Compliance Score | Compliance check pass rate |
| Publishing Readiness | Readiness score (0–100) |

Each category renders a `ScoreRing` (SVG circle with percentage) and a brief status label. The dashboard aggregates scores across all selected products.

### Quality Assurance Engine

See [quality-assurance.md](./quality-assurance.md) for full documentation.

The QA Engine runs 12 checks per product:

1. Required fields check
2. Field length validation
3. Duplicate product detection
4. Invalid value detection
5. Broken reference detection
6. Formatting validation
7. Naming convention check
8. Image validation
9. Video validation
10. Specification validation
11. Variant validation
12. Document validation

Score = (passed / total) × 100, displayed via `ScoreRing`.

### Compliance Framework

See [compliance-framework.md](./compliance-framework.md) for full documentation.

14 compliance check categories covering regulatory, legal, safety, and agricultural compliance. Each category has a pass/fail state. Score displayed as `ScoreRing`. Future integration planned with FSSAI, BIS, and Agri Marketing APIs.

### Certification System

See [certification-framework.md](./certification-framework.md) for full documentation.

5 certification levels:

| Level | Emoji | Validity |
|-------|-------|----------|
| Bronze | 🥉 | 6 months |
| Silver | 🥈 | 12 months |
| Gold | 🥇 | 24 months |
| Enterprise | 💎 | 36 months |
| Marketplace Ready | 🏪 | 12 months |
| Export Ready | 🌐 | 24 months |

Badges are displayed via `BadgeDisplay` component with issuer, date, and expiry.

### Publishing Readiness

See [publishing-readiness.md](./publishing-readiness.md) for full documentation.

6 publishing readiness states:

| State | Description |
|-------|-------------|
| Ready | All checks pass, ready to publish |
| Needs Review | Minor issues found, review recommended |
| Blocked | Critical issues preventing publication |
| Incomplete | Required data missing |
| Compliance Failure | Compliance checks not passed |
| Awaiting Approval | Submitted for approval |

Readiness score (0–100) drives the state assignment with configurable thresholds.

### Permissions Integration

The validation framework respects the 5-tier role matrix:

- **Viewer**: Read-only access to dashboards and reports
- **Editor**: Can run QA, view compliance results
- **Reviewer**: Can approve compliance, certify products
- **Approver**: Can grant certifications, override blocks
- **Administrator**: Full access including rule management

### Mock Mode

All validation data is mock. No backend integration. No government API calls. No real publishing gate. All scores, checks, and certifications are computed from mock data sets.

(End of file)
