import { useCertificates } from '../state/CertificateContext';
import { AnalyticsPanel } from '../components/AnalyticsPanel';
import { CertificateDashboardSkeleton } from '../components/Skeletons';

export function CertificateAnalyticsPage() {
  const { analytics } = useCertificates();

  if (!analytics) return <CertificateDashboardSkeleton />;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Certificate Analytics</h2>
      <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-tertiary)', margin: 0 }}>
        Detailed analytics on certificate issuance, verification, achievements, and badge distribution
      </p>
      <AnalyticsPanel analytics={analytics} />
    </div>
  );
}
