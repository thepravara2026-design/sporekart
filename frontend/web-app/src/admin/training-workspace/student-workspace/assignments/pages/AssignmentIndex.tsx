import { useState } from 'react';
import { AssignmentProvider } from '../state/AssignmentContext';
import { ASSIGNMENT_NAV_ITEMS } from '../types';
import { AssignmentDashboardPage } from './AssignmentDashboardPage';
import { AssignmentRegistryPage } from './AssignmentRegistryPage';
import { AssignmentProjectsPage } from './AssignmentProjectsPage';
import { AssignmentSubmissionsPage } from './AssignmentSubmissionsPage';
import { AssignmentEvaluationsPage } from './AssignmentEvaluationsPage';
import { AssignmentTimelinePage } from './AssignmentTimelinePage';
import { AssignmentAnalyticsPage } from './AssignmentAnalyticsPage';
import { AssignmentArchivedPage } from './AssignmentArchivedPage';

function AssignmentIndexInner() {
  const [activeSection, setActiveSection] = useState('assignments');

  const renderSection = () => {
    switch (activeSection) {
      case 'assignments/registry': return <AssignmentRegistryPage />;
      case 'assignments/projects': return <AssignmentProjectsPage />;
      case 'assignments/submissions': return <AssignmentSubmissionsPage />;
      case 'assignments/evaluations': return <AssignmentEvaluationsPage />;
      case 'assignments/timeline': return <AssignmentTimelinePage />;
      case 'assignments/analytics': return <AssignmentAnalyticsPage />;
      case 'assignments/archived': return <AssignmentArchivedPage />;
      default: return <AssignmentDashboardPage />;
    }
  };

  return (
    <div>
      <nav aria-label="Assignment sections" style={{ display: 'flex', gap: 4, flexWrap: 'wrap', marginBottom: 'var(--space-section-gap)', padding: 4, borderRadius: 'var(--radius-md)', background: 'var(--color-bg-surface-default)', border: '1px solid var(--color-border-default)' }}>
        {ASSIGNMENT_NAV_ITEMS.map((item) => (
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

export function AssignmentIndex() {
  return (
    <AssignmentProvider>
      <AssignmentIndexInner />
    </AssignmentProvider>
  );
}

export default AssignmentIndex;
