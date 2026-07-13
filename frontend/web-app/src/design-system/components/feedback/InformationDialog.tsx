import React from 'react';
import { Dialog, DialogProps } from './Dialog';
import { Button } from '../core/Button';

export interface InformationDialogProps extends Omit<DialogProps, 'children' | 'actions'> {
  message: string;
  actionLabel?: string;
  onAction?: () => void;
}

function InfoIcon() {
  return (
    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" aria-hidden="true">
      <circle cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="1.5" />
      <path d="M12 8v8M12 8h0" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" />
      <circle cx="12" cy="8" r="0.5" fill="currentColor" />
    </svg>
  );
}

export const InformationDialog: React.FC<InformationDialogProps> = ({
  open,
  onClose,
  title,
  message,
  actionLabel = 'Got it',
  onAction,
  size = 'sm',
  ...props
}) => {
  return (
    <Dialog
      open={open}
      onClose={onClose}
      title={title}
      size={size}
      {...props}
      actions={
        <Button variant="primary" onClick={() => { onAction?.(); onClose(); }}>{actionLabel}</Button>
      }
    >
      <div style={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        gap: 'var(--space-4)',
        textAlign: 'center',
      } as React.CSSProperties}>
        <div style={{ color: 'var(--color-icon-info)' } as React.CSSProperties}>
          <InfoIcon />
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

InformationDialog.displayName = 'InformationDialog';
export default InformationDialog;
