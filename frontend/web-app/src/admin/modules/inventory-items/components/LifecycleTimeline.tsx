import { memo, type CSSProperties } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import type { LifecycleEvent } from '../types';
import { getLifecycleVariant } from '../utils';
import { VARIANT_COLORS } from '../../../constants/variantColors';

const variantStyle = (v: string): CSSProperties => ({
  background: `var(--color-${v}-alpha, var(--color-surface-hover))`,
  color: VARIANT_COLORS[v] ?? VARIANT_COLORS.neutral,
  borderColor: VARIANT_COLORS[v] ?? 'var(--color-border)',
});

interface Props {
  events: LifecycleEvent[];
}

export const LifecycleTimeline = memo(function LifecycleTimeline({ events }: Props) {
  if (events.length === 0) {
    return <p style={{ color: 'var(--color-text-tertiary)', fontSize: 'var(--text-body)', padding: 24, textAlign: 'center' }}>No lifecycle events recorded.</p>;
  }

  const sorted = [...events].sort((a, b) => new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime());

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 0, padding: '8px 0' }} role="list" aria-label="Lifecycle timeline">
      {sorted.map((event, i) => {
        const variant = getLifecycleVariant(event.stage);
        const colors = variantStyle(variant);
        const isLast = i === sorted.length - 1;

        return (
          <div key={event.id} role="listitem" style={{ display: 'flex', gap: 16, position: 'relative', paddingLeft: 24 }}>
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', position: 'absolute', left: 0, top: 0, bottom: 0 }}>
              <div style={{ width: 14, height: 14, borderRadius: '50%', border: `3px solid ${colors.borderColor || 'var(--color-border)'}`, background: colors.background, zIndex: 1 }} />
              {!isLast && <div style={{ width: 2, flex: 1, background: 'var(--color-border)', marginTop: 2 }} />}
            </div>
            <div style={{ flex: 1, paddingBottom: isLast ? 4 : 24 }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <span style={{ padding: '2px 8px', borderRadius: 'var(--radius-badge)', fontSize: 'var(--text-caption)', fontWeight: 600, ...colors }}>{event.stage.replace(/_/g, ' ')}</span>
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{event.timestamp}</span>
              </div>
              {event.note && <p style={{ margin: '4px 0 0', fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>{event.note}</p>}
              <p style={{ margin: '2px 0 0', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', display: 'flex', alignItems: 'center', gap: 4 }}>
                <Icon name="user" size={12} /> {event.user}
              </p>
            </div>
          </div>
        );
      })}
    </div>
  );
});
