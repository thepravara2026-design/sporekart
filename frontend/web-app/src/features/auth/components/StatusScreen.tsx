import React from 'react';
import { Icon } from '../../../design-system/icons/Icon';
import { Button } from '../../../design-system/components/core/Button';
import '../auth.css';

export type StatusTone = 'info' | 'success' | 'warning' | 'danger';

export interface StatusAction {
  label: string;
  onClick: () => void;
}

export interface StatusScreenProps {
  icon: string;
  tone: StatusTone;
  title: string;
  body: React.ReactNode;
  primary?: StatusAction;
  secondary?: StatusAction;
  children?: React.ReactNode;
}

export function StatusScreen({ icon, tone, title, body, primary, secondary, children }: StatusScreenProps) {
  return (
    <div className="auth-status">
      <main className="auth-status__inner">
        <span className={`auth-status__icon auth-status__icon--${tone}`} aria-hidden="true">
          <Icon name={icon} size={32} color="currentColor" />
        </span>
        <h1 className="auth-status__title">{title}</h1>
        <p className="auth-status__body">{body}</p>
        {children}
        {(primary || secondary) && (
          <div className="auth-status__actions">
            {primary && (
              <Button variant="primary" size="lg" fullWidth onClick={primary.onClick}>
                {primary.label}
              </Button>
            )}
            {secondary && (
              <Button variant="secondary" size="lg" fullWidth onClick={secondary.onClick}>
                {secondary.label}
              </Button>
            )}
          </div>
        )}
      </main>
    </div>
  );
}

export default StatusScreen;
