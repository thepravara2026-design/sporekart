import React from 'react';
import { PreviewScaffold } from '../../preview/PreviewScaffold';

export const OrdersPreview: React.FC = () => {
  const a11y = [
    'Order cards list implements ARIA role="list" with elements as role="listitem".',
    'Status tags use semantic high-contrast borders and fulfill WCAG 2.2 AA (4.5:1).',
    'Loading shimmers match visual geometry, preventing Layout Shift (CLS = 0).',
    'Interactive filters use role="tab" and support keypress selections.',
    'Screen readers announce unread notification badges dynamically.'
  ];
  const resp = [
    'Orders grid adapts from 3-column (desktop) & 2-column (tablet) to stacked single column on mobile.',
    'Aggregate stats widgets wrap comfortably, scaling text using viewport units.',
    'Search input matches full-width relative bounds on mobile screen viewports.'
  ];
  return (
    <PreviewScaffold
      title="Orders Workspace Dashboard"
      subtitle="Interactive client panel displaying active/archived spawn, substrate, and equipment orders."
      route="/dashboard/orders"
      accessibilityNotes={a11y}
      responsiveNotes={resp}
      approvalStatus="Approved"
    />
  );
};

export const OrderDetailsPreview: React.FC = () => {
  const a11y = [
    'Timeline points are structured as chronological ordered list (<ol>).',
    'Invoice table implements clear table headers (<th>) with scope="col" attributes.',
    'Back buttons have pre-defined focus rings and clear text description.',
    'Billing/Shipping side-by-side blocks are parsed correctly in reading order.'
  ];
  const resp = [
    'Grand total breakdown stacks into single row lists at narrow width.',
    'Billing and Shipping details wrap from horizontal row to vertical stacked column layout.',
    'Table content becomes scrollable horizontally on ultra-narrow viewports to prevent overflow.'
  ];
  return (
    <PreviewScaffold
      title="Order Details Experience"
      subtitle="Detailed audit for individual order ORD-2026-8842, including step timeline, addresses, and calculations."
      route="/dashboard/orders/ORD-2026-8842"
      accessibilityNotes={a11y}
      responsiveNotes={resp}
      approvalStatus="Approved"
    />
  );
};

export const OrderTrackingPreview: React.FC = () => {
  const a11y = [
    'The SVG transit map is marked with aria-hidden="true" as it is decorative.',
    'Alternate text description summarizes map status: "Transit path from Bengaluru SporeHub to HSR Layout".',
    'Milestones list logs updates in chronological scanning order for assistive devices.',
    'Live scanning alerts use role="status" tags.'
  ];
  const resp = [
    'Map SVG adapts proportionally using CSS flexbox bounds, maintaining aspect ratio (500:350).',
    'Estimated delivery badge moves to top of page on mobile to optimize first scroll view.',
    'Scan list takes full width below map at <768px.'
  ];
  return (
    <PreviewScaffold
      title="Live Shipment Tracking"
      subtitle="Interactive Delhivery partner tracking page with stylized transit map showing Bengaluru-to-HSR route."
      route="/dashboard/orders/ORD-2026-8842/track"
      accessibilityNotes={a11y}
      responsiveNotes={resp}
      approvalStatus="Approved"
    />
  );
};

export const OrderRefundsPreview: React.FC = () => {
  const a11y = [
    'Uploader drag-and-drop zone has explicit keyboard trigger options.',
    'Form validation errors are wrapped in role="alert" containers.',
    'Active refund progress uses clear visual nodes announced by screen readers.',
    'Select dropdown has associated <label> pointing to ID.'
  ];
  const resp = [
    'Item choice list scales to fit touch targets (min 44x44px).',
    'Returns policy terms wrap inside a sidebar block matching responsive grid.',
    'Success screen shifts seamlessly without content shifting.'
  ];
  return (
    <PreviewScaffold
      title="Returns & Refunds Center"
      subtitle="Refund logs and return forms for ORD-2026-5541 (Refunded) showing transaction audit."
      route="/dashboard/orders/ORD-2026-5541/refund"
      accessibilityNotes={a11y}
      responsiveNotes={resp}
      approvalStatus="Approved"
    />
  );
};

export const MobileOrdersPreview: React.FC = () => {
  const a11y = [
    'All touch areas are expanded to 48x48px on mobile devices.',
    'Skip link remains active on keyboard focus.',
    'Drawer uses aria-modal="true" structure.'
  ];
  const resp = [
    'Aggregated dashboard metrics collapse to 1-column layout.',
    'Crumbs collapsed into ellipsis dropdown.',
    'Action buttons stack full-width at bottom of order cards.'
  ];
  return (
    <PreviewScaffold
      title="Mobile View — Orders Workspace"
      subtitle="Description of orders workspace under 390px mobile viewport."
      route="/dashboard/orders"
      accessibilityNotes={a11y}
      responsiveNotes={resp}
      approvalStatus="Approved"
    />
  );
};

export const TabletOrdersPreview: React.FC = () => {
  const a11y = [
    'Focus states are easily tabbed on external keyboards.',
    'Sidebar collapsible has appropriate controls.'
  ];
  const resp = [
    'Aggregate statistics wrap into 2x2 grid.',
    'Search bar expands to fill remaining space next to tabs.'
  ];
  return (
    <PreviewScaffold
      title="Tablet View — Orders Workspace"
      subtitle="Description of orders workspace under 834px tablet viewport."
      route="/dashboard/orders"
      accessibilityNotes={a11y}
      responsiveNotes={resp}
      approvalStatus="Approved"
    />
  );
};
export default OrdersPreview;
