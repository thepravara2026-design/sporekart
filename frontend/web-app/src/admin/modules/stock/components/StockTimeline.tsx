import { memo } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import type { StockTimelineEvent } from '../types';

interface Props { events: StockTimelineEvent[]; }

const actionIcons: Record<string, string> = {
  'Stock Record Created': 'database',
  'Quantity Updated': 'edit',
  'State Changed': 'activity',
  'Health Recalculated': 'heart',
  'Reservation Added': 'lock',
};

export const StockTimelineComponent = memo(function StockTimelineComponent({ events }: Props) {
  if (events.length === 0) {
    return <p style={{ color: 'var(--color-text-tertiary)', fontSize: 'var(--text-body)', padding: 24, textAlign: 'center' }}>No timeline events recorded.</p>;
  }

  const sorted = [...events].sort((a, b) => new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime());

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 0, padding: '8px 0' }} role="list" aria-label="Stock timeline">
      {sorted.map((event, i) => {
        const isLast = i === sorted.length - 1;
        return (
          <div key={event.id} role="listitem" style={{ display: 'flex', gap: 16, position: 'relative', paddingLeft: 24 }}>
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', position: 'absolute', left: 0, top: 0, bottom: 0 }}>
              <div style={{ width: 14, height: 14, borderRadius: '50%', border: '3px solid var(--color-border)', background: 'var(--color-surface)', zIndex: 1, display: 'flex', alignItems: 'center', justifyContent: 'center' }} />
              {!isLast && <div style={{ width: 2, flex: 1, background: 'var(--color-border)', marginTop: 2 }} />}
            </div>
            <div style={{ flex: 1, paddingBottom: isLast ? 4 : 24 }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: 8, flexWrap: 'wrap' }}>
                <Icon name={actionIcons[event.action] ?? 'activity'} size={14} />
                <span style={{ fontWeight: 600, fontSize: 'var(--text-body)', color: 'var(--color-text-primary)' }}>{event.action}</span>
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{event.timestamp}</span>
              </div>
              <div style={{ display: 'flex', gap: 8, marginTop: 4, flexWrap: 'wrap' }}>
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Qty: {event.quantity}</span>
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>To: {event.toState.replace(/_/g, ' ')}</span>
              </div>
              {event.note && <p style={{ margin: '4px 0 0', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{event.note}</p>}
              <p style={{ margin: '2px 0 0', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', display: 'flex', alignItems: 'center', gap: 4 }}><Icon name="user" size={12} /> {event.user}</p>
            </div>
          </div>
        );
      })}
    </div>
  );
});
