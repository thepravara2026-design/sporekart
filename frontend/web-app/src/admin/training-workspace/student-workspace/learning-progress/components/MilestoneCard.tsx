import { memo } from 'react';
import type { Milestone } from '../types';
import { MILESTONE_TYPE_LABELS } from '../types';

interface MilestoneCardProps {
  milestone: Milestone;
}

export const MilestoneCard = memo(function MilestoneCard({ milestone }: MilestoneCardProps) {
  return (
    <div style={{
      padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
      border: `1px solid ${milestone.achieved ? '#16a34a' : 'var(--color-border-default)'}`,
      background: milestone.achieved ? '#f0fdf4' : 'var(--color-bg-surface-default)',
      display: 'flex', flexDirection: 'column', gap: 8,
    }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
        <div style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-body-sm)' }}>{MILESTONE_TYPE_LABELS[milestone.type]}</div>
        <span style={{
          display: 'inline-flex', alignItems: 'center', padding: '2px 8px', borderRadius: 'var(--radius-full)',
          fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)',
          backgroundColor: milestone.achieved ? '#16a34a18' : '#9ca3af18',
          color: milestone.achieved ? '#16a34a' : '#9ca3af',
        }}>
          {milestone.achieved ? 'Achieved' : 'Pending'}
        </span>
      </div>
      <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{milestone.description}</div>
      <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>{milestone.courseName}</div>
      {milestone.achievedDate && (
        <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Achieved: {milestone.achievedDate}</div>
      )}
    </div>
  );
});
