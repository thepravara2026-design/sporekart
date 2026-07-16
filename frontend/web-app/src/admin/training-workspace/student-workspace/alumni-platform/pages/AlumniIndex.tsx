import { AlumniProvider } from '../state/AlumniContext';
import { useState } from 'react';
import { ALUMNI_NAV_ITEMS } from '../types';
import AlumniDashboard from './AlumniDashboard';
import PlacementHub from './PlacementHub';
import InternshipCenter from './InternshipCenter';
import CareerDevelopmentCenter from './CareerDevelopmentCenter';
import CompanyPartnerships from './CompanyPartnerships';
import JobOpportunities from './JobOpportunities';
import AlumniDirectory from './AlumniDirectory';
import AlumniMentorship from './AlumniMentorship';
import AlumniEvents from './AlumniEvents';
import AlumniContributions from './AlumniContributions';
import PlacementAnalytics from './PlacementAnalytics';

function AlumniIndexInner() {
  const [activeSection, setActiveSection] = useState('alumni');

  const renderSection = () => {
    switch (activeSection) {
      case 'alumni/placement': return <PlacementHub />;
      case 'alumni/internships': return <InternshipCenter />;
      case 'alumni/career': return <CareerDevelopmentCenter />;
      case 'alumni/companies': return <CompanyPartnerships />;
      case 'alumni/jobs': return <JobOpportunities />;
      case 'alumni/directory': return <AlumniDirectory />;
      case 'alumni/mentorship': return <AlumniMentorship />;
      case 'alumni/events': return <AlumniEvents />;
      case 'alumni/contributions': return <AlumniContributions />;
      case 'alumni/analytics': return <PlacementAnalytics />;
      default: return <AlumniDashboard />;
    }
  };

  return (
    <div>
      <nav aria-label="Alumni sections" style={{ display: 'flex', gap: 4, flexWrap: 'wrap', marginBottom: 'var(--space-section-gap)', padding: 4, borderRadius: 'var(--radius-md)', background: 'var(--color-bg-surface-default)', border: '1px solid var(--color-border-default)' }}>
        {ALUMNI_NAV_ITEMS.map((item) => (
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

export function AlumniIndex() {
  return (
    <AlumniProvider>
      <AlumniIndexInner />
    </AlumniProvider>
  );
}

export default AlumniIndex;
