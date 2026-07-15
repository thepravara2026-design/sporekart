import { memo, useState, useMemo } from 'react';
import type { MovementTimelineEvent } from '../types';

export const MovementTimelineComponent = memo(function MovementTimelineComponent({ events }: { events: MovementTimelineEvent[] }) {
  const [filter, setFilter] = useState('all');
  const filtered = useMemo(() => filter === 'all' ? events : events.filter((e) => e.event === filter), [events, filter]);
  const eventTypes = useMemo(() => Array.from(new Set(events.map((e) => e.event))), [events]);

  if (events.length === 0) return <div style={{ padding: 24, textAlign: 'center', color: 'var(--color-text-tertiary)' }}>No timeline events available.</div>;

  return (
    <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)' }}>
      {eventTypes.length > 1 && (
        <div style={{ padding: '12px 16px', borderBottom: '1px solid var(--color-border)', display: 'flex', gap: 6, flexWrap: 'wrap' }}>
          <button onClick={() => setFilter('all')} style={{ padding: '4px 12px', borderRadius: 'var(--radius-badge)', border: 'none', background: filter === 'all' ? 'var(--color-primary)' : 'var(--color-surface-hover)', color: filter === 'all' ? '#fff' : 'var(--color-text-secondary)', cursor: 'pointer', fontSize: 'var(--text-caption)' }}>All</button>
          {eventTypes.map((type) => (
            <button key={type} onClick={() => setFilter(type)} style={{ padding: '4px 12px', borderRadius: 'var(--radius-badge)', border: 'none', background: filter === type ? 'var(--color-primary)' : 'var(--color-surface-hover)', color: filter === type ? '#fff' : 'var(--color-text-secondary)', cursor: 'pointer', fontSize: 'var(--text-caption)' }}>{type}</button>
          ))}
        </div>
      )}
      <div style={{ padding: 16, display: 'flex', flexDirection: 'column', gap: 0, maxHeight: 480, overflowY: 'auto' }}>
        {filtered.map((event, idx) => (
          <div key={event.id} style={{ display: 'flex', alignItems: 'stretch', gap: 12, minHeight: 56 }}>
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', width: 24 }}>
              <div style={{ width: 10, height: 10, borderRadius: '50%', background: 'var(--color-primary)', flexShrink: 0, marginTop: 6 }} />
              {idx < filtered.length - 1 && <div style={{ flex: 1, width: 2, background: 'var(--color-border)', minHeight: 24 }} />}
            </div>
            <div style={{ flex: 1, paddingBottom: idx < filtered.length - 1 ? 12 : 0 }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', flexWrap: 'wrap', gap: 4 }}>
                <span style={{ fontWeight: 600, fontSize: 'var(--text-body)', color: 'var(--color-text-primary)' }}>{event.event}</span>
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{new Date(event.timestamp).toLocaleString()}</span>
              </div>
              <div style={{ display: 'flex', gap: 6, alignItems: 'center', marginTop: 4, flexWrap: 'wrap' }}>
                {event.fromStatus && <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', background: 'var(--color-surface-hover)', padding: '1px 8px', borderRadius: 'var(--radius-sm)' }}>{event.fromStatus}</span>}
                {event.fromStatus && <span style={{ fontSize: 12, color: 'var(--color-text-tertiary)' }}>→</span>}
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', background: 'var(--color-primary-alpha)', padding: '1px 8px', borderRadius: 'var(--radius-sm)' }}>{event.toStatus}</span>
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>by {event.user}</span>
              </div>
              {event.note && <p style={{ margin: '4px 0 0', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', fontStyle: 'italic' }}>{event.note}</p>}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
});
