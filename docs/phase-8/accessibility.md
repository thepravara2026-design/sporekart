# Enterprise Dashboard Accessibility

## WCAG 2.2 AA Compliance

### Semantic HTML
- Dashboard layout uses `<section>` elements with `aria-label`
- Headings follow proper hierarchy (`<h1>` → `<h2>`)
- Widgets use `<button>` elements for interactions
- Lists use appropriate list markup

### Keyboard Navigation
| Component | Keyboard Support |
|-----------|-----------------|
| Widget Menu | Tab to focus, Enter/Space to open, Arrow keys |
| Quick Actions | Tab navigation, Enter to activate |
| Announcements | Tab to dismiss button, Enter/Space to dismiss |
| Activity Feed | Tab to items, Enter/Space to expand |

### ARIA Attributes
- Widget menus: `aria-label="{title} menu"`
- KPI cards: Announce trend via text (not color-only)
- Loading skeleton: `aria-busy` compatible via `prefers-reduced-motion`
- Announcement dismiss: `aria-label="Dismiss {title}"`

### Color and Contrast
| Element | Contrast Ratio | Status |
|---------|---------------|--------|
| Body text on surface | 4.5:1+ | ✅ |
| KPI values on surface | 7:1+ | ✅ |
| Trend indicators (green/red) | 3:1+ with text | ✅ |
| Status indicators with text label | 4.5:1+ | ✅ |

### Reduced Motion
- Loading skeleton uses `animation` property (respects `prefers-reduced-motion`)
- No parallax, no auto-scrolling, no blinking
- Transitions are 150–200ms (not jarring)

### Focus Indicators
- All interactive elements have visible focus ring
- Focus style: `box-shadow` with `var(--color-primary-alpha)`
- Never removed via `outline: none` alone (always paired with focus style)

## Testing Checklist

- [ ] Tab through all interactive elements
- [ ] Verify focus order matches visual order
- [ ] Test with screen reader (NVDA/VoiceOver)
- [ ] Verify all icons have text alternatives
- [ ] Test with 200% zoom
- [ ] Test with reduced motion enabled
- [ ] Test with high contrast mode
- [ ] Verify color is not the only indicator of meaning
