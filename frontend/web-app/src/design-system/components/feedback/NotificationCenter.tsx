import React, { useMemo } from 'react';
import { createPortal } from 'react-dom';
import { NotificationItem, NotificationItemData } from './NotificationItem';
import { NotificationGroup } from './NotificationGroup';
import { NotificationCategory } from './NotificationCategory';
import { NotificationEmpty } from './NotificationEmpty';

export interface NotificationCategoryData {
  id: string;
  label: string;
  icon?: React.ReactNode;
}

export interface NotificationCenterProps {
  open: boolean;
  onClose: () => void;
  notifications: NotificationItemData[];
  onMarkRead: (id: string) => void;
  onMarkAllRead: () => void;
  onClear: () => void;
  categories?: NotificationCategoryData[];
  activeCategory?: string;
  onCategoryChange?: (category: string) => void;
  className?: string;
  style?: React.CSSProperties;
}

function groupNotifications(notifications: NotificationItemData[]): Record<string, NotificationItemData[]> {
  const groups: Record<string, NotificationItemData[]> = {};
  const now = new Date();
  const today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
  const yesterday = new Date(today.getTime() - 86400000);
  const weekAgo = new Date(today.getTime() - 7 * 86400000);

  notifications.forEach((n) => {
    const date = typeof n.timestamp === 'string' ? new Date(n.timestamp) : n.timestamp;
    let key: string;
    if (date >= today) {
      key = 'Today';
    } else if (date >= yesterday) {
      key = 'Yesterday';
    } else if (date >= weekAgo) {
      key = 'This Week';
    } else {
      key = 'Older';
    }
    if (!groups[key]) groups[key] = [];
    groups[key].push(n);
  });

  return groups;
}

const groupOrder = ['Today', 'Yesterday', 'This Week', 'Older'];

