# Sprint 21 — Part 3: Homepage Content, Storytelling & Conversion Optimization — Implementation Plan

**Branch:** `sporetest`
**Depends on:** Sprint 21 Part 2 (Homepage) — COMPLETE, pending approval
**Reuses:** Design System v1.0.0, Part 1 foundation, Part 2 homepage sections
**Constraint:** No backend/API changes. Homepage refinement/iteration only.

## Goals
1. Elevate homepage copy to production-ready, on-brand content.
2. Add brand storytelling (who we are / why mushrooms / why SporeKart).
3. Strengthen trust & credibility (animated metrics, partner/certification signals).
4. Optimize conversion (clear CTAs, microcopy, internal linking, FAQ).
5. Add tasteful, accessible motion (reveal, counters, scroll progress).
6. Enhance SEO (FAQ schema, search action, internal links, image alt strategy).
7. Prepare media slots (image/video placeholders, lazy-load-ready).

## Deliverables
| # | Item | File |
|---|------|------|
| 1 | Motion hook (prefers-reduced-motion) | `src/public-website/home/usePrefersReducedMotion.ts` |
| 2 | Reveal-on-scroll wrapper | `src/public-website/home/Reveal.tsx` |
| 3 | Animated counter (reduced-motion safe) | `src/public-website/home/AnimatedCounter.tsx` |
| 4 | Scroll progress bar | `src/public-website/home/ScrollProgress.tsx` |
| 5 | Media placeholder (lazy-ready) | `src/public-website/home/MediaPlaceholder.tsx` |
| 6 | Brand story band | `src/public-website/home/sections/StoryBand.tsx` |
| 7 | Recognition / certifications strip | `src/public-website/home/sections/RecognitionStrip.tsx` |
| 8 | TrustStrip → animated counters + placeholder flag | `src/public-website/home/sections/TrustStrip.tsx` |
| 9 | TrainingHighlight → media placeholder + copy | `src/public-website/home/sections/TrainingHighlight.tsx` |
| 10 | HeroSection → refined copy + story eyebrow | `src/public-website/home/sections/HeroSection.tsx` |
| 11 | Copy refinement (FeaturedProducts, WhyChoose, CultivationJourney, ResourcesPreview) | respective section files |
| 12 | HomePage compose (motion, story, recognition, SEO) | `src/public-website/home/HomePage.tsx` |
| 13 | Export FAQ_ITEMS | `src/public-website/home/sections/FaqPreview.tsx` |
| 14 | Preview: content + animation + compare modes | `src/public-website/preview/HomepagePreview.tsx` |

## Content Strategy
- **Tone:** professional, trustworthy, farmer-friendly, tech-enabled.
- **No fake statistics.** All numeric metrics are clearly marked placeholders (`*placeholder` + clarifying note) pending real data.
- **Hero:** concise value proposition + one primary CTA + trust badges.
- **Storytelling:** `StoryBand` after hero — three pillars (who we are, why mushrooms matter, why SporeKart exists).
- **Trust:** `TrustStrip` (animated counters, placeholder) + `RecognitionStrip` (partner/cert logo chips, placeholder).
- **Conversion:** every section ends with a contextual CTA; FAQ answers link to `/products`, `/training`, `/faq`; newsletter CTA captures community signups; hero + training CTAs drive `/products` and `/training`.

## Motion Guidelines
- All entrance animation via `Reveal` (IntersectionObserver).
- `AnimatedCounter` counts up when in view; respects reduced motion.
- `ScrollProgress` fixed top bar with `role="progressbar"`.
- Under `prefers-reduced-motion`, all motion degrades to instant visible state (no `animation`/`transition`).
- Hover micro-interactions only on interactive elements (cards, buttons).

## SEO Enhancements
- `HomePage.tsx` structured data: Organization, WebSite (+SearchAction), BreadcrumbList, FAQPage (from `FAQ_ITEMS`).
- Internal links to `/products`, `/training`, `/blog`, `/faq` for crawl depth + conversion.
- `MediaPlaceholder` provides `aria-label` + `role="img"` so visual alt text exists before real assets.
- Single H1 (hero), logical H2/H3 sequence.

## Verification
- `npx tsc --noEmit` → 0 errors.
- `npm run build` → success.
- Dev review at http://localhost:5173 and `/preview/homepage`.

## Out of Scope
- Inner pages (About, Products, Training, Blog, FAQ, Contact, Certifications, legal).
- Real copy/data finalization (needs stakeholder input).
- Backend/API.
