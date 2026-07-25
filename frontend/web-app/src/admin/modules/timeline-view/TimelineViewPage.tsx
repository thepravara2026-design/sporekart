import { useState, useEffect, memo } from 'react';
import { timelineMockService } from './services/timelineMockService';
import type { TimelineEvent } from './types';

const TYPE_COLORS: Record<string, string> = {
  BUSINESS: '#2f6f4f', RISK: '#f97316', ALERT: '#ef4444', PLATFORM: '#3b82f6',
  AI: '#7c3aed', WORKFLOW: '#d97706', TRAINING: '#0891b2', INVENTORY: '#059669', MARKETPLACE: '#1d9bf0',
};

const TYPE_ICONS: Record<string, string> = {
  BUSINESS: '📊', RISK: '⚠️', ALERT: '🔔', PLATFORM: '🖥️',
  AI: '🤖', WORKFLOW: '⚙️', TRAINING: '📚', INVENTORY: '📦', MARKETPLACE: '🏪',
};

export const TimelineViewPage = memo(function TimelineViewPage() {
  const [events, setEvents] = useState<TimelineEvent[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    timelineMockService.getEvents().then((data) => {
      setEvents(data);
      setLoading(false);
    });
  }, []);

  if (loading) return <div style={{ padding: '24px' }}>Loading timeline...</div>;

  return (
    <div style={{ padding: '24px' }}>
      <h2 style={{ margin: '0 0 24px', fontSize: '24px', fontWeight: 700 }}>Business Event Timeline</h2>

      <div style={{ position: 'relative' }}>
        {events.map((event, idx) => (
          <div key={event.id} style={{ display: 'flex', gap: '16px', marginBottom: idx < events.length - 1 ? '0' : '0' }}>
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', width: '40px' }}>
              <div style={{
                width: '32px', height: '32px', borderRadius: '50%',
                background: TYPE_COLORS[event.eventType] || '#6b7280',
                display: 'flex', alignItems: 'center', justifyContent: 'center',
                fontSize: '14px', zIndex: 1, position: 'relative',
              }}>
                {TYPE_ICONS[event.eventType] || '📌'}
              </div>
              {idx < events.length - 1 && (
                <div style={{
                  width: '2px', flex: 1, background: '#e5e7eb', minHeight: '24px',
                }} />
              )}
            </div>
            <div style={{
              flex: 1, background: '#fff', borderRadius: '8px', padding: '16px',
              boxShadow: '0 1px 3px rgba(0,0,0,0.1)', marginBottom: '16px',
              borderLeft: `4px solid ${TYPE_COLORS[event.eventType] || '#6b7280'}`,
            }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '4px' }}>
                <div style={{ fontWeight: 600, fontSize: '14px' }}>{event.title}</div>
                <div style={{ fontSize: '11px', color: '#9ca3af' }}>
                  {new Date(event.timestamp).toLocaleString()}
                </div>
              </div>
              <div style={{ fontSize: '13px', color: '#6b7280', marginBottom: '8px' }}>{event.description}</div>
              <div style={{ display: 'flex', gap: '8px', fontSize: '11px', color: '#9ca3af' }}>
                <span>{event.eventType}</span>
                <span>·</span>
                <span>{event.domain}</span>
                <span>·</span>
                <span>{event.source}</span>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
});
