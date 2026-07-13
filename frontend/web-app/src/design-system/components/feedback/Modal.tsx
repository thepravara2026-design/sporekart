import React, { useEffect, useRef, useCallback } from 'react';
import { createPortal } from 'react-dom';

export type ModalSize = 'sm' | 'md' | 'lg' | 'xl' | 'fullscreen';

export interface ModalProps {
  open: boolean;
  onClose: () => void;
  title: string;
  children: React.ReactNode;
  actions?: React.ReactNode;
  size?: ModalSize;
  closeOnOverlay?: boolean;
  closeOnEscape?: boolean;
  showCloseButton?: boolean;
  className?: string;
  style?: React.CSSProperties;
}

const sizeWidthMap: Record<ModalSize, string> = {
  sm: '480px',
  md: '640px',
  lg: '800px',
  xl: '960px',
  fullscreen: '100vw',
};

function FocusTrap({ children }: { children: React.ReactNode }) {
  const containerRef = useRef<HTMLDivElement>(null);

  const handleKeyDown = useCallback((e: KeyboardEvent) => {
    if (!containerRef.current) return;
    if (e.key !== 'Tab') return;

    const selector = 'a[href], button:not([disabled]), textarea:not([disabled]), input:not([disabled]), select:not([disabled]), [tabindex]:not([tabindex="-1"])';
    const elements = containerRef.current.querySelectorAll<HTMLElement>(selector);
    const first = elements[0];
    const last = elements[elements.length - 1];
    if (!first) return;

    if (e.shiftKey) {
      if (document.activeElement === first) {
        e.preventDefault();
        last?.focus();
      }
    } else {
      if (document.activeElement === last || !containerRef.current.contains(document.activeElement)) {
        e.preventDefault();
        first.focus();
      }
    }
  }, []);

  useEffect(() => {
    const container = containerRef.current;
    if (!container) return;
    const selector = 'a[href], button:not([disabled]), textarea:not([disabled]), input:not([disabled]), select:not([disabled]), [tabindex]:not([tabindex="-1"])';
    const elements = container.querySelectorAll<HTMLElement>(selector);
    const first = elements[0];
    if (first) first.focus();
    document.addEventListener('keydown', handleKeyDown);
    return () => document.removeEventListener('keydown', handleKeyDown);
  }, [handleKeyDown]);

  return <div ref={containerRef} tabIndex={-1}>{children}</div>;
}

export const Modal: React.FC<ModalProps> = ({
  open,
  onClose,
  title,
  children,
  actions,
  size = 'lg',
  closeOnOverlay = true,
  closeOnEscape = true,
  showCloseButton = true,
  className = '',
  style,
}) => {
  const titleId = React.useId();
  const contentId = React.useId();
  const previouslyFocused = useRef<HTMLElement | null>(null);

  useEffect(() => {
    if (open) {
      previouslyFocused.current = document.activeElement as HTMLElement;
    }
    if (!open && previouslyFocused.current) {
      previouslyFocused.current.focus();
      previouslyFocused.current = null;
    }
  }, [open]);

  useEffect(() => {
    if (!open || !closeOnEscape) return;
    const handleEscape = (e: KeyboardEvent) => {
      if (e.key === 'Escape') onClose();
    };
    document.addEventListener('keydown', handleEscape);
    return () => document.removeEventListener('keydown', handleEscape);
  }, [open, closeOnEscape, onClose]);

  const handleOverlayClick = useCallback((e: React.MouseEvent) => {
    if (closeOnOverlay && e.target === e.currentTarget) onClose();
  }, [closeOnOverlay, onClose]);

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
        backgroundColor: 'var(--color-bg-overlay)',
        animation: 'sk-modal-overlay-enter var(--duration-normal) var(--easing-standard)',
      } as React.CSSProperties}
      onClick={handleOverlayClick}
      role="dialog"
      aria-modal="true"
      aria-labelledby={titleId}
      aria-describedby={contentId}
    >
      <style>{`
        @keyframes sk-modal-overlay-enter {
          from { opacity: 0; }
          to { opacity: 1; }
        }
        @keyframes sk-modal-enter {
          from { opacity: 0; transform: translateY(24px) scale(0.97); }
          to { opacity: 1; transform: translateY(0) scale(1); }
        }
      `}</style>
      <FocusTrap>
        <div
          className={className}
          style={{
            backgroundColor: 'var(--color-bg-surface-modal)',
            borderRadius: 'var(--radius-dialog)',
            boxShadow: 'var(--shadow-4)',
            width: size === 'fullscreen' ? '100vw' : sizeWidthMap[size],
            maxWidth: size === 'fullscreen' ? '100vw' : `calc(100vw - var(--space-8))`,
            maxHeight: size === 'fullscreen' ? '100vh' : `calc(100vh - var(--space-8))`,
            height: size === 'fullscreen' ? '100vh' : 'auto',
            overflow: 'hidden',
            display: 'flex',
            flexDirection: 'column',
            zIndex: 'var(--z-modal)',
            animation: 'sk-modal-enter var(--duration-normal) var(--easing-standard)',
            ...style,
          } as React.CSSProperties}
        >
          <div style={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'space-between',
            padding: 'var(--space-6) var(--space-6) 0 var(--space-6)',
          } as React.CSSProperties}>
            <h2 id={titleId} style={{
              fontSize: 'var(--text-h2)',
              fontWeight: 'var(--weight-semibold)',
              color: 'var(--color-text-primary)',
              margin: 0,
            } as React.CSSProperties}>
              {title}
            </h2>
            {showCloseButton && (
              <button
                onClick={onClose}
                aria-label="Close modal"
                style={{
                  background: 'none',
                  border: 'none',
                  cursor: 'pointer',
                  padding: 'var(--space-1)',
                  color: 'var(--color-text-secondary)',
                  fontSize: 'var(--text-h4)',
                  lineHeight: 1,
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  borderRadius: 'var(--radius-xs)',
                } as React.CSSProperties}
              >
                <svg width="20" height="20" viewBox="0 0 20 20" fill="none" aria-hidden="true">
                  <path d="M15 5L5 15M5 5l10 10" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" />
                </svg>
              </button>
            )}
          </div>
          <div id={contentId} style={{
            padding: 'var(--space-5) var(--space-6)',
            fontSize: 'var(--text-body)',
            color: 'var(--color-text-secondary)',
            overflowY: 'auto',
            flex: 1,
          } as React.CSSProperties}>
            {children}
          </div>
          {actions && (
            <div style={{
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'flex-end',
              gap: 'var(--space-3)',
              padding: 'var(--space-4) var(--space-6) var(--space-6)',
              borderTop: '1px solid var(--color-border-default)',
            } as React.CSSProperties}>
              {actions}
            </div>
          )}
        </div>
      </FocusTrap>
    </div>
  );

  return createPortal(modal, document.body);
};

Modal.displayName = 'Modal';
export default Modal;
