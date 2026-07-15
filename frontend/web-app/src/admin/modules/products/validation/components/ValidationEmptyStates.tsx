import React from 'react';

const container: React.CSSProperties = {
  display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center',
  padding: '64px 24px', textAlign: 'center', gap: 12,
};

function EmptyState({ icon, title, desc }: { icon: string; title: string; desc: string }) {
  return (
    <div style={container} role="status">
      <div style={{ fontSize: 48, opacity: 0.3 }} aria-hidden="true">{icon}</div>
      <div style={{ fontSize: 'var(--text-h4)', color: 'var(--color-text-primary)', fontWeight: 600 }}>{title}</div>
      <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', maxWidth: 360 }}>{desc}</div>
    </div>
  );
}

export const NoValidationData: React.FC = () => (
  <EmptyState icon="📋" title="No Products to Validate" desc="All products have been validated. New products will appear here when they need validation." />
);

export const NoReports: React.FC = () => (
  <EmptyState icon="📄" title="No Reports Generated" desc="No validation reports have been generated yet. Run a validation to create reports." />
);

export const NoComplianceData: React.FC = () => (
  <EmptyState icon="🛡️" title="No Compliance Data" desc="Compliance checks have not been performed. Configure compliance rules and run checks to see data." />
);

export const NoCertifications: React.FC = () => (
  <EmptyState icon="🏅" title="No Certifications" desc="No products have been certified yet. Products need to pass validation before certification." />
);

export const PermissionDenied: React.FC = () => (
  <EmptyState icon="🔒" title="Access Denied" desc="You do not have permission to access this section. Contact your administrator." />
);

export const OfflineState: React.FC = () => (
  <EmptyState icon="📡" title="You Are Offline" desc="Validation data is unavailable while offline. Check your connection and try again." />
);

export const MaintenanceState: React.FC = () => (
  <EmptyState icon="🔧" title="Under Maintenance" desc="The validation system is currently undergoing maintenance. Please check back later." />
);
