import { PreviewScaffold } from './PreviewScaffold';

const A11Y = [
  'Sidebar opens as full-screen overlay with backdrop (aria-modal equivalent).',
  'Hamburger menu has aria-label and aria-expanded; focus trapped in drawer.',
  'All touch targets ≥44×44px; no hover-only interactions.',
  'Bottom navigation not used — primary nav in drawer; secondary in top nav dropdown.',
  'Content scales to single column; cards full-width with comfortable padding.',
  'Skip link (href="#main") visible on focus for keyboard users.',
  'Reduced motion respected: drawer slide transition disabled.',
];

const RESP = [
  'Sidebar replaced by hamburger button in top nav (left).',
  'Top nav shows brand + dropdown only (notifications, profile, logout).',
  'Widget grid becomes single column; cards stack vertically.',
  'Profile summary stacks avatar above info; meta becomes 1-column grid.',
  'Quick actions become full-width cards (1 per row).',
  'Welcome section centers text; CTA button full-width.',
  'Breadcrumb collapses after 2 items with ellipsis tooltip.',
  'Footer links wrap to two lines with centered alignment.',
];

export default function MobileDashboardPreview() {
  return (
    <PreviewScaffold
      title="Mobile Dashboard Preview"
      subtitle="Customer workspace at 390px viewport — sidebar as overlay drawer."
      route="/dashboard"
      accessibilityNotes={A11Y}
      responsiveNotes={RESP}
    />
  );
}