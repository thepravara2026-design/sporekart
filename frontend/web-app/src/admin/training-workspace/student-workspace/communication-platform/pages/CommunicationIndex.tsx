import { CommunicationProvider } from '../state/CommunicationContext';
import { useState } from 'react';
import { COMMUNICATION_NAV_ITEMS } from '../types';
import EngagementDashboard from './EngagementDashboard';
import NotificationCenter from './NotificationCenter';
import AnnouncementCenter from './AnnouncementCenter';
import StudentInbox from './StudentInbox';
import CommunicationTimeline from './CommunicationTimeline';
import NotificationTemplates from './NotificationTemplates';
import CommunicationAnalytics from './CommunicationAnalytics';
import CommunicationPreferences from './CommunicationPreferences';

function CommunicationIndexInner() {
  const [activeSection, setActiveSection] = useState('communication');

  const renderSection = () => {
    switch (activeSection) {
      case 'communication/notifications': return <NotificationCenter />;
      case 'communication/announcements': return <AnnouncementCenter />;
      case 'communication/inbox': return <StudentInbox />;
      case 'communication/timeline': return <CommunicationTimeline />;
      case 'communication/templates': return <NotificationTemplates />;
      case 'communication/analytics': return <CommunicationAnalytics />;
      case 'communication/preferences': return <CommunicationPreferences />;
      default: return <EngagementDashboard />;
    }
  };

  return (
    <div>
      <nav aria-label="Communication sections" style={{ display: 'flex', gap: 4, flexWrap: 'wrap', marginBottom: 'var(--space-section-gap)', padding: 4, borderRadius: 'var(--radius-md)', background: 'var(--color-bg-surface-default)', border: '1px solid var(--color-border-default)' }}>
        {COMMUNICATION_NAV_ITEMS.map((item) => (
          <button
            key={item.id}
            onClick={() => setActiveSection(item.id)}
            aria-current={activeSection === item.id ? 'page' : undefined}
            style={{
              padding: '6px 14px', borderRadius: 'var(--radius-sm)', border: 'none', cursor: 'pointer',
              background: activeSection === item.id ? 'var(--color-bg-primary-subtle)' : 'transparent',
              color: activeSection === item.id ? 'var(--color-primary)' : 'var(--color-text-secondary)',
              fontWeight: activeSection === item.id ? 'var(--weight-semibold)' : 'var(--weight-normal)',
              fontSize: 'var(--text-body-sm)', whiteSpace: 'nowrap',
            }}
          >
            {item.label}
          </button>
        ))}
      </nav>
      <div>{renderSection()}</div>
    </div>
  );
}

export function CommunicationIndex() {
  return (
    <CommunicationProvider>
      <CommunicationIndexInner />
    </CommunicationProvider>
  );
}

export default CommunicationIndex;
