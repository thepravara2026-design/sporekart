import React, { useCallback, useEffect, useId, useRef, useState } from 'react';
import { createPortal } from 'react-dom';
import { useTooltipContext } from './TooltipProvider';

export type TooltipPosition = 'top' | 'bottom' | 'left' | 'right';

export interface TooltipProps {
  content: React.ReactNode;
  children: React.ReactNode;
  position?: TooltipPosition;
  showDelay?: number;
  hideDelay?: number;
  open?: boolean;
  onOpenChange?: (open: boolean) => void;
  className?: string;
  style?: React.CSSProperties;
}

const arrowSize = 6;

function getPositionStyles(
  position: TooltipPosition,
  triggerRect: DOMRect,
  tooltipWidth: number,
  tooltipHeight: number,
  offset: number
): React.CSSProperties {
  const gap = offset + arrowSize;
  let top = 0;
  let left = 0;

  switch (position) {
    case 'top':
      top = triggerRect.top - tooltipHeight - gap;
      left = triggerRect.left + triggerRect.width / 2 - tooltipWidth / 2;
      break;
    case 'bottom':
      top = triggerRect.bottom + gap;
      left = triggerRect.left + triggerRect.width / 2 - tooltipWidth / 2;
      break;
    case 'left':
      top = triggerRect.top + triggerRect.height / 2 - tooltipHeight / 2;
      left = triggerRect.left - tooltipWidth - gap;
      break;
    case 'right':
      top = triggerRect.top + triggerRect.height / 2 - tooltipHeight / 2;
      left = triggerRect.right + gap;
      break;
  }

  return {
    position: 'fixed',
    top: `${top}px`,
    left: `${left}px`,
  };
}

export const Tooltip: React.FC<TooltipProps> = ({
  content,
  children,
  position: propPosition = 'top',
  showDelay = 200,
  hideDelay = 150,
  open: controlledOpen,
  onOpenChange,
  className = '',
  style,
}) => {
  const id = useId();
  const ctx = useTooltipContext();
  const triggerRef = useRef<HTMLSpanElement>(null);
  const tooltipRef = useRef<HTMLDivElement>(null);
  const showTimerRef = useRef<ReturnType<typeof setTimeout>>();
  const hideTimerRef = useRef<ReturnType<typeof setTimeout>>();
  const [internalOpen, setInternalOpen] = useState(false);
  const [positionStyle, setPositionStyle] = useState<React.CSSProperties>({});

  const isControlled = controlledOpen !== undefined;
  const isOpen = isControlled ? controlledOpen : internalOpen;

  useEffect(() => {
    ctx.registerTooltip(id);
    return () => ctx.unregisterTooltip(id);
  }, [id, ctx]);

  const updatePosition = useCallback(() => {
    if (!triggerRef.current || !tooltipRef.current) return;
    const triggerRect = triggerRef.current.getBoundingClientRect();
    const tooltipRect = tooltipRef.current.getBoundingClientRect();
    const pos = getPositionStyles(
      propPosition,
      triggerRect,
      tooltipRect.width,
      tooltipRect.height,
      4
    );
    setPositionStyle(pos);
  }, [propPosition]);

  useEffect(() => {
    if (isOpen) {
      updatePosition();
      window.addEventListener('scroll', updatePosition, true);
      window.addEventListener('resize', updatePosition);
      return () => {
        window.removeEventListener('scroll', updatePosition, true);
        window.removeEventListener('resize', updatePosition);
      };
    }
  }, [isOpen, updatePosition]);

  const clearTimers = () => {
    if (showTimerRef.current) clearTimeout(showTimerRef.current);
    if (hideTimerRef.current) clearTimeout(hideTimerRef.current);
  };

  const show = useCallback(() => {
    clearTimers();
    showTimerRef.current = setTimeout(() => {
      setInternalOpen(true);
      ctx.showTooltip(id);
      onOpenChange?.(true);
    }, showDelay);
  }, [showDelay, id, ctx, onOpenChange]);

  const hide = useCallback(() => {
    clearTimers();
    hideTimerRef.current = setTimeout(() => {
      setInternalOpen(false);
      ctx.hideTooltip(id);
      onOpenChange?.(false);
    }, hideDelay);
  }, [hideDelay, id, ctx, onOpenChange]);

  const tooltipStyle: React.CSSProperties = {
    zIndex: 'var(--z-tooltip)',
    background: 'var(--color-bg-surface-raised)',
    borderRadius: 'var(--radius-tooltip)',
    padding: 'var(--space-1) var(--space-inline-sm)',
    fontSize: 'var(--text-caption)',
    lineHeight: 'var(--leading-normal)',
    color: 'var(--color-text-primary)',
    boxShadow: 'var(--shadow-2)',
    pointerEvents: 'none',
    whiteSpace: 'nowrap',
    maxWidth: 280,
    ...positionStyle,
    ...style,
  };

  return (
    <>
      <span
        ref={triggerRef}
        onMouseEnter={show}
        onMouseLeave={hide}
        onFocus={show}
        onBlur={hide}
        style={{ display: 'inline-flex' }}
      >
        {children}
      </span>
      {isOpen && createPortal(
        <div
          ref={tooltipRef}
          role="tooltip"
          className={className}
          style={tooltipStyle}
        >
          <span
            style={{
              position: 'absolute',
              width: 0,
              height: 0,
              borderStyle: 'solid',
              borderWidth: `${arrowSize}px`,
              borderColor: 'transparent',
              ...(propPosition === 'top' ? {
                bottom: -arrowSize * 2,
                left: '50%',
                transform: 'translateX(-50%)',
                borderTopColor: 'var(--color-bg-surface-raised)',
              } : propPosition === 'bottom' ? {
                top: -arrowSize * 2,
                left: '50%',
                transform: 'translateX(-50%)',
                borderBottomColor: 'var(--color-bg-surface-raised)',
              } : propPosition === 'left' ? {
                right: -arrowSize * 2,
                top: '50%',
                transform: 'translateY(-50%)',
                borderLeftColor: 'var(--color-bg-surface-raised)',
              } : {
                left: -arrowSize * 2,
                top: '50%',
                transform: 'translateY(-50%)',
                borderRightColor: 'var(--color-bg-surface-raised)',
              }),
            }}
            aria-hidden="true"
          />
          {content}
        </div>,
        document.body
      )}
    </>
  );
};

Tooltip.displayName = 'Tooltip';
export default Tooltip;
