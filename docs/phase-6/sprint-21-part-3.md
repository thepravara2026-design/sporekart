# Phase 6 — Sprint 21 Part 3: Homepage Content, Storytelling & Conversion Optimization

**Status:** COMPLETE (awaiting approval)
**Branch:** `sporetest`
**Date:** 2026-07-13
**Depends on:** Sprint 21 Part 1 (Foundation), Part 2 (Homepage)

## Summary
Iterated on the homepage to bring content to production-ready quality, add brand
storytelling, strengthen trust and conversion signals, introduce accessible motion,
enhance SEO, and prepare media slots — all without backend/API changes and reusing
Design System v1.0.0.

## What Changed

### New files
- `src/public-website/home/usePrefersReducedMotion.ts` — hook to detect reduced-motion preference.
- `src/public-website/home/Reveal.tsx` — IntersectionObserver reveal wrapper + `MotionStyles`.
- `src/public-website/home/AnimatedCounter.tsx` — count-up counter, reduced-motion safe, placeholder flag.
- `src/public-website/home/ScrollProgress.tsx` — fixed top scroll-progress bar (`role="progressbar"`).
- `src/public-website/home/MediaPlaceholder.tsx` — accessible image/video placeholder (`role="img"`, `aria-label`, lazy-ready).
- `src/public-website/home/sections/StoryBand.tsx` — brand storytelling band (who we are / why mushrooms / why SporeKart).
- `src/public-website/home/sections/RecognitionStrip.tsx` — partner & certification logo chips (placeholders).

### Modified files
- `TrustStrip.tsx` — animated counters (in-view count-up) + `placeholder` markers on metrics.
- `TrainingHighlight.tsx` — added `MediaPlaceholder`, refined heading copy.
- `HeroSection.tsx` — refined headline, subcopy, trust badges, story eyebrow.
- `FeaturedProducts.tsx`, `WhyChoose.tsx`, `CultivationJourney.tsx`, `ResourcesPreview.tsx` — copy refinement.
- `FaqPreview.tsx` — exported `FAQ_ITEMS` for SEO FAQ schema.
- `HomePage.tsx` — composed `MotionStyles`, `ScrollProgress`, `StoryBand`, `RecognitionStrip`; wrapped sections in `Reveal`; enhanced structured data (Organization, WebSite + SearchAction, BreadcrumbList, FAQPage).
- `HomepagePreview.tsx` — added Content review, Animation preview, and Side-by-side compare modes; replay-animations control; approval status; a11y/responsive/content/motion note blocks; section & content checklists.

### Docs
- `docs/public-website/homepage-content-strategy.md`
- `docs/public-website/homepage-storytelling.md`
- `docs/public-website/conversion-optimization.md`
- `docs/public-website/animation-guidelines.md`
- `docs/public-website/media-strategy.md`
- `docs/public-website/review-notes/sprint-21-part-3.md`
- `docs/phase-6/sprint-21-part-3.md`
- `docs/sprints/phase-6/sprint-21-part-3-implementation-plan.md`

## Verification
- `npx tsc --noEmit`: 0 errors.
- `npm run build`: success (HomePage chunk ~88 kB / 19 kB gzip).
- Dev review: http://localhost:5173 and `/preview/homepage`.

## Notes
- Placeholders are clearly marked (`*placeholder`) — no fake statistics committed.
- Newsletter form remains UI-only (no network call), per Part 2 scope.
- No backend/API changes.
- **Pending:** stakeholder copy/data finalization, real imagery, real partner/cert logos.
