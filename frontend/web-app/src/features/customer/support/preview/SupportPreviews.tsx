import React from 'react';
import { PreviewScaffold } from '../../preview/PreviewScaffold';

export const SupportDashboardPreview: React.FC = () => {
  const a11y = [
    'Quick action widgets have explicit focus outlines and support tab indices.',
    'Metrics indicators use readable values and are marked with descriptive text labels.'
  ];
  const resp = [
    'Metrics block scales down to 2x2 grid on tablet viewports.',
    'helpline calendars and AI assistant columns stack vertically below the active course panel.'
  ];
  return (
    <PreviewScaffold
      title="Support Center Dashboard"
      subtitle="Interactive client panel listing open tickets, knowledge base articles, and live helplines."
      route="/dashboard/support"
      accessibilityNotes={a11y}
      responsiveNotes={resp}
      approvalStatus="Approved"
    />
  );
};

export const TicketsPreview: React.FC = () => {
  const a11y = [
    'Reply message box textarea is linked to a form label.',
    'Keyboard users can focus and submit text messages using Enter.',
    'Status update buttons include clear text indicators.'
  ];
  const resp = [
    'Chat timeline adapts fluidly, wrapping message bubbles inside responsive layout widths.',
    'Right panel sidebar stacks below the chat timeline on viewports <1024px.'
  ];
  return (
    <PreviewScaffold
      title="Support Tickets Timeline"
      subtitle="Interactive conversation timeline listing customer and expert agent logs."
      route="/dashboard/support/tickets/TKT-2026-4412"
      accessibilityNotes={a11y}
      responsiveNotes={resp}
      approvalStatus="Approved"
    />
  );
};

export const KBPreview: React.FC = () => {
  const a11y = [
    'Was this helpful Yes/No buttons are fully focusable and include tooltips.',
    'Article body uses semantic, high-contrast readable type typography.'
  ];
  const resp = [
    'Related articles sidebar moves to the bottom of the page on mobile viewports.',
    'Prose content adapts proportionally to the screen width.'
  ];
  return (
    <PreviewScaffold
      title="Knowledge Base Article Reader"
      subtitle="Searchable article browser showing sterile technique procedures and helpfulness rating stubs."
      route="/dashboard/support/help-center"
      accessibilityNotes={a11y}
      responsiveNotes={resp}
      approvalStatus="Approved"
    />
  );
};

export const MobileSupportPreview: React.FC = () => {
  const a11y = [
    'All click target sizes exceed 48x48px on mobile.',
    'Skip links are active under focus states.'
  ];
  const resp = [
    'Support overview grid scales to single vertical column.',
    'Chat bubbles take 85% width on mobile screens to preserve line width.'
  ];
  return (
    <PreviewScaffold
      title="Mobile View — Support Center"
      subtitle="Support center mobile view under 390px viewport."
      route="/dashboard/support"
      accessibilityNotes={a11y}
      responsiveNotes={resp}
      approvalStatus="Approved"
    />
  );
};
export default SupportDashboardPreview;
