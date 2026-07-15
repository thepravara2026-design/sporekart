# Comparison View

## Purpose
Side-by-side comparison of the current working product against any historical mock version, with highlighted changes.

## Component — `CompareView.tsx`
Props: `{ current: ProductWizardData; compare: ProductWizardData; compareLabel: string }`.

### Features
- **Accessible table**: real `<table>` with `<caption>`, `scope="col"`/"`row"` headers, and section-group header rows.
- **Section grouping**: Basic Info, Classification, Packaging & Physical, Pricing, SEO (ordered via `SECTION_ORDER`).
- **Highlighting**: modified cells get `--color-bg-warning-weak` background + a "Modified" tag. Same values render on a transparent background.
- **Pricing formatting**: MRP / price / wholesale / cost rendered via reused `formatCurrency`.
- **Counters**: "Added 0 · Removed 0 · Modified N" (added/removed are 0 because mock versions are full snapshots — see Future Integration).

## Entry Points
1. **In-workspace History section** — a `<select>` picks a version; `CompareView` renders below `VersionHistory`.
2. **Standalone page** — `/preview/products/compare` (same selector + `CompareView`).

## Reused Utilities
- `FIELD_SECTIONS` + `formatValue` from `changeDetection.ts`
- `FIELD_LABELS` + `formatCurrency` from `creation/validation.ts`
- `ProductWizardData` type from `creation/types.ts`

## Responsive
The table wrapper uses `overflowX: auto` so it scrolls horizontally on mobile/tablet instead of breaking layout.

## Future Backend Integration
When a real version-diff API returns field-level add/remove deltas, `CompareView` can consume an `Added/Removed` set to populate the "Added/Removed" counters and render add/remove affordances (e.g. strikethrough for removed, green for added).
