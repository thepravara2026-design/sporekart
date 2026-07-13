# Banner System

## Overview

Banners are full-width notification bars displayed at the top of the page or section. They communicate global announcements, system status, and legal notices. Unlike alerts, banners span the full viewport width and are typically used for organization-wide communications.

### Component Hierarchy

```
Banner
├── AnnouncementBanner
├── MaintenanceBanner
├── UpdateBanner
├── WarningBanner
├── OfflineBanner
└── CookieBanner
```

---

## Types

| Type | Purpose | Icon | Color |
|------|---------|------|-------|
| **AnnouncementBanner** | Product updates, feature launches, promotions | Megaphone | `--color-primary` |
| **MaintenanceBanner** | Scheduled downtime, system maintenance | Wrench | `--color-warning` |
| **UpdateBanner** | New version available, required updates | Refresh | `--color-info` |
| **WarningBanner** | Security alerts, policy changes | Triangle | `--color-danger` |
| **OfflineBanner** | Network connectivity loss | Wifi-off | `--color-danger` |
| **CookieBanner** | GDPR/CCPA cookie consent | Cookie | `--color-muted` |

---

## Positioning

| Variant | Position | Behavior |
|---------|----------|----------|
| **Fixed** | `position: fixed; top: 0` | Stays visible on scroll. Pushes page content down via padding. |
| **Relative** | `position: relative` | Flows with document. Scrolls with content. |

- **OfflineBanner**: Always fixed position at the very top of the viewport (above all navigation).
- **CookieBanner**: Fixed position at bottom of viewport (or top, configurable).
- **AnnouncementBanner**: Typically relative, displayed below the navigation bar.

---

## Props

### Base Banner Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `type` | `'announcement' \| 'maintenance' \| 'update' \| 'warning' \| 'offline' \| 'cookie'` | `'announcement'` | Banner type |
| `position` | `'fixed' \| 'relative'` | `'relative'` | CSS position |
| `dismissible` | `boolean` | `true` | Show dismiss button |
| `onDismiss` | `() => void` | — | Dismiss callback |
| `children` | `ReactNode` | — | Banner content |
| `action` | `{ label: string; onClick: () => void }` | — | Call-to-action |
| `icon` | `ReactNode` | — | Custom icon override |
| `priority` | `'high' \| 'normal' \| 'low'` | `'normal'` | Stacking order when multiple banners present |
| `animate` | `boolean` | `true` | Enable slide-in animation |
| `sticky` | `boolean` | `false` | Stick on scroll |
| `zIndex` | `number` | token | Custom z-index |

### CookieBanner Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `position` | `'top' \| 'bottom'` | `'bottom'` | Viewport position |
| `onAccept` | `() => void` | — | Accept all cookies |
| `onReject` | `() => void` | — | Reject non-essential |
| `onCustomize` | `() => void` | — | Open cookie preferences |
| `acceptLabel` | `string` | `'Accept All'` | Accept button text |
| `rejectLabel` | `string` | `'Reject'` | Reject button text |
| `customizeLabel` | `string` | `'Customize'` | Customize link text |
| `storageKey` | `string` | `'cookie-consent'` | localStorage persistence key |

### OfflineBanner Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `isOnline` | `boolean` | `navigator.onLine` | Network status |
| `retryLabel` | `string` | `'Retry'` | Retry button text |
| `onRetry` | `() => void` | — | Retry connection handler |
| `message` | `string` | `'You are offline'` | Status message |
| `reconnectedMessage` | `string` | `'Back online'` | Message on reconnection |

---

## Dismissible vs Persistent

| Behavior | Implementation |
|----------|---------------|
| **Dismissible** | Close button visible. Optional `storageKey` for persistence across sessions. |
| **Persistent** | No close button. Remains until condition resolves (e.g., OfflineBanner reconnects). |
| **Timed** | Auto-dismiss after N seconds (configurable via `autoDismissAfter` prop). |

---

## Responsive Behavior

| Breakpoint | Behavior |
|------------|----------|
| Desktop (≥1024px) | Full-width banner, horizontal layout (icon + text + action inline). |
| Tablet (768-1023px) | Full-width, text wraps, action button below text. |
| Mobile (<768px) | Full-width, compact padding, stacked layout, action as text link. |
| Reduced motion | Slide animation replaced with instant show/hide. |

---

## Examples

### AnnouncementBanner

```tsx
import { AnnouncementBanner } from '@sporekart/ui';

function ProductAnnouncement() {
  const [dismissed, setDismissed] = useLocalStorage('announcement-v3', false);

  if (dismissed) return null;

  return (
    <AnnouncementBanner
      position="relative"
      onDismiss={() => setDismissed(true)}
      action={{ label: 'Learn more', onClick: () => navigate('/whats-new') }}
    >
      Introducing real-time collaboration — work together in the same workspace.
    </AnnouncementBanner>
  );
}
```

### MaintenanceBanner

```tsx
<MaintenanceBanner
  position="fixed"
  dismissible
  onDismiss={dismissMaintenance}
  action={{ label: 'View schedule', onClick: openScheduleModal }}
>
  Scheduled maintenance: Sunday, March 15, 2:00 AM - 6:00 AM EST.
</MaintenanceBanner>
```

### OfflineBanner with retry

```tsx
import { OfflineBanner } from '@sporekart/ui';

function NetworkStatusBar() {
  return (
    <OfflineBanner
      position="fixed"
      dismissible={false}
      onRetry={() => window.location.reload()}
      message="Network connection lost. Some features may be unavailable."
      reconnectedMessage="Connection restored."
    />
  );
}
```

### CookieBanner

```tsx
<CookieBanner
  position="bottom"
  onAccept={acceptAllCookies}
  onReject={rejectNonEssential}
  onCustomize={openPreferences}
  storageKey="sporekart-cookie-consent-v2"
/>
```

### WarningBanner (security alert)

```tsx
<WarningBanner
  position="fixed"
  dismissible={false}
  priority="high"
  action={{ label: 'Review settings', onClick: openSecuritySettings }}
>
  Your password expires in 3 days. Update now to maintain account access.
</WarningBanner>
```

---

## Best Practices

- **One banner at a time**: If multiple banners are active, show only the highest-priority one. Use a banner stack manager.
- **Offline detection**: Use the `navigator.onLine` API and listen for `online`/`offline` events. Show OfflineBanner immediately on disconnect.
- **Cookie consent**: Store consent in localStorage. Respect Do Not Track (DNT) header when present.
- **Animation**: Slide down on mount, slide up on dismiss. Duration: 300ms. Respect reduced motion.
- **Z-index management**: Fixed banners need high z-index (token: `--z-banner`). OfflineBanner should be above navigation but below modals.
- **Body padding**: Fixed banners at the top must add `padding-top` to `<body>` to prevent content overlap.
- **Accessibility**: Banners use `role="banner"` with `aria-label`. AnnouncementBanner uses `role="status"`. Warning/Offline use `role="alert"`.
