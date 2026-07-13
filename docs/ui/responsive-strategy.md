# Responsive Strategy — SporeKart Enterprise Web Application
## Phase 5 / Sprint 19 (Part 1C): UX Standards & Accessibility Foundation

> **Status:** Mandatory responsive behavior. One mental model across all viewports. Reuses Layout Blueprint from Part 1B.

---

## 1. Breakpoints

| Name | Width | Target Devices |
|------|-------|----------------|
| `xs` (Mobile) | < 640px | Phone portrait |
| `sm` (Mobile Large) | 640–767px | Phone landscape, small tablet |
| `md` (Tablet) | 768–1023px | Tablet portrait |
| `lg` (Laptop) | 1024–1279px | Laptop, tablet landscape |
| `xl` (Desktop) | 1280–1535px | Desktop |
| `2xl` (Wide) | ≥ 1536px | Large desktop, ultra-wide |

**CSS Custom Properties** (defined in Part 1D tokens, referenced here):
```css
--bp-xs: 640px;
--bp-sm: 768px;
--bp-md: 1024px;
--bp-lg: 1280px;
--bp-xl: 1536px;
```

---

## 2. Grid & Container Adaptation

| Breakpoint | Content Max-Width | Sidebar | Columns (Data) | Gutter |
|------------|-------------------|---------|----------------|--------|
| xs | 100% (full bleed) | Drawer (280px) | 1 | 16px |
| sm | 100% | Drawer (280px) | 1–2 | 16px |
| md | 720px (prose) / 100% (data) | Icon rail (72px) | 2–3 | 20px |
| lg | 720px / 1200px | Icon rail (72px) | 3–4 | 24px |
| xl | 720px / 1200px | Fixed (264px) | 4–5 | 24px |
| 2xl | 720px / 1400px | Fixed (264px) | 5–6 | 32px |

**Rule:** Prose/content never exceeds 720px reading width. Data tables/forms use available width up to their max.

---

## 3. Navigation Adaptation

| Viewport | Global Nav | Workspace Nav | Breadcrumb | Command Palette |
|----------|------------|---------------|------------|-----------------|
| xs/sm | Hamburger → Drawer | Inside drawer, collapsible sections | Single line, `… › Current` | Full-screen modal |
| md | Icon rail (72px), hover expands | Tooltip on hover, click expands | Full trail, truncate middle | Centered modal |
| lg/xl/2xl | Fixed sidebar (264px) | Always visible | Full trail | Centered modal |

**Drawer Behavior (xs/sm):**
- Opens left, overlays content, backdrop `rgba(0,0,0,0.32)`
- Swipe right to close, Escape closes, focus trap
- Auto-closes on route change
- Touch target ≥ 48×48px

**Icon Rail (md):**
- Hover/focus expands to 264px with labels
- Click workspace → expands children, rail stays expanded
- Click outside → collapses

---

## 4. Table Behavior

| Breakpoint | Strategy |
|------------|----------|
| xs | **Card stack**: each row → card with label-value pairs. Horizontal scroll only for comparison views (pinned first column). |
| sm | Card stack or horizontal scroll with sticky first column. |
| md | Full table, sticky header, sticky first column. Density: comfortable (default). |
| lg+ | Full table, density toggle (comfortable/compact), column resize, virtualized >200 rows. |

**Table Skeleton:** Matches final column count and widths. Shows shimmer on cell areas.

---

## 5. Card & Tile Adaptation

| Breakpoint | Layout |
|------------|--------|
| xs | Single column, full-width cards, 16px gap |
| sm | 2-column grid (min 280px), 16px gap |
| md | 3-column grid, 20px gap |
| lg | 4-column grid, 24px gap |
| xl/2xl | 5–6 column grid, 24–32px gap |

**Card Skeleton:** Image placeholder, title line, body line, action area — all same dimensions as loaded state.

---

## 6. Form Adaptation

| Element | xs/sm | md+ |
|---------|-------|-----|
| Field width | 100% | Max 480px (text), 320px (select), auto (checkbox/radio) |
| Label position | Above field | Above (default) or inline (compact density) |
| Button group | Stacked, full-width, primary bottom | Inline, primary right |
| Validation message | Below field, full width | Below field |
| OTP / PIN | 6 large inputs, auto-advance | Same |
| Address | Stacked, auto-complete integration | Side-by-side postal/city where appropriate |

**Touch Targets:** All interactive elements ≥ 48×48px on xs/sm (WCAG 2.5.8).

---

## 7. Spacing Scale (4px Base Unit)

| Token | xs | sm | md | lg | xl | 2xl |
|-------|----|----|----|----|----|-----|
| `--space-1` | 4px | 4px | 4px | 4px | 4px | 4px |
| `--space-2` | 8px | 8px | 8px | 8px | 8px | 8px |
| `--space-3` | 12px | 12px | 12px | 12px | 12px | 12px |
| `--space-4` | 16px | 16px | 16px | 16px | 16px | 16px |
| `--space-5` | 20px | 20px | 20px | 24px | 24px | 24px |
| `--space-6` | 24px | 24px | 24px | 32px | 32px | 32px |
| `--space-8` | 32px | 32px | 32px | 40px | 40px | 48px |
| `--space-10` | 40px | 40px | 40px | 48px | 48px | 64px |
| `--space-12` | 48px | 48px | 48px | 56px | 56px | 80px |

**Page padding:** `var(--space-4)` xs/sm, `var(--space-5)` md, `var(--space-6)` lg+.

---

## 8. Typography Scaling

| Element | xs | sm | md | lg | xl | 2xl |
|---------|----|----|----|----|----|-----|
| Display (H1) | 28px | 32px | 36px | 40px | 44px | 48px |
| H2 | 22px | 24px | 26px | 28px | 30px | 32px |
| H3 | 18px | 19px | 20px | 21px | 22px | 24px |
| Body | 15px | 15px | 16px | 16px | 16px | 16px |
| Small / Caption | 13px | 13px | 13px | 13px | 13px | 13px |

**Line height:** Body 1.6, Headings 1.3.
**Font:** Single family (Part 1D tokens). System fallback stack defined.
**Respect user scaling:** `rem` units only; no `px` clamp on body.

---

## 9. Touch & Pointer

- **Tap highlight:** `-webkit-tap-highlight-color: transparent`; custom active state.
- **Scroll:** Momentum scrolling `-webkit-overflow-scrolling: touch`.
- **Inputs:** `inputmode` attributes (numeric, email, tel, decimal). Prevent zoom on focus (font-size ≥ 16px).
- **Drag/Swipe:** Card swipe actions (archive, delete) on mobile tables. Keyboard equivalent required.

---

## 10. Image & Media

- **Responsive images:** `<picture>` with WebP/AVIF, `srcset` at 1x, 2x, 3x.
- **Lazy load:** `loading="lazy"` below fold; `fetchpriority="high"` for LCP image.
- **Aspect ratio boxes:** Reserve space to prevent CLS.
- **Icons:** SVG sprite, `currentColor` for theme adaptation.

---

## 11. Print & Export

- `@media print`: Hide chrome (header, sidebar, footer, palette). Content max-width none. Tables break across pages cleanly. Links show URL in parentheses.
- "Export PDF" button on data pages → generates print-optimized view.

---

## 12. Prototype Verification Routes

- `/demo/responsive` — Live viewport toggle (xs/sm/md/lg/xl/2xl) with grid overlay
- Resize browser to test drawer, rail, table-card flip, form stacking