import { memo } from 'react';
import type { Assessment } from '../types';
import { AssessmentStatusBadge } from './AssessmentStatusBadge';
import { ASSESSMENT_TYPE_LABELS, DIFFICULTY_LABELS } from '../types';

interface AssessmentTableProps {
  assessments: Assessment[];
  onSelect?: (id: string) => void;
  selectedId?: string | null;
}

export const AssessmentTable = memo(function AssessmentTable({ assessments, onSelect, selectedId }: AssessmentTableProps) {
  return (
    <div style={{ overflowX: 'auto' }}>
      <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 'var(--text-body-sm)' }}>
        <thead>
          <tr style={{ borderBottom: '2px solid var(--color-border-default)', textAlign: 'left' }}>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Code</th>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Title</th>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Course</th>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Batch</th>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Type</th>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Duration</th>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Marks</th>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Status</th>
          </tr>
        </thead>
        <tbody>
          {assessments.map((assess) => (
            <tr
              key={assess.id}
              onClick={() => onSelect?.(assess.id)}
              onKeyDown={(e) => { if ((e.key === 'Enter' || e.key === ' ') && onSelect) { e.preventDefault(); onSelect(assess.id); } }}
              tabIndex={onSelect ? 0 : undefined}
              role={onSelect ? 'button' : undefined}
              aria-label={`Assessment ${assess.assessmentCode}`}
              style={{
                borderBottom: '1px solid var(--color-border-subtle)',
                cursor: onSelect ? 'pointer' : 'default',
                background: selectedId === assess.id ? 'var(--color-bg-primary-subtle)' : 'transparent',
              }}
            >
              <td style={{ padding: '10px 12px', fontFamily: 'monospace', fontSize: 'var(--text-caption)' }}>{assess.assessmentCode}</td>
              <td style={{ padding: '10px 12px' }}>
                <div style={{ fontWeight: 'var(--weight-medium)' }}>{assess.title}</div>
                <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{DIFFICULTY_LABELS[assess.difficulty]}</div>
              </td>
              <td style={{ padding: '10px 12px', maxWidth: 140, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{assess.courseName}</td>
              <td style={{ padding: '10px 12px' }}>{assess.batchName}</td>
              <td style={{ padding: '10px 12px', color: 'var(--color-text-tertiary)' }}>{ASSESSMENT_TYPE_LABELS[assess.assessmentType]}</td>
              <td style={{ padding: '10px 12px', whiteSpace: 'nowrap' }}>{assess.durationMinutes}m</td>
              <td style={{ padding: '10px 12px' }}>{assess.maxMarks}</td>
              <td style={{ padding: '10px 12px' }}><AssessmentStatusBadge status={assess.status} /></td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
});
