import { memo } from 'react';
import type { Priority } from '../types';
import { PRIORITY_LABELS } from '../types';

const VARIANTS: Record<string, { bg: string; color: string }> = {
  critical: { bg: '#fef2f2', color: '#dc2626' },
  high: { bg: '#fefce8', color: '#ca8a04' },
  medium: { bg: '#eff6ff', color: '#2563eb' },
  low: { bg: '#f3f4f6', color: '#6b7280' },
  informational: { bg: '#f0fdf4', color: '#16a34a' },
};

export const PriorityBadge = memo(function PriorityBadge({ priority }: { priority: Priority }) {
  const v = VARIANTS[priority] || VARIANTS.low;
  return (
    <span style={{
      display: 'inline-flex', alignItems: 'center', padding: '2px 8px', borderRadius: 'var(--radius-full)',
      fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)', background: v.bg, color: v.color,
    }}>
      {PRIORITY_LABELS[priority]}
    </span>
  );
});
