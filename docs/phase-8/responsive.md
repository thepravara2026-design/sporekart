# Admin Responsive Design

## Breakpoints

| Breakpoint | Viewport | Sidebar | Layout |
|---|---|---|---|
| Desktop | >1024px | Expanded (264px), collapsible to 72px | Multi-column grid |
| Small Desktop | 1024px | Expanded, collapsible | 3-column widget grid |
| Tablet | 768–1024px | Collapsed (72px icon-only) | 2-column widget grid |
| Mobile Portrait | <768px | Overlay drawer | Single column |
| Mobile Landscape | <768px, >480px | Overlay drawer | 2-column possible |
| Large Display | >1440px | Expanded | 4-column widget grid |

## Responsive Features

### Sidebar
- Desktop: Permanently visible, toggleable between expanded/collapsed
- Tablet: Default collapsed (icon-only), expands on click
- Mobile: Hidden behind overlay drawer, triggered by hamburger button
- CSS transition: `width var(--duration-normal) var(--easing-standard)`

### Header
- Desktop: Full brand + TopNav with labels
- Tablet: Compact layout
- Mobile: Brand only + hamburger + condensed nav items (icon-only)

### Content Grid
- Dashboard widgets use `grid-template-columns: repeat(auto-fit, minmax(240px, 1fr))`
- Automatically adjusts columns based on available width
- Consistent padding via `var(--space-page-x)` tokens
- Content container has no max-width constraint (fills available space)

### Touch Controls
- All interactive elements have minimum 44px touch targets on mobile
- Sidebar drawer triggered by tap on hamburger icon
- Close drawer by tapping backdrop or navigating

## Testing
Preview routes available for all viewports:
- `/preview/admin` — desktop layout
- `/preview/admin/sidebar` — sidebar variants
- `/preview/admin/header` — header variants
- `/preview/admin/dashboard` — dashboard layout
- `/preview/admin/mobile` — mobile-optimized view
