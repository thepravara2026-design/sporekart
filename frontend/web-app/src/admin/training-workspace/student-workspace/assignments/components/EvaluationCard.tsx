import { memo } from 'react';
import type { Evaluation } from '../types';
import { EVALUATION_STATUS_LABELS } from '../types';

interface EvaluationCardProps {
  evaluation: Evaluation;
}

const statusColors: Record<string, string> = {
  'pending': '#ca8a04', 'in-progress': '#2563eb', 'completed': '#16a34a', 'appealed': '#dc2626',
};

export const EvaluationCard = memo(function EvaluationCard({ evaluation }: EvaluationCardProps) {
  return (
    <div style={{
      padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
      border: '1px solid var(--color-border-default)',
      background: 'var(--color-bg-surface-default)',
      display: 'flex', flexDirection: 'column', gap: 8,
    }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
        <div>
          <div style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-body-sm)' }}>{evaluation.studentName}</div>
        </div>
        <span style={{
          display: 'inline-flex', alignItems: 'center', gap: 4,
          padding: '2px 8px', borderRadius: 'var(--radius-full)',
          fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)',
          backgroundColor: statusColors[evaluation.evaluationStatus] + '18',
          color: statusColors[evaluation.evaluationStatus], whiteSpace: 'nowrap',
        }}>
          {EVALUATION_STATUS_LABELS[evaluation.evaluationStatus]}
        </span>
      </div>
      <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>
        Evaluated by: {evaluation.evaluatorName}
      </div>
      <div style={{ display: 'flex', gap: 8, fontSize: 'var(--text-body-sm)', flexWrap: 'wrap' }}>
        <span>Marks: <strong>{evaluation.scoredMarks !== null ? evaluation.scoredMarks : '-'}</strong> / {evaluation.maxMarks}</span>
        {evaluation.grade && <span>Grade: <strong>{evaluation.grade}</strong></span>}
      </div>
      {evaluation.comments && (
        <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', fontStyle: 'italic' }}>
          {evaluation.comments}
        </div>
      )}
      {evaluation.suggestions && (
        <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
          Suggestions: {evaluation.suggestions}
        </div>
      )}
    </div>
  );
});
