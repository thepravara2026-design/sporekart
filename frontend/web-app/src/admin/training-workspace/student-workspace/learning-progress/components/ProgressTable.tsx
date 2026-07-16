import { memo } from 'react';
import type { LearningProgress } from '../types';
import { ProgressStatusBadge } from './ProgressStatusBadge';
import { LEARNING_STAGE_LABELS } from '../types';

interface ProgressTableProps {
  records: LearningProgress[];
  onSelect?: (id: string) => void;
  selectedId?: string | null;
}

export const ProgressTable = memo(function ProgressTable({ records, onSelect, selectedId }: ProgressTableProps) {
  return (
    <div style={{ overflowX: 'auto' }}>
      <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 'var(--text-body-sm)' }}>
        <thead>
          <tr style={{ borderBottom: '2px solid var(--color-border-default)', textAlign: 'left' }}>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Student</th>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Course</th>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Progress</th>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Stage</th>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Hours</th>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Status</th>
          </tr>
        </thead>
        <tbody>
          {records.map((rec) => (
            <tr
              key={rec.id}
              onClick={() => onSelect?.(rec.id)}
              onKeyDown={(e) => { if ((e.key === 'Enter' || e.key === ' ') && onSelect) { e.preventDefault(); onSelect(rec.id); } }}
              tabIndex={onSelect ? 0 : undefined}
              role={onSelect ? 'button' : undefined}
              aria-label={`Progress ${rec.progressCode}`}
              style={{ borderBottom: '1px solid var(--color-border-subtle)', cursor: onSelect ? 'pointer' : 'default', background: selectedId === rec.id ? 'var(--color-bg-primary-subtle)' : 'transparent' }}
            >
              <td style={{ padding: '10px 12px' }}>
                <div style={{ fontWeight: 'var(--weight-medium)' }}>{rec.studentName}</div>
                <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{rec.progressCode}</div>
              </td>
              <td style={{ padding: '10px 12px', maxWidth: 140, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{rec.courseName}</td>
              <td style={{ padding: '10px 12px' }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                  <div style={{ flex: 1, height: 8, background: 'var(--color-bg-skeleton-base)', borderRadius: 4, overflow: 'hidden', minWidth: 60 }}>
                    <div style={{ width: `${rec.progressPercent}%`, height: '100%', background: rec.progressPercent >= 80 ? '#16a34a' : rec.progressPercent >= 50 ? '#2563eb' : '#ca8a04', borderRadius: 4 }} />
                  </div>
                  <span style={{ fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)', minWidth: 36, textAlign: 'right' }}>{rec.progressPercent}%</span>
                </div>
              </td>
              <td style={{ padding: '10px 12px', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{LEARNING_STAGE_LABELS[rec.currentStage]}</td>
              <td style={{ padding: '10px 12px' }}>{rec.learningHours}h</td>
              <td style={{ padding: '10px 12px' }}><ProgressStatusBadge status={rec.status} /></td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
});
