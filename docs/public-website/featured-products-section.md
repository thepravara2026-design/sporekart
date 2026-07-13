# Featured Product Categories

**File:** `src/public-website/home/sections/FeaturedProducts.tsx`
**Purpose:** Surface the core catalog categories with clear CTAs.

## Content (6 cards, reusable `Card`)
- Spawn Seeds → `/products`
- Fresh Mushrooms → `/products`
- Dry Mushrooms → `/products`
- Growing Kits → `/products`
- Accessories → `/products`
- Knowledge → `/blog`

Each card: icon, title (H3), description, "View category" link (NavButton, variant link).

## Visual Design
- `Card` variant `outlined`, `hoverable`, `as="article"`.
- `auto-fit minmax(260px, 1fr)` grid.
- Icon chip in accent-subtle.

## Accessibility
- Cards are `<article>` with `aria-label`.
- CTA is a real button navigating to the target route.
- Heading hierarchy: section H2 → card H3.

## Responsive
- 3 columns desktop → 1 column mobile; hover lifted via `hoverable`.
