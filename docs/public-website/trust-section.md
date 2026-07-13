# Trust & Credibility Strip

**File:** `src/public-website/home/sections/TrustStrip.tsx`
**Purpose:** Quick social-proof row that builds immediate confidence.

## Content (placeholders)
- Years of Experience — 12+
- Farmers Served — 12k+
- Training Programs — 40+
- Products Delivered — 500k+
- Quality Commitment — 99.2%
- Quality Certifications — ISO

Each item pairs an icon chip with a large value and a label.

## Visual Design
- Muted surface background with top/bottom borders.
- `auto-fit minmax(150px, 1fr)` grid → reflows on small screens.
- Icon chip uses accent-subtle background.

## Accessibility
- List semantics (`<ul>`/`<li>`); each icon `aria-label` equals the stat label.
- Purely informational; no interactive elements.

## Responsive
- 6 columns on desktop → 2–3 columns on mobile, no overflow.
