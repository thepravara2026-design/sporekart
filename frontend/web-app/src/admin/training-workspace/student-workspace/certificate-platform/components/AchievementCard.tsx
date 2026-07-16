import { memo } from 'react';
import type { Achievement } from '../types';
import { ACHIEVEMENT_TYPE_LABELS } from '../types';

interface AchievementCardProps {
  achievement: Achievement;
}

export const AchievementCard = memo(function AchievementCard({ achievement: a }: AchievementCardProps) {
  return (
    <div style={{
      padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
      border: '1px solid var(--color-border-default)',
      background: 'var(--color-bg-surface-default)',
      display: 'flex', flexDirection: 'column', gap: 8,
    }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
          <div style={{
            width: 36, height: 36, borderRadius: '50%',
            background: '#fefce8', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 18,
          }}>⭐</div>
          <div>
            <div style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-body-sm)' }}>{a.name}</div>
            <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{ACHIEVEMENT_TYPE_LABELS[a.achievementType]}</div>
          </div>
        </div>
        <span style={{ fontWeight: 'var(--weight-bold)', color: '#ca8a04', fontSize: 'var(--text-body-sm)' }}>{a.points} pts</span>
      </div>
      <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{a.description}</div>
      <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
        <span>{a.courseName}</span>
        <span>{a.dateAchieved}</span>
      </div>
    </div>
  );
});
