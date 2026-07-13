# Cultivation Journey (Customer Journey)

**File:** `src/public-website/home/sections/CultivationJourney.tsx`
**Purpose:** Illustrate the end-to-end customer journey from spore to business.

## Steps (vertical stepper)
1. Learn — explore training & playbooks
2. Buy Spawn — order lab-verified spawn & kits
3. Cultivate — guided inoculation to fruiting
4. Harvest — pick at peak with guidance
5. Sell — supply via local/partner channels
6. Grow Business — scale with ongoing support

## Visual Design
- Vertical list of `step` rows (numbered badge + icon + title + description).
- Chevron-down connectors between steps (decorative, `aria-hidden`).
- `auto-fit` not used here; fixed max-width centered column for readability.

## Accessibility
- Ordered list (`<ol>`/`<li>`) conveys sequence semantically.
- Icons `aria-label` = step title; connectors `aria-hidden`.

## Responsive
- Single centered column on all viewports; spacing scales with tokens.
