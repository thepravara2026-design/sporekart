import { memo } from 'react';
import type { DigitalBadge } from '../types';
import { BADGE_TYPE_LABELS } from '../types';

interface BadgeCardProps {
  badge: DigitalBadge;
}

export const BadgeCard = memo(function BadgeCard({ badge }: BadgeCardProps) {
  return (
    <div style={{
      padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
      border: '1px solid var(--color-border-default)',
      background: badge.isNft ? 'linear-gradient(135deg, #fefce8, #f0fdf4)' : 'var(--color-bg-surface-default)',
      display: 'flex', flexDirection: 'column', gap: 8,
    }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
          <div style={{
            width: 40, height: 40, borderRadius: 'var(--radius-sm)',
            background: 'var(--color-bg-primary-subtle)',
            display: 'flex', alignItems: 'center', justifyContent: 'center',
            fontSize: 20,
          }}>🏅</div>
          <div>
            <div style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-body-sm)' }}>{badge.name}</div>
            <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{BADGE_TYPE_LABELS[badge.badgeType]}</div>
          </div>
        </div>
        {badge.isNft && (
          <span style={{ padding: '2px 6px', borderRadius: 'var(--radius-xs)', background: '#fefce8', color: '#ca8a04', fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)' }}>NFT</span>
        )}
      </div>
      <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{badge.description}</div>
      <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
        <span>{badge.studentName}</span>
        <span>Shared {badge.shareCount}x</span>
      </div>
    </div>
  );
});
