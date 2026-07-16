import { memo } from 'react';
import type { MessageStatus } from '../types';
import { MESSAGE_STATUS_LABELS } from '../types';

const VARIANTS: Record<string, { bg: string; color: string }> = {
  draft: { bg: '#f3f4f6', color: '#6b7280' },
  scheduled: { bg: '#fefce8', color: '#ca8a04' },
  queued: { bg: '#eff6ff', color: '#2563eb' },
  sent: { bg: '#f0fdf4', color: '#16a34a' },
  delivered: { bg: '#f0fdf4', color: '#16a34a' },
  read: { bg: '#f0fdf4', color: '#16a34a' },
  archived: { bg: '#f3f4f6', color: '#6b7280' },
  expired: { bg: '#fef2f2', color: '#dc2626' },
  cancelled: { bg: '#f3f4f6', color: '#6b7280' },
  failed: { bg: '#fef2f2', color: '#dc2626' },
};

export const StatusBadge = memo(function StatusBadge({ status }: { status: MessageStatus }) {
  const v = VARIANTS[status] || VARIANTS.draft;
  return (
    <span style={{
      display: 'inline-flex', alignItems: 'center', padding: '2px 8px', borderRadius: 'var(--radius-full)',
      fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)', background: v.bg, color: v.color,
    }}>
      {MESSAGE_STATUS_LABELS[status]}
    </span>
  );
});
