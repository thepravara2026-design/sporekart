import { useCertificates } from '../state/CertificateContext';
import { DashboardWidget } from '../components/DashboardWidget';
import { CertificateCard } from '../components/CertificateCard';
import { AchievementCard } from '../components/AchievementCard';
import { CertificateDashboardSkeleton } from '../components/Skeletons';

export function CertificateDashboardPage() {
  const { dashboard } = useCertificates();

  if (!dashboard) return <CertificateDashboardSkeleton />;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Certificate Dashboard</h2>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <DashboardWidget label="Certificates Issued" value={dashboard.certificatesIssued} variant="success" subtitle="Total issued" />
        <DashboardWidget label="Pending" value={dashboard.pendingCertificates} variant="warning" subtitle="Awaiting approval" />
        <DashboardWidget label="Verified" value={dashboard.verifiedCertificates} variant="info" subtitle="Verified credentials" />
        <DashboardWidget label="Revoked" value={dashboard.revokedCertificates} variant={dashboard.revokedCertificates > 0 ? 'danger' : 'default'} subtitle="Revoked certificates" />
        <DashboardWidget label="Digital Badges" value={dashboard.digitalBadges} variant="default" subtitle="Badge count" />
        <DashboardWidget label="Achievements" value={dashboard.achievements} variant="default" subtitle="All achievements" />
        <DashboardWidget label="Wallets" value={dashboard.credentialWallets} variant="info" subtitle="Active credential wallets" />
        <DashboardWidget label="Transcripts" value={dashboard.transcriptCount} variant="default" subtitle="Academic transcripts" />
        <DashboardWidget label="Verification Requests" value={dashboard.verificationRequests} variant="warning" subtitle="Pending requests" />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 'var(--space-component-gap)' }}>
        <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
          <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: '0 0 12px 0' }}>Recent Certificates</h3>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
            {dashboard.recentCertificates.length === 0 ? (
              <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-tertiary)' }}>No recent certificates.</div>
            ) : (
              dashboard.recentCertificates.map((c) => (
                <CertificateCard key={c.id} certificate={c} />
              ))
            )}
          </div>
        </div>
        <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
          <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: '0 0 12px 0' }}>Recent Achievements</h3>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
            {dashboard.recentAchievements.length === 0 ? (
              <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-tertiary)' }}>No recent achievements.</div>
            ) : (
              dashboard.recentAchievements.map((a) => (
                <AchievementCard key={a.id} achievement={a} />
              ))
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
