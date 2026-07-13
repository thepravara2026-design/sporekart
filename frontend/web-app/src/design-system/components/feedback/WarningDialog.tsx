import React from 'react';
import { Dialog, DialogProps } from './Dialog';
import { Button } from '../core/Button';

export interface WarningDialogProps extends Omit<DialogProps, 'children' | 'actions'> {
  message: string;
  actionLabel?: string;
  onAction?: () => void;
}

function WarningTriangleIcon() {
  return (
    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" aria-hidden="true">
      <path d="M12 2L1 21h22L12 2z" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
      <path d="M12 10v4M12 18h0" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" />
    </svg>
  );
}

export const WarningDialog: React.FC<WarningDialogProps> = ({
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
        <Button variant="warning" onClick={() => { onAction?.(); onClose(); }}>{actionLabel}</Button>
      }
    >
      <div style={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        gap: 'var(--space-4)',
        textAlign: 'center',
      } as React.CSSProperties}>
        <div style={{ color: 'var(--color-icon-warning)' } as React.CSSProperties}>
          <WarningTriangleIcon />
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

WarningDialog.displayName = 'WarningDialog';
export default WarningDialog;
