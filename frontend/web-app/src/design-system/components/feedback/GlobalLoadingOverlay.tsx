import React from 'react';
import { createPortal } from 'react-dom';

export interface GlobalLoadingOverlayProps {
  open: boolean;
  message?: string;
  spinner?: boolean;
  blur?: boolean;
  className?: string;
  style?: React.CSSProperties;
}

const spinKeyframes = `
  @keyframes sk-spin {
    from { transform: rotate(0deg); }
    to { transform: rotate(360deg); }
  }
`;

export const GlobalLoadingOverlay: React.FC<GlobalLoadingOverlayProps> = ({
  open,
  message,
  spinner = true,
  blur = false,
  className = '',
  style,
}) => {
  if (!open) return null;

  const overlayStyle: React.CSSProperties = {
    position: 'fixed',
    inset: 0,
    width: '100vw',
    height: '100vh',
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
    gap: 'var(--space-stack-md)',
    backgroundColor: 'var(--color-bg-overlay)',
    zIndex: 'var(--z-modal-backdrop)',
    backdropFilter: blur ? 'blur(4px)' : undefined,
    WebkitBackdropFilter: blur ? 'blur(4px)' : undefined,
    ...style,
  };

  const spinnerStyle: React.CSSProperties = {
    width: 40,
    height: 40,
    border: '4px solid var(--color-bg-skeleton-base)',
    borderTopColor: 'var(--color-bg-primary-default)',
    borderRadius: 'var(--radius-full)',
    animation: 'sk-spin 0.8s linear infinite',
  };

  const messageStyle: React.CSSProperties = {
    fontSize: 'var(--text-body)',
    color: 'var(--color-text-on-primary)',
    fontWeight: 'var(--weight-medium)',
  };

  const content = (
    <div
      className={`sk-global-loading-overlay ${className}`.trim()}
      style={overlayStyle}
      role="alertdialog"
      aria-modal="true"
      aria-label={message || 'Loading'}
    >
      <style>{spinKeyframes}</style>
      {spinner && <div style={spinnerStyle} />}
      {message && <span style={messageStyle}>{message}</span>}
    </div>
  );

  return createPortal(content, document.body);
};

GlobalLoadingOverlay.displayName = 'GlobalLoadingOverlay';
export default GlobalLoadingOverlay;
