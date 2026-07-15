import { memo } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import type { RecentActivity } from '../types';

interface RecentActivityCardProps {
  activities: RecentActivity[];
  loading?: boolean;
  title?: string;
}

export const RecentActivityCard = memo(function RecentActivityCard({ activities, loading, title = 'Recent Activity' }: RecentActivityCardProps) {
  return (
    <section style={{ background: 'var(--color-surface)', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', padding: 24 }}>
      <h3 style={{ margin: '0 0 16px', fontSize: 'var(--text-body)', fontWeight: 600, color: 'var(--color-text-primary)' }}>{title}</h3>
      {loading ? (
        <ul style={{ listStyle: 'none', margin: 0, padding: 0, display: 'flex', flexDirection: 'column', gap: 12 }} aria-busy="true">
          {Array.from({ length: 5 }).map((_, i) => (
            <li key={i} style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
              <div style={{ width: 32, height: 32, borderRadius: 'var(--radius-md)', background: 'var(--color-surface-hover)', animation: 'shimmer 1.5s infinite' }} />
              <div style={{ flex: 1, height: 12, background: 'var(--color-surface-hover)', borderRadius: 4, animation: 'shimmer 1.5s infinite' }} />
            </li>
          ))}
        </ul>
      ) : (
        <ul style={{ listStyle: 'none', margin: 0, padding: 0, display: 'flex', flexDirection: 'column', gap: 12 }}>
          {activities.map((a) => (
            <li key={a.id} style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
              <div style={{ width: 32, height: 32, borderRadius: 'var(--radius-md)', background: 'var(--color-primary-alpha)', color: 'var(--color-primary)', display: 'flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0 }}>
                <Icon name={a.icon} size={16} />
              </div>
              <div style={{ flex: 1, minWidth: 0 }}>
                <div style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-primary)' }}>{a.text}</div>
                <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{a.timestamp}</div>
              </div>
            </li>
          ))}
        </ul>
      )}
    </section>
  );
});

