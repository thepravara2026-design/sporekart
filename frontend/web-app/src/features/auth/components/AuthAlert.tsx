import React from 'react';
import { Icon } from '../../../design-system/icons/Icon';

export interface AuthAlertProps {
  type?: 'error' | 'success' | 'info';
  children: React.ReactNode;
}

export function AuthAlert({ type = 'error', children }: AuthAlertProps) {
  const icon = type === 'error' ? 'alert-circle' : type === 'success' ? 'check-circle' : 'info';
  return (
    <div className={`auth-alert auth-alert--${type}`} role={type === 'error' ? 'alert' : 'status'}>
      <span className="auth-alert__icon">
        <Icon name={icon} size={18} color="currentColor" aria-label={type} />
      </span>
      <span>{children}</span>
    </div>
  );
}

export default AuthAlert;
