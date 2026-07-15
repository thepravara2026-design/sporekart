import React from 'react';
import { PreviewScaffold } from '../../preview/PreviewScaffold';

export const WishlistPreview: React.FC = () => {
  const a11y = [
    'Sub-navigation links use semantic role="tab" and support key navigation.',
    'Product cards inside grid lists implement screen-readable status descriptors.',
    'Out of Stock buttons have disabled attributes, preventing keyboard triggers.',
    'Remove icons have clear aria-label="Remove item" labels.'
  ];
  const resp = [
    'Product cards resize from 4-column layout down to 1-column mobile viewports.',
    'Search bar and sort dropdown shift from single row layout to vertical stacking on mobile.',
    'Saved Cart list items collapse horizontally into cards.'
  ];
  return (
    <PreviewScaffold
      title="Wishlist & Browsing History Workspace"
      subtitle="Interactive client panel detailing saved cultivars, shopping checklists, and relative timestamps."
      route="/dashboard/wishlist"
      accessibilityNotes={a11y}
      responsiveNotes={resp}
      approvalStatus="Approved"
    />
  );
};

export const RecommendationsPreview: React.FC = () => {
  const a11y = [
    'Cultivation setup switcher buttons use high-contrast outline states.',
    'Dynamically generated AI recommended grid changes are announced via screen readers.',
    'Buy items action triggers clear feedback toast announcements.'
  ];
  const resp = [
    'AI simulator selection row wraps fluidly across multiple lines on smaller viewports.',
    'Recommendation shelves adapt to touch screens with swipable horizontal scrolling or standard grid stacking.'
  ];
  return (
    <PreviewScaffold
      title="Personalized Recommendations"
      subtitle="Personalization engine simulation based on monotub, cleanroom agar, or log bedding choices."
      route="/dashboard/recommendations"
      accessibilityNotes={a11y}
      responsiveNotes={resp}
      approvalStatus="Approved"
    />
  );
};

export const NotificationsPreview: React.FC = () => {
  const a11y = [
    'Unread status indicator badges use explicit screen reader descriptions.',
    'Search filter inputs link to search labels.',
    'Notifications list supports tab controls to navigate and dismiss updates.',
    'Read/Unread toggle status updates are announced dynamically.'
  ];
  const resp = [
    'Filter categories shift into responsive flex wrap columns.',
    'Notification content cards adapt, wrapping lengthy text logs securely.',
    'Delete button targets remain >= 44x44px.'
  ];
  return (
    <PreviewScaffold
      title="Notification Center Feed"
      subtitle="Enterprise system notification feed, listing order tracking updates, certification achievements, and promos."
      route="/dashboard/notifications"
      accessibilityNotes={a11y}
      responsiveNotes={resp}
      approvalStatus="Approved"
    />
  );
};

export const EngagementPreview: React.FC = () => {
  const a11y = [
    'Points progress bar maps aria-valuenow, aria-valuemin, and aria-valuemax properties.',
    'Achievements wall details dates in clear, accessible text format.',
    'Loyalty rewards coupons include simple redeem actions.'
  ];
  const resp = [
    'Loyalty cards stack horizontally, shifting to 1-column layout at 768px.',
    'Progress bar dynamically sizes relative to parent container width.'
  ];
  return (
    <PreviewScaffold
      title="Loyalty & Engagement Hub"
      subtitle="Membership progress dashboard, points redeeming store, and certified grower achievements wall."
      route="/dashboard/engagement"
      accessibilityNotes={a11y}
      responsiveNotes={resp}
      approvalStatus="Approved"
    />
  );
};
export default WishlistPreview;
