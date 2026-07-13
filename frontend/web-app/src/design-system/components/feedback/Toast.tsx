import React, { useEffect, useRef, useState, useCallback } from 'react';

export type ToastType = 'success' | 'error' | 'warning' | 'info' | 'loading';
export type ToastVariant = 'standard' | 'rich';

export interface ToastOptions {
  type: ToastType;
  title: string;
  message?: string;
  duration?: number;
  action?: { label: string; onClick: () => void };
  icon?: React.ReactNode;
  variant?: ToastVariant;
}

export interface ToastItem extends ToastOptions {
  id: string;
}

export interface ToastProps extends ToastItem {
  onClose: (id: string) => void;
}

const TYPE_ICON_MAP: Record<ToastType, React.ReactNode> = {
  success: (
    <svg width="20" height="20" viewBox="0 0 20 20" fill="none" aria-hidden="true">
      <circle cx="10" cy="10" r="9" stroke="currentColor" strokeWidth="1.5" />
      <path d="M6 10l2.5 2.5L14 7" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
    </svg>
  ),
  error: (
    <svg width="20" height="20" viewBox="0 0 20 20" fill="none" aria-hidden="true">
      <circle cx="10" cy="10" r="9" stroke="currentColor" strokeWidth="1.5" />
      <path d="M7 7l6 6M13 7l-6 6" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" />
    </svg>
  ),
  warning: (
    <svg width="20" height="20" viewBox="0 0 20 20" fill="none" aria-hidden="true">
      <path d="M10 2L2 18h16L10 2z" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
      <path d="M10 8v4M10 14.5h0" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" />
    </svg>
  ),
  info: (
    <svg width="20" height="20" viewBox="0 0 20 20" fill="none" aria-hidden="true">
      <circle cx="10" cy="10" r="9" stroke="currentColor" strokeWidth="1.5" />
      <path d="M10 9v5M10 6.5h0" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" />
    </svg>
  ),
  loading: (
    <svg width="20" height="20" viewBox="0 0 20 20" fill="none" aria-hidden="true" style={{ animation: 'sk-spin 1s linear infinite' }}>
      <circle cx="10" cy="10" r="9" stroke="currentColor" strokeWidth="1.5" opacity="0.25" />
      <path d="M10 1a9 9 0 019 9" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" />
    </svg>
  ),
};

const TYPE_ACCENT_MAP: Record<ToastType, string> = {
  success: 'var(--color-icon-success)',
  error: 'var(--color-icon-danger)',
  warning: 'var(--color-icon-warning)',
  info: 'var(--color-icon-info)',
  loading: 'var(--color-icon-info)',
};

const TYPE_LIVE_MAP: Record<ToastType, 'polite' | 'assertive'> = {
  success: 'polite',
  error: 'assertive',
  warning: 'polite',
  info: 'polite',
  loading: 'polite',
};

let spinStyleInjected = false;

function injectSpinKeyframes() {
  if (spinStyleInjected) return;
  spinStyleInjected = true;
  if (typeof document === 'undefined') return;
  if (document.getElementById('sk-toast-spin')) return;
  const style = document.createElement('style');
  style.id = 'sk-toast-spin';
  style.textContent = '@keyframes sk-spin{to{transform:rotate(360deg)}}';
  document.head.appendChild(style);
}

function CloseIcon() {
  return (
    <svg width="16" height="16" viewBox="0 0 16 16" fill="none" aria-hidden="true">
      <path d="M4 4l8 8M12 4l-8 8" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" />
    </svg>
  );
}

type ToastInternalProps = ToastProps & {
  isClosing?: boolean;
  paused?: boolean;
};

