# Homepage

The SporeKart public homepage (`/`) is the primary entry point of the public website.
It is built as a single `HomePage` component (`src/public-website/home/HomePage.tsx`)
that composes 10 sections inside `PublicLayout` (reused from Sprint 21 Part 1).

## Design Goals
Premium · Modern · Fast · Minimal · Professional · Agricultural · Technology-first.

## Story
Visitors understand within seconds that SporeKart provides premium spawn, fresh
mushrooms, professional training, cultivation knowledge, farmer support, and
technology-driven agriculture.

## Sections
1. Hero — `sections/HeroSection.tsx`
2. Trust & Credibility Strip — `sections/TrustStrip.tsx`
3. Featured Product Categories — `sections/FeaturedProducts.tsx`
4. Training Highlight — `sections/TrainingHighlight.tsx`
5. Why Choose SporeKart — `sections/WhyChoose.tsx`
6. Farmer Success Stories — `sections/SuccessStories.tsx`
7. Cultivation Journey — `sections/CultivationJourney.tsx`
8. Latest Resources Preview — `sections/ResourcesPreview.tsx`
9. FAQ Preview — `sections/FaqPreview.tsx`
10. Newsletter / Community CTA — `sections/NewsletterCta.tsx`

## Routing & Preview
- `/` → `HomePage`
- `/preview/homepage` (+ `/desktop`, `/tablet`, `/mobile`) → `HomepagePreview`

## Notes
- No backend/API changes; newsletter form is UI-only.
- All styling uses Design System tokens.
- Footer is the Part 1 `PublicFooter`.
