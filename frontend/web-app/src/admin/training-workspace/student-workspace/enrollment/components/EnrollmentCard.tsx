import { memo } from 'react';
import type { EnrollmentRequest } from '../types';
import { EnrollmentStatusBadge } from './EnrollmentStatusBadge';
import { ADMISSION_TYPE_LABELS } from '../types';

interface EnrollmentCardProps {
  request: EnrollmentRequest;
  onSelect: (id: string) => void;
  selected?: boolean;
}

export const EnrollmentCard = memo(function EnrollmentCard({ request, onSelect, selected }: EnrollmentCardProps) {
  return (
    <div
      className="enrollment-card"
      onClick={() => onSelect(request.id)}
      onKeyDown={(e) => { if (e.key === 'Enter' || e.key === ' ') { e.preventDefault(); onSelect(request.id); } }}
      tabIndex={0}
      role="button"
      aria-label={`Enrollment ${request.enrollmentId} for ${request.studentName}`}
      style={{
        padding: 'var(--space-3)',
        borderRadius: 'var(--radius-md)',
        border: `1px solid ${selected ? 'var(--color-primary)' : 'var(--color-border-default)'}`,
        background: selected ? 'var(--color-bg-primary-subtle)' : 'var(--color-bg-surface-default)',
        cursor: 'pointer',
        display: 'flex', flexDirection: 'column', gap: 8,
      }}
    >
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
        <div>
          <div style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-body-sm)' }}>{request.studentName}</div>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{request.applicationNumber}</div>
        </div>
        <EnrollmentStatusBadge status={request.enrollmentStatus} />
      </div>
      <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>{request.courseName}</div>
      <div style={{ display: 'flex', gap: 8, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', flexWrap: 'wrap' }}>
        <span>{ADMISSION_TYPE_LABELS[request.admissionType]}</span>
        <span>&middot;</span>
        <span style={{ textTransform: 'capitalize' }}>{request.trainingMode}</span>
        <span>&middot;</span>
        <span style={{ textTransform: 'capitalize' }}>{request.priority} priority</span>
        <span>&middot;</span>
        <span>{request.applicationDate}</span>
      </div>
      {request.batchName && (
        <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-primary)' }}>
          Batch: {request.batchName}
        </div>
      )}
    </div>
  );
});
