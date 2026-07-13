import React from 'react';
import { Alert, AlertProps } from './Alert';

export interface PageAlertAction {
  label: string;
  onClick: () => void;
}

export interface PageAlertProps extends AlertProps {
  action?: PageAlertAction;
}

export const PageAlert: React.FC<PageAlertProps> = ({
  action,
  className = '',
  style,
  ...props
}) => {
  const containerStyle: React.CSSProperties = {
    width: '100%',
    borderRadius: 0,
    borderLeft: 'none',
    borderRight: 'none',
    ...style,
  };

  const actionButtonStyle: React.CSSProperties = {
    display: 'inline-flex',
    alignItems: 'center',
    gap: 'var(--space-inline-xs)',
    padding: 'var(--space-1) var(--space-inline-md)',
    fontSize: 'var(--text-button)',
    fontWeight: 'var(--weight-semibold)',
    lineHeight: 'var(--leading-normal)',
    borderRadius: 'var(--radius-btn)',
    border: '1px solid var(--color-border-strong)',
    background: 'var(--color-bg-surface-default)',
    color: 'var(--color-text-primary)',
    cursor: 'pointer',
    marginTop: 'var(--space-stack-sm)',
  };

  return (
    <Alert
      {...props}
      className={className}
      style={containerStyle}
    >
      {action && (
        <button
          style={actionButtonStyle}
          onClick={action.onClick}
          type="button"
        >
          {action.label}
        </button>
      )}
    </Alert>
  );
};

PageAlert.displayName = 'PageAlert';
export default PageAlert;
