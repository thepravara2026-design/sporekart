import { memo } from 'react';
import type { AlumniEvent } from '../types';
import { EVENT_TYPE_LABELS } from '../types';

const STATUS_COLORS: Record<string, { bg: string; color: string }> = {
  draft: { bg: '#f3f4f6', color: '#6b7280' },
  announced: { bg: '#eff6ff', color: '#2563eb' },
  open: { bg: '#f0fdf4', color: '#16a34a' },
  closed: { bg: '#fefce8', color: '#ca8a04' },
  'in-progress': { bg: '#f0fdf4', color: '#059669' },
  completed: { bg: '#f3f4f6', color: '#6b7280' },
  cancelled: { bg: '#fef2f2', color: '#dc2626' },
};

export const EventCard = memo(function EventCard({ event }: { event: AlumniEvent }) {
  const sc = STATUS_COLORS[event.status] || { bg: '#f3f4f6', color: '#6b7280' };
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 6, padding: 12, borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 4 }}>
        <span style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-body)' }}>{event.title}</span>
        <span style={{ padding: '1px 6px', borderRadius: 'var(--radius-full)', fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)', background: sc.bg, color: sc.color }}>{event.status}</span>
      </div>
      <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>{EVENT_TYPE_LABELS[event.eventType]} • {event.mode}</span>
      <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-tertiary)' }}>{event.description}</span>
      <div style={{ display: 'flex', gap: 12, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', flexWrap: 'wrap' }}>
        <span>Date: {event.date} at {event.time}</span>
        <span>Venue: {event.venue}</span>
      </div>
      <div style={{ display: 'flex', gap: 16, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
        <span>Registered: {event.registeredCount}/{event.maxAttendees}</span>
        <span>Attended: {event.attendedCount}</span>
        <span>Fee: {event.fee}</span>
      </div>
    </div>
  );
});
