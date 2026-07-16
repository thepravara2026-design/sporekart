import { memo } from 'react';
import type { Submission } from '../types';
import { SUBMISSION_STATUS_LABELS, SUBMISSION_TYPE_LABELS } from '../types';

interface SubmissionCardProps {
  submission: Submission;
}

const statusColors: Record<string, string> = {
  'not-started': '#6b7280', 'draft': '#9ca3af', 'submitted': '#2563eb',
  'late': '#dc2626', 'under-review': '#ca8a04', 'reviewed': '#2563eb',
  'resubmitted': '#ca8a04', 'accepted': '#16a34a', 'rejected': '#dc2626',
};

export const SubmissionCard = memo(function SubmissionCard({ submission }: SubmissionCardProps) {
  return (
    <div style={{
      padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
      border: '1px solid var(--color-border-default)',
      background: 'var(--color-bg-surface-default)',
      display: 'flex', flexDirection: 'column', gap: 8,
    }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
        <div>
          <div style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-body-sm)' }}>{submission.studentName}</div>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{submission.submissionCode}</div>
        </div>
        <span style={{
          display: 'inline-flex', alignItems: 'center', gap: 4,
          padding: '2px 8px', borderRadius: 'var(--radius-full)',
          fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)',
          backgroundColor: statusColors[submission.status] + '18',
          color: statusColors[submission.status], whiteSpace: 'nowrap',
        }}>
          {SUBMISSION_STATUS_LABELS[submission.status]}
        </span>
      </div>
      <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>{submission.assignmentTitle}</div>
      <div style={{ display: 'flex', gap: 8, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', flexWrap: 'wrap' }}>
        <span>{SUBMISSION_TYPE_LABELS[submission.submissionType]}</span>
        <span>&middot;</span>
        <span>{submission.wordCount} words</span>
        {submission.plagiarismScore !== null && (
          <><span>&middot;</span><span style={{ color: submission.plagiarismScore > 25 ? '#dc2626' : '#16a34a' }}>Plagiarism: {submission.plagiarismScore}%</span></>
        )}
      </div>
      {submission.submittedDate && (
        <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
          Submitted: {submission.submittedDate}
        </div>
      )}
    </div>
  );
});
