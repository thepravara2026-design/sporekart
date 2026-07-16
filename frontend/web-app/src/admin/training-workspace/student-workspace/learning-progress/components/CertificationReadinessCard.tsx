import { memo } from 'react';
import type { CertificationReadiness } from '../types';

interface CertificationReadinessCardProps {
  readiness: CertificationReadiness;
}

export const CertificationReadinessCard = memo(function CertificationReadinessCard({ readiness }: CertificationReadinessCardProps) {
  return (
    <div style={{
      padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
      border: `1px solid ${readiness.isReady ? '#16a34a' : '#ca8a04'}`,
      background: readiness.isReady ? '#f0fdf4' : '#fefce8',
      display: 'flex', flexDirection: 'column', gap: 8,
    }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
        <div>
          <div style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-body-sm)' }}>{readiness.studentName}</div>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{readiness.courseName}</div>
        </div>
        <span style={{
          display: 'inline-flex', alignItems: 'center', padding: '2px 8px', borderRadius: 'var(--radius-full)',
          fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)',
          backgroundColor: readiness.isReady ? '#16a34a18' : '#ca8a0418',
          color: readiness.isReady ? '#16a34a' : '#ca8a04',
        }}>
          {readiness.isReady ? 'Ready' : `${readiness.overallEligibilityPercent}%`}
        </span>
      </div>
      <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
        <span style={{ fontSize: 'var(--text-caption)' }}>Eligibility</span>
        <div style={{ flex: 1, height: 10, background: 'var(--color-bg-skeleton-base)', borderRadius: 5, overflow: 'hidden' }}>
          <div style={{ width: `${readiness.overallEligibilityPercent}%`, height: '100%', background: readiness.isReady ? '#16a34a' : '#ca8a04', borderRadius: 5 }} />
        </div>
        <span style={{ fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-bold)' }}>{readiness.overallEligibilityPercent}%</span>
      </div>
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 4, fontSize: 'var(--text-caption)' }}>
        <span>Attendance: {readiness.attendancePercent}% (req: {readiness.attendanceRequirement}%)</span>
        <span>Assignments: {readiness.assignmentCompletion}% (req: {readiness.assignmentRequirement}%)</span>
        <span>Assessment: {readiness.assessmentScore}% (req: {readiness.assessmentRequirement}%)</span>
        <span>Competencies: {readiness.competencyCount} (req: {readiness.competencyRequirement})</span>
      </div>
      {readiness.pendingRequirements.length > 0 && (
        <div style={{ fontSize: 'var(--text-caption)', color: '#dc2626' }}>
          Pending: {readiness.pendingRequirements.join(', ')}
        </div>
      )}
    </div>
  );
});
