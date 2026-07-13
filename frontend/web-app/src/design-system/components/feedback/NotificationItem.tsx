import React from 'react';

export interface NotificationItemData {
  id: string;
  type: 'success' | 'error' | 'warning' | 'info';
  title: string;
  message?: string;
  timestamp: Date | string;
  read: boolean;
  category?: string;
  priority?: 'low' | 'normal' | 'high' | 'urgent';
  action?: { label: string; onClick: () => void };
  avatar?: string;
}

export interface NotificationItemProps {
  notification: NotificationItemData;
  onMarkRead?: (id: string) => void;
  onRemove?: (id: string) => void;
  compact?: boolean;
  className?: string;
  style?: React.CSSProperties;
}

const typeIcons: Record<string, React.ReactNode> = {
  success: (
    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="var(--color-icon-success)" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14" />
      <polyline points="22 4 12 14.01 9 11.01" />
    </svg>
  ),
  error: (
    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="var(--color-icon-danger)" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <circle cx="12" cy="12" r="10" />
      <line x1="15" y1="9" x2="9" y2="15" />
      <line x1="9" y1="9" x2="15" y2="15" />
    </svg>
  ),
  warning: (
    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="var(--color-icon-warning)" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z" />
      <line x1="12" y1="9" x2="12" y2="13" />
      <line x1="12" y1="17" x2="12.01" y2="17" />
    </svg>
  ),
  info: (
    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="var(--color-icon-info)" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <circle cx="12" cy="12" r="10" />
      <line x1="12" y1="16" x2="12" y2="12" />
      <line x1="12" y1="8" x2="12.01" y2="8" />
    </svg>
  ),
};

const priorityBarColors: Record<string, string> = {
  low: 'var(--color-neutral-300)',
  normal: 'var(--color-info-500)',
  high: 'var(--color-warning-500)',
  urgent: 'var(--color-danger-500)',
};

function formatRelativeTime(timestamp: Date | string): string {
  const date = typeof timestamp === 'string' ? new Date(timestamp) : timestamp;
  const now = new Date();
  const diffMs = now.getTime() - date.getTime();
  const diffMins = Math.floor(diffMs / 60000);
  const diffHours = Math.floor(diffMs / 3600000);
  const diffDays = Math.floor(diffMs / 86400000);

  if (diffMins < 1) return 'Just now';
  if (diffMins < 60) return `${diffMins}m ago`;
  if (diffHours < 24) return `${diffHours}h ago`;
  if (diffDays < 7) return `${diffDays}d ago`;
  return date.toLocaleDateString();
}

