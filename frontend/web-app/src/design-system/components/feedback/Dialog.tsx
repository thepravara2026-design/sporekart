import React, { useEffect, useRef, useCallback } from 'react';
import { createPortal } from 'react-dom';

export type DialogSize = 'sm' | 'md' | 'lg' | 'xl' | 'fullscreen';

export interface DialogProps {
  open: boolean;
  onClose: () => void;
  title: string;
  children: React.ReactNode;
  actions?: React.ReactNode;
  size?: DialogSize;
  closeOnOverlay?: boolean;
  closeOnEscape?: boolean;
  showCloseButton?: boolean;
  className?: string;
  style?: React.CSSProperties;
}

const sizeWidthMap: Record<DialogSize, string> = {
  sm: '400px',
  md: '480px',
  lg: '640px',
  xl: '800px',
  fullscreen: '100vw',
};

function FocusTrap({ children }: { children: React.ReactNode }) {
  const containerRef = useRef<HTMLDivElement>(null);

  const handleKeyDown = useCallback((e: KeyboardEvent) => {
    if (!containerRef.current) return;
    if (e.key !== 'Tab') return;

    const focusableSelectors = 'a[href], button:not([disabled]), textarea:not([disabled]), input:not([disabled]), select:not([disabled]), [tabindex]:not([tabindex="-1"])';
    const focusableElements = containerRef.current.querySelectorAll<HTMLElement>(focusableSelectors);
    const firstElement = focusableElements[0];
    const lastElement = focusableElements[focusableElements.length - 1];

    if (!firstElement) return;

    if (e.shiftKey) {
      if (document.activeElement === firstElement) {
        e.preventDefault();
        lastElement?.focus();
      }
    } else {
      if (document.activeElement === lastElement || !containerRef.current.contains(document.activeElement)) {
        e.preventDefault();
        firstElement.focus();
      }
    }
  }, []);

  useEffect(() => {
    const container = containerRef.current;
    if (!container) return;

    const focusableSelectors = 'a[href], button:not([disabled]), textarea:not([disabled]), input:not([disabled]), select:not([disabled]), [tabindex]:not([tabindex="-1"])';
    const focusableElements = container.querySelectorAll<HTMLElement>(focusableSelectors);
    const firstElement = focusableElements[0];
    if (firstElement) {
      firstElement.focus();
    }

    document.addEventListener('keydown', handleKeyDown);
    return () => {
      document.removeEventListener('keydown', handleKeyDown);
    };
  }, [handleKeyDown]);

  return (
    <div ref={containerRef} tabIndex={-1}>
      {children}
    </div>
  );
}

export const Dialog: React.FC<DialogProps> = ({
  open,
  onClose,
  title,
  children,
  actions,
  size = 'md',
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
      if (e.key === 'Escape') {
        onClose();
      }
    };
    document.addEventListener('keydown', handleEscape);
    return () => document.removeEventListener('keydown', handleEscape);
  }, [open, closeOnEscape, onClose]);

  const handleOverlayClick = useCallback((e: React.MouseEvent) => {
    if (closeOnOverlay && e.target === e.currentTarget) {
      onClose();
    }
  }, [closeOnOverlay, onClose]);

  if (!open) return null;

  const dialog = (
    <div
      style={{
        position: 'fixed',
        inset: 0,
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        zIndex: 'var(--z-modal-backdrop)',
        backgroundColor: 'var(--color-bg-overlay)',
        animation: 'sk-dialog-overlay-enter var(--duration-normal) var(--easing-standard)',
      } as React.CSSProperties}
      onClick={handleOverlayClick}
      role="dialog"
      aria-modal="true"
      aria-labelledby={titleId}
      aria-describedby={contentId}
    >
      <style>{`
        @keyframes sk-dialog-overlay-enter {
          from { opacity: 0; }
          to { opacity: 1; }
        }
        @keyframes sk-dialog-enter {
          from { opacity: 0; transform: translateY(16px) scale(0.98); }
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
            animation: 'sk-dialog-enter var(--duration-normal) var(--easing-standard)',
            ...style,
          } as React.CSSProperties}
        >
          <div style={{
            display: 'flex',
            alignItems: 'flex-start',
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
                aria-label="Close dialog"
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
            padding: 'var(--space-4) var(--space-6)',
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
            } as React.CSSProperties}>
              {actions}
            </div>
          )}
        </div>
      </FocusTrap>
    </div>
  );

  return createPortal(dialog, document.body);
};

Dialog.displayName = 'Dialog';
export default Dialog;
