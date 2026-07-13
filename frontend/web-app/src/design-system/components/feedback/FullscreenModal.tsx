import React from 'react';
import { Modal, ModalProps } from './Modal';

export interface FullscreenModalProps extends Omit<ModalProps, 'size'> {}

export const FullscreenModal: React.FC<FullscreenModalProps> = (props) => {
  return <Modal {...props} size="fullscreen" />;
};

FullscreenModal.displayName = 'FullscreenModal';
export default FullscreenModal;
