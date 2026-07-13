import React, { useEffect, useRef } from 'react';
import { createPortal } from 'react-dom';

export interface StackedDrawerProps {
  open: boolean;
  onClose: () => void;
  title: string;
  children: React.ReactNode;
  level?: number;
  onBack?: () => void;
  maxWidth?: string;
  className?: string;
  style?: React.CSSProperties;
}

export const StackedDrawer: React.FC<StackedDrawerProps> = ({
  open,
  onClose,
  title,
  children,
  level = 1,
  onBack,
  maxWidth = 'var(--layout-sidebar-width)',
  className = '',
  style,
}) => {
  const panelRef = useRef<HTMLDivElement>(null);

  const offset = Math.min(level - 1, 3) * 16;
  const zIndex = 80 + level;

  const overlayStyle: React.CSSProperties = {
    position: 'fixed',
    inset: 0,
    background: 'var(--color-bg-overlay)',
    zIndex: zIndex as unknown as number,
    opacity: open ? 1 : 0,
    visibility: open ? 'visible' : 'hidden',
    transition: 'opacity var(--duration-normal) var(--easing-standard), visibility var(--duration-normal) var(--easing-standard)',
  };

  const panelStyle: React.CSSProperties = {
    position: 'fixed',
    top: offset,
    right: offset,
    bottom: 0,
    width: maxWidth,
    maxWidth: '100vw',
    zIndex: zIndex as unknown as number,
    background: 'var(--color-bg-surface-default)',
    display: 'flex',
    flexDirection: 'column',
    boxShadow: 'var(--shadow-4)',
    transform: open ? 'translateX(0)' : 'translateX(100%)',
    transition: 'transform var(--duration-slow) var(--easing-emphasized)',
    borderLeft: '1px solid var(--color-border-default)',
    ...style,
  };

  const headerStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-inline-sm)',
    padding: 'var(--space-stack-md) var(--space-page-x)',
    borderBottom: '1px solid var(--color-border-default)',
    flexShrink: 0,
    minHeight: 'var(--layout-header-height)',
  };

  const backBtnStyle: React.CSSProperties = {
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

  const titleStyle: React.CSSProperties = {
    flex: 1,
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
    <div className={`sk-stacked-drawer ${className}`.trim()}>
      <div style={overlayStyle} onClick={onClose} aria-hidden="true" />
      <div
        ref={panelRef}
        style={panelStyle}
        role="dialog"
        aria-modal="true"
        aria-label={title}
      >
        <div style={headerStyle}>
          {onBack && (
            <button style={backBtnStyle} onClick={onBack} aria-label="Go back" type="button">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <line x1="19" y1="12" x2="5" y2="12" />
                <polyline points="12 19 5 12 12 5" />
              </svg>
            </button>
          )}
          <h2 style={titleStyle}>{title}</h2>
          <button style={closeBtnStyle} onClick={onClose} aria-label="Close drawer" type="button">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <line x1="18" y1="6" x2="6" y2="18" />
              <line x1="6" y1="6" x2="18" y2="18" />
            </svg>
          </button>
        </div>
        <div style={contentStyle}>
          {children}
        </div>
      </div>
    </div>,
    document.body
  );
};

StackedDrawer.displayName = 'StackedDrawer';
export default StackedDrawer;
