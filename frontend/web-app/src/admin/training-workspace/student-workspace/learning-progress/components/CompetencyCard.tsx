import { memo } from 'react';
import type { Competency } from '../types';
import { COMPETENCY_CATEGORY_LABELS } from '../types';

interface CompetencyCardProps {
  competency: Competency;
}

export const CompetencyCard = memo(function CompetencyCard({ competency }: CompetencyCardProps) {
  const levelColor = competency.percentage >= 80 ? '#16a34a' : competency.percentage >= 60 ? '#2563eb' : competency.percentage >= 40 ? '#ca8a04' : '#dc2626';
  return (
    <div style={{ padding: 'var(--space-3)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)', display: 'flex', flexDirection: 'column', gap: 8 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
        <div>
          <div style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-body-sm)' }}>{competency.name}</div>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{COMPETENCY_CATEGORY_LABELS[competency.category]}</div>
        </div>
        <span style={{
          display: 'inline-flex', alignItems: 'center', padding: '2px 8px', borderRadius: 'var(--radius-full)',
          fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)',
          backgroundColor: levelColor + '18', color: levelColor,
        }}>
          {competency.status === 'mastered' ? 'Mastered' : competency.status === 'achieved' ? 'Achieved' : 'In Progress'}
        </span>
      </div>
      <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{competency.description}</div>
      <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
        <span style={{ fontSize: 'var(--text-caption)' }}>Level {competency.level}/{competency.maxLevel}</span>
        <div style={{ flex: 1, height: 8, background: 'var(--color-bg-skeleton-base)', borderRadius: 4, overflow: 'hidden' }}>
          <div style={{ width: `${competency.percentage}%`, height: '100%', background: levelColor, borderRadius: 4 }} />
        </div>
        <span style={{ fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)' }}>{competency.percentage}%</span>
      </div>
      <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{competency.courseName}</div>
    </div>
  );
});
