# Admin Header / Top Bar

## Implementation

The admin header is a custom layout within `AdminLayout.tsx` wrapping the design system's `TopNav` component.

## Layout

```
┌─────────────────────────────────────────────────┐
│ [☰] [🛡] Admin    [Profile] [Settings] [Help]  │
└─────────────────────────────────────────────────┘
```

Left side:
- **Hamburger menu** (mobile only, `display: none` on desktop via `.admin-header__menu-btn`)
- **Brand icon** (shield icon in primary color)
- **"Admin" label** (bold text)

Right side:
- **TopNav component** with `ADMIN_TOP_NAV` items
- Profile, Settings, Help links

## Future Enhancements
- Global search bar (placeholder ready)
- Notification center (placeholder ready)
- Theme toggle (dark/light mode switch)
- AI assistant trigger
- Workspace selector
- Quick action buttons
