import React from 'react';

export interface NotificationBadgeProps {
  count?: number;
  dot?: boolean;
  max?: number;
  color?: string;
  size?: 'sm' | 'md';
  className?: string;
  style?: React.CSSProperties;
  children?: React.ReactNode;
}

export const NotificationBadge: React.FC<NotificationBadgeProps> = ({
  count = 0,
  dot = false,
  max = 99,
  color,
  size = 'sm',
  className = '',
  style,
  children,
}) => {
  const badgeColor = color || 'var(--color-danger-500)';

  const wrapperStyle: React.CSSProperties = {
    position: 'relative',
    display: 'inline-flex',
    alignItems: 'center',
    justifyContent: 'center',
    ...style,
  };

  const dotSize = size === 'sm' ? 8 : 10;

  const badgeStyle: React.CSSProperties = dot
    ? {
        position: 'absolute',
        top: 0,
        right: 0,
        width: dotSize,
        height: dotSize,
        borderRadius: 'var(--radius-full)',
        background: badgeColor,
        transform: 'translate(25%, -25%)',
        zIndex: 1,
      }
    : {
        position: 'absolute',
        top: 0,
        right: 0,
        minWidth: size === 'sm' ? 16 : 20,
        height: size === 'sm' ? 16 : 20,
        padding: '0 var(--space-inline-xs)',
        borderRadius: 'var(--radius-badge)',
        background: badgeColor,
        color: '#FFFFFF',
        fontSize: 'var(--text-caption)',
        fontWeight: 'var(--weight-bold)',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        transform: 'translate(50%, -50%)',
        zIndex: 1,
        lineHeight: 1,
        whiteSpace: 'nowrap',
      };

  const displayCount = count > max ? `${max}+` : count;

  if (dot && !children) {
    return (
      <span
        className={`sk-notification-badge ${className}`.trim()}
        style={{
          ...badgeStyle,
          position: 'relative',
          transform: 'none',
        }}
        role="status"
        aria-label={count > 0 ? `${count} unread notifications` : 'No unread notifications'}
      />
    );
  }

  if (!children) {
    if (count === 0) return null;
    return (
      <span
        className={`sk-notification-badge ${className}`.trim()}
        style={{
          ...badgeStyle,
          position: 'relative',
          transform: 'none',
          display: 'inline-flex',
        }}
        role="status"
        aria-label={`${count} unread notifications`}
      >
        {displayCount}
      </span>
    );
  }

  if (count === 0 && !dot) return <>{children}</>;

  return (
    <span className={`sk-notification-badge-wrapper ${className}`.trim()} style={wrapperStyle}>
      {children}
      {dot ? (
        <span style={badgeStyle} role="status" aria-label={`${count} unread notifications`} />
      ) : (
        <span style={badgeStyle} role="status" aria-label={`${count} unread notifications`}>
          {displayCount}
        </span>
      )}
    </span>
  );
};

NotificationBadge.displayName = 'NotificationBadge';
export default NotificationBadge;
