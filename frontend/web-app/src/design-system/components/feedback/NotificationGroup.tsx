import React from 'react';

export interface NotificationGroupProps {
  label: string;
  children: React.ReactNode;
  count?: number;
  className?: string;
  style?: React.CSSProperties;
}

export const NotificationGroup: React.FC<NotificationGroupProps> = ({
  label,
  children,
  count,
  className = '',
  style,
}) => {
  const containerStyle: React.CSSProperties = {
    marginBottom: 'var(--space-stack-md)',
    ...style,
  };

  const headerStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    padding: 'var(--space-stack-xs) var(--space-stack-md)',
  };

  const labelStyle: React.CSSProperties = {
    fontSize: 'var(--text-label)',
    fontWeight: 'var(--weight-semibold)',
    color: 'var(--color-text-secondary)',
    textTransform: 'uppercase',
    letterSpacing: 'var(--tracking-wide)',
    margin: 0,
  };

  const countStyle: React.CSSProperties = {
    fontSize: 'var(--text-caption)',
    color: 'var(--color-text-disabled)',
    fontWeight: 'var(--weight-medium)',
    background: 'var(--color-neutral-100)',
    borderRadius: 'var(--radius-full)',
    padding: '0 var(--space-inline-sm)',
    minWidth: 20,
    height: 20,
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
  };

  const dividerStyle: React.CSSProperties = {
    height: '1px',
    background: 'var(--color-border-default)',
    margin: '0 var(--space-inline-md)',
    border: 'none',
  };

  const listStyle: React.CSSProperties = {
    listStyle: 'none',
    margin: 0,
    padding: 0,
  };

  return (
    <div
      className={`sk-notification-group ${className}`.trim()}
      style={containerStyle}
      role="group"
      aria-label={label}
    >
      <div style={headerStyle}>
        <h3 style={labelStyle}>{label}</h3>
        {count !== undefined && count > 0 && (
          <span style={countStyle} aria-label={`${count} notifications`}>
            {count}
          </span>
        )}
      </div>
      <div style={dividerStyle} role="separator" />
      <div style={listStyle} role="list">
        {children}
      </div>
    </div>
  );
};

NotificationGroup.displayName = 'NotificationGroup';
export default NotificationGroup;
