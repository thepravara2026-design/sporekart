import { memo } from 'react';
import type { CompanyPartnership } from '../types';
import { TierBadge } from './TierBadge';

const STATUS_COLORS: Record<string, string> = { active: '#16a34a', inactive: '#6b7280', suspended: '#dc2626' };

export const CompanyCard = memo(function CompanyCard({ company }: { company: CompanyPartnership }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 8, padding: 12, borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 4 }}>
        <span style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-body)' }}>{company.companyName}</span>
        <TierBadge tier={company.tier} />
      </div>
      <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>{company.industry}</span>
      <div style={{ display: 'flex', gap: 16, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
        <span>Hires: {company.totalHires}</span>
        <span>Internships: {company.totalInternshipsOffered}</span>
        <span>Drives: {company.totalPlacementDrives}</span>
      </div>
      <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
        Partner since: {company.partnershipDate}
        <span style={{ marginLeft: 12, color: STATUS_COLORS[company.status] }}>● {company.status}</span>
      </div>
    </div>
  );
});
