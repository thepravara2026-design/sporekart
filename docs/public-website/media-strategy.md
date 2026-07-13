# Homepage Media Strategy

The homepage uses placeholder media slots so the layout, accessibility, and
performance characteristics are correct before real assets are supplied.

## `MediaPlaceholder` (`MediaPlaceholder.tsx`)
A reusable figure used wherever an image or video will eventually live:
- `role="img"` + `aria-label` so screen readers and SEO receive descriptive text now.
- `aspectRatio` prop reserves layout space (prevents CLS).
- `icon` + `label` render a clear placeholder; `caption` documents intent.
- Ready to swap for `<img loading="lazy" ... />` or `<video>` without layout change.

## Media Slots on Homepage
| Location | Slot | Notes |
|----------|------|-------|
| Hero | Background/photo placeholder (decorative, `aria-hidden`) | Real hero image/video |
| TrainingHighlight | Training session photo (`MediaPlaceholder`) | Farmer training photography |
| SuccessStories | Video thumbnail + farmer avatars (`Avatar`) | Testimonial video + photos |
| StoryBand | Icons only (no photo) | Keep band lightweight |

## Image SEO & Accessibility
- Every real `<img>` must include descriptive `alt`.
- Decorative imagery stays `aria-hidden`.
- `MediaPlaceholder` already supplies `aria-label` so alt text exists pre-asset.
- Use `loading="lazy"` for below-the-fold media; `decoding="async"`.

## Performance
- Reserve aspect ratio to avoid CLS.
- Lazy-load below-the-fold media.
- Provide responsive `srcset`/sizes when real assets land.
- Keep hero media optimized (AVIF/WebP) and under budget.

## Open Items
- Real hero photo/video.
- Farmer training & testimonial photography (with consent/releases).
- Brand film / explainer video.
- Partner & certification logo assets (see RecognitionStrip).
