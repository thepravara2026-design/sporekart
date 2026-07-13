import React from 'react';
import { Popover, PopoverProps } from './Popover';

export interface InformationPopoverProps extends PopoverProps {
  title?: string;
  description?: string;
  icon?: React.ReactNode;
}

export const InformationPopover: React.FC<InformationPopoverProps> = ({
  title,
  description,
  icon,
  children,
  style,
  ...props
}) => {
  const containerStyle: React.CSSProperties = {
    padding: 'var(--space-inline-md)',
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-stack-sm)',
  };

  const headerStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-inline-sm)',
  };

  const iconStyle: React.CSSProperties = {
    width: 'var(--icon-sm)',
    height: 'var(--icon-sm)',
    flexShrink: 0,
    color: 'var(--color-icon-primary)',
  };

  const titleStyle: React.CSSProperties = {
    fontSize: 'var(--text-body-sm)',
    fontWeight: 'var(--weight-semibold)',
    color: 'var(--color-text-primary)',
    margin: 0,
  };

  const descStyle: React.CSSProperties = {
    fontSize: 'var(--text-body-sm)',
    color: 'var(--color-text-secondary)',
    margin: 0,
    lineHeight: 'var(--leading-normal)',
  };

  return (
    <Popover
      style={style}
      {...props}
    >
      <div style={containerStyle}>
        {(title || icon) && (
          <div style={headerStyle}>
            {icon && <span style={iconStyle}>{icon}</span>}
            {title && <p style={titleStyle}>{title}</p>}
          </div>
        )}
        {description && <p style={descStyle}>{description}</p>}
        {children}
      </div>
    </Popover>
  );
};

InformationPopover.displayName = 'InformationPopover';
export default InformationPopover;
