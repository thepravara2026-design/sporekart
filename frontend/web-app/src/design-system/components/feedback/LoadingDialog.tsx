import React from 'react';
import { Dialog, DialogProps } from './Dialog';

export interface LoadingDialogProps extends Omit<DialogProps, 'children' | 'actions' | 'showCloseButton' | 'closeOnOverlay' | 'closeOnEscape'> {
  message?: string;
}

function SpinnerIcon() {
  return (
    <svg width="32" height="32" viewBox="0 0 32 32" fill="none" aria-hidden="true">
      <circle cx="16" cy="16" r="14" stroke="currentColor" strokeWidth="3" opacity="0.2" />
      <path d="M16 2a14 14 0 0114 14" stroke="currentColor" strokeWidth="3" strokeLinecap="round">
        <animateTransform attributeName="transform" type="rotate" from="0 16 16" to="360 16 16" dur="0.8s" repeatCount="indefinite" />
      </path>
    </svg>
  );
}

export const LoadingDialog: React.FC<LoadingDialogProps> = ({
  open,
  onClose,
  title,
  message = 'Loading...',
  size = 'sm',
  ...props
}) => {
  return (
    <Dialog
      open={open}
      onClose={onClose}
      title={title}
      size={size}
      showCloseButton={false}
      closeOnOverlay={false}
      closeOnEscape={false}
      {...props}
    >
      <div style={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        gap: 'var(--space-4)',
        textAlign: 'center',
        padding: 'var(--space-6) 0',
      } as React.CSSProperties}>
        <div style={{ color: 'var(--color-icon-primary)' } as React.CSSProperties}>
          <SpinnerIcon />
        </div>
        <p style={{
          margin: 0,
          fontSize: 'var(--text-body)',
          color: 'var(--color-text-secondary)',
          lineHeight: 'var(--leading-relaxed)',
        } as React.CSSProperties}>
          {message}
        </p>
      </div>
    </Dialog>
  );
};

LoadingDialog.displayName = 'LoadingDialog';
export default LoadingDialog;
