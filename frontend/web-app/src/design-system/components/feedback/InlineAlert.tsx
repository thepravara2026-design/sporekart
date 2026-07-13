import React from 'react';
import { Alert, AlertProps } from './Alert';

export interface InlineAlertProps extends AlertProps {
  compact?: boolean;
}

export const InlineAlert: React.FC<InlineAlertProps> = ({
  compact = false,
  className = '',
  style,
  ...props
}) => {
  const inlineStyle: React.CSSProperties = {
    border: 'none',
    borderRadius: 0,
    padding: compact ? 'var(--space-1) var(--space-inline-sm)' : 'var(--space-inline-sm) var(--space-inline-md)',
    background: 'transparent',
    ...style,
  };

  return (
    <Alert
      {...props}
      className={className}
      style={inlineStyle}
    />
  );
};

InlineAlert.displayName = 'InlineAlert';
export default InlineAlert;
