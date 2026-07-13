import React from 'react';

export type AlertType = 'success' | 'warning' | 'info' | 'error';

export interface AlertProps {
  type?: AlertType;
  title?: string;
  message?: string;
  children?: React.ReactNode;
  onClose?: () => void;
  dismissible?: boolean;
  className?: string;
  style?: React.CSSProperties;
}

function CheckmarkIcon() {
  return (
    <svg width="20" height="20" viewBox="0 0 20 20" fill="none" aria-hidden="true">
      <circle cx="10" cy="10" r="9" stroke="currentColor" strokeWidth="1.5" />
      <path d="M6 10l2.5 2.5L14 7" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
    </svg>
  );
}

function TriangleIcon() {
  return (
    <svg width="20" height="20" viewBox="0 0 20 20" fill="none" aria-hidden="true">
      <path d="M10 2L2 17h16L10 2z" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
      <path d="M10 7.5v3.5M10 13.5h0" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" />
    </svg>
  );
}

function InfoCircleIcon() {
  return (
    <svg width="20" height="20" viewBox="0 0 20 20" fill="none" aria-hidden="true">
      <circle cx="10" cy="10" r="9" stroke="currentColor" strokeWidth="1.5" />
      <path d="M10 9v4.5M10 6.5h0" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" />
    </svg>
  );
}

function XCircleIcon() {
  return (
    <svg width="20" height="20" viewBox="0 0 20 20" fill="none" aria-hidden="true">
      <circle cx="10" cy="10" r="9" stroke="currentColor" strokeWidth="1.5" />
      <path d="M7 7l6 6M13 7l-6 6" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" />
    </svg>
  );
}

const iconMap: Record<AlertType, React.ReactNode> = {
  success: <CheckmarkIcon />,
  warning: <TriangleIcon />,
  info: <InfoCircleIcon />,
  error: <XCircleIcon />,
};

const colorMap: Record<AlertType, { bg: string; border: string; icon: string; text: string }> = {
  success: {
    bg: 'var(--color-success-50)',
    border: 'var(--color-border-success)',
    icon: 'var(--color-icon-success)',
    text: 'var(--color-text-success)',
  },
  warning: {
    bg: 'var(--color-warning-50)',
    border: 'var(--color-warning-100)',
    icon: 'var(--color-icon-warning)',
    text: 'var(--color-text-warning)',
  },
  info: {
    bg: 'var(--color-info-50)',
    border: 'var(--color-info-100)',
    icon: 'var(--color-icon-info)',
    text: 'var(--color-text-info)',
  },
  error: {
    bg: 'var(--color-danger-50)',
    border: 'var(--color-danger-100)',
    icon: 'var(--color-icon-danger)',
    text: 'var(--color-text-danger)',
  },
};

export const Alert: React.FC<AlertProps> = ({
  type = 'info',
  title,
  message,
  children,
  onClose,
  dismissible = false,
  className = '',
  style,
}) => {
  const colors = colorMap[type];

  const containerStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'flex-start',
    gap: 'var(--space-inline-md)',
    padding: 'var(--space-inline-md) var(--space-inline-md)',
    borderRadius: 'var(--radius-md)',
    border: '1px solid',
    borderColor: colors.border,
    backgroundColor: colors.bg,
    ...style,
  };

  const iconStyle: React.CSSProperties = {
    flexShrink: 0,
    color: colors.icon,
    width: 'var(--icon-sm)',
    height: 'var(--icon-sm)',
    marginTop: '2px',
  };

  const contentStyle: React.CSSProperties = {
    flex: 1,
    minWidth: 0,
  };

  const titleStyle: React.CSSProperties = {
    fontSize: 'var(--text-body-sm)',
    fontWeight: 'var(--weight-semibold)',
    color: 'var(--color-text-primary)',
    margin: 0,
  };

  const messageStyle: React.CSSProperties = {
    fontSize: 'var(--text-body-sm)',
    color: 'var(--color-text-secondary)',
    margin: 0,
    marginTop: title ? 'var(--space-stack-xs)' : 0,
  };

  const closeButtonStyle: React.CSSProperties = {
    flexShrink: 0,
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    width: 'var(--icon-sm)',
    height: 'var(--icon-sm)',
    background: 'transparent',
    border: 'none',
    cursor: 'pointer',
    color: 'var(--color-text-secondary)',
    padding: 0,
    borderRadius: 'var(--radius-xs)',
    marginTop: '2px',
  };

  return (
    <div
      className={className}
      style={containerStyle}
      role="alert"
      aria-live="polite"
    >
      <span style={iconStyle}>{iconMap[type]}</span>
      <div style={contentStyle}>
        {title && <p style={titleStyle}>{title}</p>}
        {message && <p style={messageStyle}>{message}</p>}
        {children}
      </div>
      {dismissible && onClose && (
        <button
          style={closeButtonStyle}
          onClick={onClose}
          aria-label="Dismiss alert"
          type="button"
        >
          <svg width="16" height="16" viewBox="0 0 16 16" fill="none" aria-hidden="true">
            <path d="M4 4l8 8M12 4l-8 8" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" />
          </svg>
        </button>
      )}
    </div>
  );
};

Alert.displayName = 'Alert';
export default Alert;
