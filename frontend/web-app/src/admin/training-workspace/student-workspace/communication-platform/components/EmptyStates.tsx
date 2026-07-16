import { memo } from 'react';
import type { EmptyStateType } from '../types';

const CONFIG: Record<string, { icon: string; title: string; message: string }> = {
  noNotifications: { icon: '🔔', title: 'No Notifications', message: 'No notifications found matching your criteria.' },
  noAnnouncements: { icon: '📢', title: 'No Announcements', message: 'No announcements available at this time.' },
  noMessages: { icon: '✉️', title: 'No Messages', message: 'No messages in this folder.' },
  noInbox: { icon: '📥', title: 'Inbox Empty', message: 'Your inbox is empty. New messages will appear here.' },
  noCommunicationHistory: { icon: '📋', title: 'No History', message: 'No communication history available.' },
  noSearchResults: { icon: '🔍', title: 'No Results', message: 'Try adjusting your search or filters.' },
};

export const EmptyState = memo(function EmptyState({ type, onClearFilters }: { type: EmptyStateType; onClearFilters?: () => void }) {
  const cfg = CONFIG[type];
  return (
    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', padding: 'var(--space-8)', gap: 8, textAlign: 'center' }}>
      <div style={{ fontSize: 40 }}>{cfg.icon}</div>
      <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>{cfg.title}</h3>
      <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-tertiary)', margin: 0, maxWidth: 360 }}>{cfg.message}</p>
      {onClearFilters && (
        <button onClick={onClearFilters} style={{ marginTop: 8, padding: '6px 16px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)', cursor: 'pointer', fontSize: 'var(--text-body-sm)' }}>
          Clear Filters
        </button>
      )}
    </div>
  );
});
