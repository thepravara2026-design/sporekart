import { PreviewScaffold } from './PreviewScaffold';

const A11Y = [
  'Sidebar defaults to collapsed (icon-only) at tablet width; expands on hover/focus.',
  'Top nav condenses — secondary actions move into profile dropdown.',
  'Widget grid uses 2-column layout; cards maintain touch-friendly sizing.',
  'All interactive elements have visible focus states with --color-focus-ring.',
  'Sidebar collapse/expand button has aria-expanded and aria-controls.',
  'Breadcrumb collapses intermediate items with accessible tooltip.',
  'Reduced motion: sidebar transition disabled, no scale/transform animations.',
];

const RESP = [
  'Sidebar: collapsed by default (icon-only) with tooltip on hover; expands on click.',
  'Top nav: primary brand + profile dropdown only; notifications bell with badge.',
  'Widget grid: 2 columns (min 340px) → single column at <640px.',
  'Welcome section: text left-aligned; CTA button full-width on narrow.',
  'Profile summary: meta grid becomes 2 columns; avatar above on narrow.',
  'Footer: links wrap to two lines, centered.',
];

export default function TabletDashboardPreview() {
  return (
    <PreviewScaffold
      title="Tablet Dashboard Preview"
      subtitle="Customer workspace at 834px viewport — collapsed sidebar, 2-column grid."
      route="/dashboard"
      accessibilityNotes={A11Y}
      responsiveNotes={RESP}
    />
  );
}