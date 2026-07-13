import React, { useEffect, useState } from 'react';
import { createPortal } from 'react-dom';
import type { DialogSize } from './Dialog';

export type ResponsiveDialogSize = Exclude<DialogSize, 'fullscreen'>;

export interface ResponsiveDialogProps {
  open: boolean;
  onClose: () => void;
  title: string;
  children: React.ReactNode;
  actions?: React.ReactNode;
  size?: ResponsiveDialogSize;
  closeOnOverlay?: boolean;
  closeOnEscape?: boolean;
  showCloseButton?: boolean;
  className?: string;
  style?: React.CSSProperties;
}

const sizeWidthMap: Record<ResponsiveDialogSize, string> = {
  sm: '400px',
  md: '480px',
  lg: '640px',
  xl: '800px',
};

export const ResponsiveDialog: React.FC<ResponsiveDialogProps> = ({
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
  const [isMobile, setIsMobile] = useState(false);

  useEffect(() => {
    const mq = window.matchMedia('(max-width: 767px)');
    setIsMobile(mq.matches);
    const handler = (e: MediaQueryListEvent) => setIsMobile(e.matches);
    mq.addEventListener('change', handler);
    return () => mq.removeEventListener('change', handler);
  }, []);

  const titleId = React.useId();
  const contentId = React.useId();

  useEffect(() => {
    if (!open || !closeOnEscape) return;
    const handleEscape = (e: KeyboardEvent) => {
      if (e.key === 'Escape') onClose();
    };
    document.addEventListener('keydown', handleEscape);
    return () => document.removeEventListener('keydown', handleEscape);
  }, [open, closeOnEscape, onClose]);

  if (!open) return null;

  const sheetStyle: React.CSSProperties = {
    position: 'fixed',
    bottom: 0,
    left: 0,
    right: 0,
    backgroundColor: 'var(--color-bg-surface-modal)',
    borderRadius: 'var(--radius-dialog) var(--radius-dialog) 0 0',
    boxShadow: 'var(--shadow-4)',
    maxHeight: '85vh',
    overflow: 'hidden',
    display: 'flex',
    flexDirection: 'column',
    zIndex: 'var(--z-modal)',
    animation: 'sk-dialog-enter var(--duration-normal) var(--easing-standard)',
    ...style,
  };

  const dialogStyle: React.CSSProperties = {
    backgroundColor: 'var(--color-bg-surface-modal)',
    borderRadius: 'var(--radius-dialog)',
    boxShadow: 'var(--shadow-4)',
    width: sizeWidthMap[size],
    maxWidth: 'calc(100vw - var(--space-8))',
    maxHeight: 'calc(100vh - var(--space-8))',
    overflow: 'hidden',
    display: 'flex',
    flexDirection: 'column',
    zIndex: 'var(--z-modal)',
    animation: 'sk-dialog-enter var(--duration-normal) var(--easing-standard)',
    ...style,
  };

  const dialog = (
    <div
      style={{
        position: 'fixed',
        inset: 0,
        display: 'flex',
        alignItems: isMobile ? 'flex-end' : 'center',
        justifyContent: 'center',
        zIndex: 'var(--z-modal-backdrop)',
        backgroundColor: 'var(--color-bg-overlay)',
        animation: 'sk-dialog-overlay-enter var(--duration-normal) var(--easing-standard)',
      } as React.CSSProperties}
      onClick={(e) => {
        if (closeOnOverlay && e.target === e.currentTarget) onClose();
      }}
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
        @keyframes sk-sheet-enter {
          from { transform: translateY(100%); }
          to { transform: translateY(0); }
        }
      `}</style>
      <div
        className={className}
        style={isMobile
          ? { ...sheetStyle, animation: 'sk-sheet-enter var(--duration-normal) var(--easing-standard)' }
          : dialogStyle
        }
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
    </div>
  );

  return createPortal(dialog, document.body);
};

ResponsiveDialog.displayName = 'ResponsiveDialog';
export default ResponsiveDialog;
