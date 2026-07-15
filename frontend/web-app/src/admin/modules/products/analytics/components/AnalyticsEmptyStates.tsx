import React from 'react';

interface EmptyStateProps {
  icon: string;
  title: string;
  description: string;
}

const stateStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
  justifyContent: 'center',
  textAlign: 'center',
  gap: 12,
  padding: '64px 24px',
  minHeight: 280,
};

const iconBoxStyle: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  width: 64,
  height: 64,
  borderRadius: 'var(--radius-lg)',
  background: 'var(--color-bg-surface-raised)',
  fontSize: 28,
  marginBottom: 4,
};

const EmptyStateShell: React.FC<EmptyStateProps> = React.memo(({ icon, title, description }) => (
  <div style={stateStyle} role="status" aria-label={title}>
    <div style={iconBoxStyle} aria-hidden="true">{icon}</div>
    <h3 style={{ margin: 0, fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)', fontWeight: 600 }}>{title}</h3>
    <p style={{ margin: 0, maxWidth: 420, fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>{description}</p>
  </div>
));

export const NoAnalyticsData: React.FC = React.memo(() => (
  <EmptyStateShell icon="📊" title="No analytics data" description="Analytics data is not available yet. Data will appear once products are created and processed." />
));

export const NoReports: React.FC = React.memo(() => (
  <EmptyStateShell icon="📄" title="No reports" description="No reports have been generated yet. Generate your first report to see it here." />
));

export const NoInsights: React.FC = React.memo(() => (
  <EmptyStateShell icon="💡" title="No insights" description="No insights or recommendations are available at this time. Insights appear as data is analyzed." />
));

export const PermissionDenied: React.FC = React.memo(() => (
  <EmptyStateShell icon="🛡️" title="Access restricted" description="You don't have permission to view this analytics section. Contact an administrator for access." />
));

export const OfflineState: React.FC = React.memo(() => (
  <EmptyStateShell icon="🌐" title="You're offline" description="Analytics can't load right now. Check your connection and try again." />
));

export const MaintenanceState: React.FC = React.memo(() => (
  <EmptyStateShell icon="⚙️" title="Under maintenance" description="The analytics module is temporarily unavailable while we perform maintenance." />
));

export default { NoAnalyticsData, NoReports, NoInsights, PermissionDenied, OfflineState, MaintenanceState };
