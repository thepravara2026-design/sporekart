# Enterprise Dashboard Responsive Design

## Breakpoints

| Device | Viewport | Grid Columns | Behavior |
|--------|----------|-------------|----------|
| Desktop | 1200px+ | 4 | Full layout, side-by-side panels |
| Laptop | 1024px | 4 | Reduced spacing |
| Tablet | 768px | 2–3 | KPI grid reduces, widgets stack |
| Mobile Landscape | 480px | 1–2 | Compact layout |
| Mobile Portrait | 375px | 1 | Single column layout |

## Responsive Strategies

### KPI Grid
- `repeat(4, 1fr)` on desktop
- `repeat(2, 1fr)` on tablet (via CSS)
- `repeat(1, 1fr)` on mobile (via CSS)

### Widget Grid
- 4-column grid on desktop
- 2-column on tablet
- 1-column on mobile

### Bottom Row (Activity + System Status)
- CSS Grid `1fr 1fr` on desktop
- Single column stack on tablet/mobile

### Quick Actions
- 4 columns on desktop
- 2 columns on tablet
- 1 column on mobile

## CSS Classes

The dashboard uses inline styles with CSS variables. Responsive behavior is achieved through:
1. `style` objects with conditional properties
2. CSS Grid auto-fit/auto-fill
3. Viewport-relative units (`maxWidth: 768px`, etc.)
4. Container queries for widget layouts

## Touch-Friendly

- All interactive targets minimum 44px
- Clear touch feedback on taps
- Sufficient spacing between touch targets
- No hover-dependent interactions
