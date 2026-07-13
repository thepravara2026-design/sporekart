import React, { useState, useCallback } from 'react';
import { Tooltip, TooltipProps } from './Tooltip';

export interface IconTooltipProps extends Omit<TooltipProps, 'children'> {}

export const IconTooltip: React.FC<IconTooltipProps> = ({
  content,
  position = 'right',
  ...props
}) => {
  const [open, setOpen] = useState(false);

  const handleClick = useCallback(() => {
    setOpen((prev) => !prev);
  }, []);

  const handleOpenChange = useCallback((val: boolean) => {
    setOpen(val);
  }, []);

  return (
    <Tooltip
      content={content}
      position={position}
      open={open}
      onOpenChange={handleOpenChange}
      {...props}
    >
      <button
        type="button"
        onClick={handleClick}
        onFocus={() => setOpen(true)}
        onBlur={() => setOpen(false)}
        aria-label="More information"
        style={{
          display: 'inline-flex',
          alignItems: 'center',
          justifyContent: 'center',
          width: 'var(--icon-sm)',
          height: 'var(--icon-sm)',
          padding: 0,
          border: 'none',
          background: 'transparent',
          cursor: 'pointer',
          color: 'var(--color-text-secondary)',
          borderRadius: 'var(--radius-full)',
        }}
      >
        <svg width="16" height="16" viewBox="0 0 16 16" fill="none" aria-hidden="true">
          <circle cx="8" cy="8" r="7" stroke="currentColor" strokeWidth="1.5" />
          <path d="M8 7v3.5M8 5.5h0" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" />
        </svg>
      </button>
    </Tooltip>
  );
};

IconTooltip.displayName = 'IconTooltip';
export default IconTooltip;
