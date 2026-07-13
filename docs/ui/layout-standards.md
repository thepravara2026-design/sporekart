# Layout Standards — SporeKart Enterprise Web Application
## Phase 5 / Sprint 19 (Part 1B): Enterprise Web Experience — IA & Navigation Blueprint

> **Status:** Documentation. Concrete layout measurements and responsive rules.
> Values are **proposed standards** for review; they intentionally avoid hardcoding
> design tokens (tokens are deferred to a later sprint). Use spacing in a 4px base
> scale and a single type family per the Design Philosophy (Part 1A).

---

## 1. Container Widths
- **Max app width:** none (full-bleed shell); inner content uses max widths below.
- **Content max width (prose/read):** 720px for reading views.
- **Content max width (data/forms):** 1200px for tables/dashboards.
- **Centered** with symmetric page margins.

## 2. Content Margins
- Page horizontal padding: 24px desktop / 16px mobile.
- Page vertical padding: 24px top, 32px bottom.
- Gap between header regions: 16–24px.

## 3. Maximum Reading Width
- Long-form text capped at **720px** to protect readability (Principle 3, Clarity).
- Data surfaces may use the full 1200px budget.

## 4. Sidebar Behavior
- **Desktop (≥1024px):** fixed 264px; collapses to 72px icon rail via toggle.
- **Laptop (768–1023px):** 240px, auto-collapses to icon rail.
- **Tablet/Mobile (<768px):** hidden; opened as an overlay drawer from the left.
- Active workspace auto-expands its children; others collapse.

## 5. Sticky Header Rules
- Header is `position: sticky; top: 0`; height 56px; stays during content scroll.
- Breadcrumb bar scrolls with content (not sticky) to maximize vertical space;
  on mobile it becomes a compact single-line trail.
- No layout shift on load (reserve header height).

## 6. Scrollable Regions
- **Header:** fixed. **Sidebar:** independently scrollable if long.
- **Content:** the only always-scrolling region.
- **Utility panel:** internal scroll; overlay on mobile.

## 7. Responsive Layout Rules
| Breakpoint | Layout |
|-----------|--------|
| Desktop ≥1280 | Sidebar 264 + content max 1200 + utility dock optional |
| Laptop 1024–1279 | Sidebar 240; content fluid |
| Tablet 768–1023 | Sidebar icon rail 72; content fluid |
| Mobile <768 | Drawer sidebar; bottom-safe areas; content full width |

- **One mental model** across breakpoints (Principle 6, Mobile-Responsive).
- Touch targets ≥ 44×44px on mobile.
- Utility panel becomes a bottom sheet on mobile.

## 8. Workspace Switching
- Desktop: click workspace in sidebar (no page reload; SPA route change).
- Mobile: open drawer → tap workspace → drawer closes.
- Command palette: type workspace → Enter (any viewport).
- The active workspace is always reflected in header + breadcrumb + sidebar.

## 9. Accessibility Layout Rules
- Skip link as first focusable element jumps to `<main>`.
- Visible focus ring on every interactive region.
- `prefers-reduced-motion` disables non-essential transitions.
- Landmarks as defined in `layout-blueprint.md`.
