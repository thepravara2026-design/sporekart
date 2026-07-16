import { useMemo, useState, useCallback } from 'react';
import { useEnrollment } from '../state/EnrollmentContext';
import { EnrollmentTable } from '../components/EnrollmentTable';
import { EnrollmentPagination } from '../components/EnrollmentPagination';
import { ApprovalActionsPanel } from '../components/ApprovalActionsPanel';
import { EnrollmentTimeline } from '../components/EnrollmentTimeline';
import { EmptyState } from '../components/EmptyStates';
import { EnrollmentTableSkeleton } from '../components/Skeletons';
import { getTimelineForEnrollment } from '../data/mockData';

export function EnrollmentApprovalQueuePage() {
  const { pendingApprovals, selectRequest, currentRequest, isLoading } = useEnrollment();
  const [page, setPage] = useState(1);
  const pageSize = 10;

  const paginated = useMemo(() => {
    const start = (page - 1) * pageSize;
    return pendingApprovals.slice(start, start + pageSize);
  }, [pendingApprovals, page]);

  const totalPages = Math.max(1, Math.ceil(pendingApprovals.length / pageSize));

  const timelineEvents = useMemo(() => {
    if (!currentRequest) return [];
    return getTimelineForEnrollment(currentRequest.enrollmentId);
  }, [currentRequest]);

  const handleApprove = useCallback((id: string) => {
    console.log('Approve', id);
  }, []);

  const handleReject = useCallback((id: string) => {
    console.log('Reject', id);
  }, []);

  const handleRequestInfo = useCallback((id: string) => {
    console.log('Request info for', id);
  }, []);

  if (isLoading) return <EnrollmentTableSkeleton />;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Approval Queue</h1>
        <p style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)', margin: '4px 0 0 0' }}>
          Review and process pending enrollment applications
        </p>
      </div>

      {pendingApprovals.length === 0 ? (
        <EmptyState type="noPendingApprovals" />
      ) : (
        <div style={{ display: 'grid', gridTemplateColumns: currentRequest ? '1.5fr 1fr' : '1fr', gap: 'var(--space-component-gap)', alignItems: 'start' }}>
          <div style={{
            padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
            border: '1px solid var(--color-border-default)',
            background: 'var(--color-bg-surface-default)',
          }}>
            <h3 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', margin: '0 0 var(--space-2) 0' }}>
              Pending Approvals ({pendingApprovals.length})
            </h3>
            <EnrollmentTable requests={paginated} onSelect={selectRequest} selectedId={currentRequest?.id ?? null} />
            <EnrollmentPagination
              page={page} totalPages={totalPages} totalFiltered={pendingApprovals.length}
              pageSize={pageSize} onPageChange={setPage} onPageSizeChange={() => {}}
              pageSizeOptions={[10]}
            />
          </div>

          {currentRequest && (
            <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
              <ApprovalActionsPanel
                request={currentRequest}
                onApprove={handleApprove}
                onReject={handleReject}
                onRequestInfo={handleRequestInfo}
              />
              <div style={{
                padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
                border: '1px solid var(--color-border-default)',
                background: 'var(--color-bg-surface-default)',
              }}>
                <EnrollmentTimeline events={timelineEvents} />
              </div>
            </div>
          )}
        </div>
      )}
    </div>
  );
}
