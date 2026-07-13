# Homepage Conversion Optimization

The homepage is the primary top-of-funnel surface. Conversion goals are:
discovery → trust → action (browse, learn, subscribe). No checkout/checkout-style
friction exists on the homepage by design.

## Conversion Levers Implemented
- **Single primary CTA in hero** (“Browse products”) to avoid choice paralysis.
- **Contextual CTAs per section** — each content block ends with a relevant next step.
- **Trust-first ordering** — StoryBand → TrustStrip → RecognitionStrip before product
  categories, so users build confidence before committing attention.
- **FAQ objection handling** — common questions answered inline; answers link to
  `/products`, `/training`, `/faq` to deepen engagement.
- **Newsletter as soft conversion** — low-commitment community capture with explicit
  benefits and a keyboard-accessible, status-announced form.
- **Internal linking** — every CTA is a real route (`/products`, `/training`, `/blog`,
  `/faq`), improving both SEO crawl depth and user flow.

## Microcopy
- Buttons use action verbs (“Browse products”, “Explore training”, “Read the blog”).
- Benefit-led headings (“Everything a cultivator needs, in one trusted place”).
- Trust badges in hero (“Lab-verified spawn”, “Farmer-first support”, “Pan-India delivery”).

## Accessibility & Conversion
- All CTAs are real `<button>`/`<a>` with visible focus states (design-system tokens).
- Newsletter confirmation uses `role="status"` for screen-reader feedback.
- Reduced-motion users get the same content with no animation penalties.

## Metrics to Track (post-launch)
- Hero CTA click-through rate.
- Newsletter signup rate.
- Scroll depth to Training / Products CTAs.
- FAQ expand rate.

## Open Items
- A/B test hero headline + primary CTA label.
- Add a contact/speak-to-expert CTA once `/contact` ships (Part 4+).
- Real conversion analytics wiring (post-backend).
