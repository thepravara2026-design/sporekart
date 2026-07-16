import { useState } from 'react';
import { CertificateProvider } from '../state/CertificateContext';
import { CERTIFICATE_NAV_ITEMS } from '../types';
import { CertificateDashboardPage } from './CertificateDashboardPage';
import { CertificateRegistryPage } from './CertificateRegistryPage';
import { CredentialWalletPage } from './CredentialWalletPage';
import { AchievementCenterPage } from './AchievementCenterPage';
import { DigitalBadgesPage } from './DigitalBadgesPage';
import { AcademicTranscriptPage } from './AcademicTranscriptPage';
import { VerificationCenterPage } from './VerificationCenterPage';
import { CertificateAnalyticsPage } from './CertificateAnalyticsPage';

function CertificateIndexInner() {
  const [activeSection, setActiveSection] = useState('certificates');

  const renderSection = () => {
    switch (activeSection) {
      case 'certificates/registry': return <CertificateRegistryPage />;
      case 'certificates/wallet': return <CredentialWalletPage />;
      case 'certificates/achievements': return <AchievementCenterPage />;
      case 'certificates/badges': return <DigitalBadgesPage />;
      case 'certificates/transcript': return <AcademicTranscriptPage />;
      case 'certificates/verification': return <VerificationCenterPage />;
      case 'certificates/analytics': return <CertificateAnalyticsPage />;
      default: return <CertificateDashboardPage />;
    }
  };

  return (
    <div>
      <nav aria-label="Certificate sections" style={{ display: 'flex', gap: 4, flexWrap: 'wrap', marginBottom: 'var(--space-section-gap)', padding: 4, borderRadius: 'var(--radius-md)', background: 'var(--color-bg-surface-default)', border: '1px solid var(--color-border-default)' }}>
        {CERTIFICATE_NAV_ITEMS.map((item) => (
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

export function CertificateIndex() {
  return (
    <CertificateProvider>
      <CertificateIndexInner />
    </CertificateProvider>
  );
}

export default CertificateIndex;
