import React from 'react';
import { PreviewScaffold } from '../../preview/PreviewScaffold';

export const InsightsPreview: React.FC = () => {
  const a11y = [
    'Profile progress bar maps explicit screen reader percentages.',
    'Metrics indicators use high-contrast text tags.'
  ];
  const resp = [
    'Insights split page layout collapses to single column on viewports < 1024px.',
    'Verification grids wrap gracefully on smaller displays.'
  ];
  return (
    <PreviewScaffold
      title="Grower Insights Dashboard"
      subtitle="Insights console displaying profile completions and smart AI grow recommendations."
      route="/dashboard/insights"
      accessibilityNotes={a11y}
      responsiveNotes={resp}
      approvalStatus="Approved"
    />
  );
};

export const PersonalizedPreview: React.FC = () => {
  const a11y = [
    'Digital assistant widget suggested questions are focusable and include action descriptions.',
    'Greeting panel details use descriptive text alternatives.'
  ];
  const resp = [
    'Metrics widgets layout wrap from 3-columns to 1-column on mobile.',
    'Digital Assistant floating chat drawer adjusts size dynamically to avoid overlapping content.'
  ];
  return (
    <PreviewScaffold
      title="Personalized Grower Homepage"
      subtitle="Intelligent personal hub listing continue learning modules, recent order status, and trending products."
      route="/dashboard/personalized"
      accessibilityNotes={a11y}
      responsiveNotes={resp}
      approvalStatus="Approved"
    />
  );
};

export const ActivityPreview: React.FC = () => {
  const a11y = [
    'Activity log timeline indicators use non-color signposts (icons).',
    'Category filter buttons use standard focus rings and tab indices.'
  ];
  const resp = [
    'Timeline circles bullet-line adjusts dynamically, keeping vertical alignments.',
    'Timeline cards wrap text logs proportionally.'
  ];
  return (
    <PreviewScaffold
      title="Unified Activity Feed"
      subtitle="Chronological feed logging activities across orders, courses, wishlist shelves, and support tickets."
      route="/dashboard/activity"
      accessibilityNotes={a11y}
      responsiveNotes={resp}
      approvalStatus="Approved"
    />
  );
};

export const AnalyticsPreview: React.FC = () => {
  const a11y = [
    'Harvest progress bars use semantic markup detailing value percentages.',
    'Gauges and target stats use readable, high-contrast label pairs.'
  ];
  const resp = [
    'Monthly yields progress grid scales fluidly on ultra-wide and mobile devices.',
    'Sterility indicators wrap cleanly.'
  ];
  return (
    <PreviewScaffold
      title="Grower Workspace Analytics"
      subtitle="Analytics console detailing monthly harvest metrics, sterility success ratios, and AI yield forecasts."
      route="/dashboard/analytics"
      accessibilityNotes={a11y}
      responsiveNotes={resp}
      approvalStatus="Approved"
    />
  );
};

export const MobileIntelligencePreview: React.FC = () => {
  const a11y = [
    'Touch area targets are expanded to at least 48x48px on mobile.',
    'Floating assistant trigger button supports high contrast outline states.'
  ];
  const resp = [
    'Insights widgets scale full width on compact screens.',
    'Loyalty point progress indicator centers layout.'
  ];
  return (
    <PreviewScaffold
      title="Mobile View — Grower Workspace"
      subtitle="Grower workspace mobile view under 390px viewport."
      route="/dashboard/personalized"
      accessibilityNotes={a11y}
      responsiveNotes={resp}
      approvalStatus="Approved"
    />
  );
};
export default InsightsPreview;
