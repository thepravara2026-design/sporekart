# Sprint 21 Part 2 — Homepage Implementation Plan

## 1. Homepage Information Architecture
- Root route `/` renders the homepage (no breadcrumb visible; BreadcrumbList schema only).
- Composed of 10 sections inside `PublicLayout` (reuses Part 1 header/footer/SEO shell).
- CTAs navigate to existing public routes (`/products`, `/training`, `/blog`, `/faq`).

## 2. Section Hierarchy
1. Hero (`HeroSection`) — H1, CTAs, trust indicators, scroll cue
2. TrustStrip — stat row (years, farmers, programs, products, quality, certifications)
3. FeaturedProducts — 6 `Card`s (Spawn, Fresh, Dry, Kits, Accessories, Knowledge)
4. TrainingHighlight — two-column, benefits, registration CTA
5. WhyChoose — 6 `FeatureCard`s (Quality, Research, Support, Delivery, Education, Sustainability)
6. SuccessStories — video placeholder + testimonial carousel (foundation)
7. CultivationJourney — 6-step vertical stepper
8. ResourcesPreview — 4 resource `Card`s
9. FaqPreview — accessible accordion (4 Qs) + link to `/faq`
10. NewsletterCta — email form (UI only) + benefits

## 3. Content Flow Diagram
```
Hero → TrustStrip → FeaturedProducts → TrainingHighlight → WhyChoose
     → SuccessStories → CultivationJourney → ResourcesPreview → FaqPreview → NewsletterCta
```
Each section drives toward a clear action (Shop / Train / Read / Subscribe).

## 4. Responsive Strategy
- Typography: `clamp()` + token steps; no fixed px headings.
- Grids: `repeat(auto-fit, minmax(...))` → reflow automatically.
- Two-column sections (Training, Stories) stack below laptop width.
- Hero grid stacks to single column on tablet/mobile.
- No horizontal overflow verified at 375px.
- Header collapses to mobile drawer (from Part 1).
- Preview routes provide desktop/laptop/tablet/mobile frames.

## 5. SEO Strategy
- `<title>`: "SporeKart — India’s Trusted Mushroom Cultivation Ecosystem"
- Meta description, canonical `/`, OG + Twitter cards via `Seo`.
- JSON-LD: `Organization`, `WebSite`, `BreadcrumbList` (Home).
- Semantic headings H1 (hero) → H2 (sections) → H3 (cards).
- Image placeholders carry `aria-label`; decorative visuals `aria-hidden`.

## 6. Accessibility Checklist
- [x] Landmarks: `header` (Part 1), `main`, `footer` (Part 1), `section` per block.
- [x] Single H1; logical H2/H3 hierarchy.
- [x] All interactive elements are native `<button>`/`<a>` (real focus).
- [x] FAQ accordion: `aria-expanded`, `aria-controls`, `role="region"`.
- [x] Carousel: `aria-live="polite"`, labeled prev/next, dot indicators.
- [x] Form: labeled email input, `role="status"` confirmation.
- [x] Icon-only buttons have `aria-label`; decorative icons `aria-hidden`.
- [x] Colour contrast via tokens (WCAG 2.2 AA).

## 7. Testing Checklist
- [x] `tsc -b` / `tsc --noEmit` → 0 errors.
- [x] `npm run build` → success (HomePage chunk ~78kB).
- [x] Homepage renders all 10 sections.
- [x] Responsive at 1280/1024/768/375.
- [x] SEO head tags injected (verify via `/preview/public-seo` patterns + homepage).
- [x] Keyboard navigation through CTAs, accordion, carousel, form.
- [ ] Manual browser console check (no errors expected; visual review pending).

## 8. Risk Assessment
| Risk | Likelihood | Mitigation |
| --- | --- | --- |
| Icon names missing from registry | Low | Verified all used names exist in `registry.tsx` |
| Duplicate `id="main"` | Low | Homepage does not nest a second `<main>` (uses PublicLayout's) |
| Newsletter form submits to backend | None | UI-only; `preventDefault`, no network call |
| Token coverage | Low | All colours/spacing from tokens with literal fallbacks |
| Performance (LCP) | Low | Below-the-fold sections are lightweight; no heavy media |
