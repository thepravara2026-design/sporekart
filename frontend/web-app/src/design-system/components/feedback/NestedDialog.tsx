import React, { useState } from 'react';
import { Dialog, DialogProps } from './Dialog';

export interface ChildDialogConfig {
  title: string;
  children: React.ReactNode;
  actions?: React.ReactNode;
}

export interface NestedDialogProps extends Omit<DialogProps, 'children'> {
  children: React.ReactNode;
  childDialog?: ChildDialogConfig;
  onChildClose?: () => void;
}

export const NestedDialog: React.FC<NestedDialogProps> = ({
  open,
  onClose,
  title,
  children,
  actions,
  childDialog,
  onChildClose,
  size = 'md',
  ...props
}) => {
  const [childOpen, setChildOpen] = useState(false);

  React.useEffect(() => {
    if (childDialog && open) {
      setChildOpen(true);
    }
  }, [childDialog, open]);

  const handleChildClose = () => {
    setChildOpen(false);
    onChildClose?.();
  };

  return (
    <>
      <Dialog open={open} onClose={onClose} title={title} actions={actions} size={size} {...props}>
        {children}
      </Dialog>
      {childDialog && (
        <Dialog
          open={childOpen}
          onClose={handleChildClose}
          title={childDialog.title}
          actions={childDialog.actions}
          size="sm"
        >
          {childDialog.children}
        </Dialog>
      )}
    </>
  );
};

NestedDialog.displayName = 'NestedDialog';
export default NestedDialog;