export const NotificationCenter: React.FC<NotificationCenterProps> = ({
  open,
  onClose,
  notifications,
  onMarkRead,
  onMarkAllRead,
  categories,
  activeCategory,
  onCategoryChange,
  className = '',
  style,
}) => {
  const unreadCount = useMemo(() => notifications.filter((n) => !n.read).length, [notifications]);

  const panelWidth = 400;

  const overlayStyle: React.CSSProperties = {
    position: 'fixed',
    inset: 0,
    background: 'var(--color-bg-overlay)',
    zIndex: 'var(--z-drawer)' as unknown as number,
    opacity: open ? 1 : 0,
    visibility: open ? 'visible' : 'hidden',
    transition: 'opacity var(--duration-normal) var(--easing-standard), visibility var(--duration-normal) var(--easing-standard)',
  };

  const panelStyle: React.CSSProperties = {
    position: 'fixed',
    top: 0,
    right: 0,
    bottom: 0,
    width: panelWidth,
    maxWidth: '100vw',
    zIndex: 'var(--z-drawer)' as unknown as number,
    background: 'var(--color-bg-surface-default)',
    display: 'flex',
    flexDirection: 'column',
    boxShadow: 'var(--shadow-3)',
    transform: open ? 'translateX(0)' : 'translateX(100%)',
    transition: 'transform var(--duration-slow) var(--easing-emphasized)',
    borderLeft: '1px solid var(--color-border-default)',
    ...style,
  };

  const headerStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    padding: 'var(--space-stack-md) var(--space-page-x)',
    borderBottom: '1px solid var(--color-border-default)',
    flexShrink: 0,
  };

  const headerLeftStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-inline-sm)',
  };

  const titleStyle: React.CSSProperties = {
    fontSize: 'var(--text-h4)',
    fontWeight: 'var(--weight-semibold)',
    color: 'var(--color-text-primary)',
    margin: 0,
  };

  const unreadBadgeStyle: React.CSSProperties = {
    background: 'var(--color-danger-500)',
    color: '#FFFFFF',
    fontSize: 'var(--text-caption)',
    fontWeight: 'var(--weight-bold)',
    borderRadius: 'var(--radius-full)',
    padding: '0 var(--space-inline-sm)',
    minWidth: 20,
    height: 20,
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    lineHeight: 1,
  };

  const headerActionsStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-inline-xs)',
  };

  const headerBtnStyle: React.CSSProperties = {
    background: 'none',
    border: 'none',
    cursor: 'pointer',
    padding: 'var(--space-inline-xs)',
    color: 'var(--color-text-secondary)',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 'var(--radius-xs)',
    transition: 'background var(--duration-fast) var(--easing-standard)',
  };

  const markAllBtnStyle: React.CSSProperties = {
    ...headerBtnStyle,
    fontSize: 'var(--text-caption)',
    fontWeight: 'var(--weight-medium)',
    padding: 'var(--space-inline-xs) var(--space-inline-sm)',
    color: 'var(--color-text-info)',
  };

  const categoriesBarStyle: React.CSSProperties = {
    display: 'flex',
    gap: 'var(--space-inline-xs)',
    padding: 'var(--space-stack-sm) var(--space-page-x)',
    overflowX: 'auto',
    flexShrink: 0,
    borderBottom: '1px solid var(--color-border-default)',
  };

  const bodyStyle: React.CSSProperties = {
    flex: 1,
    overflowY: 'auto',
    overflowX: 'hidden',
  };

  const footerStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    padding: 'var(--space-stack-md) var(--space-page-x)',
    borderTop: '1px solid var(--color-border-default)',
    flexShrink: 0,
  };

  const footerLinkStyle: React.CSSProperties = {
    background: 'none',
    border: 'none',
    cursor: 'pointer',
    color: 'var(--color-text-info)',
    fontSize: 'var(--text-button)',
    fontWeight: 'var(--weight-medium)',
    padding: 0,
  };

  const filteredNotifications = activeCategory && activeCategory !== 'all'
    ? notifications.filter((n) => n.category === activeCategory)
    : notifications;

  const filteredGroups = useMemo(() => groupNotifications(filteredNotifications), [filteredNotifications, activeCategory]);
  const hasNotifications = Object.keys(filteredGroups).length > 0 && Object.values(filteredGroups).some((g) => g.length > 0);

  if (!open) return null;

  return createPortal(
    <div className={`sk-notification-center ${className}`.trim()}>
      <div style={overlayStyle} onClick={onClose} aria-hidden="true" />
      <div
        style={panelStyle}
        role="dialog"
        aria-modal="true"
        aria-label="Notification center"
      >
        <div style={headerStyle}>
          <div style={headerLeftStyle}>
            <h2 style={titleStyle}>Notifications</h2>
            {unreadCount > 0 && (
              <span style={unreadBadgeStyle}>{unreadCount}</span>
            )}
          </div>
          <div style={headerActionsStyle}>
            {unreadCount > 0 && (
              <button style={markAllBtnStyle} onClick={onMarkAllRead} type="button" aria-label="Mark all as read">
                Mark all read
              </button>
            )}
            <button style={headerBtnStyle} onClick={onClose} type="button" aria-label="Close notification center">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <line x1="18" y1="6" x2="6" y2="18" />
                <line x1="6" y1="6" x2="18" y2="18" />
              </svg>
            </button>
          </div>
        </div>

        {categories && categories.length > 0 && (
          <div style={categoriesBarStyle} role="tablist" aria-label="Notification categories">
            <NotificationCategory
              label="All"
              count={notifications.length}
              active={!activeCategory || activeCategory === 'all'}
              onClick={() => onCategoryChange?.('all')}
            />
            {categories.map((cat) => (
              <NotificationCategory
                key={cat.id}
                label={cat.label}
                icon={cat.icon}
                count={notifications.filter((n) => n.category === cat.id).length}
                active={activeCategory === cat.id}
                onClick={() => onCategoryChange?.(cat.id)}
              />
            ))}
          </div>
        )}

        <div style={bodyStyle}>
          {!hasNotifications ? (
            <NotificationEmpty
              message="No notifications to show"
              action={{ label: 'Clear filters', onClick: () => onCategoryChange?.('all') }}
            />
          ) : (
            groupOrder.map((groupKey) => {
              const items = filteredGroups[groupKey];
              if (!items || items.length === 0) return null;
              return (
                <NotificationGroup key={groupKey} label={groupKey} count={items.length}>
                  {items.map((notification) => (
                    <NotificationItem
                      key={notification.id}
                      notification={notification}
                      onMarkRead={onMarkRead}
                      onRemove={(id) => onMarkRead(id)}
                    />
                  ))}
                </NotificationGroup>
              );
            })
          )}
        </div>

        <div style={footerStyle}>
          <button style={footerLinkStyle} type="button" aria-label="View all notifications">
            View all notifications
          </button>
        </div>
      </div>
    </div>,
    document.body
  );
};

NotificationCenter.displayName = 'NotificationCenter';
export default NotificationCenter;
