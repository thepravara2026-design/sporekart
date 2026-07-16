# Enterprise Communication Platform — Responsive Report

**Sprint 26 · Part 10.** How each screen of the mock-mode Communication feature adapts across
mobile, tablet, and desktop viewports. Layouts rely on CSS custom-property tokens and fluid
grids; no media queries are defined inside feature code (responsiveness is intrinsic to the
token-driven flex/grid model).

## 1. Core Responsive Techniques

| Technique | Usage | Effect |
| --- | --- | --- |
| Auto-fill grids | `repeat(auto-fill, minmax(Npx, 1fr))` | Card grids reflow column count by width |
| Flex-wrap bars | `flexWrap: 'wrap'` on nav/toolbar/tab rows | Controls wrap instead of overflowing |
| Horizontal-scroll tables | `overflowX: 'auto'` wrapper | History table scrolls on narrow screens |
| Labeled selects/inputs | inline `minWidth` | Search/filter controls stay tappable |
| Dialog sizing | `size="lg"` from design-system | Centered, max-width capped |

## 2. Breakpoint Behavior (Qualitative)

| Viewport | Behavior |
| --- | --- |
| Mobile (< 640px) | Single-column auto-fill grids; nav tabs and toolbar wrap; History table scrolls horizontally; pagination wraps. |
| Tablet (640–1024px) | 2–3 card columns; section nav may wrap to multiple rows but remains reachable; KPI grid shows 2–3 columns. |
| Desktop (> 1024px) | Full multi-column grids (up to 4–6 KPIs, 3 widget cards); nav in a single row; tables use full width. |

## 3. Per-Page Responsive Strategy

| Page | Layout | Responsive strategy |
| --- | --- | --- |
| Overview | KPI grid `auto-fill minmax(220px,1fr)` + `WidgetGrid` (`minColWidth={360}`) | KPIs and widgets reflow; status bars stack. |
| Announcements | `auto-fill minmax(360px,1fr)` card grid + `CommToolbar` (wrap) + `CommPagination` (wrap) | Cards 1→N columns; toolbar/search/selects wrap; pagination wraps. |
| Notifications | Read `tablist` (wrap) + `CommToolbar` (wrap) + `<ul>` list | List is single-column; tabs wrap; toolbar wraps. |
| Scheduled | `<ul>` list of rows | Rows are flex with `flex:1` middle; wraps gracefully; date block right-aligned. |
| Templates | `auto-fill minmax(320px,1fr)` grid + `CommToolbar` | Cards reflow; future vs active differentiated by border style not layout. |
| History | `overflowX:'auto'` `<table minWidth={640}>` | Table scrolls horizontally on mobile; toolbar wraps above. |
| Delivery | State `tablist` (wrap) + `<ul>` list | Tabs wrap; list single-column; state chip stays right-aligned. |
| Future Channels | `auto-fill minmax(300px,1fr)` grid | Roadmap cards 1→N columns; banner full-width. |
| Statistics | KPI grid `auto-fill minmax(220px,1fr)` + `WidgetGrid` (`minColWidth={340}`) | Bars stack vertically inside cards; cards reflow. |

## 4. Toolbar and Tab Bars

- `CommToolbar` uses `flexWrap: 'wrap'`, a flexible search field (`flex: '1 1 220px'`,
  `minWidth: 200`), and `marginLeft: 'auto'` on the actions cluster so filters collapse to
  multiple rows on small screens.
- `SectionTabs`, the Notifications read filter, and the Delivery state filter all use
  `overflowX: 'auto'` or `flexWrap: 'wrap'` so they remain usable without clipping.

## 5. Dialog and Editor

- Template preview and announcement detail use the design-system `Dialog` (`size="lg"`), which
  is centered with a responsive max-width and internal scroll.
- `RichTextEditor` toolbar (`role="toolbar"`) wraps; the textarea is full-width and resizable;
  the preview region has `minHeight` so it remains legible when stacked.

## 6. Notes / Future

- If a hard breakpoint is needed (e.g. hide sidebar metadata on mobile), introduce token-driven
  media queries in the global theme rather than per-component styles.
- The History table's `minWidth: 640` is the only fixed width; consider column priority hiding
  for very small screens.
