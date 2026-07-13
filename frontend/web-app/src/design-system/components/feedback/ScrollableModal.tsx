import React from 'react';
import { Modal, ModalProps } from './Modal';

export interface ScrollableModalProps extends Omit<ModalProps, 'children'> {
  children: React.ReactNode;
  header?: React.ReactNode;
  footer?: React.ReactNode;
}

export const ScrollableModal: React.FC<ScrollableModalProps> = ({
  open,
  onClose,
  title,
  children,
  actions,
  header,
  footer,
  size = 'lg',
  ...props
}) => {
  return (
    <Modal
      open={open}
      onClose={onClose}
      title={title}
      size={size}
      actions={footer || actions}
      {...props}
    >
      {header && (
        <div style={{
          paddingBottom: 'var(--space-4)',
          borderBottom: '1px solid var(--color-border-default)',
          marginBottom: 'var(--space-4)',
        } as React.CSSProperties}>
          {header}
        </div>
      )}
      <div style={{
        overflowY: 'auto',
        flex: 1,
        maxHeight: '60vh',
      } as React.CSSProperties}>
        {children}
      </div>
    </Modal>
  );
};

ScrollableModal.displayName = 'ScrollableModal';
export default ScrollableModal;
