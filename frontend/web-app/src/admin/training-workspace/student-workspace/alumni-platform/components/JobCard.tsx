import { memo } from 'react';
import type { JobOpportunity } from '../types';
import { JOB_TYPE_LABELS } from '../types';
import { TierBadge } from './TierBadge';

export const JobCard = memo(function JobCard({ job }: { job: JobOpportunity }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 8, padding: 12, borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', flexWrap: 'wrap', gap: 4 }}>
        <div>
          <span style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-body)' }}>{job.title}</span>
          <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', marginLeft: 8 }}>at {job.companyName}</span>
        </div>
        <TierBadge tier={job.partnerTier} />
      </div>
      <div style={{ display: 'flex', gap: 8, flexWrap: 'wrap', fontSize: 'var(--text-caption)' }}>
        <span style={{ padding: '1px 6px', borderRadius: 'var(--radius-full)', background: '#f3f4f6', color: '#6b7280' }}>{JOB_TYPE_LABELS[job.jobType]}</span>
        <span style={{ color: 'var(--color-text-tertiary)' }}>{job.location}</span>
        <span style={{ color: 'var(--color-text-tertiary)' }}>{job.salaryRange}</span>
        <span style={{ color: 'var(--color-text-tertiary)' }}>{job.experienceRequired}</span>
      </div>
      <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-tertiary)' }}>{job.description}</div>
      <div style={{ display: 'flex', gap: 4, flexWrap: 'wrap' }}>
        {job.requiredSkills.map((s, i) => (
          <span key={i} style={{ padding: '1px 6px', borderRadius: 'var(--radius-full)', background: '#f0fdf4', color: '#16a34a', fontSize: 'var(--text-caption)' }}>{s}</span>
        ))}
      </div>
      <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
        <span>Positions: {job.filledPositions}/{job.totalPositions}</span>
        <span>Deadline: {job.applicationDeadline}</span>
      </div>
    </div>
  );
});
