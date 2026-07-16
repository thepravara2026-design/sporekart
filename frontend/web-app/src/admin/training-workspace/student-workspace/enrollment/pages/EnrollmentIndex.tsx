import { useState } from 'react';
import { EnrollmentProvider } from '../state/EnrollmentContext';
import { ENROLLMENT_NAV_ITEMS } from '../types';
import { EnrollmentDashboardPage } from './EnrollmentDashboardPage';
import { EnrollmentRequestsPage } from './EnrollmentRequestsPage';
import { EnrollmentApprovalQueuePage } from './EnrollmentApprovalQueuePage';
import { EnrollmentBatchManagementPage } from './EnrollmentBatchManagementPage';
import { EnrollmentCapacityDashboardPage } from './EnrollmentCapacityDashboardPage';
import { EnrollmentTimelinePage } from './EnrollmentTimelinePage';
import { EnrollmentArchivedPage } from './EnrollmentArchivedPage';

function EnrollmentIndexInner() {
  const [activeSection, setActiveSection] = useState('enrollment');

  const renderSection = () => {
    switch (activeSection) {
      case 'enrollment/requests': return <EnrollmentRequestsPage />;
      case 'enrollment/approval': return <EnrollmentApprovalQueuePage />;
      case 'enrollment/batches': return <EnrollmentBatchManagementPage />;
      case 'enrollment/capacity': return <EnrollmentCapacityDashboardPage />;
      case 'enrollment/timeline': return <EnrollmentTimelinePage />;
      case 'enrollment/archived': return <EnrollmentArchivedPage />;
      default: return <EnrollmentDashboardPage />;
    }
  };

  return (
    <div className="enrollment-index">
      <nav className="enrollment-subnav" aria-label="Enrollment sections" style={{
        display: 'flex', gap: 4, flexWrap: 'wrap', marginBottom: 'var(--space-section-gap)',
        padding: '4px', borderRadius: 'var(--radius-md)',
        background: 'var(--color-bg-surface-default)',
        border: '1px solid var(--color-border-default)',
      }}>
        {ENROLLMENT_NAV_ITEMS.map((item) => (
          <button
            key={item.id}
            className={`enrollment-subnav__item ${activeSection === item.id ? 'enrollment-subnav__item--active' : ''}`}
            onClick={() => setActiveSection(item.id)}
            aria-current={activeSection === item.id ? 'page' : undefined}
            style={{
              padding: '6px 14px', borderRadius: 'var(--radius-sm)',
              border: 'none', cursor: 'pointer',
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
      <div className="enrollment-index__content">
        {renderSection()}
      </div>
    </div>
  );
}

export function EnrollmentIndex() {
  return (
    <EnrollmentProvider>
      <EnrollmentIndexInner />
    </EnrollmentProvider>
  );
}

export default EnrollmentIndex;
