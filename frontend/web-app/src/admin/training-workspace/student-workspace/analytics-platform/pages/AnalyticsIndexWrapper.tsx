import { useState } from 'react';
import { AnalyticsProvider } from '../state/AnalyticsContext';
import { ANALYTICS_NAV_ITEMS } from '../types';
import ExecutiveDashboard from './ExecutiveDashboard';
import StudentAnalytics from './StudentAnalytics';
import CourseAnalytics from './CourseAnalytics';
import TrainerAnalytics from './TrainerAnalytics';
import BatchAnalytics from './BatchAnalytics';
import AcademicIntelligence from './AcademicIntelligence';
import LearningIntelligence from './LearningIntelligence';

function AnalyticsIndexInner() {
  const [activeSection, setActiveSection] = useState('analytics');

  const renderSection = () => {
    switch (activeSection) {
      case 'analytics/students': return <StudentAnalytics />;
      case 'analytics/courses': return <CourseAnalytics />;
      case 'analytics/trainers': return <TrainerAnalytics />;
      case 'analytics/batches': return <BatchAnalytics />;
      case 'analytics/academic': return <AcademicIntelligence />;
      case 'analytics/learning-intelligence': return <LearningIntelligence />;
      default: return <ExecutiveDashboard />;
    }
  };

  return (
    <div>
      <nav aria-label="Analytics sections" style={{ display: 'flex', gap: 4, flexWrap: 'wrap', marginBottom: 'var(--space-section-gap)', padding: 4, borderRadius: 'var(--radius-md)', background: 'var(--color-bg-surface-default)', border: '1px solid var(--color-border-default)' }}>
        {ANALYTICS_NAV_ITEMS.map((item) => (
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

export function AnalyticsIndex() {
  return (
    <AnalyticsProvider>
      <AnalyticsIndexInner />
    </AnalyticsProvider>
  );
}

export default AnalyticsIndex;
