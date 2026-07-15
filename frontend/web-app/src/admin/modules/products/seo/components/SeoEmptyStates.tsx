import React from 'react';

const c: React.CSSProperties = { display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', padding: '64px 24px', textAlign: 'center', gap: 12 };

function E({ icon, title, desc }: { icon: string; title: string; desc: string }) {
  return <div style={c}><div style={{ fontSize: 48, opacity: 0.3 }} aria-hidden="true">{icon}</div><div style={{ fontSize: 'var(--text-h4)', color: 'var(--color-text-primary)', fontWeight: 600 }}>{title}</div><div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', maxWidth: 360 }}>{desc}</div></div>;
}

export const NoSeoData: React.FC = () => <E icon="🔍" title="No SEO Data" desc="No SEO metadata has been created for any products. Start by setting up meta information." />;
export const NoMarketplaceData: React.FC = () => <E icon="🛒" title="No Marketplace Data" desc="No marketplace readiness information available. Configure marketplace settings to get started." />;
export const NoValidationData: React.FC = () => <E icon="✅" title="No Validation Results" desc="No validation checks have been run. Run a validation scan to identify SEO issues." />;
export const NoSearchResults: React.FC = () => <E icon="🔍" title="No Results Found" desc="No SEO entries match your search criteria. Try adjusting your search terms." />;
export const PermissionDenied: React.FC = () => <E icon="🔒" title="Access Denied" desc="You do not have permission to access this section. Contact your administrator." />;