export function Toast({
  id,
  type,
  title,
  message,
  duration,
  action,
  onClose,
  icon,
  variant = 'standard',
  isClosing = false,
  paused = false,
}: ToastInternalProps) {
  const [isVisible, setIsVisible] = useState(false);
  const dismissedRef = useRef(false);

  useEffect(() => {
    if (type === 'loading') injectSpinKeyframes();
  }, [type]);

  useEffect(() => {
    const raf = requestAnimationFrame(() => {
      setIsVisible(true);
    });
    return () => cancelAnimationFrame(raf);
  }, []);

  useEffect(() => {
    if (!isVisible || paused || dismissedRef.current) return;
    if (duration == null || duration === 0) return;

    const timer = setTimeout(() => {
      dismissedRef.current = true;
      onClose(id);
    }, duration);

    return () => clearTimeout(timer);
  }, [isVisible, paused, duration, id, onClose]);

  const handleClose = useCallback(() => {
    dismissedRef.current = true;
    onClose(id);
  }, [id, onClose]);

  const accentColor = TYPE_ACCENT_MAP[type];
  const ariaLive = TYPE_LIVE_MAP[type];
  const isRich = variant === 'rich';

  const toastStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'flex-start',
    gap: 'var(--space-3)',
    padding: isRich ? 'var(--space-4)' : 'var(--space-3) var(--space-4)',
    borderRadius: 'var(--radius-md)',
    backgroundColor: 'var(--color-bg-surface-default)',
    boxShadow: 'var(--shadow-3)',
    borderLeft: `3px solid ${accentColor}`,
    minWidth: isRich ? 360 : 320,
    maxWidth: isRich ? 480 : 420,
    transform: isVisible && !isClosing ? 'translateX(0)' : 'translateX(120%)',
    opacity: isVisible && !isClosing ? 1 : 0,
    transition: 'transform var(--duration-normal) var(--easing-standard), opacity var(--duration-normal) var(--easing-standard)',
    pointerEvents: 'auto' as const,
  };

  const iconStyle: React.CSSProperties = {
    flexShrink: 0,
    width: 'var(--icon-sm)',
    height: 'var(--icon-sm)',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    color: accentColor,
  };

  const contentStyle: React.CSSProperties = {
    flex: 1,
    minWidth: 0,
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-stack-xs)',
  };

  const titleStyle: React.CSSProperties = {
    fontSize: 'var(--text-body-sm)',
    fontWeight: 'var(--weight-semibold)',
    color: 'var(--color-text-primary)',
    margin: 0,
    lineHeight: 'var(--leading-normal)',
  };

  const messageStyle: React.CSSProperties = {
    fontSize: 'var(--text-caption)',
    color: 'var(--color-text-secondary)',
    margin: 0,
    lineHeight: 'var(--leading-normal)',
  };

  const actionStyle: React.CSSProperties = {
    background: 'none',
    border: 'none',
    padding: 0,
    fontSize: 'var(--text-button)',
    fontWeight: 'var(--weight-medium)',
    color: accentColor,
    cursor: 'pointer',
    whiteSpace: 'nowrap' as const,
    fontFamily: 'inherit',
    lineHeight: 'var(--leading-normal)',
  };

  const closeBtnStyle: React.CSSProperties = {
    flexShrink: 0,
    background: 'none',
    border: 'none',
    padding: 'var(--space-1)',
    cursor: 'pointer',
    color: 'var(--color-text-disabled)',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 'var(--radius-xs)',
    transition: 'color var(--duration-fast) var(--easing-standard)',
  };

  const actionsRowStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-3)',
    marginTop: 'var(--space-stack-xs)',
  };

  return (
    <div
      style={toastStyle}
      role="alert"
      aria-live={ariaLive}
      aria-atomic="true"
      data-toast-id={id}
    >
      <div style={iconStyle}>
        {icon ?? TYPE_ICON_MAP[type]}
      </div>
      <div style={contentStyle}>
        <p style={titleStyle}>{title}</p>
        {message && <p style={messageStyle}>{message}</p>}
        {action && (
          <div style={actionsRowStyle}>
            <button
              type="button"
              style={actionStyle}
              onClick={() => {
                action.onClick();
                handleClose();
              }}
            >
              {action.label}
            </button>
          </div>
        )}
      </div>
      <button
        type="button"
        style={closeBtnStyle}
        onClick={handleClose}
        aria-label="Dismiss"
      >
        <CloseIcon />
      </button>
    </div>
  );
}

Toast.displayName = 'Toast';
