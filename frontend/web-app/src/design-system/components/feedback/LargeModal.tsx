import React from 'react';
import { Modal, ModalProps } from './Modal';

export interface LargeModalProps extends Omit<ModalProps, 'size'> {}

export const LargeModal: React.FC<LargeModalProps> = (props) => {
  return <Modal {...props} size="lg" />;
};

LargeModal.displayName = 'LargeModal';
export default LargeModal;
