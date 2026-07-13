import React from 'react';
import { Dialog, DialogProps } from './Dialog';
import { Button } from '../core/Button';

export interface ConfirmationDialogProps extends Omit<DialogProps, 'children' | 'actions'> {
  message: string;
  confirmLabel?: string;
  cancelLabel?: string;
  variant?: 'primary' | 'destructive';
  icon?: React.ReactNode;
  onConfirm: () => void;
}

function WarningIcon() {
  return (
    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" aria-hidden="true">
      <path d="M12 2L1 21h22L12 2z" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
      <path d="M12 10v4M12 18h0" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" />
    </svg>
  );
}

export const ConfirmationDialog: React.FC<ConfirmationDialogProps> = ({
  open,
  onClose,
  onConfirm,
  title,
  message,
  confirmLabel = 'Confirm',
  cancelLabel = 'Cancel',
  variant = 'primary',
  icon,
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
        <>
          <Button variant="secondary" onClick={onClose}>{cancelLabel}</Button>
          <Button variant={variant === 'destructive' ? 'destructive' : 'primary'} onClick={onConfirm}>{confirmLabel}</Button>
        </>
      }
    >
      <div style={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        gap: 'var(--space-4)',
        textAlign: 'center',
      } as React.CSSProperties}>
        {icon ? (
          <div style={{ color: variant === 'destructive' ? 'var(--color-icon-danger)' : 'var(--color-icon-warning)' } as React.CSSProperties}>
            {icon}
          </div>
        ) : (
          <div style={{ color: variant === 'destructive' ? 'var(--color-icon-danger)' : 'var(--color-icon-warning)' } as React.CSSProperties}>
            <WarningIcon />
          </div>
        )}
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

ConfirmationDialog.displayName = 'ConfirmationDialog';
export default ConfirmationDialog;
