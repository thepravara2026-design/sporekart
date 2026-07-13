import React from 'react';
import { Dialog, DialogProps } from './Dialog';
import { Button } from '../core/Button';

export interface AlertDialogProps extends Omit<DialogProps, 'children' | 'actions'> {
  message: string;
  actionLabel?: string;
}

export const AlertDialog: React.FC<AlertDialogProps> = ({
  open,
  onClose,
  title,
  message,
  actionLabel = 'OK',
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
        <Button variant="primary" onClick={onClose}>{actionLabel}</Button>
      }
    >
      <p style={{
        margin: 0,
        fontSize: 'var(--text-body)',
        color: 'var(--color-text-secondary)',
        lineHeight: 'var(--leading-relaxed)',
      } as React.CSSProperties}>
        {message}
      </p>
    </Dialog>
  );
};

AlertDialog.displayName = 'AlertDialog';
export default AlertDialog;
