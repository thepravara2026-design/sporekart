import { memo } from 'react';
import type { Assignment } from '../types';
import { AssignmentStatusBadge } from './AssignmentStatusBadge';
import { ASSIGNMENT_TYPE_LABELS } from '../types';

interface AssignmentTableProps {
  assignments: Assignment[];
  onSelect?: (id: string) => void;
  selectedId?: string | null;
}

export const AssignmentTable = memo(function AssignmentTable({ assignments, onSelect, selectedId }: AssignmentTableProps) {
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
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Due Date</th>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Status</th>
          </tr>
        </thead>
        <tbody>
          {assignments.map((assgn) => (
            <tr
              key={assgn.id}
              onClick={() => onSelect?.(assgn.id)}
              onKeyDown={(e) => { if ((e.key === 'Enter' || e.key === ' ') && onSelect) { e.preventDefault(); onSelect(assgn.id); } }}
              tabIndex={onSelect ? 0 : undefined}
              role={onSelect ? 'button' : undefined}
              aria-label={`Assignment ${assgn.assignmentCode}`}
              style={{
                borderBottom: '1px solid var(--color-border-subtle)',
                cursor: onSelect ? 'pointer' : 'default',
                background: selectedId === assgn.id ? 'var(--color-bg-primary-subtle)' : 'transparent',
              }}
            >
              <td style={{ padding: '10px 12px', fontFamily: 'monospace', fontSize: 'var(--text-caption)' }}>{assgn.assignmentCode}</td>
              <td style={{ padding: '10px 12px' }}>
                <div style={{ fontWeight: 'var(--weight-medium)' }}>{assgn.title}</div>
              </td>
              <td style={{ padding: '10px 12px', maxWidth: 160, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{assgn.courseName}</td>
              <td style={{ padding: '10px 12px' }}>{assgn.batchName}</td>
              <td style={{ padding: '10px 12px', color: 'var(--color-text-tertiary)' }}>{ASSIGNMENT_TYPE_LABELS[assgn.assignmentType]}</td>
              <td style={{ padding: '10px 12px', whiteSpace: 'nowrap' }}>{assgn.dueDate}</td>
              <td style={{ padding: '10px 12px' }}><AssignmentStatusBadge status={assgn.status} /></td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
});
