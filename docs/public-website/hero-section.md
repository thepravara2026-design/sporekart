# Hero Section

**File:** `src/public-website/home/sections/HeroSection.tsx`
**Purpose:** Primary above-the-fold impact establishing brand trust and direction.

## Content
- Eyebrow badge: "India's mushroom cultivation ecosystem" (sustainable/agriculture cue)
- H1: "Grow premium mushrooms with SporeKart."
- Supporting paragraph (value proposition)
- Primary CTA: "Shop Spawn & Kits" → `/products`
- Secondary CTA: "Explore Training" → `/training`
- Trust indicators: lab-verified quality, pan-India delivery, grower support
- Visual panel: floating chips (spawn, fresh/dried, training, yields, innovation)
- Scroll cue (chevron-down)

## Visual Design
- Radial-gradient backdrop using accent + success subtle tokens (no images required).
- Two-column grid (1.1fr / 0.9fr) that stacks on tablet/mobile.
- Headline uses `clamp()` for fluid scaling; no fixed px.

## Accessibility
- Single H1; decorative visual `aria-hidden`.
- CTAs are real `NavButton`s (keyboard focusable, navigate via router).
- Icons carry `aria-label`; visual chips are presentational.

## Responsive
- Desktop/Laptop: two columns.
- Tablet/Mobile: single column, visual panel below copy, no horizontal overflow.