export const NotificationItem: React.FC<NotificationItemProps> = ({
  notification,
  onMarkRead,
  onRemove,
  compact = false,
  className = '',
  style,
}) => {
  const handleClick = () => {
    if (!notification.read && onMarkRead) {
      onMarkRead(notification.id);
    }
  };

  const handleDismiss = (e: React.MouseEvent) => {
    e.stopPropagation();
    if (onRemove) {
      onRemove(notification.id);
    }
  };

  const handleActionClick = (e: React.MouseEvent) => {
    e.stopPropagation();
    notification.action?.onClick();
  };

  const containerStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'flex-start',
    gap: compact ? 'var(--space-inline-sm)' : 'var(--space-inline-md)',
    padding: compact ? 'var(--space-inline-sm) var(--space-inline-md)' : 'var(--space-stack-sm) var(--space-stack-md)',
    background: notification.read ? 'transparent' : 'var(--color-bg-primary-weak)',
    cursor: notification.read ? 'default' : 'pointer',
    borderBottom: '1px solid var(--color-border-default)',
    position: 'relative',
    transition: 'background var(--duration-fast) var(--easing-standard)',
    ...style,
  };

  const priorityBarStyle: React.CSSProperties = {
    position: 'absolute',
    left: 0,
    top: 0,
    bottom: 0,
    width: '3px',
    background: notification.priority ? priorityBarColors[notification.priority] : 'transparent',
    borderRadius: '0 var(--radius-xs) var(--radius-xs) 0',
  };

  const avatarSectionStyle: React.CSSProperties = {
    flexShrink: 0,
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    width: compact ? 'var(--icon-sm)' : 'var(--icon-md)',
    height: compact ? 'var(--icon-sm)' : 'var(--icon-md)',
  };

  const avatarStyle: React.CSSProperties = {
    width: '100%',
    height: '100%',
    borderRadius: 'var(--radius-avatar)',
    objectFit: 'cover',
  };

  const contentStyle: React.CSSProperties = {
    flex: 1,
    minWidth: 0,
  };

  const headerStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    gap: 'var(--space-inline-sm)',
    marginBottom: 'var(--space-inline-xs)',
  };

  const titleStyle: React.CSSProperties = {
    fontSize: compact ? 'var(--text-caption)' : 'var(--text-body-sm)',
    fontWeight: notification.read ? 'var(--weight-normal)' : 'var(--weight-semibold)',
    color: 'var(--color-text-primary)',
    margin: 0,
    overflow: 'hidden',
    textOverflow: 'ellipsis',
    whiteSpace: 'nowrap',
  };

  const timestampStyle: React.CSSProperties = {
    fontSize: 'var(--text-caption)',
    color: 'var(--color-text-disabled)',
    whiteSpace: 'nowrap',
    flexShrink: 0,
  };

  const messageStyle: React.CSSProperties = {
    fontSize: 'var(--text-caption)',
    color: 'var(--color-text-secondary)',
    margin: 0,
    marginBottom: notification.action ? 'var(--space-inline-xs)' : 0,
    overflow: 'hidden',
    textOverflow: 'ellipsis',
    display: '-webkit-box',
    WebkitLineClamp: 2,
    WebkitBoxOrient: 'vertical',
    lineClamp: 2,
  };

  const actionStyle: React.CSSProperties = {
    background: 'none',
    border: 'none',
    padding: 0,
    color: 'var(--color-primary)',
    fontSize: 'var(--text-caption)',
    fontWeight: 'var(--weight-medium)',
    cursor: 'pointer',
    textDecoration: 'underline',
  };

  const dismissBtnStyle: React.CSSProperties = {
    background: 'none',
    border: 'none',
    cursor: 'pointer',
    padding: '2px',
    color: 'var(--color-text-disabled)',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    flexShrink: 0,
    opacity: 0.6,
    transition: 'opacity var(--duration-fast) var(--easing-standard)',
  };

  return (
    <div
      className={`sk-notification-item ${className}`.trim()}
      style={containerStyle}
      onClick={handleClick}
      role="listitem"
      aria-readonly={notification.read}
    >
      {notification.priority && <div style={priorityBarStyle} aria-hidden="true" />}
      <div style={avatarSectionStyle}>
        {notification.avatar ? (
          <img src={notification.avatar} alt="" style={avatarStyle} />
        ) : (
          typeIcons[notification.type]
        )}
      </div>
      <div style={contentStyle}>
        <div style={headerStyle}>
          <span style={titleStyle}>{notification.title}</span>
          <span style={timestampStyle}>{formatRelativeTime(notification.timestamp)}</span>
        </div>
        {notification.message && <p style={messageStyle}>{notification.message}</p>}
        {notification.action && (
          <button style={actionStyle} onClick={handleActionClick} type="button">
            {notification.action.label}
          </button>
        )}
      </div>
      {onRemove && (
        <button
          style={dismissBtnStyle}
          onClick={handleDismiss}
          aria-label="Dismiss notification"
          type="button"
        >
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
            <line x1="18" y1="6" x2="6" y2="18" />
            <line x1="6" y1="6" x2="18" y2="18" />
          </svg>
        </button>
      )}
    </div>
  );
};

NotificationItem.displayName = 'NotificationItem';
export default NotificationItem;
