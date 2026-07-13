import React from 'react';
import { Dialog, DialogProps } from './Dialog';
import { Button } from '../core/Button';

export interface SuccessDialogProps extends Omit<DialogProps, 'children' | 'actions'> {
  message: string;
  actionLabel?: string;
  onAction?: () => void;
}

function CheckIcon() {
  return (
    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" aria-hidden="true">
      <circle cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="1.5" />
      <path d="M8 12l2.5 2.5L16 9" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
    </svg>
  );
}

export const SuccessDialog: React.FC<SuccessDialogProps> = ({
  open,
  onClose,
  title,
  message,
  actionLabel = 'OK',
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
        <div style={{ color: 'var(--color-icon-success)' } as React.CSSProperties}>
          <CheckIcon />
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

SuccessDialog.displayName = 'SuccessDialog';
export default SuccessDialog;
