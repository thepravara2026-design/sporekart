import React from 'react';
import { Modal, ModalProps } from './Modal';

export interface PersistentModalProps extends Omit<ModalProps, 'closeOnOverlay' | 'closeOnEscape'> {
  closeOnOverlay?: false;
  closeOnEscape?: false;
}

export const PersistentModal: React.FC<PersistentModalProps> = ({
  closeOnOverlay = false,
  closeOnEscape = false,
  ...props
}) => {
  return (
    <Modal
      closeOnOverlay={closeOnOverlay}
      closeOnEscape={closeOnEscape}
      {...props}
    />
  );
};

PersistentModal.displayName = 'PersistentModal';
export default PersistentModal;
