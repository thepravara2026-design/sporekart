import React from 'react';
import { Card } from '../../../../design-system/components/composite/Card';
import { Button } from '../../../../design-system/components/core/Button';
import { Icon } from '../../../../design-system/icons/Icon';
import type { ReactNode } from 'react';

export interface CreationStateProps {
  title: string;
  message: string;
  icon: string;
  iconColor?: string;
  actionLabel?: string;
  onAction?: () => void;
  children?: ReactNode;
}

const BaseState: React.FC<CreationStateProps> = ({ title, message, icon, iconColor, actionLabel, onAction, children }) => {
  return (
    <Card variant="outlined" padding="lg" style={{ maxWidth: 520, margin: 'var(--space-section-gap) auto', textAlign: 'center' }}>
      <div
        style={{
          width: 56,
          height: 56,
          borderRadius: 'var(--radius-full)',
          background: iconColor ? `${iconColor}1a` : 'var(--color-bg-surface-raised)',
          color: iconColor ?? 'var(--color-text-secondary)',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          margin: '0 auto var(--space-stack-md)',
        }}
      >
        <Icon name={icon} size={28} />
      </div>
      <h2 style={{ margin: '0 0 var(--space-stack-xs)', fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)' }}>
        {title}
      </h2>
      <p style={{ margin: '0 0 var(--space-stack-md)', color: 'var(--color-text-secondary)', fontSize: 'var(--text-body-sm)' }}>
        {message}
      </p>
      {children}
      {actionLabel && onAction && (
        <Button variant="primary" onClick={onAction}>
          {actionLabel}
        </Button>
      )}
    </Card>
  );
};

export const ValidationFailedState: React.FC<{ onFix?: () => void }> = ({ onFix }) => (
  <BaseState
    title="Validation Failed"
    message="Some required fields are missing or invalid. Return to the highlighted steps to fix them."
    icon="Info"
    iconColor="var(--color-warning)"
    actionLabel="Back to Review"
    onAction={onFix}
  />
);

export const DraftMissingState: React.FC<{ onStart?: () => void }> = ({ onStart }) => (
  <BaseState
    title="No Saved Draft"
    message="We couldn't find a saved draft for this product. Start a new product to create one."
    icon="File"
    iconColor="var(--color-info)"
    actionLabel="Start New Product"
    onAction={onStart}
  />
);

export const PermissionDeniedState: React.FC<{ onBack?: () => void }> = ({ onBack }) => (
  <BaseState
    title="Permission Denied"
    message="Your role does not have permission to create products. Contact an administrator if you need access."
    icon="Lock"
    iconColor="var(--color-danger)"
    actionLabel="Back to Products"
    onAction={onBack}
  />
);

export const OfflineState: React.FC<{ onRetry?: () => void }> = ({ onRetry }) => (
  <BaseState
    title="You're Offline"
    message="Product creation requires a connection. Drafts are saved locally and will sync when you're back online."
    icon="Info"
    iconColor="var(--color-warning)"
    actionLabel="Retry"
    onAction={onRetry}
  />
);

export const MaintenanceState: React.FC<{ onRetry?: () => void }> = ({ onRetry }) => (
  <BaseState
    title="Under Maintenance"
    message="The product service is temporarily unavailable. Please try again shortly."
    icon="Settings"
    iconColor="var(--color-warning)"
    actionLabel="Retry"
    onAction={onRetry}
  />
);

export const UnknownErrorState: React.FC<{ onRetry?: () => void }> = ({ onRetry }) => (
  <BaseState
    title="Something Went Wrong"
    message="An unexpected error occurred while loading the wizard. You can try again."
    icon="Info"
    iconColor="var(--color-danger)"
    actionLabel="Reload"
    onAction={onRetry}
  />
);

export default BaseState;
