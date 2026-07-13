import React, { useCallback, useEffect, useRef, useState } from 'react';
import { createPortal } from 'react-dom';

export type PopoverPosition = 'top' | 'bottom' | 'left' | 'right' | 'auto';

export interface PopoverProps {
  open: boolean;
  onClose: () => void;
  anchorEl?: HTMLElement | null;
  children?: React.ReactNode;
  position?: PopoverPosition;
  offset?: number;
  className?: string;
  style?: React.CSSProperties;
}

const arrowSize = 6;

function computeAutoPosition(
  anchorRect: DOMRect,
  popoverWidth: number,
  popoverHeight: number,
  offset: number,
  preferred: PopoverPosition
): PopoverPosition {
  if (preferred !== 'auto') return preferred;

  const gap = offset + arrowSize;
  const viewportW = window.innerWidth;
  const viewportH = window.innerHeight;

  const fitsTop = anchorRect.top - popoverHeight - gap > 0;
  const fitsBottom = anchorRect.bottom + popoverHeight + gap < viewportH;
  const fitsLeft = anchorRect.left - popoverWidth - gap > 0;
  const fitsRight = anchorRect.right + popoverWidth + gap < viewportW;

  if (fitsBottom) return 'bottom';
  if (fitsTop) return 'top';
  if (fitsRight) return 'right';
  if (fitsLeft) return 'left';
  return 'bottom';
}

function getPositionStyles(
  position: PopoverPosition,
  anchorRect: DOMRect,
  popoverWidth: number,
  popoverHeight: number,
  offset: number
): React.CSSProperties {
  const resolvedPosition = computeAutoPosition(anchorRect, popoverWidth, popoverHeight, offset, position);
  const gap = offset + arrowSize;
  let top = 0;
  let left = 0;

  switch (resolvedPosition) {
    case 'top':
      top = anchorRect.top - popoverHeight - gap;
      left = anchorRect.left + anchorRect.width / 2 - popoverWidth / 2;
      break;
    case 'bottom':
      top = anchorRect.bottom + gap;
      left = anchorRect.left + anchorRect.width / 2 - popoverWidth / 2;
      break;
    case 'left':
      top = anchorRect.top + anchorRect.height / 2 - popoverHeight / 2;
      left = anchorRect.left - popoverWidth - gap;
      break;
    case 'right':
      top = anchorRect.top + anchorRect.height / 2 - popoverHeight / 2;
      left = anchorRect.right + gap;
      break;
  }

  return {
    position: 'fixed',
    top: `${top}px`,
    left: `${left}px`,
  };
}

export const Popover: React.FC<PopoverProps> = ({
  open,
  onClose,
  anchorEl,
  children,
  position = 'bottom',
  offset = 8,
  className = '',
  style,
}) => {
  const popoverRef = useRef<HTMLDivElement>(null);
  const [positionStyle, setPositionStyle] = useState<React.CSSProperties>({});

  const updatePosition = useCallback(() => {
    const el = anchorEl;
    if (!el || !popoverRef.current) return;
    const anchorRect = el.getBoundingClientRect();
    const popoverRect = popoverRef.current.getBoundingClientRect();
    const pos = getPositionStyles(position, anchorRect, popoverRect.width, popoverRect.height, offset);
    setPositionStyle(pos);
  }, [anchorEl, position, offset]);

  useEffect(() => {
    if (open) {
      updatePosition();
      window.addEventListener('scroll', updatePosition, true);
      window.addEventListener('resize', updatePosition);
      return () => {
        window.removeEventListener('scroll', updatePosition, true);
        window.removeEventListener('resize', updatePosition);
      };
    }
  }, [open, updatePosition]);

  useEffect(() => {
    if (!open) return;

    const handleEscape = (e: KeyboardEvent) => {
      if (e.key === 'Escape') {
        onClose();
      }
    };

    const handleOutsideClick = (e: MouseEvent) => {
      if (
        popoverRef.current &&
        !popoverRef.current.contains(e.target as Node) &&
        anchorEl &&
        !anchorEl.contains(e.target as Node)
      ) {
        onClose();
      }
    };

    document.addEventListener('keydown', handleEscape);
    document.addEventListener('mousedown', handleOutsideClick);
    return () => {
      document.removeEventListener('keydown', handleEscape);
      document.removeEventListener('mousedown', handleOutsideClick);
    };
  }, [open, onClose, anchorEl]);

  if (!open || !anchorEl) return null;

  const popoverStyle: React.CSSProperties = {
    zIndex: 'var(--z-dropdown)',
    background: 'var(--color-bg-surface-overlay)',
    borderRadius: 'var(--radius-dropdown)',
    boxShadow: 'var(--shadow-3)',
    minWidth: 160,
    maxWidth: 320,
    ...positionStyle,
    ...style,
  };

  return createPortal(
    <div
      ref={popoverRef}
      role="dialog"
      aria-modal="false"
      className={className}
      style={popoverStyle}
    >
      <span
        style={{
          position: 'absolute',
          width: 0,
          height: 0,
          borderStyle: 'solid',
          borderWidth: `${arrowSize}px`,
          borderColor: 'transparent',
        }}
        aria-hidden="true"
      />
      {children}
    </div>,
    document.body
  );
};

Popover.displayName = 'Popover';
export default Popover;
