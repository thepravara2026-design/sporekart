import { memo } from 'react';
import type { Assignment } from '../types';
import { AssignmentStatusBadge } from './AssignmentStatusBadge';
import { ASSIGNMENT_TYPE_LABELS, DIFFICULTY_LABELS } from '../types';

interface AssignmentCardProps {
  assignment: Assignment;
  onSelect?: (id: string) => void;
  selected?: boolean;
}

export const AssignmentCard = memo(function AssignmentCard({ assignment, onSelect, selected }: AssignmentCardProps) {
  return (
    <div
      onClick={() => onSelect?.(assignment.id)}
      onKeyDown={(e) => { if ((e.key === 'Enter' || e.key === ' ') && onSelect) { e.preventDefault(); onSelect(assignment.id); } }}
      tabIndex={onSelect ? 0 : undefined}
      role={onSelect ? 'button' : undefined}
      aria-label={`Assignment ${assignment.assignmentCode}`}
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
          <div style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-body-sm)' }}>{assignment.title}</div>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', fontFamily: 'monospace' }}>{assignment.assignmentCode}</div>
        </div>
        <AssignmentStatusBadge status={assignment.status} />
      </div>
      <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>{assignment.courseName} &middot; {assignment.batchName}</div>
      <div style={{ display: 'flex', gap: 8, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', flexWrap: 'wrap' }}>
        <span>{ASSIGNMENT_TYPE_LABELS[assignment.assignmentType]}</span>
        <span>&middot;</span>
        <span>{DIFFICULTY_LABELS[assignment.difficulty]}</span>
        <span>&middot;</span>
        <span>Due: {assignment.dueDate}</span>
      </div>
      <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
        Marks: {assignment.maxMarks} | Passing: {assignment.passingMarks}
      </div>
    </div>
  );
});
