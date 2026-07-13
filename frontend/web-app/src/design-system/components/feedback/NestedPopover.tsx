import React, { useCallback, useRef, useState } from 'react';
import { createPortal } from 'react-dom';
import { Popover, PopoverProps } from './Popover';

export interface NestedPopoverProps extends PopoverProps {
  childOpen?: boolean;
  onChildOpenChange?: (open: boolean) => void;
  childContent?: React.ReactNode;
  childPosition?: PopoverProps['position'];
}

export const NestedPopover: React.FC<NestedPopoverProps> = ({
  open,
  onClose,
  anchorEl,
  children,
  childOpen = false,
  onChildOpenChange,
  childContent,
  childPosition = 'right',
  style,
  ...props
}) => {
  const childAnchorRef = useRef<HTMLDivElement>(null);
  const [childPos, setChildPos] = useState<{ top: number; left: number }>({ top: 0, left: 0 });

  const handleItemMouseEnter = useCallback((e: React.MouseEvent<HTMLDivElement>) => {
    const rect = e.currentTarget.getBoundingClientRect();
    setChildPos({ top: rect.top, left: rect.right });
    onChildOpenChange?.(true);
  }, [onChildOpenChange]);

  const handleItemMouseLeave = useCallback(() => {
    onChildOpenChange?.(false);
  }, [onChildOpenChange]);

  const containerStyle: React.CSSProperties = {
    padding: 'var(--space-1)',
    ...style,
  };

  const childPopoverStyle: React.CSSProperties = {
    position: 'fixed',
    top: `${childPos.top}px`,
    left: `${childPos.left}px`,
    zIndex: 'var(--z-dropdown)',
    background: 'var(--color-bg-surface-overlay)',
    borderRadius: 'var(--radius-dropdown)',
    boxShadow: 'var(--shadow-3)',
    minWidth: 140,
    padding: 'var(--space-1)',
    marginLeft: 'var(--space-inline-xs)',
  };

  return (
    <Popover
      open={open}
      onClose={onClose}
      anchorEl={anchorEl}
      style={containerStyle}
      {...props}
    >
      <div
        ref={childAnchorRef}
        onMouseEnter={handleItemMouseEnter}
        onMouseLeave={handleItemMouseLeave}
      >
        {children}
      </div>
      {childOpen && childContent && createPortal(
        <div style={childPopoverStyle} role="dialog" aria-modal="false">
          {childContent}
        </div>,
        document.body
      )}
    </Popover>
  );
};

NestedPopover.displayName = 'NestedPopover';
export default NestedPopover;
