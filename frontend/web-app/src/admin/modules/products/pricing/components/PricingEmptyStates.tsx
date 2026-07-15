import React from 'react';

const containerStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
  justifyContent: 'center',
  padding: '64px 24px',
  textAlign: 'center',
  gap: 12,
};

const iconStyle: React.CSSProperties = {
  fontSize: 48,
  opacity: 0.3,
  marginBottom: 8,
};

const titleStyle: React.CSSProperties = {
  fontSize: 'var(--text-h4)',
  color: 'var(--color-text-primary)',
  fontWeight: 600,
};

const descStyle: React.CSSProperties = {
  fontSize: 'var(--text-body-sm)',
  color: 'var(--color-text-secondary)',
  maxWidth: 360,
};

interface EmptyStateProps {
  icon: string;
  title: string;
  description: string;
}

const EmptyState: React.FC<EmptyStateProps> = ({ icon, title, description }) => (
  <div style={containerStyle}>
    <div style={iconStyle} aria-hidden="true">{icon}</div>
    <div style={titleStyle}>{title}</div>
    <div style={descStyle}>{description}</div>
  </div>
);

export const NoPricing: React.FC = () => (
  <EmptyState icon="🏷️" title="No Pricing Found" description="No pricing data is available. Add products to get started with pricing management." />
);

export const NoDiscounts: React.FC = () => (
  <EmptyState icon="💯" title="No Discount Rules" description="No discount rules have been created yet. Create your first discount rule to start offering promotions." />
);

export const NoCampaigns: React.FC = () => (
  <EmptyState icon="📢" title="No Campaigns" description="No promotional campaigns are currently active. Schedule a campaign to drive sales." />
);

export const NoScheduledPricing: React.FC = () => (
  <EmptyState icon="📅" title="No Scheduled Pricing" description="No pricing schedules have been created. Schedule pricing changes for future dates." />
);

export const NoHistory: React.FC = () => (
  <EmptyState icon="🕐" title="No Price History" description="No price changes have been recorded yet. History will appear here when prices are updated." />
);

export const NoSearchResults: React.FC = () => (
  <EmptyState icon="🔍" title="No Results Found" description="No pricing data matches your search criteria. Try adjusting your search terms or filters." />
);

export const PermissionDenied: React.FC = () => (
  <EmptyState icon="🔒" title="Access Denied" description="You do not have permission to access this section. Contact your administrator for access." />
);

export const PricingEmptyStates: Record<string, React.FC> = {
  noPricing: NoPricing,
  noDiscounts: NoDiscounts,
  noCampaigns: NoCampaigns,
  noScheduled: NoScheduledPricing,
  noHistory: NoHistory,
  noSearch: NoSearchResults,
  permissionDenied: PermissionDenied,
};
