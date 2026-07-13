import React from 'react';
import { Tooltip, TooltipProps } from './Tooltip';

export interface RichTooltipProps extends TooltipProps {
  image?: string;
  icon?: React.ReactNode;
}

export const RichTooltip: React.FC<RichTooltipProps> = ({
  image,
  icon,
  content,
  style,
  ...props
}) => {
  const richContentStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-inline-sm)',
    maxWidth: 320,
    whiteSpace: 'normal',
  };

  const imageStyle: React.CSSProperties = {
    width: '100%',
    height: 'auto',
    borderRadius: 'var(--radius-xs)',
    objectFit: 'cover',
    maxHeight: 120,
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
    color: 'var(--color-text-secondary)',
  };

  return (
    <Tooltip
      {...props}
      content={
        <div style={richContentStyle}>
          {image && <img src={image} alt="" style={imageStyle} />}
          <div style={headerStyle}>
            {icon && <span style={iconStyle}>{icon}</span>}
            <span>{content}</span>
          </div>
        </div>
      }
      style={{ whiteSpace: 'normal', ...style }}
    />
  );
};

RichTooltip.displayName = 'RichTooltip';
export default RichTooltip;
