import React from 'react';
import { Modal, ModalProps } from './Modal';

export interface StandardModalProps extends Omit<ModalProps, 'size'> {}

export const StandardModal: React.FC<StandardModalProps> = (props) => {
  return <Modal {...props} size="md" />;
};

StandardModal.displayName = 'StandardModal';
export default StandardModal;
