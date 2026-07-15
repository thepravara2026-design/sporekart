import { PreviewScaffold } from './PreviewScaffold';

const A11Y = [
  'Brand mark and sidebar logo are decorative (aria-hidden).',
  'All inputs, buttons, and links use semantic HTML with visible focus rings (--color-focus-ring).',
  'Sidebar is a <nav role="navigation"> with ARIA label; collapsible on mobile with overlay.',
  'Top nav dropdown uses aria-expanded, aria-controls, and keyboard trap (Escape to close).',
  'Breadcrumb is a <nav aria-label="Breadcrumb"> with <ol> landmarks.',
  'Widget cards use <section> with aria-labelledby pointing to their title.',
  'Profile avatar uses an <Icon> with aria-hidden; name is live text.',
  'Status badges (unread count) use aria-label for screen readers.',
  'Honors prefers-reduced-motion: transitions disabled, skeleton animation paused.',
  'Color contrast meets WCAG 2.2 AA for all token-driven colors.',
];

const RESP = [
  'Desktop (≥1024px): sidebar persistent, 3-column widget grid, full top nav.',
  'Tablet (768–1023px): sidebar collapsible to mini (icons only), 2-column grid, top nav condensed.',
  'Mobile (<768px): sidebar becomes overlay drawer (hamburger opens), single-column grid, top nav stacks.',
  'Sidebar collapsed state persisted via onCollapse callback.',
  'Widget cards use min-width 340px with auto-fit grid; no horizontal scroll at any breakpoint.',
  'Touch targets ≥44px on all interactive elements.',
];

export default function DashboardPreview() {
  return (
    <PreviewScaffold
      title="Customer Dashboard — Preview"
      subtitle="Full authenticated workspace: welcome, profile, widgets for orders, training, wishlist, notifications, and more."
      route="/dashboard"
      accessibilityNotes={A11Y}
      responsiveNotes={RESP}
      approvalStatus="Pending approval"
    />
  );
}