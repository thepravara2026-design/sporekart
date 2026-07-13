import React, { useState, useCallback } from 'react';
import { Dialog, DialogProps } from './Dialog';

export interface QueuedDialogConfig extends Omit<DialogProps, 'open' | 'onClose'> {
  id: string;
  onClose?: () => void;
}

export interface DialogQueueProps {
  dialogs: QueuedDialogConfig[];
  onComplete?: () => void;
}

export const DialogQueue: React.FC<DialogQueueProps> = ({ dialogs, onComplete }) => {
  const [currentIndex, setCurrentIndex] = useState(0);
  const [completedIds, setCompletedIds] = useState<Set<string>>(new Set());

  const currentDialog = dialogs[currentIndex];

  const handleClose = useCallback(() => {
    if (!currentDialog) return;
    setCompletedIds((prev) => new Set(prev).add(currentDialog.id));
    currentDialog.onClose?.();
    const nextIndex = currentIndex + 1;
    if (nextIndex < dialogs.length) {
      setCurrentIndex(nextIndex);
    } else {
      onComplete?.();
    }
  }, [currentDialog, currentIndex, dialogs.length, onComplete]);

  if (!currentDialog) return null;

  const isCompleted = completedIds.has(currentDialog.id);

  return (
    <Dialog
      {...currentDialog}
      open={!isCompleted}
      onClose={handleClose}
    />
  );
};

DialogQueue.displayName = 'DialogQueue';
export default DialogQueue;
