import { memo, useMemo } from 'react';
import type { EnrollmentRequest } from '../types';
import { useEnrollment } from '../state/EnrollmentContext';

interface ApprovalActionsPanelProps {
  request: EnrollmentRequest;
  onApprove: (id: string) => void;
  onReject: (id: string) => void;
  onRequestInfo: (id: string) => void;
  onReserveSeat: (id: string) => void;
  onAssignBatch: (id: string) => void;
}

export const ApprovalActionsPanel = memo(function ApprovalActionsPanel({
  request, onApprove, onReject, onRequestInfo, onReserveSeat, onAssignBatch,
}: ApprovalActionsPanelProps) {
  const { batches } = useEnrollment();

  const capacityWarning = useMemo(() => {
    if (batches.length === 0) return null;
    const totalCapacity = batches.reduce((s, b) => s + b.capacity, 0);
    const totalFilled = batches.reduce((s, b) => s + b.filledSeats, 0);
    const utilization = Math.round((totalFilled / totalCapacity) * 100);
    if (utilization >= 95) return { level: 'critical', message: `Capacity at ${utilization}% — nearly full` };
    if (utilization >= 80) return { level: 'warning', message: `Capacity at ${utilization}% — filling up` };
    return null;
  }, [batches]);

  const isUnderReview = request.enrollmentStatus === 'pending-approval' || request.enrollmentStatus === 'under-review';
  const isApproved = request.enrollmentStatus === 'approved';
  const isSeatReserved = request.enrollmentStatus === 'seat-reserved';
  const canReject = request.enrollmentStatus !== 'rejected' && request.enrollmentStatus !== 'cancelled' && request.enrollmentStatus !== 'archived';
  const canRequestInfo = canReject;
  const canReserveSeat = isApproved && (capacityWarning?.level !== 'critical');
  const canAssignBatch = isSeatReserved;

  return (
    <div className="approval-actions" style={{
      padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
      border: '1px solid var(--color-border-default)',
      background: 'var(--color-bg-surface-default)',
    }}>
      <h3 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', margin: '0 0 var(--space-2) 0' }}>
        Approval Workflow
      </h3>
      <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', margin: '0 0 var(--space-3) 0' }}>
        {request.studentName} &middot; {request.applicationNumber}
      </p>

      {capacityWarning && (
        <div style={{
          marginBottom: 'var(--space-2)', padding: '6px 10px', borderRadius: 'var(--radius-sm)',
          background: capacityWarning.level === 'critical' ? '#fef2f2' : '#fefce8',
          border: `1px solid ${capacityWarning.level === 'critical' ? '#dc2626' : '#ca8a04'}`,
          color: capacityWarning.level === 'critical' ? '#dc2626' : '#ca8a04',
          fontSize: 'var(--text-caption)',
        }}>
          {capacityWarning.message}
        </div>
      )}

      <div style={{
        display: 'flex', gap: 4, marginBottom: 'var(--space-2)',
        fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)',
      }}>
        {['pending-approval', 'approved', 'seat-reserved', 'batch-assigned', 'enrolled'].map((stage, i) => {
          const statusOrder = ['pending-approval', 'under-review', 'approved', 'seat-reserved', 'batch-assigned', 'enrolled'];
          const currentIdx = statusOrder.indexOf(request.enrollmentStatus);
          const stageIdx = statusOrder.indexOf(stage);
          const isActive = stageIdx <= currentIdx;
          return (
            <span key={stage} style={{
              padding: '2px 6px', borderRadius: 'var(--radius-sm)',
              background: isActive ? 'var(--color-bg-primary-subtle)' : 'transparent',
              color: isActive ? 'var(--color-primary)' : 'var(--color-text-tertiary)',
              fontWeight: isActive ? 'var(--weight-medium)' : 'var(--weight-normal)',
            }}>
              {stage.replace(/-/g, ' ')}
              {i < 4 && <span style={{ marginLeft: 4 }}>→</span>}
            </span>
          );
        })}
      </div>

      <div style={{ display: 'flex', gap: 8, flexWrap: 'wrap' }}>
        {isUnderReview && (
          <button
            onClick={() => onApprove(request.id)}
            aria-label="Approve enrollment"
            style={{
              padding: '6px 16px', borderRadius: 'var(--radius-sm)', border: 'none',
              background: '#16a34a', color: '#fff', cursor: 'pointer',
              fontWeight: 'var(--weight-medium)', fontSize: 'var(--text-body-sm)',
            }}
          >
            ✓ Approve
          </button>
        )}

        {canReserveSeat && (
          <button
            onClick={() => onReserveSeat(request.id)}
            aria-label="Reserve seat"
            title={capacityWarning?.level === 'critical' ? 'No capacity available' : 'Reserve a seat for this student'}
            style={{
              padding: '6px 16px', borderRadius: 'var(--radius-sm)', border: 'none',
              background: '#7c3aed', color: '#fff', cursor: 'pointer',
              fontWeight: 'var(--weight-medium)', fontSize: 'var(--text-body-sm)',
            }}
          >
            💺 Reserve Seat
          </button>
        )}

        {canAssignBatch && (
          <button
            onClick={() => onAssignBatch(request.id)}
            aria-label="Assign batch"
            style={{
              padding: '6px 16px', borderRadius: 'var(--radius-sm)', border: 'none',
              background: '#2563eb', color: '#fff', cursor: 'pointer',
              fontWeight: 'var(--weight-medium)', fontSize: 'var(--text-body-sm)',
            }}
          >
            📦 Assign Batch
          </button>
        )}

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
          ✕ Reject
        </button>
      </div>
    </div>
  );
});
