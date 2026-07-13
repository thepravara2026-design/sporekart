# Homepage Animation Guidelines

Motion on the homepage is purposeful, subtle, and fully accessible. It must never
hinderror comprehension or exclude reduced-motion users.

## Components
- **`Reveal`** (`Reveal.tsx`) — wraps a section; fades + lifts it into view when
  scrolled into the viewport (IntersectionObserver). Exposes `MotionStyles` which
  injects the keyframes and the `prefers-reduced-motion` override.
- **`AnimatedCounter`** (`AnimatedCounter.tsx`) — counts up from 0 to `value` when
  in view; respects reduced motion (renders final value instantly). Supports a
  `placeholder` flag for marked demo metrics.
- **`ScrollProgress`** (`ScrollProgress.tsx`) — fixed top bar reflecting page scroll
  position; `role="progressbar"` with `aria-valuenow/min/max`.

## Rules
1. **Reduced motion is mandatory-safe.** Under `prefers-reduced-motion: reduce`, every
   animation is replaced by the final visible state — no `animation` or `transition`.
2. **Entrance only.** Animations are reveal/entrance; no looping or decorative motion.
3. **Respect `aria-hidden`.** Decorative visuals that animate are `aria-hidden="true"`.
4. **Hover micro-interactions** only on interactive elements (cards, buttons), kept
   short (<200ms) and subtle.
5. **No layout shift.** Reveal uses opacity + small translate; final layout is static.
6. **Performance.** IntersectionObserver avoids scroll listeners; counters use
   `requestAnimationFrame`.

## How to Use
```tsx
import { Reveal, MotionStyles } from './Reveal';

<PublicLayout ...>
  <MotionStyles />
  <ScrollProgress />
  <HeroSection />
  <Reveal><StoryBand /></Reveal>
  {/* ...wrap each section in Reveal... */}
</PublicLayout>
```

## Preview
The homepage preview (`/preview/homepage`) includes an **Animation preview** toggle
and a **Replay animations** button to re-trigger entrance effects for review.
