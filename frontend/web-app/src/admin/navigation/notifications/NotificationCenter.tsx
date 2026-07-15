import { useState, useRef, useEffect, memo } from 'react';
import { Icon } from '../../../design-system/icons/Icon';
import { NavEmptyState } from '../empty-states/NavEmptyState';
import type { NotificationItem } from '../types';

interface NotificationCenterProps {
  notifications: NotificationItem[];
  onMarkRead: (id: string) => void;
  onMarkAllRead: () => void;
  onDismiss: (id: string) => void;
  onViewAll?: () => void;
  unreadCount: number;
}

export const NotificationCenter = memo(function NotificationCenter({ notifications, onMarkRead, onMarkAllRead, onDismiss, onViewAll, unreadCount }: NotificationCenterProps) {
  const [open, setOpen] = useState(false);
  const ref = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const handleOutside = (e: MouseEvent) => {
      if (ref.current && !ref.current.contains(e.target as Node)) setOpen(false);
    };
    document.addEventListener('mousedown', handleOutside);
    return () => document.removeEventListener('mousedown', handleOutside);
  }, []);

  return (
    <div ref={ref} style={{ position: 'relative' }}>
      <button
        onClick={() => setOpen(!open)}
        aria-label={`Notifications ${unreadCount > 0 ? `(${unreadCount} unread)` : ''}`}
        aria-expanded={open}
        style={{
          position: 'relative',
          width: 36,
          height: 36,
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          border: '1px solid var(--color-border)',
          borderRadius: 'var(--radius-md)',
          background: 'var(--color-surface)',
          cursor: 'pointer',
          color: 'var(--color-text-primary)',
        }}
      >
        <Icon name="bell" size={16} />
        {unreadCount > 0 && (
          <span
            style={{
              position: 'absolute',
              top: -4,
              right: -4,
              minWidth: 16,
              height: 16,
              borderRadius: 8,
              background: 'var(--color-error)',
              color: '#fff',
              fontSize: 10,
              fontWeight: 700,
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              padding: '0 4px',
            }}
          >
            {unreadCount > 9 ? '9+' : unreadCount}
          </span>
        )}
      </button>
      {open && (
        <div
          role="dialog"
          aria-label="Notifications"
          style={{
            position: 'absolute',
            top: '100%',
            right: 0,
            marginTop: 8,
            width: 360,
            maxHeight: 480,
            background: 'var(--color-surface)',
            border: '1px solid var(--color-border)',
            borderRadius: 'var(--radius-lg)',
            boxShadow: 'var(--elevation-xl)',
            zIndex: 200,
            display: 'flex',
            flexDirection: 'column',
            overflow: 'hidden',
          }}
        >
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: '12px 16px', borderBottom: '1px solid var(--color-border)' }}>
            <span style={{ fontWeight: 600, fontSize: 'var(--text-body)', color: 'var(--color-text-primary)' }}>Notifications</span>
            <div style={{ display: 'flex', gap: 4 }}>
              {unreadCount > 0 && (
                <button onClick={onMarkAllRead} style={{ padding: '4px 10px', border: 'none', borderRadius: 'var(--radius-sm)', background: 'var(--color-surface-hover)', cursor: 'pointer', color: 'var(--color-text-secondary)', fontSize: 'var(--text-caption)' }}>
                  Mark all read
                </button>
              )}
            </div>
          </div>
          <div style={{ flex: 1, overflowY: 'auto' }}>
            {notifications.length === 0 ? (
              <NavEmptyState icon="bell" title="No notifications" description="You're all caught up!" />
            ) : (
              <div role="list">
              {notifications.map((n) => (
                <div
                  key={n.id}
                  role="listitem"
                  style={{
                    display: 'flex',
                    gap: 10,
                    padding: '10px 16px',
                    borderBottom: '1px solid var(--color-border)',
                    background: n.read ? 'transparent' : 'var(--color-surface-hover)',
                    cursor: 'default',
                  }}
                >
                  <div style={{ width: 20, flexShrink: 0, paddingTop: 2 }}>
                    {n.type === 'success' && <Icon name="check-circle" size={16} style={{ color: 'var(--color-success)' }} />}
                    {n.type === 'warning' && <Icon name="alert-triangle" size={16} style={{ color: 'var(--color-warning)' }} />}
                    {n.type === 'error' && <Icon name="alert-circle" size={16} style={{ color: 'var(--color-error)' }} />}
                    {n.type === 'info' && <Icon name="info" size={16} style={{ color: 'var(--color-info)' }} />}
                  </div>
                  <div style={{ flex: 1, minWidth: 0 }}>
                    <div style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-primary)', marginBottom: 2, fontWeight: n.read ? 400 : 600 }}>{n.title}</div>
                    {n.message && <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', marginBottom: 4 }}>{n.message}</div>}
                    <div style={{ fontSize: 'var(--text-small)', color: 'var(--color-text-tertiary)' }}>
                      {n.timestamp}
                    </div>
                  </div>
                  <div style={{ display: 'flex', flexDirection: 'column', gap: 2, flexShrink: 0 }}>
                    {!n.read && (
                      <button onClick={() => onMarkRead(n.id)} aria-label="Mark as read" style={{ padding: 2, border: 'none', background: 'none', cursor: 'pointer', color: 'var(--color-text-tertiary)' }}>
                        <Icon name="check" size={12} />
                      </button>
                    )}
                    <button onClick={() => onDismiss(n.id)} aria-label="Dismiss" style={{ padding: 2, border: 'none', background: 'none', cursor: 'pointer', color: 'var(--color-text-tertiary)' }}>
                      <Icon name="x" size={12} />
                    </button>
                  </div>
                </div>
              ))}
              </div>
            )}
          </div>
          {onViewAll && (
            <div style={{ padding: '8px 16px', borderTop: '1px solid var(--color-border)' }}>
              <button
                onClick={onViewAll}
                style={{
                  width: '100%',
                  padding: '6px 0',
                  border: 'none',
                  borderRadius: 'var(--radius-sm)',
                  background: 'var(--color-surface-hover)',
                  cursor: 'pointer',
                  color: 'var(--color-text-secondary)',
                  fontSize: 'var(--text-caption)',
                }}
              >
                View all notifications
              </button>
            </div>
          )}
        </div>
      )}
    </div>
  );
});
