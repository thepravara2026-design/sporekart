import React, { useCallback, useRef } from 'react';
import { Popover, PopoverProps } from './Popover';

export interface InteractivePopoverProps extends PopoverProps {
  closeOnInnerClick?: boolean;
}

export const InteractivePopover: React.FC<InteractivePopoverProps> = ({
  open,
  onClose,
  anchorEl,
  closeOnInnerClick = false,
  children,
  style,
  ...props
}) => {
  const contentRef = useRef<HTMLDivElement>(null);

  const handleMouseDown = useCallback((e: React.MouseEvent) => {
    if (!closeOnInnerClick) {
      e.stopPropagation();
    }
  }, [closeOnInnerClick]);

  const containerStyle: React.CSSProperties = {
    padding: 'var(--space-inline-md)',
    ...style,
  };

  return (
    <Popover
      open={open}
      onClose={onClose}
      anchorEl={anchorEl}
      style={containerStyle}
      {...props}
    >
      <div ref={contentRef} onMouseDown={handleMouseDown}>
        {children}
      </div>
    </Popover>
  );
};

InteractivePopover.displayName = 'InteractivePopover';
export default InteractivePopover;
