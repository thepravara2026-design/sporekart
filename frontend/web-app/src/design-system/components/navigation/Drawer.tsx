import React, { useEffect, useRef, useCallback } from 'react';

export interface DrawerProps {
  open: boolean;
  onClose: () => void;
  children: React.ReactNode;
  side?: 'left' | 'right' | 'bottom';
  width?: string;
  className?: string;
  closeOnOverlay?: boolean;
  title?: string;
}

const sideTransforms: Record<string, string> = {
  left: 'translateX(-100%)',
  right: 'translateX(100%)',
  bottom: 'translateY(100%)',
};

const sideStyles: Record<string, React.CSSProperties> = {
  left: {
    top: 0,
    left: 0,
    bottom: 0,
    height: '100%',
    borderRight: '1px solid var(--color-border-default)',
  },
  right: {
    top: 0,
    right: 0,
    bottom: 0,
    height: '100%',
    borderLeft: '1px solid var(--color-border-default)',
  },
  bottom: {
    left: 0,
    right: 0,
    bottom: 0,
    width: '100%',
    maxHeight: '80vh',
    borderTop: '1px solid var(--color-border-default)',
  },
};

const FOCUSABLE_SELECTOR = [
  'a[href]',
  'button:not([disabled])',
  'input:not([disabled])',
  'textarea:not([disabled])',
  'select:not([disabled])',
  '[tabindex]:not([tabindex="-1"])',
].join(', ');

export const Drawer: React.FC<DrawerProps> = ({
  open,
  onClose,
  children,
  side = 'right',
  width = 'var(--layout-sidebar-width)',
  className = '',
  closeOnOverlay = true,
  title,
}) => {
  const panelRef = useRef<HTMLDivElement>(null);
  const previousActiveElement = useRef<HTMLElement | null>(null);

  const getFocusableElements = useCallback((): HTMLElement[] => {
    if (!panelRef.current) return [];
    return Array.from(panelRef.current.querySelectorAll<HTMLElement>(FOCUSABLE_SELECTOR));
  }, []);

  const focusFirstElement = useCallback(() => {
    const elements = getFocusableElements();
    if (elements.length > 0) {
      elements[0].focus();
    } else {
      panelRef.current?.focus();
    }
  }, [getFocusableElements]);

  useEffect(() => {
    if (open) {
      previousActiveElement.current = document.activeElement as HTMLElement;
      document.body.style.overflow = 'hidden';
      requestAnimationFrame(() => focusFirstElement());
    } else {
      document.body.style.overflow = '';
      previousActiveElement.current?.focus();
    }

    return () => {
      document.body.style.overflow = '';
    };
  }, [open, focusFirstElement]);

  useEffect(() => {
    if (!open) return;

    const handleKeyDown = (e: KeyboardEvent) => {
      if (e.key === 'Escape') {
        e.stopPropagation();
        onClose();
        return;
      }

      if (e.key === 'Tab') {
        const elements = getFocusableElements();
        if (elements.length === 0) {
          e.preventDefault();
          return;
        }

        const first = elements[0];
        const last = elements[elements.length - 1];

        if (e.shiftKey) {
          if (document.activeElement === first) {
            e.preventDefault();
            last.focus();
          }
        } else {
          if (document.activeElement === last) {
            e.preventDefault();
            first.focus();
          }
        }
      }
    };

    document.addEventListener('keydown', handleKeyDown);
    return () => document.removeEventListener('keydown', handleKeyDown);
  }, [open, onClose, getFocusableElements]);

  const overlayStyle: React.CSSProperties = {
    position: 'fixed',
    inset: 0,
    background: 'var(--color-bg-overlay)',
    zIndex: 'var(--z-drawer)' as unknown as number,
    opacity: open ? 1 : 0,
    visibility: open ? 'visible' : 'hidden',
    transition: 'opacity var(--duration-normal) var(--easing-standard), visibility var(--duration-normal) var(--easing-standard)',
  };

  const panelStyle: React.CSSProperties = {
    position: 'fixed',
    zIndex: 'var(--z-drawer)' as unknown as number,
    background: 'var(--color-bg-surface-default)',
    overflowY: 'auto',
    display: 'flex',
    flexDirection: 'column',
    ...sideStyles[side],
    width: side === 'bottom' ? '100%' : width,
    transform: open ? 'translateX(0) translateY(0)' : sideTransforms[side],
    transition: 'transform var(--duration-normal) var(--easing-standard)',
  };

  const handleOverlayClick = () => {
    if (closeOnOverlay) {
      onClose();
    }
  };

  const headerStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    padding: 'var(--space-stack-md) var(--space-page-x)',
    borderBottom: '1px solid var(--color-border-default)',
    minHeight: 'var(--layout-header-height)',
  };

  const titleStyle: React.CSSProperties = {
    font: 'var(--text-h3)',
    fontWeight: 'var(--weight-semibold)',
    color: 'var(--color-text-primary)',
    margin: 0,
  };

  const closeBtnStyle: React.CSSProperties = {
    background: 'none',
    border: 'none',
    cursor: 'pointer',
    padding: 'var(--space-inline-xs)',
    color: 'var(--color-text-secondary)',
    font: 'var(--text-h4)',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
  };

  const contentStyle: React.CSSProperties = {
    flex: 1,
    overflowY: 'auto',
    padding: 'var(--space-stack-md) var(--space-page-x)',
  };

  return (
    <>
      <div
        className="sk-drawer__overlay"
        style={overlayStyle}
        onClick={handleOverlayClick}
        aria-hidden="true"
      />
      <div
        ref={panelRef}
        className={`sk-drawer sk-drawer--${side} ${className}`.trim()}
        style={panelStyle}
        role="dialog"
        aria-modal="true"
        aria-label={title || 'Drawer'}
        tabIndex={-1}
      >
        {title && (
          <div className="sk-drawer__header" style={headerStyle}>
            <h2 className="sk-drawer__title" style={titleStyle}>{title}</h2>
            <button
              className="sk-drawer__close"
              style={closeBtnStyle}
              onClick={onClose}
              aria-label="Close drawer"
              type="button"
            >
              &#x2715;
            </button>
          </div>
        )}
        <div className="sk-drawer__content" style={contentStyle}>
          {children}
        </div>
      </div>
    </>
  );
};

Drawer.displayName = 'Drawer';
export default Drawer;
