import React, { useState, useCallback } from 'react';
import { Portal } from './Portal';
import { Toast } from './Toast';
import type { ToastItem } from './Toast';

export type ToastPosition =
  | 'top-right'
  | 'top-left'
  | 'bottom-right'
  | 'bottom-left'
  | 'top-center'
  | 'bottom-center';

export interface ToastContainerProps {
  toasts: ToastItem[];
  onClose: (id: string) => void;
  position?: ToastPosition;
  pauseOnHover?: boolean;
}

const CONTAINER_BASE: React.CSSProperties = {
  position: 'fixed',
  zIndex: 'var(--z-toast)',
  padding: 'var(--space-4)',
  display: 'flex',
  flexDirection: 'column',
  gap: 'var(--space-3)',
  pointerEvents: 'none',
};

const POSITION_STYLES: Record<ToastPosition, React.CSSProperties> = {
  'top-right': {
    top: 0,
    right: 0,
    alignItems: 'flex-end',
  },
  'top-left': {
    top: 0,
    left: 0,
    alignItems: 'flex-start',
  },
  'bottom-right': {
    bottom: 0,
    right: 0,
    alignItems: 'flex-end',
    flexDirection: 'column-reverse',
  },
  'bottom-left': {
    bottom: 0,
    left: 0,
    alignItems: 'flex-start',
    flexDirection: 'column-reverse',
  },
  'top-center': {
    top: 0,
    left: '50%',
    transform: 'translateX(-50%)',
    alignItems: 'center',
  },
  'bottom-center': {
    bottom: 0,
    left: '50%',
    transform: 'translateX(-50%)',
    alignItems: 'center',
    flexDirection: 'column-reverse',
  },
};

export function ToastContainer({
  toasts,
  onClose,
  position = 'top-right',
  pauseOnHover = true,
}: ToastContainerProps) {
  const [closingIds, setClosingIds] = useState<Set<string>>(new Set());
  const [isHovered, setIsHovered] = useState(false);

  const handleClose = useCallback(
    (id: string) => {
      setClosingIds((prev) => new Set(prev).add(id));
      setTimeout(() => {
        setClosingIds((prev) => {
          const next = new Set(prev);
          next.delete(id);
          return next;
        });
        onClose(id);
      }, 200);
    },
    [onClose]
  );

  const handleMouseEnter = useCallback(
    () => pauseOnHover && setIsHovered(true),
    [pauseOnHover]
  );

  const handleMouseLeave = useCallback(
    () => pauseOnHover && setIsHovered(false),
    [pauseOnHover]
  );

  if (toasts.length === 0) return null;

  const paused = pauseOnHover && isHovered;
  const containerStyle: React.CSSProperties = {
    ...CONTAINER_BASE,
    ...POSITION_STYLES[position],
  };

  return (
    <Portal>
      <div
        style={containerStyle}
        role="region"
        aria-label="Notifications"
        aria-live="polite"
        onMouseEnter={handleMouseEnter}
        onMouseLeave={handleMouseLeave}
      >
        {toasts.map((toast) => (
          <Toast
            key={toast.id}
            {...toast}
            isClosing={closingIds.has(toast.id)}
            paused={paused}
            onClose={handleClose}
          />
        ))}
      </div>
    </Portal>
  );
}

ToastContainer.displayName = 'ToastContainer';
