import { useMemo, useState } from 'react';
import { useEnrollment } from '../state/EnrollmentContext';
import { EnrollmentTimeline } from '../components/EnrollmentTimeline';
import { EmptyState } from '../components/EmptyStates';
import { getTimelineForEnrollment } from '../data/mockData';

export function EnrollmentTimelinePage() {
  const { requests } = useEnrollment();
  const [selectedEnrollmentId, setSelectedEnrollmentId] = useState<string | null>(null);

  const timelineEvents = useMemo(() => {
    if (!selectedEnrollmentId) return [];
    return getTimelineForEnrollment(selectedEnrollmentId);
  }, [selectedEnrollmentId]);

  const enrolledRequests = useMemo(() => {
    return requests.filter((r) => r.enrollmentStatus === 'enrolled' || r.enrollmentStatus === 'batch-assigned' || r.enrollmentStatus === 'seat-reserved');
  }, [requests]);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Enrollment Timeline</h1>
        <p style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)', margin: '4px 0 0 0' }}>
          Track enrollment activity across the lifecycle
        </p>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 2fr', gap: 'var(--space-component-gap)', alignItems: 'start' }}>
        <div style={{
          padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
          border: '1px solid var(--color-border-default)',
          background: 'var(--color-bg-surface-default)',
        }}>
          <h3 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', margin: '0 0 var(--space-2) 0' }}>
            Enrolled Students
          </h3>
          {enrolledRequests.length === 0 ? (
            <EmptyState type="noApprovedStudents" />
          ) : (
            <div style={{ display: 'flex', flexDirection: 'column', gap: 4 }}>
              {enrolledRequests.map((req) => (
                <button
                  key={req.id}
                  onClick={() => setSelectedEnrollmentId(req.enrollmentId)}
                  style={{
                    display: 'flex', justifyContent: 'space-between', alignItems: 'center',
                    padding: '8px 12px', borderRadius: 'var(--radius-sm)',
                    border: `1px solid ${selectedEnrollmentId === req.enrollmentId ? 'var(--color-primary)' : 'transparent'}`,
                    background: selectedEnrollmentId === req.enrollmentId ? 'var(--color-bg-primary-subtle)' : 'transparent',
                    cursor: 'pointer', textAlign: 'left', width: '100%',
                    fontSize: 'var(--text-body-sm)',
                  }}
                >
                  <div>
                    <div style={{ fontWeight: 'var(--weight-medium)' }}>{req.studentName}</div>
                    <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{req.applicationNumber}</div>
                  </div>
                  <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{req.enrolledDate || '-'}</span>
                </button>
              ))}
            </div>
          )}
        </div>

        <div style={{
          padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
          border: '1px solid var(--color-border-default)',
          background: 'var(--color-bg-surface-default)',
        }}>
          {selectedEnrollmentId ? (
            <EnrollmentTimeline events={timelineEvents} />
          ) : (
            <div style={{ padding: 'var(--space-6)', textAlign: 'center', color: 'var(--color-text-tertiary)' }}>
              Select a student to view their enrollment timeline
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
