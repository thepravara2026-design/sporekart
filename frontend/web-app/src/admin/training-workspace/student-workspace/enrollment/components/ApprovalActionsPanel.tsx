import { memo } from 'react';
import type { EnrollmentRequest } from '../types';

interface ApprovalActionsPanelProps {
  request: EnrollmentRequest;
  onApprove: (id: string) => void;
  onReject: (id: string) => void;
  onRequestInfo: (id: string) => void;
}

export const ApprovalActionsPanel = memo(function ApprovalActionsPanel({
  request, onApprove, onReject, onRequestInfo,
}: ApprovalActionsPanelProps) {
  const canApprove = request.enrollmentStatus === 'pending-approval' || request.enrollmentStatus === 'under-review';
  const canReject = request.enrollmentStatus !== 'rejected' && request.enrollmentStatus !== 'cancelled' && request.enrollmentStatus !== 'archived';
  const canRequestInfo = request.enrollmentStatus !== 'rejected' && request.enrollmentStatus !== 'cancelled' && request.enrollmentStatus !== 'archived';

  return (
    <div className="approval-actions" style={{
      padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
      border: '1px solid var(--color-border-default)',
      background: 'var(--color-bg-surface-default)',
    }}>
      <h3 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', margin: '0 0 var(--space-2) 0' }}>
        Approval Actions
      </h3>
      <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', margin: '0 0 var(--space-3) 0' }}>
        {request.studentName} &middot; {request.applicationNumber}
      </p>
      <div style={{ display: 'flex', gap: 8, flexWrap: 'wrap' }}>
        <button
          onClick={() => onApprove(request.id)}
          disabled={!canApprove}
          aria-label="Approve enrollment"
          style={{
            padding: '6px 16px', borderRadius: 'var(--radius-sm)', border: 'none',
            background: canApprove ? '#16a34a' : 'var(--color-bg-skeleton-base)',
            color: '#fff', cursor: canApprove ? 'pointer' : 'default',
            fontWeight: 'var(--weight-medium)', fontSize: 'var(--text-body-sm)',
            opacity: canApprove ? 1 : 0.5,
          }}
        >
          Approve
        </button>
        <button
          onClick={() => onRequestInfo(request.id)}
          disabled={!canRequestInfo}
          aria-label="Request more information"
          style={{
            padding: '6px 16px', borderRadius: 'var(--radius-sm)',
            border: '1px solid var(--color-border-default)',
            background: 'var(--color-bg-surface-default)',
            color: 'var(--color-text-primary)', cursor: canRequestInfo ? 'pointer' : 'default',
            fontWeight: 'var(--weight-medium)', fontSize: 'var(--text-body-sm)',
            opacity: canRequestInfo ? 1 : 0.5,
          }}
        >
          Request Info
        </button>
        <button
          onClick={() => onReject(request.id)}
          disabled={!canReject}
          aria-label="Reject enrollment"
          style={{
            padding: '6px 16px', borderRadius: 'var(--radius-sm)', border: 'none',
            background: canReject ? '#dc2626' : 'var(--color-bg-skeleton-base)',
            color: '#fff', cursor: canReject ? 'pointer' : 'default',
            fontWeight: 'var(--weight-medium)', fontSize: 'var(--text-body-sm)',
            opacity: canReject ? 1 : 0.5,
          }}
        >
          Reject
        </button>
      </div>
    </div>
  );
});
