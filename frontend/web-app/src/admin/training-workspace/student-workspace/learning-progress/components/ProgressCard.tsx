import { memo } from 'react';
import type { LearningProgress } from '../types';
import { ProgressStatusBadge } from './ProgressStatusBadge';
import { LEARNING_STAGE_LABELS } from '../types';

interface ProgressCardProps {
  record: LearningProgress;
  onSelect?: (id: string) => void;
  selected?: boolean;
}

export const ProgressCard = memo(function ProgressCard({ record, onSelect, selected }: ProgressCardProps) {
  return (
    <div
      onClick={() => onSelect?.(record.id)}
      onKeyDown={(e) => { if ((e.key === 'Enter' || e.key === ' ') && onSelect) { e.preventDefault(); onSelect(record.id); } }}
      tabIndex={onSelect ? 0 : undefined}
      role={onSelect ? 'button' : undefined}
      aria-label={`Progress ${record.progressCode}`}
      style={{
        padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
        border: `1px solid ${selected ? 'var(--color-primary)' : 'var(--color-border-default)'}`,
        background: selected ? 'var(--color-bg-primary-subtle)' : 'var(--color-bg-surface-default)',
        cursor: onSelect ? 'pointer' : 'default',
        display: 'flex', flexDirection: 'column', gap: 8,
      }}
    >
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
        <div>
          <div style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-body-sm)' }}>{record.studentName}</div>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', fontFamily: 'monospace' }}>{record.progressCode}</div>
        </div>
        <ProgressStatusBadge status={record.status} />
      </div>
      <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>{record.courseName}</div>
      <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
        <div style={{ flex: 1, height: 10, background: 'var(--color-bg-skeleton-base)', borderRadius: 5, overflow: 'hidden' }}>
          <div style={{ width: `${record.progressPercent}%`, height: '100%', background: record.progressPercent >= 80 ? '#16a34a' : record.progressPercent >= 50 ? '#2563eb' : '#ca8a04', borderRadius: 5 }} />
        </div>
        <span style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-bold)' }}>{record.progressPercent}%</span>
      </div>
      <div style={{ display: 'flex', gap: 8, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', flexWrap: 'wrap' }}>
        <span>{LEARNING_STAGE_LABELS[record.currentStage]}</span>
        <span>&middot;</span>
        <span>{record.learningHours}h</span>
        <span>&middot;</span>
        <span>Last: {record.lastActivityDate}</span>
      </div>
    </div>
  );
});
