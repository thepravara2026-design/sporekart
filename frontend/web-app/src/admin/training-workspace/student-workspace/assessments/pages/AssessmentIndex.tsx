import { useState } from 'react';
import { AssessmentProvider } from '../state/AssessmentContext';
import { ASSESSMENT_NAV_ITEMS } from '../types';
import { AssessmentDashboardPage } from './AssessmentDashboardPage';
import { AssessmentRegistryPage } from './AssessmentRegistryPage';
import { AssessmentBuilderPage } from './AssessmentBuilderPage';
import { QuestionBankPage } from './QuestionBankPage';
import { ExaminationCenterPage } from './ExaminationCenterPage';
import { ResultCenterPage } from './ResultCenterPage';
import { AssessmentAnalyticsPage } from './AssessmentAnalyticsPage';
import { AssessmentArchivedPage } from './AssessmentArchivedPage';

function AssessmentIndexInner() {
  const [activeSection, setActiveSection] = useState('assessments');

  const renderSection = () => {
    switch (activeSection) {
      case 'assessments/registry': return <AssessmentRegistryPage />;
      case 'assessments/builder': return <AssessmentBuilderPage />;
      case 'assessments/question-bank': return <QuestionBankPage />;
      case 'assessments/examinations': return <ExaminationCenterPage />;
      case 'assessments/results': return <ResultCenterPage />;
      case 'assessments/analytics': return <AssessmentAnalyticsPage />;
      case 'assessments/archived': return <AssessmentArchivedPage />;
      default: return <AssessmentDashboardPage />;
    }
  };

  return (
    <div>
      <nav aria-label="Assessment sections" style={{ display: 'flex', gap: 4, flexWrap: 'wrap', marginBottom: 'var(--space-section-gap)', padding: 4, borderRadius: 'var(--radius-md)', background: 'var(--color-bg-surface-default)', border: '1px solid var(--color-border-default)' }}>
        {ASSESSMENT_NAV_ITEMS.map((item) => (
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

export function AssessmentIndex() {
  return (
    <AssessmentProvider>
      <AssessmentIndexInner />
    </AssessmentProvider>
  );
}

export default AssessmentIndex;
