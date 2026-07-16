import { memo } from 'react';
import type { AlumniContribution } from '../types';
import { CONTRIBUTION_TYPE_LABELS } from '../types';

const STATUS_COLORS: Record<string, { bg: string; color: string }> = {
  acknowledged: { bg: '#f0fdf4', color: '#16a34a' },
  'pending-acknowledgment': { bg: '#fefce8', color: '#ca8a04' },
  featured: { bg: '#eff6ff', color: '#2563eb' },
};

export const ContributionCard = memo(function ContributionCard({ contribution }: { contribution: AlumniContribution }) {
  const sc = STATUS_COLORS[contribution.status] || { bg: '#f3f4f6', color: '#6b7280' };
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 6, padding: 12, borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 4 }}>
        <span style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-body)' }}>{contribution.alumniName}</span>
        <span style={{ padding: '1px 6px', borderRadius: 'var(--radius-full)', fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)', background: sc.bg, color: sc.color }}>{contribution.status.replace(/-/g, ' ')}</span>
      </div>
      <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>{CONTRIBUTION_TYPE_LABELS[contribution.type]}</span>
      <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-tertiary)' }}>{contribution.description}</span>
      <div style={{ display: 'flex', gap: 12, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
        <span>Value: {contribution.value}</span>
        <span>Date: {contribution.date}</span>
      </div>
    </div>
  );
});
