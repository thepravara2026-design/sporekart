import { memo } from 'react';
import type { Result } from '../types';
import { RESULT_STATUS_LABELS, PERFORMANCE_BAND_LABELS, COMPETENCY_LEVEL_LABELS } from '../types';

interface ResultCardProps {
  result: Result;
}

export const ResultCard = memo(function ResultCard({ result }: ResultCardProps) {
  const passColor = result.resultStatus === 'pass' ? '#16a34a' : '#dc2626';
  return (
    <div style={{
      padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
      border: '1px solid var(--color-border-default)',
      background: 'var(--color-bg-surface-default)',
      display: 'flex', flexDirection: 'column', gap: 8,
    }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
        <div>
          <div style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-body-sm)' }}>{result.studentName}</div>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{result.resultCode}</div>
        </div>
        <span style={{
          display: 'inline-flex', alignItems: 'center', gap: 4,
          padding: '2px 8px', borderRadius: 'var(--radius-full)',
          fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)',
          backgroundColor: passColor + '18', color: passColor,
          whiteSpace: 'nowrap',
        }}>
          {RESULT_STATUS_LABELS[result.resultStatus]}
        </span>
      </div>
      <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>{result.assessmentTitle}</div>
      <div style={{ display: 'flex', gap: 12, fontSize: 'var(--text-body-sm)', flexWrap: 'wrap', alignItems: 'center' }}>
        <span>Score: <strong style={{ color: passColor }}>{result.scoredMarks}/{result.maxMarks}</strong></span>
        <span>Percentage: <strong>{result.percentage}%</strong></span>
        {result.grade && <span>Grade: <strong>{result.grade}</strong></span>}
      </div>
      <div style={{ display: 'flex', gap: 8, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', flexWrap: 'wrap' }}>
        <span>{PERFORMANCE_BAND_LABELS[result.performanceBand]}</span>
        <span>&middot;</span>
        <span>{COMPETENCY_LEVEL_LABELS[result.competencyLevel]}</span>
        <span>&middot;</span>
        <span>Attempt {result.attemptNumber}/{result.totalAttempts}</span>
        <span>&middot;</span>
        <span>{result.timeTakenMinutes} min</span>
      </div>
      <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
        {result.courseName} &middot; {result.batchName}
      </div>
    </div>
  );
});
