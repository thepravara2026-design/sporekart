
import { PreviewScaffold } from './PreviewScaffold';

const A11Y = [
  'Sidebar has role="navigation" with aria-label="Customer workspace navigation".',
  'Items are menu items (role="menuitem") with arrow-key navigation (Up/Down/Home/End).',
  'Active item has aria-current="page"; disabled items have aria-disabled.',
  'Collapsed state: tooltips appear on hover/focus with item label (no layout shift).',
  'Pin/unpin button: aria-label changes based on state; keyboard accessible.',
  'Responsive overlay: on mobile, backdrop click closes; Escape key closes.',
  'Focus management: when drawer opens, focus moves to first item; on close, returns to hamburger.',
];

const RESP = [
  'Desktop (≥960px): full-width sidebar (~280px) with labels always visible.',
  'Collapsed desktop: mini sidebar (~64px) icons only; hover shows tooltip label.',
  'Tablet (768–959px): collapsed by default; click to expand as overlay.',
  'Mobile (<768px): overlay drawer from left; backdrop dim; body scroll locked.',
  'Sidebar header: brand mark + text; collapse toggle in header.',
  'Sidebar footer: secondary nav (Settings, Help, Sign out) always accessible.',
  'Transition: width change uses CSS var(--duration-normal) var(--easing-standard).',
];

export default function SidebarPreview() {
  return (
    <PreviewScaffold
      title="Sidebar Behavior Preview"
      subtitle="Responsive navigation sidebar — desktop, collapsed, tablet, mobile states."
      route="/dashboard"
      accessibilityNotes={A11Y}
      responsiveNotes={RESP}
    />
  );
}