import React from 'react';

export interface NotificationEmptyProps {
  message?: string;
  action?: { label: string; onClick: () => void };
  className?: string;
  style?: React.CSSProperties;
}

export const NotificationEmpty: React.FC<NotificationEmptyProps> = ({
  message = 'No notifications',
  action,
  className = '',
  style,
}) => {
  const containerStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
    padding: 'calc(var(--space-section-gap) * 2) var(--space-stack-md)',
    gap: 'var(--space-stack-lg)',
    textAlign: 'center',
    ...style,
  };

  const iconWrapperStyle: React.CSSProperties = {
    width: 'var(--icon-3xl)',
    height: 'var(--icon-3xl)',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 'var(--radius-full)',
    background: 'var(--color-neutral-100)',
    color: 'var(--color-text-disabled)',
  };

  const messageStyle: React.CSSProperties = {
    fontSize: 'var(--text-body)',
    color: 'var(--color-text-secondary)',
    margin: 0,
    fontWeight: 'var(--weight-medium)',
  };

  const actionBtnStyle: React.CSSProperties = {
    background: 'none',
    border: '1px solid var(--color-border-default)',
    borderRadius: 'var(--radius-btn)',
    padding: 'var(--space-inline-sm) var(--space-inline-md)',
    color: 'var(--color-text-primary)',
    fontSize: 'var(--text-button)',
    fontWeight: 'var(--weight-medium)',
    cursor: 'pointer',
    transition: 'all var(--duration-fast) var(--easing-standard)',
  };

  return (
    <div
      className={`sk-notification-empty ${className}`.trim()}
      style={containerStyle}
      role="status"
    >
      <div style={iconWrapperStyle} aria-hidden="true">
        <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
          <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9" />
          <path d="M13.73 21a2 2 0 0 1-3.46 0" />
        </svg>
      </div>
      <p style={messageStyle}>{message}</p>
      {action && (
        <button style={actionBtnStyle} onClick={action.onClick} type="button">
          {action.label}
        </button>
      )}
    </div>
  );
};

NotificationEmpty.displayName = 'NotificationEmpty';
export default NotificationEmpty;
