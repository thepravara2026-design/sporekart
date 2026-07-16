import { memo } from 'react';
import type { CommunicationType } from '../types';
import { COMMUNICATION_TYPE_LABELS } from '../types';

const COLORS: Record<string, string> = {
  announcement: '#8b5cf6', 'general-notification': '#2563eb', 'academic-notification': '#0891b2',
  'assignment-reminder': '#ca8a04', 'assessment-reminder': '#f59e0b', 'attendance-alert': '#dc2626',
  'certificate-issued': '#16a34a', 'enrollment-confirmation': '#059669', 'course-completion': '#16a34a',
  'batch-notification': '#6366f1', 'holiday-notice': '#ec4899', 'schedule-update': '#f97316',
  'trainer-announcement': '#8b5cf6', 'learning-reminder': '#14b8a6', 'achievement-notification': '#f59e0b',
  'system-notification': '#6b7280', 'maintenance-notification': '#6b7280', 'ai-recommendation': '#a855f7',
};

export const TypeBadge = memo(function TypeBadge({ type }: { type: CommunicationType }) {
  const color = COLORS[type] || '#6b7280';
  return (
    <span style={{
      display: 'inline-flex', alignItems: 'center', padding: '2px 8px', borderRadius: 'var(--radius-full)',
      fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)', background: `${color}18`, color,
    }}>
      {COMMUNICATION_TYPE_LABELS[type]}
    </span>
  );
});
