import { memo } from 'react';
import { ENGAGEMENT_LABELS, ENGAGEMENT_VARIANTS } from '../types';

export const EngagementBadge = memo(function EngagementBadge({ level }: { level: string }) {
  const v = ENGAGEMENT_VARIANTS[level] || { bg: '#f3f4f6', color: '#6b7280' };
  const label = ENGAGEMENT_LABELS[level as keyof typeof ENGAGEMENT_LABELS] || level;
  return (
    <span style={{
      display: 'inline-flex', alignItems: 'center', padding: '2px 8px', borderRadius: 'var(--radius-full)',
      fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)', background: v.bg, color: v.color,
    }}>
      {label}
    </span>
  );
});
