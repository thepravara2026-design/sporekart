import { memo } from 'react';
import type { SkillGapAssessment } from '../types';
import { SKILL_CATEGORY_LABELS } from '../types';

const PRIORITY_COLORS: Record<string, { bg: string; color: string }> = {
  critical: { bg: '#fef2f2', color: '#dc2626' },
  high: { bg: '#fff7ed', color: '#d97706' },
  medium: { bg: '#fefce8', color: '#ca8a04' },
  low: { bg: '#f0fdf4', color: '#16a34a' },
};

export const SkillGapCard = memo(function SkillGapCard({ gap }: { gap: SkillGapAssessment }) {
  const priority = PRIORITY_COLORS[gap.priority] || { bg: '#f3f4f6', color: '#6b7280' };
  const pct = Math.min(100, Math.round((gap.currentLevel / gap.requiredLevel) * 100));
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 6, padding: 12, borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 4 }}>
        <div>
          <span style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-body)' }}>{gap.skillName}</span>
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', marginLeft: 8 }}>{SKILL_CATEGORY_LABELS[gap.category]}</span>
        </div>
        <span style={{ padding: '1px 6px', borderRadius: 'var(--radius-full)', fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)', background: priority.bg, color: priority.color }}>{gap.priority}</span>
      </div>
      <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>{gap.studentName}</span>
      <div style={{ height: 8, borderRadius: 4, background: 'var(--color-bg-skeleton-base)', overflow: 'hidden' }}>
        <div style={{ height: '100%', borderRadius: 4, background: gap.priority === 'critical' ? '#dc2626' : gap.priority === 'high' ? '#d97706' : gap.priority === 'medium' ? '#ca8a04' : '#16a34a', width: `${pct}%`, transition: 'width 0.3s' }} />
      </div>
      <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
        <span>Current: {gap.currentLevel}%</span>
        <span>Required: {gap.requiredLevel}%</span>
        <span>Gap: {gap.gap}%</span>
      </div>
    </div>
  );
});
