# Quick Preview

> Quick-preview doc for [Sprint 24 Part 2](./sprint-24-part-2.md). Code: `src/admin/modules/products/catalog`. Mock Mode.

## Overview

`ProductQuickPreview` is a non-blocking, **read-only** drawer that opens from a table row click or the
row eye action. It reuses the design-system `Dialog` (`src/design-system/components/feedback/Dialog.tsx`)
configured as a side drawer. There is **no edit** affordance — creation/editing is Sprint 24 Part 3.

## Trigger

```tsx
// src/admin/modules/products/catalog/ProductTableView.tsx
<tr onClick={() => onPreview(p)} role="row" tabIndex={0}
    onKeyDown={(e) => e.key === 'Enter' && onPreview(p)}>
  <td><button aria-label={`Quick preview ${p.name}`} onClick={(e) => { e.stopPropagation(); onPreview(p); }}>
    <Icon name="eye" aria-hidden />
  </button></td>
</tr>
```

Opening sets `useCatalogState.preview = product`; closing clears it.

## Contents

| Section | Source | Notes |
|---------|--------|-------|
| Gallery | `media.images[]`, `thumbnail` | `Skeleton` while "loading" (mock) |
| Basic info | `name`, `sku`, `barcode`, `shortDescription` | |
| Pricing | `pricing.{mrp,sellingPrice,wholesalePrice,discount}` | formatted via `--text-*` tokens |
| Category / Brand / Tags | `category`, `brand`, `tags[]` | `StatusBadge`/`Chip` styled |
| Publishing status | `publishingStatus` | Part 1 `LifecycleBadge` |
| Dates | `createdAt`, `updatedAt` | ISO → localized |
| Inventory | **placeholder** | deferred sub-domain (stock shown as mock flag) |
| Orders | **placeholder** | future sub-domain |
| Analytics | **placeholder** | `analyticsRef` stub |

```tsx
// src/admin/modules/products/catalog/ProductQuickPreview.tsx
import { Dialog } from 'src/design-system/components/feedback/Dialog';
import { LifecycleBadge } from '../lifecycle/LifecycleBadge';
import { StatusBadge } from 'src/admin/components/status/StatusBadge';
import { Card } from 'src/design-system/components/composite/Card';

<Dialog role="dialog" aria-modal="true" aria-labelledby="qp-title" onClose={onClose}>
  <header>
    <h2 id="qp-title">{product.name}</h2>
    <LifecycleBadge status={product.publishingStatus} />
  </header>
  <Card>/* gallery + pricing + dates */</Card>
  <Card>/* inventory / orders / analytics placeholders */</Card>
</Dialog>;
```

## Accessibility (WCAG 2.2 AA)

- `role="dialog"`, `aria-modal="true"`, `aria-labelledby` pointing at the title.
- **Escape** and **overlay click** close the drawer (handled by `Dialog`).
- **Focus management:** focus moves to the close button on open; on close, focus returns to the triggering row/action.
- Focus is **trapped** within the drawer while open.
- Decorative media uses `alt`/aria where meaningful; status conveyed by text not colour alone.

## Mock-Only

- The drawer reads from the in-memory `CatalogProduct`; no API call.
- Inventory/orders/analytics sections are explicit placeholders because those sub-domains are Part 1 stubs.
- No save/delete buttons — editing belongs to the Part 3 creation wizard.

Related: [product-catalog.md](./product-catalog.md), [sprint-24-part-2.md](./sprint-24-part-2.md).
