import React, { useEffect, useRef } from 'react';
import { Popover, PopoverProps } from './Popover';

export interface ContextPopoverProps extends PopoverProps {
  x?: number;
  y?: number;
}

export const ContextPopover: React.FC<ContextPopoverProps> = ({
  open,
  onClose,
  x,
  y,
  children,
  style,
  ...props
}) => {
  const positioned = useRef(false);

  useEffect(() => {
    if (open && x !== undefined && y !== undefined) {
      positioned.current = true;
    }
  }, [open, x, y]);

  if (!positioned.current && !open) return null;

  const contextStyle: React.CSSProperties = {
    position: 'fixed',
    top: y !== undefined ? `${y}px` : undefined,
    left: x !== undefined ? `${x}px` : undefined,
    minWidth: 180,
    ...style,
  };

  return (
    <Popover
      open={open}
      onClose={onClose}
      style={contextStyle}
      {...props}
    >
      {children}
    </Popover>
  );
};

ContextPopover.displayName = 'ContextPopover';
export default ContextPopover;
