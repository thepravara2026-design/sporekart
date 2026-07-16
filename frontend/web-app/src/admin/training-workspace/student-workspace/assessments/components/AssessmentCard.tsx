import { memo } from 'react';
import type { Assessment } from '../types';
import { AssessmentStatusBadge } from './AssessmentStatusBadge';
import { ASSESSMENT_TYPE_LABELS, DIFFICULTY_LABELS } from '../types';

interface AssessmentCardProps {
  assessment: Assessment;
  onSelect?: (id: string) => void;
  selected?: boolean;
}

export const AssessmentCard = memo(function AssessmentCard({ assessment, onSelect, selected }: AssessmentCardProps) {
  return (
    <div
      onClick={() => onSelect?.(assessment.id)}
      onKeyDown={(e) => { if ((e.key === 'Enter' || e.key === ' ') && onSelect) { e.preventDefault(); onSelect(assessment.id); } }}
      tabIndex={onSelect ? 0 : undefined}
      role={onSelect ? 'button' : undefined}
      aria-label={`Assessment ${assessment.assessmentCode}`}
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
          <div style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-body-sm)' }}>{assessment.title}</div>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', fontFamily: 'monospace' }}>{assessment.assessmentCode}</div>
        </div>
        <AssessmentStatusBadge status={assessment.status} />
      </div>
      <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>{assessment.courseName} &middot; {assessment.batchName}</div>
      <div style={{ display: 'flex', gap: 8, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', flexWrap: 'wrap' }}>
        <span>{ASSESSMENT_TYPE_LABELS[assessment.assessmentType]}</span>
        <span>&middot;</span>
        <span>{DIFFICULTY_LABELS[assessment.difficulty]}</span>
        <span>&middot;</span>
        <span>{assessment.durationMinutes} min</span>
        <span>&middot;</span>
        <span>{assessment.totalQuestions} Q</span>
      </div>
      <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
        Marks: {assessment.maxMarks} | Passing: {assessment.passingMarks} | Attempts: {assessment.attemptLimit}
      </div>
    </div>
  );
});
