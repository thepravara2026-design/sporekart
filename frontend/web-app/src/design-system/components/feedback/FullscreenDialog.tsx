import React from 'react';
import { Dialog, DialogProps } from './Dialog';

export interface FullscreenDialogProps extends Omit<DialogProps, 'size'> {}

export const FullscreenDialog: React.FC<FullscreenDialogProps> = (props) => {
  return (
    <Dialog
      {...props}
      size="fullscreen"
    />
  );
};

FullscreenDialog.displayName = 'FullscreenDialog';
export default FullscreenDialog;
