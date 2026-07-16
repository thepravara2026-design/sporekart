import { useState } from 'react';
import { LearningProgressProvider } from '../state/LearningProgressContext';
import { LEARNING_PROGRESS_NAV_ITEMS } from '../types';
import { LearningProgressDashboardPage } from './LearningProgressDashboardPage';
import { StudentProgressPage } from './StudentProgressPage';
import { CourseProgressPage } from './CourseProgressPage';
import { ModuleProgressPage } from './ModuleProgressPage';
import { CompetencyCenterPage } from './CompetencyCenterPage';
import { MilestoneCenterPage } from './MilestoneCenterPage';
import { LearningTimelinePage } from './LearningTimelinePage';
import { CertificationReadinessPage } from './CertificationReadinessPage';
import { AnalyticsPage } from './AnalyticsPage';

function LearningProgressIndexInner() {
  const [activeSection, setActiveSection] = useState('learning-progress');

  const renderSection = () => {
    switch (activeSection) {
      case 'learning-progress/students': return <StudentProgressPage />;
      case 'learning-progress/courses': return <CourseProgressPage />;
      case 'learning-progress/modules': return <ModuleProgressPage />;
      case 'learning-progress/competencies': return <CompetencyCenterPage />;
      case 'learning-progress/milestones': return <MilestoneCenterPage />;
      case 'learning-progress/timeline': return <LearningTimelinePage />;
      case 'learning-progress/certification': return <CertificationReadinessPage />;
      case 'learning-progress/analytics': return <AnalyticsPage />;
      default: return <LearningProgressDashboardPage />;
    }
  };

  return (
    <div>
      <nav aria-label="Learning Progress sections" style={{ display: 'flex', gap: 4, flexWrap: 'wrap', marginBottom: 'var(--space-section-gap)', padding: 4, borderRadius: 'var(--radius-md)', background: 'var(--color-bg-surface-default)', border: '1px solid var(--color-border-default)' }}>
        {LEARNING_PROGRESS_NAV_ITEMS.map((item) => (
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

export function LearningProgressIndex() {
  return (
    <LearningProgressProvider>
      <LearningProgressIndexInner />
    </LearningProgressProvider>
  );
}

export default LearningProgressIndex;
