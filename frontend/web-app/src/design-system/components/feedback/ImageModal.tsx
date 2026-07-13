import React from 'react';
import { createPortal } from 'react-dom';

export interface ImageModalProps {
  open: boolean;
  onClose: () => void;
  src: string;
  alt?: string;
  className?: string;
  style?: React.CSSProperties;
}

export const ImageModal: React.FC<ImageModalProps> = ({
  open,
  onClose,
  src,
  alt = '',
  className = '',
  style,
}) => {
  React.useEffect(() => {
    if (!open) return;
    const handleEscape = (e: KeyboardEvent) => {
      if (e.key === 'Escape') onClose();
    };
    document.addEventListener('keydown', handleEscape);
    return () => document.removeEventListener('keydown', handleEscape);
  }, [open, onClose]);

  if (!open) return null;

  const modal = (
    <div
      style={{
        position: 'fixed',
        inset: 0,
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        zIndex: 'var(--z-modal-backdrop)',
        backgroundColor: 'var(--backdrop-heavy)',
        animation: 'sk-modal-overlay-enter var(--duration-normal) var(--easing-standard)',
        cursor: 'zoom-out',
        ...style,
      } as React.CSSProperties}
      onClick={onClose}
      role="dialog"
      aria-modal="true"
      aria-label={alt || 'Image preview'}
    >
      <style>{`
        @keyframes sk-modal-overlay-enter {
          from { opacity: 0; }
          to { opacity: 1; }
        }
        @keyframes sk-image-enter {
          from { opacity: 0; transform: scale(0.95); }
          to { opacity: 1; transform: scale(1); }
        }
      `}</style>
      <img
        src={src}
        alt={alt}
        className={className}
        style={{
          maxWidth: '90vw',
          maxHeight: '90vh',
          objectFit: 'contain',
          borderRadius: 'var(--radius-image)',
          boxShadow: 'var(--shadow-4)',
          cursor: 'default',
          animation: 'sk-image-enter var(--duration-normal) var(--easing-standard)',
        } as React.CSSProperties}
        onClick={(e) => e.stopPropagation()}
      />
    </div>
  );

  return createPortal(modal, document.body);
};

ImageModal.displayName = 'ImageModal';
export default ImageModal;
