import React, { useEffect } from 'react';
import { createPortal } from 'react-dom';

export interface ContextDrawerProps {
  open: boolean;
  onClose: () => void;
  context?: string;
  contexts?: Record<string, { title: string; content: React.ReactNode }>;
  title?: string;
  children?: React.ReactNode;
  width?: string;
  className?: string;
  style?: React.CSSProperties;
}

export const ContextDrawer: React.FC<ContextDrawerProps> = ({
  open,
  onClose,
  context,
  contexts,
  title,
  children,
  width = 'var(--layout-sidebar-width)',
  className = '',
  style,
}) => {
  const resolvedTitle = context && contexts?.[context]?.title
    ? contexts[context].title
    : title || 'Drawer';

  const resolvedContent = context && contexts?.[context]?.content
    ? contexts[context].content
    : children;

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
    top: 0,
    right: 0,
    bottom: 0,
    width,
    maxWidth: '100vw',
    zIndex: 'var(--z-drawer)' as unknown as number,
    background: 'var(--color-bg-surface-default)',
    display: 'flex',
    flexDirection: 'column',
    boxShadow: 'var(--shadow-3)',
    transform: open ? 'translateX(0)' : 'translateX(100%)',
    transition: 'transform var(--duration-slow) var(--easing-emphasized)',
    borderLeft: '1px solid var(--color-border-default)',
    ...style,
  };

  const headerStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    padding: 'var(--space-stack-md) var(--space-page-x)',
    borderBottom: '1px solid var(--color-border-default)',
    flexShrink: 0,
    minHeight: 'var(--layout-header-height)',
  };

  const titleStyle: React.CSSProperties = {
    fontSize: 'var(--text-h4)',
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
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 'var(--radius-xs)',
  };

  const contentStyle: React.CSSProperties = {
    flex: 1,
    overflowY: 'auto',
    padding: 'var(--space-stack-md) var(--space-page-x)',
  };

  useEffect(() => {
    if (open) {
      document.body.style.overflow = 'hidden';
    } else {
      document.body.style.overflow = '';
    }
    return () => {
      document.body.style.overflow = '';
    };
  }, [open]);

  if (!open) return null;

  return createPortal(
    <div className={`sk-context-drawer ${className}`.trim()}>
      <div style={overlayStyle} onClick={onClose} aria-hidden="true" />
      <div
        style={panelStyle}
        role="dialog"
        aria-modal="true"
        aria-label={resolvedTitle}
      >
        <div style={headerStyle}>
          <h2 style={titleStyle}>{resolvedTitle}</h2>
          <button style={closeBtnStyle} onClick={onClose} aria-label="Close drawer" type="button">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <line x1="18" y1="6" x2="6" y2="18" />
              <line x1="6" y1="6" x2="18" y2="18" />
            </svg>
          </button>
        </div>
        <div style={contentStyle}>
          {resolvedContent}
        </div>
      </div>
    </div>,
    document.body
  );
};

ContextDrawer.displayName = 'ContextDrawer';
export default ContextDrawer;
