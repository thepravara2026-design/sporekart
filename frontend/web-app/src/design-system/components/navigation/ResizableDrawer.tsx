import React, { useState, useRef, useCallback, useEffect } from 'react';
import { createPortal } from 'react-dom';

export interface ResizableDrawerProps {
  open: boolean;
  onClose: () => void;
  title: string;
  children: React.ReactNode;
  defaultWidth?: number;
  minWidth?: number;
  maxWidth?: number;
  position?: 'left' | 'right';
  onWidthChange?: (width: number) => void;
  className?: string;
  style?: React.CSSProperties;
}

const HANDLE_WIDTH = 6;

export const ResizableDrawer: React.FC<ResizableDrawerProps> = ({
  open,
  onClose,
  title,
  children,
  defaultWidth = 320,
  minWidth = 240,
  maxWidth = 600,
  position = 'right',
  onWidthChange,
  className = '',
  style,
}) => {
  const [width, setWidth] = useState(defaultWidth);
  const [isResizing, setIsResizing] = useState(false);
  const startXRef = useRef(0);
  const startWidthRef = useRef(0);
  const panelRef = useRef<HTMLDivElement>(null);

  const handleMouseDown = useCallback((e: React.MouseEvent | React.TouchEvent) => {
    e.preventDefault();
    const clientX = 'touches' in e ? e.touches[0].clientX : e.clientX;
    startXRef.current = clientX;
    startWidthRef.current = width;
    setIsResizing(true);
  }, [width]);

  const handleResize = useCallback((clientX: number) => {
    const delta = position === 'right' ? startXRef.current - clientX : clientX - startXRef.current;
    const newWidth = Math.max(minWidth, Math.min(maxWidth, startWidthRef.current + delta));
    setWidth(newWidth);
    onWidthChange?.(newWidth);
  }, [minWidth, maxWidth, position, onWidthChange]);

  const handleMouseMove = useCallback((e: MouseEvent) => {
    handleResize(e.clientX);
  }, [handleResize]);

  const handleTouchMove = useCallback((e: TouchEvent) => {
    handleResize(e.touches[0].clientX);
  }, [handleResize]);

  const stopResize = useCallback(() => {
    setIsResizing(false);
  }, []);

  useEffect(() => {
    if (!isResizing) return;
    document.addEventListener('mousemove', handleMouseMove);
    document.addEventListener('mouseup', stopResize);
    document.addEventListener('touchmove', handleTouchMove);
    document.addEventListener('touchend', stopResize);
    document.body.style.userSelect = 'none';
    document.body.style.cursor = 'ew-resize';

    return () => {
      document.removeEventListener('mousemove', handleMouseMove);
      document.removeEventListener('mouseup', stopResize);
      document.removeEventListener('touchmove', handleTouchMove);
      document.removeEventListener('touchend', stopResize);
      document.body.style.userSelect = '';
      document.body.style.cursor = '';
    };
  }, [isResizing, handleMouseMove, handleTouchMove, stopResize]);

  const isLeft = position === 'left';

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
    [isLeft ? 'left' : 'right']: 0,
    bottom: 0,
    width,
    zIndex: 'var(--z-drawer)' as unknown as number,
    background: 'var(--color-bg-surface-default)',
    display: 'flex',
    flexDirection: 'column',
    boxShadow: isLeft ? 'var(--shadow-3)' : 'var(--shadow-3)',
    transform: open ? 'translateX(0)' : `translateX(${isLeft ? '-100%' : '100%'})`,
    transition: isResizing ? 'none' : 'transform var(--duration-slow) var(--easing-emphasized)',
    borderRight: isLeft ? '1px solid var(--color-border-default)' : 'none',
    borderLeft: isLeft ? 'none' : '1px solid var(--color-border-default)',
    ...style,
  };

  const handleStyle: React.CSSProperties = {
    position: 'absolute',
    top: 0,
    bottom: 0,
    [isLeft ? 'right' : 'left']: -HANDLE_WIDTH / 2,
    width: HANDLE_WIDTH,
    cursor: 'ew-resize',
    zIndex: 10,
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
  };

  const handleLineStyle: React.CSSProperties = {
    width: 2,
    height: 32,
    background: 'var(--color-border-strong)',
    borderRadius: 'var(--radius-xs)',
    opacity: isResizing ? 1 : 0.4,
    transition: 'opacity var(--duration-fast) var(--easing-standard)',
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
    <div className={`sk-resizable-drawer ${className}`.trim()}>
      <div style={overlayStyle} onClick={onClose} aria-hidden="true" />
      <div
        ref={panelRef}
        style={panelStyle}
        role="dialog"
        aria-modal="true"
        aria-label={title}
      >
        <div
          style={handleStyle}
          onMouseDown={handleMouseDown}
          onTouchStart={handleMouseDown}
          role="separator"
          aria-label="Resize drawer"
          aria-orientation="vertical"
        >
          <div style={handleLineStyle} />
        </div>
        <div style={headerStyle}>
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

ResizableDrawer.displayName = 'ResizableDrawer';
export default ResizableDrawer;
